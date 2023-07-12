package com.example.pqchatclient.Controller.Login;

import com.example.pqchatclient.Model.Model;
import com.example.pqchatclient.View.LoginViewOptions;
import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public TextField email__textField;
    public PasswordField password__textField;
    public Button createAccount__btn;
    public Button signIn__btn;
    public TextField validationCode__textField;
    public CheckBox pqTerms__checkBox;
    public Label error__lbl;
    public Button hidePassword__btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(
                event -> error__lbl.setText("")
        );
        pauseTransition.play();
    }

    private void addListeners() {
        signIn__btn.setOnAction(event -> onSignInView());
        createAccount__btn.setOnAction(event -> onCreateAccount());
    }

    private void onCreateAccount() {
        if(!pqTerms__checkBox.isSelected()){
            error__lbl.setText("Click the phuquocchamp's Terms & Condition first !");

        }
    }

    private void onSignInView() {
        Model.getInstance().getViewFactory().getLoginSelectedMenuItem().set(LoginViewOptions.SIGNIN);
    }
}
