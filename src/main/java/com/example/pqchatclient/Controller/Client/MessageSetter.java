package com.example.pqchatclient.Controller.Client;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MessageSetter {
    public static HBox makeMessageArea(String message){
        Label label  = new Label(message);
        HBox messageHolder = new HBox(5);
//        Circle avatar = new Circle(20);

        messageHolder.getChildren().addAll(label);
        return messageHolder;
    }
}
