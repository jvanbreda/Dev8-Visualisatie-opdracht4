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
public abstract class Clickable extends UIElement {
    public Clickable(PApplet applet, String text, Rect<Integer> area) {
        super(applet, text, area);
    }
    
    public Clickable(PApplet applet, String text, Vector2<Integer> position) {
        super(applet, text, position);
    }
    
    public void checkClick(Vector2<Integer> clickPosition) {
        if(isInArea(clickPosition))
            onClick();
    }
    
    public boolean isInArea(Vector2<Integer> mousePosition) {
        return (area.getX() <= mousePosition.getX() && area.getX() + area.getWidth() >= mousePosition.getX() && area.getY() <= mousePosition.getY() && area.getY() + area.getHeight() >= mousePosition.getY());
    }
    
    protected abstract void onClick();
}
