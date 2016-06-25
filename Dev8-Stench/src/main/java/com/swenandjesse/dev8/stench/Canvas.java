/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench;

import com.swenandjesse.dev8.stench.data.DataProvider;
import com.swenandjesse.dev8.stench.models.Complaint;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;

/**
 *
 * @author Jesse
 */
public class Canvas extends PApplet {

    private List<Complaint> complaintList;

    @Override
    public void setup() {
        try {
            size(600, 600);
            System.out.println(new DataProvider().getDataWithCoordinates());
        } catch (Exception ex) {
            Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void draw() {

    }

}
