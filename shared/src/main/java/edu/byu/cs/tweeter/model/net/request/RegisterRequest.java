package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest {
    private String username;
    private String password;
    private String imageBytesBase64;
    private String firstName;
    private String lastName;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private RegisterRequest() {}

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be logged in.
     * @param password the password of the user to be logged in.
     */
    public RegisterRequest(String username, String password, String imageBytesBase64,
                           String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.imageBytesBase64 = imageBytesBase64;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the username of the user to be logged in by this request.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Returns the password of the user to be logged in by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageBytesBase64() {
        return imageBytesBase64;
    }

    public void setImageBytesBase64(String imageBytesBase64) {
        this.imageBytesBase64 = imageBytesBase64;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
