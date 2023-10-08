package com.example.pqchatclient.Model;

import com.example.pqchatclient.Utilities.Encrypt;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DatabaseDriver {
    // SQLite
    private Connection connection;
    public DatabaseDriver() {
        try{
            this.connection = DriverManager.getConnection("jdbc:sqlite:pqchat_database.db");

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // Get Client List
    public ResultSet getClientList(){
        String currentClient = Model.getInstance().getCurrentClient().clientIDProperty().get();
        ResultSet resultSet = null;
        try {
            String sql = "SELECT * FROM Client WHERE clientID != ?;";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, currentClient);
            resultSet = preparedStatement.executeQuery();
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    // Get Single Conversation
    public ResultSet getSingleConversation(String Sender, String Receiver, int LIMIT){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM SingleConversation where (Sender = ? AND Receiver = ?) OR (Sender = ? AND Receiver = ?) ORDER BY ID DESC LIMIT ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, Sender);
            preparedStatement.setString(2, Receiver);
            preparedStatement.setString(3, Receiver);
            preparedStatement.setString(4, Sender);
            preparedStatement.setInt(5, LIMIT);
            resultSet = preparedStatement.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error at getLastSingleConversation Function");
        }
        return  resultSet;
    }
    // Set singleConversation
    public void setSingleConversation(String sender, String receiver, String message, String timeCreated){

        try{
            String sql = "INSERT INTO SingleConversation (Sender, Receiver, Message, timeCreated) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, receiver);
            preparedStatement.setString(3, message);
            preparedStatement.setString(4, timeCreated);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0){
                System.out.println("Insert singleConversation fail!");
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at setSingleConversation function in database class!");
        }

    }


    // get Current Account
    public ResultSet getCurrentAccount(String email, String password){
        String emailEncrypt = Encrypt.encodePassword(email);
        String passwordEncrypt = Encrypt.encodePassword(password);
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Account WHERE Username = ? AND Password = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, emailEncrypt);
            preparedStatement.setString(2, passwordEncrypt);
            resultSet = preparedStatement.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at getCurrentAccount Function in DatabaseDriver Class");
        }
        return resultSet;
    }

    // Get current Client
    public ResultSet getCurrentClient(String accountID){
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM Client WHERE accountID = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, accountID);
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error at getCurrentClient Function in DatabaseDriver Class");
        }
        return resultSet;
    }


}
