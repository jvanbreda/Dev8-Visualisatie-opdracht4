/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench;

import com.swenandjesse.dev8.stench.data.DataProvider;
import com.swenandjesse.dev8.stench.models.Crematoria;
import com.swenandjesse.dev8.stench.models.Rect;
import com.swenandjesse.dev8.stench.models.Vector2;
import java.net.MalformedURLException;
import java.net.URL;
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
    
    private final float maxScale = 2f;
    private Vector2<Integer> worldSize;
    private Vector2<Integer> viewport;
    private Vector2<Float> position;
    private Rect<Integer> drawArea;
    private float scale;

    private Vector2<Integer> mousePosition;
    
    private PImage testImage;
    
    private List<Crematoria> crematorias;

    @Override
    public void setup() {
        frame.setTitle("Jesse and Swen - Development 8 - Assignment 4 - Stankoverlast");
        
//        testImage = loadImage("testimage.jpg");
        testImage = loadImage("map.png");
        
        worldSize = new Vector2<>(testImage.width, testImage.height);
        viewport = new Vector2<>(600, 600);
        position = new Vector2<>(0f, 0f);
        drawArea = new Rect<>(0, 0, viewport.getX(), viewport.getY());
        scale = 1f;
        size(viewport.getX(), viewport.getY());
        frameRate(60);

        mousePosition = new Vector2<>(mouseX, mouseY);
        
        crematorias = new DataProvider().getCrematoriaList();
    }

    @Override
    public void draw() {
        clear();
        mousePosition = new Vector2<>(mouseX, mouseY);

        // Moveable
        pushMatrix();
        translate(-position.getX(), -position.getY());
        scale(scale);
        
        image(testImage, 0, 0);
        
        for (Crematoria crematoria: crematorias) {
            fill(255, 0, 0);
            stroke(0);
            ellipse(map(crematoria.getRdX(), 56682, 107674, drawArea.getX(), drawArea.getX() + drawArea.getWidth()), map(crematoria.getRdY(), 447962, 431038, drawArea.getY(), drawArea.getY() + drawArea.getHeight()), 5, 5);
        }
        
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
        
        ClampPosition();
    }

    @Override
    public void mouseReleased() {
        cursor(ARROW);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        float e = -event.getCount();
        
        // Restrict scale so it won't show an empty screen when the user zooms too far out
        if(!(testImage.width * (scale + e / 10) < viewport.getX() || testImage.height * (scale + e / 10) < viewport.getY()) && scale + e / 10 < maxScale)
            scale += e / 10;

        ClampPosition();
    }
    
    //To-do: Should be a method with parameters to clamp, so that it can be used for both position and scale
    private void ClampPosition() {
        // Clamp position so it won't show an empty screen when the user moves too far
        if(position.getX() < 0)
            position.setX(0f);
        if(position.getX() > worldSize.getX() * scale - viewport.getX())
            position.setX(worldSize.getX() * scale - viewport.getX());
        if(position.getY() < 0)
            position.setY(0f);
        if(position.getY() > worldSize.getY() * scale - viewport.getY())
            position.setY(worldSize.getY() * scale - viewport.getY());
    }
}
