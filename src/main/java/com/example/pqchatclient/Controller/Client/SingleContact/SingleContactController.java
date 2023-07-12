package com.example.pqchatclient.Controller.Client.SingleContact;

import com.example.pqchatclient.Model.Client;
import com.example.pqchatclient.Model.Model;
import com.example.pqchatclient.View.SingleMessagesCellFactory;
import com.example.pqchatclient.View.SingleOnlineCellFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SingleContactController implements Initializable {

    public FontIcon collapse_btn;

    public ListView<Client> userOnline__listView;

    public  ListView<Client> userChat__listView;

    ObservableList<Client> clientList = FXCollections.observableArrayList();
    Client selectedClient;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Online ListView
        getClientList();

        userOnline__listView.setItems(clientList );
        userOnline__listView.setCellFactory(event -> new SingleOnlineCellFactory());





        // Messages ListView
        userChat__listView.setItems(clientList);
        userChat__listView.setCellFactory(event -> new SingleMessagesCellFactory());

        // Get Selected Client
        userChat__listView.setOnMouseClicked(mouseEvent -> {
            selectedClient = userChat__listView.getSelectionModel().getSelectedItem();
            if(selectedClient != null){
                Model.getInstance().getTargetClient().clientIDProperty().bind(selectedClient.clientIDProperty());
                Model.getInstance().getTargetClient().accountIDProperty().bind(selectedClient.accountIDProperty());
                Model.getInstance().getTargetClient().firstNameProperty().bind(selectedClient.firstNameProperty());
                Model.getInstance().getTargetClient().lastNameProperty().bind(selectedClient.lastNameProperty());
                Model.getInstance().getTargetClient().imagePathProperty().bind(selectedClient.imagePathProperty());
            }
        });


    }

    private void getClientList() {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientList();
        try{
            while(resultSet.next()){
                String clientID = resultSet.getString("clientID");
                String accountID = resultSet.getString("accountID");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String imagePath = resultSet.getString("imagePath");
                clientList.add(new Client(clientID, accountID, firstName, lastName, imagePath));
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at getClientList function!");
        }
    }


}
