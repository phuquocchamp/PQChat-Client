package com.example.pqchatclient.Controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Demo {

    public static void main(String[] args) {
        try {
            String apiKey = "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";
            String prompt = "Translate the following English text to French: ";
            String model = "text-davinci-003";  // GPT-3 model

            String apiUrl = "https://api.openai.com/v1/engines/" + model + "/completions";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            // Prepare the input data
            String postData = "{\"prompt\": \"" + prompt + "\"}";

            // Send the request
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(postData);
            }

            // Get the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                System.out.println("GPT-3 Response: " + response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
