package com.example.pqchatclient.Model;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class SocketManager {
    private final Socket clientSocket;
    private final BufferedReader clientReader;
    private final BufferedWriter clientWriter;

    public SocketManager() throws IOException {
        this.clientSocket = new Socket("localhost", 7778);
        System.out.println("Connected successfully!");
        this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    public void sendMessage(String message){
        try{
            clientWriter.write(message);
            clientWriter.newLine();
            clientWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Sending file error in SocketManager class");
        }
    }

    public String receiverMessage() throws IOException {
        return clientReader.readLine();
    }

    public void sendFile(File file) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        clientWriter.write(encodedString);
        clientWriter.newLine();
        clientWriter.flush();

    }

    public void receiverFile(File file) throws IOException {
        String encodedString = receiverMessage();
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        FileUtils.writeByteArrayToFile(file, decodedBytes);
    }


    public BufferedReader getClientReader() {
        return clientReader;
    }

    public BufferedWriter getClientWriter() {
        return clientWriter;
    }
}
