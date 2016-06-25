/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.models;

/**
 *
 * @author Jesse
 */
public class Complaint {
    
    private String complaintMedium;
    private String date;
    private int count;
    private String streetName;
    private String postCode;
    private String city;
    private String complaintType;
    private String complaintSubType;
    private String complaintSubSubType;
    private boolean feedbackRequested;

    public String getComplaintMedium() {
        return complaintMedium;
    }

    public void setComplaintMedium(String complaintMedium) {
        this.complaintMedium = complaintMedium;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintSubType() {
        return complaintSubType;
    }

    public void setComplaintSubType(String complaintSubType) {
        this.complaintSubType = complaintSubType;
    }

    public String getComplaintSubSubType() {
        return complaintSubSubType;
    }

    public void setComplaintSubSubType(String complaintSubSubType) {
        this.complaintSubSubType = complaintSubSubType;
    }

    public boolean isFeedbackRequested() {
        return feedbackRequested;
    }

    public void setFeedbackRequested(boolean feedbackRequested) {
        this.feedbackRequested = feedbackRequested;
    }
    
    @Override
    public String toString(){
        return "Complaint: " + complaintMedium + "|" + date + "|" + count + "|" + streetName + "|" + postCode + "|" + complaintType;
    }

    
}