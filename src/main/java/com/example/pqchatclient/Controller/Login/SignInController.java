package com.example.pqchatclient.Controller.Login;

import com.example.pqchatclient.Model.Model;
import com.example.pqchatclient.View.LoginViewOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    }


    private void onLogin() {
        Stage stage = (Stage) error__lbl.getScene().getWindow();
        // Security to validate account.
        String email = "";
        String password = "";
        try{
            email = email__textField.getText();
        }catch (Exception e){
            e.printStackTrace();
            error__lbl.setText("Please Input Email");
        }
        try{
            password = password__passwordField.getText();
        }catch (Exception e){
            e.printStackTrace();
            error__lbl.setText("Please Input Password");
        }
        if(Model.getInstance().getDatabaseDriver().evaluatedAccount(email, password)){
            // Set Current Account

            ResultSet currentAccountRS = Model.getInstance().getDatabaseDriver().getCurrentAccount(email, password);
            try{
                if (currentAccountRS.isBeforeFirst()){
                    String accountID = currentAccountRS.getString("accountID");
                    // Set currentAccount object
                    Model.getInstance().setCurrentClient(accountID);
                    // set targetAccount object
                    Model.getInstance().setTargetClient(accountID);
                    // Set lastMessage
                    Model.getInstance().setLastSingleMessage();
                }
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Error at getCurrentAccount function in SignInController Class!");
            }
            Model.getInstance().getSocketManager().sendMessage("clientLogin_" + Model.getInstance().getCurrentClient().clientIDProperty().get());
            Model.getInstance().getViewFactory().showClientWindow();
            // Close login stage
            Model.getInstance().getViewFactory().closeStage(stage);

        }else{
            email__textField.setText("");
            password__textField.setText("");
            error__lbl.setText("No Such Login Credential!");
        }



    }

    private void onSignUpView() {
        Model.getInstance().getViewFactory().getLoginSelectedMenuItem().set(LoginViewOptions.SIGNUP);
    }

    private void onShowPassword() {
        clickCount++;
        if(clickCount%2 == 1){
            password__textField.textProperty().bindBidirectional(password__passwordField.textProperty());
            password__passwordField.setVisible(false);
        }else{
            password__passwordField.setVisible(true);
        }

    }

    private void onForgotPassword(){
        Stage stage = (Stage) error__lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().showForgotPasswordWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
