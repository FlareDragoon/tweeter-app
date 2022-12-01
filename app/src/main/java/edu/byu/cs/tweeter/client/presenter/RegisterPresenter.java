package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;


import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.user.service.UserService;
import edu.byu.cs.tweeter.client.user.service.observer.LoginAndRegisterObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends LoginAndRegisterPresenter implements LoginAndRegisterObserver {
    public RegisterPresenter(View view) {
        this.view = view;
    }

    public void initiateRegister(String firstName, String lastName, String username,
                                 String password, Bitmap image) {
        String imageBytesBase64;
        if (image != null) {
            imageBytesBase64 = convertImageToByteArray(image);
        } else {
            imageBytesBase64 = null;
        }
        String message = validateRegistration(firstName, lastName,
                username, password, imageBytesBase64);

        if (message == null) {
            displaySuccessMessage();
            new UserService().register(firstName, lastName, username, password,
                    imageBytesBase64, this);
        } else {
            failure(message, view);
        }
    }

    public String validateRegistration(String firstName, String lastName, String username,
                                       String password, String imageBytesBase64) {
        if (firstName.length() == 0) {
            return("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            return ("Last Name cannot be empty.");
        }
        if (username.length() == 0) {
            return ("Alias cannot be empty.");
        }

        String checkUsernameAndPassword = validateLogin(username, password);
        if (checkUsernameAndPassword != null) {
            return checkUsernameAndPassword;
        }

        if (imageBytesBase64 == null) {
            return ("Profile image must be uploaded.");
        }
        return null;
    }

    public String convertImageToByteArray(Bitmap image) {
        // Convert image to byte array.
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        return  imageBytesBase64;
    }

    @Override
    public void loginOrRegisterUser(User user, AuthToken authToken) {
        loginOrRegister(user, authToken);
    }

    @Override
    public void handleFailure(String message) {
        failure(message, view);
    }

    @Override
    public void handleException(Exception exception) {
        String message = "Failed to register because of exception: " + exception.getMessage();
        failure(message, view);
    }
}
