package web;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class ServerConnector {

    public void connect(URI uri, String requestType, String header, String body) throws Exception {
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setReadTimeout(5000);
        http.setRequestMethod(requestType);


        if (Objects.equals(requestType, "POST")) {
            // Specify that we are going to write out data
            http.setDoOutput(true);
            //header
            if (header != null) {
                http.addRequestProperty("Authorization", header);
            }
        }

        // Make the request
        http.connect();

        //body
        if (body != null) {
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }

        // Output the response body
        if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
            }
        } else {
            InputStream responseBody = http.getErrorStream();
            if (responseBody != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
    }
}
