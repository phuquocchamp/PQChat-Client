package com.example.pqchatclient.Controller.Client;

import com.example.pqchatclient.Model.Model;
import com.example.pqchatclient.View.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {

    public Button singleChat__btn;
    public Button groupChat__btn;
    public Button setting__btn;
    public Button exit__btn;
    public Button chatBot_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    public void addListeners() {
        singleChat__btn.setOnAction(event -> onSingleChat());
        groupChat__btn.setOnAction(event -> onGroupChat());
        chatBot_btn.setOnAction(event -> onChatBot());
        exit__btn.setOnAction(event -> onExitWindow());
    }


    private void onSingleChat() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.SINGLECHAT);
    }

    private void onGroupChat() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.GROUPCHAT);
    }
    private void onChatBot() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.CHATBOT);
    }

    private void onSetting() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.SETTING);
    }

    private void onExitWindow(){
        Stage stage = (Stage) exit__btn.getScene().getWindow();
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
