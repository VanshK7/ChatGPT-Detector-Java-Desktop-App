package com.example.gptdetector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiService {
    private static final String API_URL = "https://api-inference.huggingface.co/models/roberta-base-openai-detector";
    private static final String API_TOKEN = "Bearer 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";

    public String getHumanScore(String inputText) throws IOException {
        String response = getResponse(inputText);
        String realRegex = "\"Real\",\"score\":0\\.(\\d{2})"; // Real regex pattern
        Pattern pattern = Pattern.compile(realRegex);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IOException("Unable to extract human score");
        }
    }

    public String getFakeScore(String inputText) throws IOException {
        String response = getResponse(inputText);
        Pattern pattern = Pattern.compile("\"label\":\"Fake\",\"score\":(\\d\\.\\d{2})");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1).substring(2);
        } else {
            throw new IOException("Unable to extract fake score");
        }
    }

    private String getResponse(String inputText) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Authorization", API_TOKEN);
        httpConn.setRequestProperty("Content-Type", "application/json");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        String requestBody = "{\"inputs\": \"" + inputText + "\"}";
        writer.write(requestBody);
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner sc = new Scanner(responseStream).useDelimiter("\\A");
        String response = sc.hasNext() ? sc.next() : "";

        return response;
    }
}
