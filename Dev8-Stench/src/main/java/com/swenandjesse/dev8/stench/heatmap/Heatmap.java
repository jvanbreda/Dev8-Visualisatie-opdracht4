/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.heatmap;

import processing.core.PApplet;
import com.swenandjesse.dev8.stench.models.Rect;
import com.swenandjesse.dev8.stench.models.Vector2;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author swenm_000
 */
public class Heatmap {

    private PApplet applet;

    private Rect<Integer> worldspace;
    private Rect<Integer> area;
    private Integer[][] pixels;
    
    private boolean enabled = false;

    private Integer pointRadius = 128;
    private Integer pointRadiusCenter;

    private int maxIntensity = Integer.MIN_VALUE;
    private float scale = 1f;

    public Heatmap(PApplet applet, Rect<Integer> worldspace) {
        this.applet = applet;
        this.worldspace = worldspace;
        this.area = worldspace;

        pixels = new Integer[worldspace.getWidth()][worldspace.getWidth()];
        pointRadiusCenter = pointRadius / 2; // + 1?

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = 0;
            }
        }
    }
    
    public Heatmap(PApplet applet, Rect<Integer> worldspace, Rect<Integer> area) {
        this.applet = applet;
        this.worldspace = worldspace;
        this.area = area;

        pixels = new Integer[worldspace.getWidth()][worldspace.getWidth()];
        pointRadiusCenter = pointRadius / 2;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = 0;
            }
        }
    }

    public Heatmap(PApplet applet, Rect<Integer> worldspace, Rect<Integer> area, Integer pointRadius) {
        this.applet = applet;
        this.worldspace = worldspace;
        this.area = area;
        this.pointRadius = pointRadius;

        pixels = new Integer[worldspace.getWidth()][worldspace.getWidth()];
        pointRadiusCenter = pointRadius / 2;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = 0;
            }
        }
    }

    public void addPoint(Vector2<Integer> point) {
        point.setX(point.getX() - worldspace.getX());
        point.setY(point.getY() - worldspace.getY());

        for (int i = -pointRadiusCenter; i < pointRadiusCenter; i++) {
            for (int j = -pointRadiusCenter; j < pointRadiusCenter; j++) {
                if (point.getX() + i < pixels.length && point.getX() + i >= 0 && point.getY() + j < pixels[0].length && point.getY() + j >= 0) { // && !(pointRadiusCenter - Math.abs(i) == 0 || pointRadiusCenter - Math.abs(j) == 0)
                    if ((i - pointRadiusCenter / 2) * (i - pointRadiusCenter / 2) + (j - pointRadiusCenter / 2) * (j - pointRadiusCenter / 2) <= (pointRadiusCenter / 2) * (pointRadiusCenter / 2)) {
                        int newValue = (pointRadiusCenter / 2) * (pointRadiusCenter / 2) - ((i - pointRadiusCenter / 2) * (i - pointRadiusCenter / 2) + (j - pointRadiusCenter / 2) * (j - pointRadiusCenter / 2));
                        pixels[point.getX() + i][point.getY() + j] += newValue;
                        
//                        int newValue = (pointRadiusCenter / 2) * (pointRadiusCenter / 2) - ((i - pointRadiusCenter / 2) * (i - pointRadiusCenter / 2) + (j - pointRadiusCenter / 2) * (j - pointRadiusCenter / 2));
//                        // Creates a more visible line, but doesn't generate high intensities
//                        if(newValue > pixels[point.getX() + i][point.getY() + j])
//                            pixels[point.getX() + i][point.getY() + j] = newValue;

                        if (pixels[point.getX() + i][point.getY() + j] > maxIntensity) {
                            maxIntensity = pixels[point.getX() + i][point.getY() + j];
                        }
                    }
                }
            }
        }
    }

    public void draw() {
        if(!enabled)
            return;
        applet.noStroke();
        
        for (int i = area.getX(); i < area.getX() + area.getWidth(); i++) {
            for (int j = area.getY(); j < area.getY() + area.getHeight(); j++) {
                if(i >= worldspace.getWidth() || j >= worldspace.getHeight())
                    continue;
                
                if (pixels[i][j] <= 0) {
                    continue;
                }
                float value = applet.map(pixels[i][j], 0, maxIntensity, 0, 510);
                if(value <= 255)
                    applet.fill(value, 255, 0, 200);
                else
                    applet.fill(255, 510 - value, 0, 200);
                applet.ellipse(i + worldspace.getX(), j + worldspace.getY(), 1, 1);
//                applet.stroke(255, 255 - applet.map(pixels[i][j], 0, maxIntensity, 0, 255), 0, 200);
//                applet.point(i + area.getX(), j + area.getY());
            }
        }
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void printMatrixToFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("HeatmapMatrix.txt", "UTF-8");
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[0].length; j++) {
                    writer.print(" " + pixels[i][j]);
                }
                writer.println();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Heatmap.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Heatmap.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setArea(Rect<Integer> area) {
        this.area = area;
    }

    @Deprecated
    private int getMaxIntensity() {
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] > maxValue) {
                    maxValue = pixels[i][j];
                }
            }
        }
        return maxValue;
    }
}
