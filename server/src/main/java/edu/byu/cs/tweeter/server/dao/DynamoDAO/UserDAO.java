package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.AuthTokens;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.StatusListTable;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DatabaseObjects.Users;
import edu.byu.cs.tweeter.server.dao.UserDAOInterface;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class UserDAO implements UserDAOInterface {
    private static final String UsersTableName = "users";
    private static final String TokensTableName = "authtokens";

    private DynamoDbClient dynamoDbClient;
    private DynamoDbEnhancedClient enhancedClient;

    public DynamoDbEnhancedClient getEnhancedClient() {
        if (dynamoDbClient == null) {
            dynamoDbClient = DynamoDbClient.builder()
                    .region(Region.US_WEST_2)
                    .build();
        }
        System.out.println("Db client made");

        if (enhancedClient == null) {
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }
        System.out.println("Enhanced CLient made");

        return enhancedClient;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws DynamoDbException,
            ParseException, NoSuchAlgorithmException, InvalidKeySpecException {
        assert request.getUsername() != null;
        assert request.getPassword() != null;

        String alias = request.getUsername();
        String password = request.getPassword();

        System.out.println("Login - making table");
        DynamoDbTable<Users> table = getEnhancedClient()
                .table(UsersTableName, TableSchema.fromBean(Users.class));

        System.out.println("Login - making key");
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        System.out.println("Login - getting from table");
        Users usersData = table.getItem(key);

        System.out.println("Login - checking password");
        String hashedPassword = usersData.getPassword();
        if (!validatePassword(password, hashedPassword)) {
            return new LoginResponse("Password does not match");
        }

        System.out.println("Login success");
        User user = new User(usersData.getFirstName(), usersData.getLastName(),
                usersData.getUser_alias(), usersData.getImageURL());
        AuthToken authToken = uploadNewToken();

        return new LoginResponse(user, authToken);
    }

    @Override
    public RegisterResponse register(RegisterRequest request)
            throws DynamoDbException, ParseException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        assert request.getFirstName() != null;
        assert request.getLastName() != null;
        assert request.getImageBytesBase64() != null;
        assert request.getUsername() != null;
        assert request.getPassword() != null;

        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String alias = request.getUsername();
        String password = request.getPassword();
        String imageBytesBase64 = request.getImageBytesBase64();

        System.out.println("About to make table");
        DynamoDbTable<Users> table = getEnhancedClient()
                .table(UsersTableName, TableSchema.fromBean(Users.class));
        System.out.println("Table made");

        System.out.println("ABout to upload image");
        String imageURL = uploadImageToS3(imageBytesBase64, alias);
        System.out.println("Image uploaded.");

        User newUser = new User(firstName, lastName, alias, imageURL);
        String hashedPassword = generateStrongPasswordHash(password);

        Users newUsersData = new Users();

        newUsersData.setUser_alias(alias);
        newUsersData.setFirstName(firstName);
        newUsersData.setLastName(lastName);
        newUsersData.setImageURL(imageURL);

        newUsersData.setPassword(hashedPassword);
        newUsersData.setFollowingCount(0);
        newUsersData.setFollowerCount(0);

        System.out.println("Made new user data");

        table.putItem(newUsersData);
        System.out.println("Uploaded user");

        AuthToken authToken = uploadNewToken();

        return new RegisterResponse(newUser, authToken);
    }

    private String uploadImageToS3(String imageBytesBase64,
                                   String alias) {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();

        byte[] image = Base64.getDecoder().decode(imageBytesBase64);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.length);
        metadata.setContentType("image/png");

        String bucketName = "krauscs340";

        System.out.println("Putting image");

        String filename = alias.substring(1) + ".png";
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                filename, new ByteArrayInputStream(image), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(putObjectRequest);
        return s3.getUrl(bucketName, filename).toString();
    }

    private AuthToken uploadNewToken() throws ParseException {
        String tokenValue = NewUUID();
        String dateTime = getDateTime();

        AuthToken authToken = new AuthToken(tokenValue, dateTime);

        System.out.println("Made tokens table");
        DynamoDbTable<AuthTokens> table = getEnhancedClient()
                .table(TokensTableName, TableSchema.fromBean(AuthTokens.class));

        AuthTokens newTokensData = new AuthTokens();

        newTokensData.setToken(tokenValue);
        newTokensData.setDateTime(dateTime);

        System.out.println("Made tokens data");
        table.putItem(newTokensData);

        System.out.println("Uploaded authtoken");

        return authToken;
    }

    @Override
    public GetUserResponse getUser(GetUserRequest request) throws DynamoDbException {
        assert request.getAlias() != null;

        DynamoDbTable<Users> table = getEnhancedClient()
                .table(UsersTableName, TableSchema.fromBean(Users.class));

        String alias = request.getAlias();

        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        System.out.println("Getting user");
        Users usersData = table.getItem(key);

        User user = new User(usersData.getFirstName(), usersData.getLastName(),
                usersData.getUser_alias(), usersData.getImageURL());
        return new GetUserResponse(user);
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) {
        assert request.getAuthToken() != null;

        System.out.println("Making tokens table - logout");
        DynamoDbTable<AuthTokens> table = getEnhancedClient()
                .table(TokensTableName, TableSchema.fromBean(AuthTokens.class));

        AuthToken token = request.getAuthToken();
        String tokenValue = token.getToken();

        System.out.println("Making Key");
        Key key = Key.builder()
                .partitionValue(tokenValue)
                .build();

        System.out.println("Deleting token");
        table.deleteItem(key);

        return new LogoutResponse();
    }

    @Override
    public void updateCount(User user, boolean incrementCount, boolean isFollower) {
        DynamoDbTable<Users> table = getEnhancedClient()
                .table(UsersTableName, TableSchema.fromBean(Users.class));

        String alias = user.getAlias();

        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        Users usersData = table.getItem(key);

        if (isFollower) {
           if (incrementCount) {
               usersData.setFollowerCount(
                       usersData.getFollowerCount() + 1
               );
           } else {
               usersData.setFollowerCount(
                       usersData.getFollowerCount() - 1
               );
           }
        } else {
            if (incrementCount) {
                usersData.setFollowingCount(
                        usersData.getFollowingCount() + 1
                );
            } else {
                usersData.setFollowingCount(
                        usersData.getFollowingCount() - 1
                );
            }
        }

        table.updateItem(usersData);
    }

    @Override
    public GetCountResponse getCount(User user, boolean isFollowingCount) {
        System.out.println("isFollowingCount: " + isFollowingCount);
        System.out.println("Getting table");
        DynamoDbTable<Users> table = getEnhancedClient()
                .table(UsersTableName, TableSchema.fromBean(Users.class));

        String alias = user.getAlias();
        System.out.println("Alias: " + alias);

        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        System.out.println("Getting user info");
        Users usersData = table.getItem(key);

        System.out.println("Getting count");
        if (isFollowingCount) {
            return new GetCountResponse(usersData.getFollowingCount());
        } else {
            return new GetCountResponse(usersData.getFollowerCount());
        }
    }

    /**
     * The following methods are for password security
     */
    private static boolean validatePassword(String originalPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private static String generateStrongPasswordHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    /**
     * Creates a authtoken string
     */
    public String NewUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String getDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        return userFormat.format(userFormat.parse(LocalDate.now().toString() +
            " " + LocalTime.now().toString().substring(0, 8)));
    }
}
