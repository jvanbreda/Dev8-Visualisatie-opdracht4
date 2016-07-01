/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.heatmap;

import processing.core.PApplet;
import com.swenandjesse.dev8.stench.models.Rect;
import com.swenandjesse.dev8.stench.models.Vector2;

/**
 *
 * @author swenm_000
 */
public class Heatmap {
    private PApplet applet;
    
    private Rect<Integer> area;
    private Integer[][] pixels;
    
    private Integer pointRadius = 13;
    private Integer pointRadiusCenter;
    
    private float scale = 1f;

    public Heatmap(PApplet applet, Rect<Integer> area) {
        this.applet = applet;
        this.area = area;
        
        pixels = new Integer[area.getWidth()][area.getWidth()];
        pointRadiusCenter = pointRadius / 2; // + 1?
        
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = 0;
            }
        }
    }
    
    public Heatmap(PApplet applet, Rect<Integer> area, Integer pointRadius) {
        this.applet = applet;
        this.area = area;
        this.pointRadius = pointRadius;
        
        pixels = new Integer[area.getWidth()][area.getWidth()];
        pointRadiusCenter = pointRadius / 2; // + 1?
        
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = 0;
            }
        }
    }
    
    public void addPoint(Vector2<Integer> point) {
        point.setX(point.getX() - area.getX());
        point.setY(point.getY() - area.getY());
        
        for (int i = -pointRadiusCenter; i < pointRadius; i++) {
            for (int j = -pointRadiusCenter; j < pointRadius; j++) {
                if(point.getX() + i < pixels.length  && point.getY() + j < pixels[0].length) {
                    int increasement = (pointRadius - Math.abs(i)) + (pointRadius - Math.abs(j));
                    pixels[point.getX() + i][point.getY() + j] += increasement;
                }
            }
        }
    }
    
    public void draw() {
        int maxIntensity = getMaxIntensity();
//        System.out.println(maxIntensity);
//        applet.noStroke();
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if(pixels[i][j] <= 0)
                    continue;
//                applet.fill(255, 255 - applet.map(pixels[i][j], 0, maxIntensity, 0, 255), 0, 200);
//                applet.ellipse(i + area.getX(), j + area.getY(), scale, scale);
                applet.stroke(255, 255 - applet.map(pixels[i][j], 0, maxIntensity, 0, 255), 0, 200);
                applet.point(i + area.getX(), j + area.getY());
            }
        }
    }
    
    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    
    private int getMaxIntensity() {
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if(pixels[i][j] > maxValue)
                    maxValue = pixels[i][j];
            }
        }
        return maxValue;
    }
}
