package com.example.pqchatclient.Controller.Login;

import com.example.pqchatclient.Model.Client;
import com.example.pqchatclient.Model.Model;
import com.example.pqchatclient.Utilities.Encrypt;
import com.example.pqchatclient.View.LoginViewOptions;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    public TextField email__textField;
    public TextField password__textField;
    public CheckBox remember__checkBox;
    public Button forgotPassword__btn;
    public Button login__btn;
    public Button register__btn;
    public Button hidePassword__btn;
    public Label error__lbl;
    public PasswordField password__passwordField;


    int clickCount = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        register__btn.setOnAction(event -> onSignUpView());
        login__btn.setOnAction(event -> onLogin());
        forgotPassword__btn.setOnAction(event -> onForgotPassword());
        hidePassword__btn.setOnAction(event -> onShowPassword());
        // unset text for errol label
        error__lbl.setText("");
    }


    private void onLogin() {
        Stage stage = (Stage) error__lbl.getScene().getWindow();
        // Security to validate account.
        String email = "";
        String password = "";
        try {
            email = email__textField.getText();
        } catch (Exception e) {
            e.printStackTrace();
            error__lbl.setText("Please Input Email");
        }
        try {
            password = password__passwordField.getText();
        } catch (Exception e) {
            e.printStackTrace();
            error__lbl.setText("Please Input Password");
        }



        // ---------------------- Verify account from server -------------------------//
        String finalEmail = email;
        String finalPassword = password;
        Thread signInThread = new Thread(() -> {
            // Encrypt account info
            String emailEncrypt = Encrypt.encodePassword(finalEmail);
            String passwordEncrypt = Encrypt.encodePassword(finalPassword);
            String messageForm = "evaluateAccount_" + finalEmail + "_" + finalPassword;

            System.out.println("[Client Log] --> " + messageForm);
            Model.getInstance().getSocketManager().sendMessage(messageForm);
            try {
                String messageResponse = Model.getInstance().getSocketManager().receiverMessage();
                String[] messageSplit = messageResponse.split("_");
                Platform.runLater(() -> {
                    if (messageSplit[6].equals("success")) {
                        System.out.println(messageResponse);
                        String accountID = messageSplit[1];
                        // Set currentAccount object
                        Model.getInstance().setCurrentClient(accountID);
                        // set targetAccount object
                        Model.getInstance().setTargetClient(accountID);
                        // add online client to clientList


                        // Show main chat window
                        Model.getInstance().getViewFactory().showClientWindow();
                        // Close login stage
                        Model.getInstance().getViewFactory().closeStage(stage);

                    } else {
                        email__textField.setText("");
                        password__textField.setText("");
                        error__lbl.setText("No Such Login Credential!");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error at Evaluating account");
            }
        });
        signInThread.start();

    }

    private void onSignUpView() {
        Model.getInstance().getViewFactory().getLoginSelectedMenuItem().set(LoginViewOptions.SIGNUP);
    }

    private void onShowPassword() {
        clickCount++;
        if (clickCount % 2 == 1) {
            password__textField.textProperty().bindBidirectional(password__passwordField.textProperty());
            password__passwordField.setVisible(false);
        } else {
            password__passwordField.setVisible(true);
        }

    }

    private void onForgotPassword() {
        Stage stage = (Stage) error__lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().showForgotPasswordWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
