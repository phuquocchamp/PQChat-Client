package com.example.pqchatclient.Controller.Client;

import com.example.pqchatclient.Model.Model;
import io.github.gleidson28.GNAvatarView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ChattingController implements Initializable {
    public Label senderName__lbl;
    public GNAvatarView senderAvatar__img;
    public ScrollPane messageContainer__scrollPane;
    public TextArea enterMessage__TextArea;
    public Button sendMessage__btn;
//    public VBox messageSection__vBox;
    public Label status;
    public Button fileSend__btn;
    public Button imageSend__btn;
    public Button emojiSend__btn;



    public VBox messageBox__vBox;
    public Button download__btn;
    // Variable

    private String targetClientID;


    private Map<String, VBox> messageBoxMap;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread chattingThread = new Thread(() -> {
            messageBoxMap = new HashMap<>();


            Platform.runLater(() -> {


                // Listen for changes in targetClient
                targetClientID = Model.getInstance().getTargetClient().clientIDProperty().get();
                Model.getInstance().getTargetClient().clientIDProperty().addListener((observableValue, oldValue, newValue) -> {
                    targetClientID = Model.getInstance().getTargetClient().clientIDProperty().get();
                    senderName__lbl.textProperty().bind(Bindings.concat(Model.getInstance().getTargetClient().lastNameProperty().concat(" ").concat(Model.getInstance().getTargetClient().firstNameProperty())));


                    Platform.runLater(() -> {
                        senderAvatar__img.imageProperty().bind(Bindings.createObjectBinding(() -> new Image(Model.getInstance().getTargetClient().imagePathProperty().get())));

                        System.out.println(Model.getInstance().getTargetClient().firstNameProperty().get() + " panel");
                        if(!messageBoxMap.containsKey(targetClientID)){
                            messageBox__vBox = new VBox();
                            messageBox__vBox.setFillWidth(true);
                            messageBox__vBox.setPadding(new Insets(10, 0 , 0, 10));

                            // Add messageBox__vBox into map
                            messageBoxMap.put(targetClientID, messageBox__vBox);
                        }
                        VBox targetBox = messageBoxMap.get(targetClientID);
                        targetBox.setFillWidth(true);
                        targetBox.setPadding(new Insets(10, 0 , 0, 10));

                        messageContainer__scrollPane.setContent(targetBox);

                        loadMessage();

                        // Initialize the client connection and start receiving messages from the server.
                        serverResponse();
                        // Sending message
                        sendMessage__btn.setOnAction(event -> onSendingMessage());


                        fileSend__btn.setOnAction(event -> onSendingFile());
                        imageSend__btn.setOnAction(event -> onSendingImage());
                        emojiSend__btn.setOnAction(event -> onSendingEmoji());

                        targetBox.heightProperty().addListener(new ChangeListener<Number>() {
                            @Override
                            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                                messageContainer__scrollPane.setVvalue((Double) newValue);
                            }
                        });

                    });
                });



            });




            // Load chatting message from database.
            senderName__lbl.textProperty().bind(Bindings.concat(Model.getInstance().getTargetClient().lastNameProperty().concat(" ").concat(Model.getInstance().getTargetClient().firstNameProperty())));
            senderAvatar__img.imageProperty().bind(Bindings.createObjectBinding(() -> new Image(Model.getInstance().getTargetClient().imagePathProperty().get())));

            //loadData();


        });

        chattingThread.setDaemon(true);
        chattingThread.start();

    }


    private void loadMessage() {

    }


    private void onSendingEmoji() {
    }

    private void onSendingImage() {
        Stage stage = (Stage) sendMessage__btn.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        File selectedImage = fileChooser.showOpenDialog(stage);
        fileChooser.setTitle("Open Image");
        if(selectedImage != null){
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedImage);
                byte[] fileData = new byte[(int) selectedImage.length()];
                fileInputStream.read(fileData);
                fileInputStream.close();

                download__btn = new Button();
                generateImageBox(selectedImage, getTimeNow(),"CENTER_RIGHT");

                saveImageFile(selectedImage);

                // Sending data to socket

                String targetClientID = Model.getInstance().getTargetClient().clientIDProperty().get();
                String currentClientID = Model.getInstance().getCurrentClient().clientIDProperty().get();
                String fileName = selectedImage.getName();
                String timeCreated = getTimeNow();
                String messageForm = "imageTransfer_" + currentClientID + "_" + targetClientID + "_" + fileName+ "_" + timeCreated ;
                System.out.println(Model.getInstance().getCurrentClient().firstNameProperty().get() + " send image to " + Model.getInstance().getTargetClient().firstNameProperty().get());

                Model.getInstance().getSocketManager().sendMessage(messageForm);
                Model.getInstance().getSocketManager().sendFile(selectedImage);


                System.out.println("Sending : "+ messageForm);

            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Sending file error!");
            }

        }
    }

    private void saveImageFile(File selectedImage) throws IOException {
        Stage stage = (Stage) sendMessage__btn.getScene().getWindow();

        FileInputStream fileInputStream = new FileInputStream(selectedImage);
        byte[] fileData = new byte[(int) selectedImage.length()];
        fileInputStream.read(fileData);
        fileInputStream.close();

        // Lấy đuôi file
        String filePath = selectedImage.getAbsolutePath();
        Path path = Paths.get(filePath);
        String fileExtension = path.getFileName().toString().split("\\.")[1];


        // download image
        download__btn.setOnAction(event -> {
            FileChooser saveFile = new FileChooser();
            saveFile.setInitialFileName("Image." + fileExtension);


            File localSaveFile = saveFile.showSaveDialog(stage);
            saveFile.setTitle("Save Image");
            if(localSaveFile != null){
                try{
                    FileOutputStream fileOutputStream = new FileOutputStream(localSaveFile);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("Error at saving image");
                }
            }

        });

    }

    private void generateImageBox(File selectedImage, String timeCreated, String align) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(selectedImage);
        byte[] fileData = new byte[(int) selectedImage.length()];
        fileInputStream.read(fileData);
        fileInputStream.close();

        HBox container = new HBox();
        container.setAlignment(Pos.valueOf(align));
        container.setPadding(new Insets(10, 5, 5, 10));

        FontIcon dl__icon = new FontIcon("fltfal-arrow-download-16");
        dl__icon.setIconSize(15);
        dl__icon.setIconColor(Color.DARKGRAY);
        download__btn.setGraphic(dl__icon);
        download__btn.setStyle("-fx-background-color: transparent; -fx-text-alignment: center; -fx-cursor: hand;");

        download__btn.setPadding(new Insets(0 , 15, 0 , 0));
        // Download picture function


        Image image = new Image(new ByteArrayInputStream(fileData));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle();
        clip.setWidth(400);
        clip.setHeight(200);

        clip.setArcHeight(20.0);
        clip.setArcWidth(20.0);

        imageView.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage writableImage = imageView.snapshot(parameters, null);
        imageView.setClip(null);
        imageView.setEffect(new DropShadow(20, Color.DARKGRAY));
        imageView.setImage(writableImage);

        Text timeText = new Text(timeCreated);
        timeText.setStyle("-fx-fill: gray;");

        TextFlow timeCreated_tf = new TextFlow(timeText);
        timeCreated_tf.setPadding(new Insets(15, 0, 0, 15));


        container.getChildren().addAll(download__btn, imageView, timeCreated_tf);
        messageBox__vBox.getChildren().addAll(container);

    }

    private void onSendingFile() {

        Stage stage = (Stage) sendMessage__btn.getScene().getWindow();


        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        fileChooser.setTitle("Open File");
        if(selectedFile != null){
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                byte[] fileData = new byte[(int) selectedFile.length()];
                fileInputStream.read(fileData);
                fileInputStream.close();

                download__btn = new Button();
                generateFileBox(selectedFile.getName(),getTimeNow(),"CENTER_RIGHT");

//                saveFile(selectedFile);


                // Sending data to socket
                String targetClientID = Model.getInstance().getTargetClient().clientIDProperty().get();
                String currentClientID = Model.getInstance().getCurrentClient().clientIDProperty().get();
                String timeCreated = getTimeNow();
                String messageForm = "fileTransfer_" + currentClientID + "_" + targetClientID + "_" + timeCreated ;
                Model.getInstance().getSocketManager().sendFile(selectedFile);

            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Sending file error!");
            }

        }
    }

//    private void saveFile(File selectedFile, String fileName ) {
//        Stage stage = (Stage) sendMessage__btn.getScene().getWindow();
//
//        download__btn.setOnAction(event -> {
//            FileChooser saveFile = new FileChooser();
//            saveFile.setInitialFileName(fileName);
//            saveFile.setTitle("Save File");
//            File localSaveFile = saveFile.showSaveDialog(stage);
//            if(localSaveFile != null){
//                try{
//                    FileOutputStream fileOutputStream = new FileOutputStream(localSaveFile);
//                    fileOutputStream.write();
//                }catch (Exception e){
//                    e.printStackTrace();
//                    System.out.println("Eror at saving file!");
//                }
//            }
//
//        });
//
//    }

    private void generateFileBox(String enterMessage, String timeCreated, String align) {
        HBox container = new HBox();
        container.setAlignment(Pos.valueOf(align));
        container.setPadding(new Insets(5, 5, 5, 10));

        FontIcon dl__icon = new FontIcon("fltfal-arrow-download-16");
        dl__icon.setIconSize(15);
        dl__icon.setIconColor(Color.DARKGRAY);
        download__btn.setGraphic(dl__icon);
        download__btn.setStyle("-fx-background-color: transparent; -fx-text-alignment: center; -fx-cursor: hand;");

        download__btn.setPadding(new Insets(0 , 15, 0 , 0));
        // Download picture function


        // Text Message
        Text text = new Text(enterMessage);
        TextFlow textFlow = new TextFlow();
        textFlow.setStyle(
                "-fx-color: rgb(239, 242, 255);" +
                        "-fx-font-size: 16px;"+
                        "-fx-background-color: #1B292A;" +
                        "-fx-background-radius: 15px 15px 15px 15px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.925, 0.996));

        Text timeText = new Text(timeCreated);
        timeText.setStyle("-fx-fill: gray;");

        TextFlow timeCreated_tf = new TextFlow(timeText);
        timeCreated_tf.setPadding(new Insets(15, 0, 0, 15));

        textFlow.getChildren().add(text);
        container.getChildren().addAll( download__btn, textFlow, timeCreated_tf);
        messageBox__vBox.getChildren().add(container);
        enterMessage__TextArea.setText("");
    }


    private void onSendingMessage() {

        String enterMessage = enterMessage__TextArea.getText();
        if (!enterMessage.isEmpty()) {
            // singleChat_sender_receiver_messageContent
            String currentClientID = Model.getInstance().getCurrentClient().clientIDProperty().get();
            // TimeCreated
            String timeCreated =  getTimeNow();
            String messageForm = "singleChat_" + currentClientID + "_" + targetClientID + "_" + enterMessage + "_" + timeCreated;
            System.out.println(Model.getInstance().getCurrentClient().firstNameProperty().get() + " send message to " + Model.getInstance().getTargetClient().firstNameProperty().get());

            System.out.println(messageForm);
            Model.getInstance().getSocketManager().sendMessage(messageForm);
            // Show on GUI
            generateMessageBox(enterMessage, timeCreated, "CENTER_RIGHT");

            // Binding lastMessageCell
            Model.getInstance().getLastSingleMessage().messageProperty().set(enterMessage);
            Model.getInstance().getLastSingleMessage().timeCreatedProperty().set(timeCreated);
            Model.getInstance().getLastSingleMessage().senderProperty().set(currentClientID);
            Model.getInstance().getLastSingleMessage().receiverProperty().set(targetClientID);
        }
    }

    private void serverResponse() {

        try {
            Thread clientThread = new Thread(() -> {

                // Process the stream from the server
                Thread clientReceive = new Thread(() -> {
                    try {

                        while (true) {
                            String streamMessage = Model.getInstance().getSocketManager().receiverMessage();
                            if (streamMessage == null) {
                                break;
                            }
                            String[] messageSplit = streamMessage.split("_");
                            // Get Client ID of

                            // Update Online List

                            // Single Chat
                            Platform.runLater(() ->{
                                if(messageSplit[0].equals("singleChat")){
                                    // Show on GUI
                                    String mesage = messageSplit[3];
                                    String timeCreated = messageSplit[4];
                                    String receiver = messageSplit[2];
                                    String sender = messageSplit[1];
                                    if(receiver.equals(Model.getInstance().getCurrentClient().clientIDProperty().get())){
                                        System.out.println(streamMessage);

                                        Platform.runLater(() -> {
                                            generateMessageBox(messageSplit[3], timeCreated, "CENTER_LEFT");
//                                            Model.getInstance().getDatabaseDriver().setSingleConversation(sender,receiver, mesage, timeCreated);
                                        });
                                    }

                                }else if(messageSplit[0].equals("fileTransfer")){
                                    String sender = messageSplit[1];
                                    String receiver = messageSplit[2];
                                    String timeCreated = messageSplit[3];
                                    if(receiver.equals(Model.getInstance().getCurrentClient().clientIDProperty().get())){
                                        System.out.println(streamMessage);

                                        Platform.runLater(() -> {
//                                            showScrollPanel(sender);
//                                            generateMessageBox(messageSplit[3], timeCreated, "CENTER_LEFT");
                                        });


                                    }


                                }else if(messageSplit[0].equals("imageTransfer")){
                                    String sender = messageSplit[1];
                                    String receiver = messageSplit[2];
                                    String fileName = messageSplit[3];
                                    String timeCreated = messageSplit[4];

                                    if(receiver.equals(Model.getInstance().getCurrentClient().clientIDProperty().get())){
                                        System.out.println("Receive :" + streamMessage);

                                        Platform.runLater(() -> {

                                            String projectPath = "D:\\Coding\\Back End\\Java\\JavaFX\\PQChat-Client\\src\\main\\resources\\Files\\";
                                            File imageFile = new File(projectPath + fileName);
                                            Model.getInstance().getSocketManager().receiverFile(imageFile);
                                            try {
                                                generateImageBox(imageFile, timeCreated, "CENTER_LEFT");
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        });


                                    }

                                }

                            });


                            // Group Chat
                            if (messageSplit[0].equals("groupChat")) {
                                System.out.println(messageSplit[1]);
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Client Receive Stream Error!");
                    }
                });
                clientReceive.setDaemon(true); // Set as daemon thread to automatically terminate with the main thread
                clientReceive.start();
            });

            clientThread.setDaemon(true); // Set as daemon thread to automatically terminate with the main thread
            clientThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Utilities

    // Get the time now
    private String getTimeNow() {
        LocalTime localTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return  localTime.format(formatter);
    }

       // Generate MessageBox
    private void generateMessageBox(String enterMessage, String timeCreated, String align) {


        HBox container = new HBox();
        container.setAlignment(Pos.valueOf(align));
        container.setPadding(new Insets(5, 5, 5, 10));

        // Text Message
        Text text = new Text(enterMessage);
        TextFlow textFlow = new TextFlow();
        textFlow.setStyle(
                "-fx-color: rgb(239, 242, 255);" +
                        "-fx-font-size: 16px;"+
                        "-fx-background-color: #1B292A;" +
                        "-fx-background-radius: 15px 15px 15px 15px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.925, 0.996));

        Text timeText = new Text(timeCreated);
        timeText.setStyle("-fx-fill: gray;");

        TextFlow timeCreated_tf = new TextFlow(timeText);
        timeCreated_tf.setPadding(new Insets(15, 0, 0, 15));

        textFlow.getChildren().add(text);
        container.getChildren().addAll( textFlow, timeCreated_tf);


        messageBox__vBox.getChildren().add(container);

        enterMessage__TextArea.setText("");
    }


}




