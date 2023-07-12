package com.example.pqchatclient.View;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {


    // Window View
    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login/Login.fxml"));
        createStage(loader);
    }
    public void showClientWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
//        ClientController clientController = new ClientController();
//        fxmlLoader.setController(clientController);
        createStage(fxmlLoader);
    }
    public void showForgotPasswordWindow(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Login/ForgotPassword.fxml"));
        createStage(fxmlLoader);
    }
    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/conversationLogo.png"))));
        stage.setResizable(false);
//        stage.setTitle("PQCHAT");
        stage.show();
    }
    public void closeStage(Stage stage){
        stage.close();
    }


    // Login View
    private final ObjectProperty<LoginViewOptions> loginSelectedMenuItem;
    private AnchorPane signIn;
    private AnchorPane signUp;
    // Client View
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane singleChat;
    private AnchorPane groupChat;


    public ViewFactory() {
        this.loginSelectedMenuItem = new SimpleObjectProperty<>();
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
    }
    // Login View
    public ObjectProperty<LoginViewOptions> getLoginSelectedMenuItem(){
        return loginSelectedMenuItem;
    }

    public AnchorPane getSignInView() throws IOException {
        if(signIn == null){
            signIn = new FXMLLoader(getClass().getResource("/Fxml/Login/SignIn.fxml")).load();
        }
        return signIn;
    }

    public AnchorPane getSignUpView() throws IOException {
        if(signUp == null){
            signUp = new FXMLLoader(getClass().getResource("/Fxml/Login/SignUp.fxml")).load();
        }
        return signUp;
    }





    // Client View


    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem(){
        return clientSelectedMenuItem;
    }

    public AnchorPane getSingleChatView() {
        if(singleChat == null){
            try{
                singleChat = new FXMLLoader(getClass().getResource("/Fxml/Client/SingleContact/SingleContact.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return singleChat;
    }

    public AnchorPane getGroupChatView() {
        if(groupChat == null) {
            try {
                groupChat = new FXMLLoader(getClass().getResource("/Fxml/Client/GroupContact/GroupContact.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return groupChat;
    }
}
