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
public abstract class ToggleButton extends Clickable {
    private boolean toggled = false;
    
    private String text;
    private String toggleText;
    
    public ToggleButton(PApplet applet, String text, String toggleText, Rect<Integer> area) {
        super(applet, text, area);
        
        this.text = text;
        this.toggleText = toggleText;
    }
    
    public ToggleButton(PApplet applet, String text, String toggleText, Vector2<Integer> position) {
        super(applet, text, position);
        
        this.text = text;
        this.toggleText = toggleText;
    }
    
    public ToggleButton(PApplet applet, String text, String toggleText, Rect<Integer> area, boolean toggled) {
        super(applet, text, area);
        
        this.text = text;
        this.toggleText = toggleText;
        this.toggled = toggled;
    }
    
    public ToggleButton(PApplet applet, String text, String toggleText, Vector2<Integer> position, boolean toggled) {
        super(applet, text, position);
        
        this.text = text;
        this.toggleText = toggleText;
        this.toggled = toggled;
    }
    
    @Override
    public void draw() {
        if(toggled)
            applet.fill(0, 230, 0);
        else
            applet.fill(230, 0, 0);
        
        applet.stroke(0);
        applet.rect(area.getX(), area.getY(), area.getWidth(), area.getHeight(), 7);
//        applet.fill(0);
        
        super.draw();
    }

    protected void onClick() {
        if(toggled) {
            toggled = false;
            onToggleOff();
            setText(text, false);
        }
        else 
        {
            toggled = true;
            onToggleOn();
            setText(toggleText, false);
        }
    }
    
    protected abstract void onToggleOn();
    
    protected abstract void onToggleOff();
}
