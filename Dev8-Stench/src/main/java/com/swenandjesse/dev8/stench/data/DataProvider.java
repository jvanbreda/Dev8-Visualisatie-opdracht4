/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.data;

import com.sun.javafx.fxml.builder.URLBuilder;
import com.swenandjesse.dev8.stench.Canvas;
import com.swenandjesse.dev8.stench.ConfigReader;
import com.swenandjesse.dev8.stench.HelperMethods;
import com.swenandjesse.dev8.stench.models.Complaint;
import com.swenandjesse.dev8.stench.models.Crematoria;
import com.swenandjesse.dev8.stench.models.Vector2;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collections;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 *
 * @author Jesse
 */
public class DataProvider {
    
    private final String API_KEY = new ConfigReader().getAPIkey();

    public void getComplaintList(Canvas canvas) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("HACKATON--KLACHTEN-2011.csv").getFile());
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file.toURI().toURL().openStream()));
            Iterator lines = br.lines().iterator();
            //Skip table headers
            lines.next();
            while (lines.hasNext()) {
                String line = (String) lines.next();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(";");
                Complaint c = new Complaint();
                c.setComplaintMedium(lineScanner.next());
                c.setDate(lineScanner.next());
                c.setCount(Integer.parseInt(lineScanner.next()));
                c.setStreetName(lineScanner.next());
                c.setPostCode(lineScanner.next());
                c.setCity(lineScanner.next());
                c.setComplaintType(lineScanner.next());
                c.setComplaintSubType(lineScanner.next());
                c.setComplaintSubSubType(lineScanner.next());
                c.setFeedbackRequested(lineScanner.next().equalsIgnoreCase("j"));
                
                if (c.getComplaintType().equalsIgnoreCase("Stank") && !c.getPostCode().equals("")){
                    Vector2 coordinates = getCoordinatesFrom(c);
                    c.setCoordinates(coordinates);
                    canvas.complaints.add(c);
                }                
            }
        } catch (IOException e) {
            System.err.println("IO Exception occured while reading the data!");
        }
    }

    private Vector2 getCoordinatesFrom(Complaint c) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/geocode/json?key=");
            sb.append(API_KEY);
            sb.append("&address=");
            sb.append(c.getPostCode());
            System.out.println(sb.toString());
            JSONObject jObject = getResponseJSON(new URL(sb.toString()));
            return getCoordinates(jObject);
        } catch (MalformedURLException e) {
            System.out.println("Exception occurred during converting");
        }
        return new Vector2(0,0);
    }

    private JSONObject getResponseJSON(URL url) {
        String json = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    json += line + "\n";
                }
                br.close();
            }

        } catch (Exception e) {
            System.out.println("Http request failed!");
        }
        //System.out.println(json);
        JSONObject jObject = JSONObject.parse(json);

        return jObject;
    }

    private Vector2 getCoordinates(JSONObject jObject) {
        double latitude = 0, longitude = 0;

        JSONArray results = jObject.getJSONArray("results");
        if (results.size() > 0) {
            JSONObject allResults = results.getJSONObject(0);
            JSONObject geometry = allResults.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            latitude = location.getDouble("lat");
            longitude = location.getDouble("lng");
        }

        Vector2 decimalCoordinates = new Vector2(latitude, longitude);

        return HelperMethods.convertFromDecimalToRd(decimalCoordinates);
    }

//    public String getData(URL url) {
//        String allData = "";
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//            Iterator lines = br.lines().iterator();
//            while (lines.hasNext()) {
//                String line = (String) lines.next();
//                allData += line + "\n";
//            }
//        } catch (IOException e) {
//            System.err.println("IO Exception occured while reading the data!");
//        }
//        return allData;
//    }
    public void getCrematoriaList(Canvas canvas) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Crematoria_Rotterdam_eo.csv").getFile());
        String data = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file.toURI().toURL().openStream()));
            Iterator lines = br.lines().iterator();
            //Skip table headers
            lines.next();
            
            while (lines.hasNext()) {
                String line = (String) lines.next();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(";");
                Crematoria crematoria = new Crematoria();
                crematoria.setName(lineScanner.next());
                crematoria.setAddress(lineScanner.next());
                crematoria.setCity(lineScanner.next());
                crematoria.setRdX(Integer.parseInt(lineScanner.next()));
                crematoria.setRdY(Integer.parseInt(lineScanner.next()));
                canvas.crematorias.add(crematoria);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
