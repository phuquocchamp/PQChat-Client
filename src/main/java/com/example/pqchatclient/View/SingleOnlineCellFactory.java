package com.example.pqchatclient.View;

import com.example.pqchatclient.Controller.Client.SingleContact.SingleOnlineCellController;
import com.example.pqchatclient.Model.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;
public class SingleOnlineCellFactory extends ListCell<Client> {
    @Override
    protected void updateItem(Client client, boolean empty) {
        super.updateItem(client, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Client/SingleContact/SingleOnlineCell.fxml"));
            SingleOnlineCellController controller = new SingleOnlineCellController(client);
            fxmlLoader.setController(controller);
            setText(null);

            try {
                setGraphic(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

  