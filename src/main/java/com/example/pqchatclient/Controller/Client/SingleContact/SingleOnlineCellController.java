package com.example.pqchatclient.Controller.Client.SingleContact;

import com.example.pqchatclient.Model.Client;
import io.github.gleidson28.GNAvatarView;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;

public class SingleOnlineCellController implements Initializable {

    public GNAvatarView clientAvatar__img;

    public Label clientName__lbl;
    private final Client targetClient;

    public SingleOnlineCellController(Client targetClient) {
        this.targetClient = targetClient;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientAvatar__img.setImage(new Image(targetClient.imagePathProperty().get()));
        clientName__lbl.textProperty().bind(targetClient.firstNameProperty());
    }
}