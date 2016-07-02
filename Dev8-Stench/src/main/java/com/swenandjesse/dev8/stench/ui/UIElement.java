/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.ui;

import com.swenandjesse.dev8.stench.models.Rect;
import com.swenandjesse.dev8.stench.models.Vector2;
import processing.core.PApplet;

/**
 *
 * @author swenm_000
 */
public abstract class UIElement {
    private final int padding = 8;
    
    protected PApplet applet;
    
    protected String text;
    protected Rect<Integer> area;
    
    public UIElement(PApplet applet, String text, Rect<Integer> area) {
        this.applet = applet;
        this.text = text;
        this.area = area;
    }
    
    // Automatic scaling
    public UIElement(PApplet applet, String text, Vector2<Integer> position) {
        this.applet = applet;
        this.text = text;
        this.area = new Rect<>(position.getX(), position.getY(), (int)applet.textWidth(text) + padding * 2, (int)applet.textAscent() + padding * 2);
    }
    
    public void draw() {
        applet.textAlign(applet.CENTER, applet.TOP);
        applet.textSize(12);
        applet.fill(0);
        applet.text(text, area.getX() + area.getWidth() / 2, area.getY() + padding);
    }
    
    public void setText(String text, boolean resize) {
        this.text = text;
        if(resize)
            this.area = new Rect<>(area.getX(), area.getY(), (int)applet.textWidth(text) + padding * 2, (int)applet.textAscent() + padding * 2);
    }
}
