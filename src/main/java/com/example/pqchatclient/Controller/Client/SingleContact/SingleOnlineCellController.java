package com.example.pqchatclient.Controller.Client.SingleContact;

import com.example.pqchatclient.Model.Client;
import io.github.gleidson28.GNAvatarView;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class SingleOnlineCellController implements Initializable {
    public GNAvatarView clientAvatar_img;
    public Label clientName__lbl;
    private final Client targetClient;

    public SingleOnlineCellController(Client targetClient) {
        this.targetClient = targetClient;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientAvatar_img.setImage(new Image(getClass().getResourceAsStream(targetClient.imagePathProperty().getValue())));
        clientName__lbl.textProperty().bind(targetClient.firstNameProperty());
    }
}