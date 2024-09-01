//package com.sangh.bioauth;
//
//import android.os.AsyncTask;
//
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class FlaskAppComm extends AsyncTask<String, Void, Void> {
//
//    @Override
//    protected Void doInBackground(String... params) {
//
//        String jsonData = params[0];
//        try {
//            // URL of your Flask server's endpoint
//            URL url = new URL("https://192.168.11.63:8080/endpoint");
//            System.out.println("in try");
//
//            // Open connection
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setDoOutput(true);
//
//            // Message to send
////            String message = "hello";
//
//            // Write message to the connection's output stream
//            OutputStream os = conn.getOutputStream();
//            os.write(jsonData.getBytes());
//            os.flush();
//            os.close();
//
//            // Get response code (optional)
//            int responseCode = conn.getResponseCode();
//            System.out.println("Response Code: " + responseCode);
//
//            // Close connection
//            conn.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("in catch");
//        }
//        return null;
//    }
//}
