package com.example.pqchatclient.Controller.Login;

import com.example.pqchatclient.Model.Model;
import com.example.pqchatclient.View.LoginViewOptions;
import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.pqchatclient.Utilities.Encrypt.encodePassword;

public class SignUpController implements Initializable {
    public TextField email__textField;
    public PasswordField password__textField;
    public Button createAccount__btn;
    public Button signIn__btn;
    public TextField validationCode__textField;
    public CheckBox pqTerms__checkBox;
    public Label error__lbl;
    public Button hidePassword__btn;
    public Button sendValidationCode__btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(
                event -> error__lbl.setText("")
        );
        pauseTransition.play();

        sendValidationCode__btn.setOnAction(event -> onValidateCode());
    }

    private void onValidateCode() {
        String email = email__textField.getText();
        String password = encodePassword(password__textField.getText());
        String messageForm = "validateAccount_" + email + "_" + password;
        Model.getInstance().getSocketManager().sendMessage(messageForm);
    }

    private void addListeners() {
        signIn__btn.setOnAction(event -> onSignInView());
        createAccount__btn.setOnAction(event -> onCreateAccount());
    }

    private void onCreateAccount() {
        if(!pqTerms__checkBox.isSelected()){
            error__lbl.setText("Click the phuquocchamp's Terms & Condition first !");
        }else{
            String email = email__textField.getText();
            String password = encodePassword(password__textField.getText());
            String messageForm = "signUpAccount_" + email + "_" + password;
            Model.getInstance().getSocketManager().sendMessage(messageForm);


        }

    }

    private void onSignInView() {
        Model.getInstance().getViewFactory().getLoginSelectedMenuItem().set(LoginViewOptions.SIGNIN);
    }
}
