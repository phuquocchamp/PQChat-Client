package com.example.pqchatclient.Model;

import java.io.*;
import java.net.Socket;

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

    public void sendFile(File file){
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            OutputStream outputStream = clientSocket.getOutputStream();
            while ((bytesRead = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Sending file error!");
        }

    }

    public void receiverFile(File file){
        try{
             FileOutputStream fileOutputStream = new FileOutputStream(file);
             byte[] buffer = new byte[1024];
             int bytesRead;
             InputStream inputStream = clientSocket.getInputStream();
             while((bytesRead = inputStream.read(buffer)) != -1){
                 fileOutputStream.write(buffer, 0, bytesRead);
             }
             fileOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error at receiving file!");
        }
    }

    public void sendImage(){

    }

    public BufferedReader getClientReader() {
        return clientReader;
    }

    public BufferedWriter getClientWriter() {
        return clientWriter;
    }
}
