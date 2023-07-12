module com.example.chattingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fluentui;
    requires java.sql;
    requires mysql.connector.j;
    requires org.xerial.sqlitejdbc;
    requires GNAvatarView;
    requires jbcrypt;

                            
    opens com.example.pqchatclient to javafx.fxml;
    exports com.example.pqchatclient;
    exports com.example.pqchatclient.Utilities;
    exports com.example.pqchatclient.Controller;
    exports com.example.pqchatclient.Controller.Client;
    exports com.example.pqchatclient.Controller.Login;


    exports com.example.pqchatclient.Model;
    exports com.example.pqchatclient.View;
    exports com.example.pqchatclient.Controller.Client.GroupContact;
    exports com.example.pqchatclient.Controller.Client.SingleContact;

}