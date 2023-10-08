package com.example.pqchatclient.Controller.Client.SingleContact;

import com.example.pqchatclient.Model.Client;
import com.example.pqchatclient.Model.Model;
import io.github.gleidson28.GNAvatarView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SingleMessagesCellController implements Initializable {
 
    public Label timeCreated__lbl;
    public GNAvatarView clientAvatar__img;
    public Label clientName__lbl;
    public Label lastMessages__lbl;
    private final Client targetClient;

    public SingleMessagesCellController(Client targetClient){
        this.targetClient = targetClient;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            clientName__lbl.textProperty().bind(Bindings.concat(targetClient.lastNameProperty().concat(" ").concat(targetClient.firstNameProperty())));
//            clientAvatar__img.setImage(new Image(targetClient.imagePathProperty().get()));


            //
            String targetClientID = targetClient.clientIDProperty().get();
            String currentClientID = Model.getInstance().getCurrentClient().clientIDProperty().get();


            ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSingleConversation(currentClientID, targetClientID, 1);
            while (resultSet.next()){
                lastMessages__lbl.textProperty().set(resultSet.getString("Message"));
                timeCreated__lbl.textProperty().set(resultSet.getString("timeCreated"));
            }

            Model.getInstance().getLastSingleMessage().messageProperty().addListener((observableValue, oldValue, newValue) -> {

                if(Model.getInstance().getLastSingleMessage().receiverProperty().get().equals(targetClientID) || Model.getInstance().getLastSingleMessage().senderProperty().get().equals(targetClientID)){
                    lastMessages__lbl.textProperty().bind(Model.getInstance().getLastSingleMessage().messageProperty());
                    timeCreated__lbl.textProperty().bind(Model.getInstance().getLastSingleMessage().timeCreatedProperty());
                }

            });





        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Null Value");
        }

    }
}
