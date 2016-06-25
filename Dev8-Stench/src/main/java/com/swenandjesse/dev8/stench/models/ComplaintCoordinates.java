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
public class ComplaintCoordinates {
    
    private Complaint complaint;
    private long rdX;
    private long rdY;

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public long getRdX() {
        return rdX;
    }

    public void setRdX(long rdX) {
        this.rdX = rdX;
    }

    public long getRdY() {
        return rdY;
    }

    public void setRdY(long rdY) {
        this.rdY = rdY;
    }
    
    
    
}
