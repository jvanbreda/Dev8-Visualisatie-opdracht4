/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.data;

import com.sun.javafx.fxml.builder.URLBuilder;
import com.swenandjesse.dev8.stench.models.Complaint;
import com.swenandjesse.dev8.stench.models.ComplaintCoordinates;
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
    
    private final String API_KEY = "7aKDCk1a2C12QMLRq7coN23h4Fcu4Lar5KaTLZVJ";
    
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
            String baseURL = "https://postcode-api.apiwise.nl/v2/addresses/?postcode=";
            sb.append(baseURL);
            for (Complaint c : complaints){
                sb.setLength(baseURL.length());
                sb.append(c.getPostCode());
                System.out.println(sb.toString());
                JSONObject jObject = getResponseJSON(new URL(sb.toString()));
                ComplaintCoordinates cc = new ComplaintCoordinates();
                cc.setComplaint(c);
                cc.setRdCoordinates(getCoordinates(jObject));
                complaintsCoordinatesList.add(cc);
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
            conn.setRequestProperty("X-Api-Key", API_KEY);
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
        float rdX = 0;
        float rdY = 0;
        
        JSONObject _embedded = jObject.getJSONObject("_embedded");
        JSONArray addresses = _embedded.getJSONArray("addresses");
        JSONObject rd;
        if(addresses.size() > 0){
            JSONObject fullInformation = addresses.getJSONObject(0);
            JSONObject geo = fullInformation.getJSONObject("geo");
            JSONObject center = geo.getJSONObject("center");
            rd = center.getJSONObject("rd");
            rdX = rd.getJSONArray("coordinates").getFloat(0);
            rdY = rd.getJSONArray("coordinates").getFloat(1);
        }
        return new Vector2(rdX, rdY);
    }
    
}
