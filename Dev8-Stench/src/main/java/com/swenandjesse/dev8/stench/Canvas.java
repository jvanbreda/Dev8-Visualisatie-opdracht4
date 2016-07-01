/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench;

import com.swenandjesse.dev8.stench.data.DataProvider;
import com.swenandjesse.dev8.stench.heatmap.Heatmap;
import com.swenandjesse.dev8.stench.models.Complaint;
import com.swenandjesse.dev8.stench.models.ComplaintCoordinates;
import com.swenandjesse.dev8.stench.models.Crematoria;
import com.swenandjesse.dev8.stench.models.Rect;
import com.swenandjesse.dev8.stench.models.Vector2;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.MouseEvent;

/**
 *
 * @author Jesse
 */
public class Canvas extends PApplet {

    public List<Complaint> complaints = Collections.synchronizedList(new ArrayList<Complaint>());
    public List<ComplaintCoordinates> complaintsCoordinates = Collections.synchronizedList(new ArrayList<ComplaintCoordinates>());
    private DataProvider provider;

    private Canvas canvas;

    private final float maxScale = 2f;
    private Vector2<Integer> worldSize;
    private Vector2<Integer> viewport;
    private Vector2<Float> position;
    private Rect<Integer> drawArea;
    private float scale;

    private Vector2<Integer> mousePosition;

    private PImage mapImage;
    private PImage crematoryImage;

    public List<Crematoria> crematorias = Collections.synchronizedList(new ArrayList<Crematoria>());
    
    private Heatmap heatmap;

    @Override
    public void setup() {
        frame.setTitle("Jesse and Swen - Development 8 - Assignment 4 - Stankoverlast");

        mapImage = loadImage("map_confirmed.png");
        crematoryImage = loadImage("crematory_icon.png");

        worldSize = new Vector2<>(mapImage.width, mapImage.height);
        viewport = new Vector2<>(600, 600);
        position = new Vector2<>(0f, 0f);
        drawArea = new Rect<>(0, 0, viewport.getX(), viewport.getY());
        scale = 1f;
        size(viewport.getX(), viewport.getY());
        frameRate(60);

        mousePosition = new Vector2<>(mouseX, mouseY);

        provider = new DataProvider();
        provider.getCrematoriaList(this);

        canvas = this;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                provider.getComplaintList(canvas);
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Give first thread a little head start to load some data in the complaint list
                    //This will prevent the list to be empty when this thread starts
                    Thread.sleep(4);
                    provider.getDataWithCoordinates(canvas);
                } catch (Exception ex) {
                    Logger.getLogger(Canvas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t1.setPriority(Thread.MIN_PRIORITY);
        t1.start();
        t2.setPriority(Thread.MIN_PRIORITY);
        t2.start();
        
        heatmap = new Heatmap(this, new Rect<Integer>(drawArea.getX(), drawArea.getY(), drawArea.getX() + worldSize.getX(), drawArea.getY() + worldSize.getY()));
    }

    @Override
    public void draw() {
        clear();
        mousePosition = new Vector2<>(mouseX, mouseY);

        // Moveable
        pushMatrix();
        translate(-position.getX(), -position.getY());
        scale(scale);

        image(mapImage, 0, 0);

        for (Crematoria crematoria : crematorias) {
//            fill(255, 0, 0);
//            stroke(0);
            //ellipse(mapRdX(crematoria.getRdX()), mapRdY(crematoria.getRdY()), 5, 5);
            image(crematoryImage, mapRdX(crematoria.getRdX()) - 8, mapRdY(crematoria.getRdY()) - 8, 16, 16);
        }

//        synchronized (complaintsCoordinates) {
//            for (ComplaintCoordinates complaintCoordinate : complaintsCoordinates) {
//                fill(255, 0, 0);
//                stroke(0);
//                ellipse(mapRdX((long) complaintCoordinate.getCoordinates().getX()), mapRdY((long) complaintCoordinate.getCoordinates().getY()), 5, 5);
//            }
//        }
        
        heatmap.draw();

        // Static / Absolute, for UI
        popMatrix();
        fill(0);
        textSize(24);
        text("Hold and drag your move around", width - 16 - textWidth("Hold and drag your move around"), 16 + textAscent());
        text("Scroll the mouse wheel to zoom", width - 16 - textWidth("Scroll the mouse wheel to zoom"), 16 + textAscent() * 2 + 8);
    }

    @Override
    public void mousePressed() {
        cursor(MOVE);
    }

    @Override
    public void mouseDragged() {
        Vector2<Float> mousePositionDelta = new Vector2<>((float) mousePosition.getX() - mouseX, (float) mousePosition.getY() - mouseY);
        position = Vector2.Sum(position, Vector2.Divide(mousePositionDelta, 5));

        clampPosition();
    }

    @Override
    public void mouseReleased() {
        cursor(ARROW);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        float e = -event.getCount();

        // Restrict scale so it won't show an empty screen when the user zooms too far out
        if (!(mapImage.width * (scale + e / 10) < viewport.getX() || mapImage.height * (scale + e / 10) < viewport.getY()) && scale + e / 10 < maxScale) {
            scale += e / 10;
            heatmap.setScale(scale);
        }
        
        clampPosition();
    }
    
    public void addCoordinate(ComplaintCoordinates coord) {
        complaintsCoordinates.add(coord);
        heatmap.addPoint(new Vector2<Integer>(Math.round(mapRdX((long)coord.getCoordinates().getX())), Math.round(mapRdY((long)coord.getCoordinates().getY()))));
    }

    //To-do: Should be a method with parameters to clamp, so that it can be used for both position and scale
    private void clampPosition() {
        // Clamp position so it won't show an empty screen when the user moves too far
        if (position.getX() < 0) {
            position.setX(0f);
        }
        if (position.getX() > worldSize.getX() * scale - viewport.getX()) {
            position.setX(worldSize.getX() * scale - viewport.getX());
        }
        if (position.getY() < 0) {
            position.setY(0f);
        }
        if (position.getY() > worldSize.getY() * scale - viewport.getY()) {
            position.setY(worldSize.getY() * scale - viewport.getY());
        }
    }

    private float mapRdX(int value) {
        return map(value, 59373, 118118, drawArea.getX(), drawArea.getX() + worldSize.getX());
    }

    private float mapRdY(int value) {
        return map(value, 473806, 419058, drawArea.getY(), drawArea.getY() + worldSize.getY());
    }

    private float mapRdX(long value) {
        return map(value, 59373, 118118, drawArea.getX(), drawArea.getX() + worldSize.getX());
    }

    private float mapRdY(long value) {
        return map(value, 473806, 419058, drawArea.getY(), drawArea.getY() + worldSize.getY());
    }
}
