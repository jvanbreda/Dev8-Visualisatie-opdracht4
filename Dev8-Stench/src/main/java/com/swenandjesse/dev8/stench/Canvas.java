/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench;

import com.swenandjesse.dev8.stench.data.DataProvider;
import com.swenandjesse.dev8.stench.heatmap.Heatmap;
import com.swenandjesse.dev8.stench.models.Complaint;
import com.swenandjesse.dev8.stench.models.Crematoria;
import com.swenandjesse.dev8.stench.models.Rect;
import com.swenandjesse.dev8.stench.models.Vector2;
import com.swenandjesse.dev8.stench.ui.ToggleButton;
import com.swenandjesse.dev8.stench.ui.UIOverlay;
import com.swenandjesse.dev8.stench.ui.legend.Legend;
import com.swenandjesse.dev8.stench.ui.legend.LegendItem;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
    private DataProvider provider;
    
    Canvas canvas = this;

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
    private UIOverlay ui;
    private Legend legend;

    private boolean showCrematoria = true;

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
        
        
        Thread t1 = new Thread(new Runnable(){
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
                    //provider.getDataWithCoordinates(canvas);
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
        ui = new UIOverlay(this);
        ui.pushElement(new ToggleButton(this, "Heatmap", "Heatmap", new Rect<>(width - 96 - 12, 128, 96, 28)) {

            @Override
            protected void onToggleOn() {
                heatmap.setEnabled(true);
            }

            @Override
            protected void onToggleOff() {
                heatmap.setEnabled(false);
            }
        });
        ui.pushElement(new ToggleButton(this, "Crematoria", "Crematoria", new Rect<>(width - 96 - 12, 160, 96, 28), true) {

            @Override
            protected void onToggleOn() {
                showCrematoria = true;
            }

            @Override
            protected void onToggleOff() {
                showCrematoria = false;
            }
        });
        
        PImage pepeImage = loadImage("Pepe_rare.png");
        
        legend = new Legend(this);
        legend.pushItem(new LegendItem(crematoryImage, "Crematory"));
        legend.pushItem(new LegendItem(pepeImage, "Rare Pepe"));

        Random random = new Random();
        // Test
        for (int i = 0; i < 100; i++) {
            heatmap.addPoint(new Vector2<>(Math.round(mapRdX(59373 + random.nextInt(58745))), Math.round(mapRdY(419058 + random.nextInt(54748)))));
        }

        for (int i = 0; i < 2; i++) {
            heatmap.addPoint(new Vector2<>(Math.round(mapRdX(59373 + 58745 / 2)), Math.round(mapRdY(419058 + 54748 / 2))));
        }

        heatmap.printMatrixToFile();
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

        if (showCrematoria) {
            for (Crematoria crematoria : crematorias) {
                image(crematoryImage, mapRdX(crematoria.getRdX()) - 8, mapRdY(crematoria.getRdY()) - 8, 16, 16);
            }
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
        textAlign(LEFT, BOTTOM);
        textSize(24);
        text("Hold and drag your move around", width - 16 - textWidth("Hold and drag your move around"), 16 + textAscent());
        text("Scroll the mouse wheel to zoom", width - 16 - textWidth("Scroll the mouse wheel to zoom"), 16 + textAscent() * 2 + 8);
        ui.draw();
        legend.draw();
    }

    @Override
    public void mousePressed() {
        ui.checkClick(new Vector2<Integer>(mouseX, mouseY));
    }

    @Override
    public void mouseDragged() {
        cursor(MOVE);

        Vector2<Float> mousePositionDelta = new Vector2<>((float) mousePosition.getX() - mouseX, (float) mousePosition.getY() - mouseY);
        position = Vector2.Sum(position, Vector2.Divide(mousePositionDelta, 5));

        clampPosition();
        
//        heatmap.setArea(new Rect<>(Math.round(position.getX()), Math.round(position.getY()), width, height));
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
        
//        heatmap.setArea(new Rect<>(Math.round(position.getX()), Math.round(position.getY()), Math.round(width - worldSize.getX() * scale), Math.round(height - worldSize.getY() * scale)));
    }

    public void addCoordinate(Complaint coord) {
        complaints.add(coord);
        heatmap.addPoint(new Vector2<Integer>(Math.round(mapRdX((long) coord.getCoordinates().getX())), Math.round(mapRdY((long) coord.getCoordinates().getY()))));
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
