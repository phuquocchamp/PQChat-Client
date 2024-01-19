package com.example.pqchatclient.Model;

import com.example.pqchatclient.View.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private final SocketManager socketManager;
    private final SingleConversation lastSingleMessage;

    // Client Data Section
    private final Client currentClient;
    private final Client targetClient;
    private final ObservableList<Client> onlineClientList;

    private Model() throws IOException {
        this.viewFactory =  new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.socketManager = new SocketManager();


        // Client Constructor
        this.currentClient = new Client("", "", "", "", "");
        this.targetClient = new Client("", "", "", "", "");

        this.lastSingleMessage = new SingleConversation("","", "", "");

        this.onlineClientList = FXCollections.observableArrayList();

    }

    // Singleton
    public static synchronized Model getInstance(){
        if(model == null){
            try{
                model = new Model();

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return model;
    }

    public ViewFactory getViewFactory(){
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver(){
        return databaseDriver;
    }

    public SocketManager getSocketManager(){
        return socketManager;
    }

    // Client Request


    public ObservableList<Client> getOnlineClientList() {
        return onlineClientList;
    }

    public Client getCurrentClient(){
        return currentClient;
    }
    public Client getTargetClient(){
        return targetClient;
    }


    public SingleConversation getLastSingleMessage(){
        return lastSingleMessage;
    }

    public void setLastSingleMessage(String sender, String receiver, String message, String timeCreated){

        String currentClientID = currentClient.clientIDProperty().get();
        String targetClientID = targetClient.clientIDProperty().get();
        ResultSet resultSet = databaseDriver.getSingleConversation(currentClientID, targetClientID, 1);
        try{
            while (resultSet.next()){
                this.lastSingleMessage.senderProperty().set(resultSet.getString("Sender"));
                this.lastSingleMessage.receiverProperty().set( resultSet.getString("Receiver"));
                this.lastSingleMessage.messageProperty().set(resultSet.getString("Message"));
                this.lastSingleMessage.timeCreatedProperty().set(resultSet.getString("timeCreated"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQL Error at SingleMessagesCellController!");
        }
    }

    //
    public void setCurrentClient(String accountID){
        ResultSet resultSet = databaseDriver.getCurrentClient(accountID);
        try{
            if (resultSet.isBeforeFirst()){
                this.currentClient.clientIDProperty().set(resultSet.getString("clientID"));
                this.currentClient.accountIDProperty().set(resultSet.getString("accountID"));
                this.currentClient.firstNameProperty().set(resultSet.getString("firstName"));
                this.currentClient.lastNameProperty().set(resultSet.getString("lastName"));
                this.currentClient.imagePathProperty().set(resultSet.getString("imagePath"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at setCurrentAccount function in Model class");
        }
    }
    //
    public void setTargetClient(String accountID){
        ResultSet resultSet = databaseDriver.getCurrentClient(accountID);
        try{
            if (resultSet.isBeforeFirst()){
                this.targetClient.clientIDProperty().set(resultSet.getString("clientID"));
                this.targetClient.accountIDProperty().set(resultSet.getString("accountID"));
                this.targetClient.firstNameProperty().set(resultSet.getString("firstName"));
                this.targetClient.lastNameProperty().set(resultSet.getString("lastName"));
                this.targetClient.imagePathProperty().set(resultSet.getString("imagePath"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at setCurrentAccount function in Model class");
        }
    }


}
