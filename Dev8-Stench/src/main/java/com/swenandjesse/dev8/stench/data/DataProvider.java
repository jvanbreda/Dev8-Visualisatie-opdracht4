/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.data;

import com.sun.javafx.fxml.builder.URLBuilder;
import com.swenandjesse.dev8.stench.HelperMethods;
import com.swenandjesse.dev8.stench.models.Complaint;
import com.swenandjesse.dev8.stench.models.ComplaintCoordinates;
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
import processing.data.JSONArray;
import processing.data.JSONObject;


/**
 *
 * @author Jesse
 */
public class DataProvider {
    
    //Used for old lookup method for postcode coordinates, with the use of the google api, this is deprecated
    //private final String API_KEY = "7aKDCk1a2C12QMLRq7coN23h4Fcu4Lar5KaTLZVJ";
    
    public String getData(URL url) {
        String allData = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            Iterator lines = br.lines().iterator();
            //Skip table headers
            lines.next();
            int counter = 0;
            while (lines.hasNext()) {
                String line = (String) lines.next();
                allData += line + "\n";
                counter++;
                System.out.println(counter);
            }
        } catch (IOException e) {
            System.err.println("IO Exception occured while reading the data!");
        }
        return allData;
    }
    
    public List<Complaint> getDataList() {
        List<Complaint> complaints = new ArrayList<>();
        String data = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("HACKATON--KLACHTEN-2011.csv").getFile());
            data = getData(file.toURI().toURL());
        } catch (IOException e) {
            System.err.println("IO Exception occured while transforming data to list!");
        }
        
        Scanner fileScanner = new Scanner(data);
        fileScanner.useDelimiter("\n");
        
        while (fileScanner.hasNext()){
            String line = fileScanner.next();
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
            
            if(c.getComplaintType().equalsIgnoreCase("Stank")){
                complaints.add(c);
            }
        }
        
        return complaints;
    }
    
    public List<ComplaintCoordinates> getDataWithCoordinates() {
        List<ComplaintCoordinates> complaintsCoordinatesList = new ArrayList<>();
        try {
            List<Complaint> complaints = getDataList();
            StringBuilder sb = new StringBuilder();
            String baseURL = "http://maps.googleapis.com/maps/api/geocode/json?address=";
            sb.append(baseURL);
            for (Complaint c : complaints){
                if (!c.getPostCode().equals("")){
                    sb.setLength(baseURL.length());
                    sb.append(c.getPostCode());
                    System.out.println(sb.toString());
                    JSONObject jObject = getResponseJSON(new URL(sb.toString()));
                    ComplaintCoordinates cc = new ComplaintCoordinates();
                    cc.setComplaint(c);
                    cc.setCoordinates(getCoordinates(jObject));
                    complaintsCoordinatesList.add(cc);
                }
            }
        } catch (MalformedURLException e){
            System.out.println("Exception occurred during converting");
        }
        return complaintsCoordinatesList;
    }
    
    private JSONObject getResponseJSON(URL url){
        String json  = "";
        try {            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null){
                    json += line + "\n";
                }
                br.close();
            }
            
        } catch (Exception e){
            System.out.println("Http request failed!");
        }
        //System.out.println(json);
        JSONObject jObject = JSONObject.parse(json);
        
        return jObject;
    }
    
    private Vector2 getCoordinates(JSONObject jObject){      
        float latitude = 0, longitude = 0;
        
        JSONArray results = jObject.getJSONArray("results");
        if (results.size() > 0){
            JSONObject allResults = results.getJSONObject(0);
            JSONObject geometry = allResults.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            latitude = location.getFloat("lat");
            longitude = location.getFloat("lng");
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

    public List<Crematoria> getCrematoriaList() {
        List<Crematoria> crematoriaList = new ArrayList<>();
        String data = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("Crematoria_Rotterdam_eo.csv").getFile());
            data = getData(file.toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner fileScanner = new Scanner(data);
        fileScanner.useDelimiter("\n");

        // Skip first row with column names
        fileScanner.next();

        while (fileScanner.hasNext()) {
            String line = fileScanner.next();
            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter(";");
            Crematoria crematoria = new Crematoria();
            crematoria.setName(lineScanner.next());
            crematoria.setAddress(lineScanner.next());
            crematoria.setCity(lineScanner.next());
            crematoria.setRdX(Integer.parseInt(lineScanner.next()));
            crematoria.setRdY(Integer.parseInt(lineScanner.next()));
            crematoriaList.add(crematoria);
        }

        return crematoriaList;
    }
}
