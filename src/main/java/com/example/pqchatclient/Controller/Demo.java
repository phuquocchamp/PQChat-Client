package com.example.pqchatclient.Controller;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Demo {

    public static void main(String[] args) {
        File imageFile = new File("/home/phuquocchamp/Coding/BE/Java/JavaFX/PQChat-Client/src/main/resources/Images/Clients/phuquocchamp.jpg");
        String fileExtension = FilenameUtils.getExtension(imageFile.getName());
        System.out.println(fileExtension);
    }
}
