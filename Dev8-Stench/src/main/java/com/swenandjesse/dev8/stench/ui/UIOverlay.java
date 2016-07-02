/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.ui;

import com.swenandjesse.dev8.stench.models.Vector2;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
/**
 *
 * @author swenm_000
 */
public class UIOverlay {
    private PApplet applet;
    private List<UIElement> elements;

    public UIOverlay(PApplet applet) {
        this.applet = applet;
        elements = new ArrayList<>();
    }

    public UIOverlay(PApplet applet, List<UIElement> elements) {
        this.applet = applet;
        this.elements = elements;
    }

    public void draw() {
        Vector2<Integer> mousePosition = new Vector2<>(applet.mouseX, applet.mouseY);
        int cursorType = applet.ARROW;
        for (UIElement element : elements) {
            element.draw();
            if (element instanceof Clickable) {
                if (((Clickable) element).isInArea(mousePosition)) {
                    cursorType = applet.HAND;
                }
            }
        }
        applet.cursor(cursorType);
    }

    public void clear() {
        applet.fill(0);
        applet.stroke(0);
        for (UIElement element : elements) {
            applet.rect(element.area.getX(), element.area.getY(), element.area.getWidth(), element.area.getHeight());
        }
    }

    public void checkClick(Vector2<Integer> clickPosition) {
        for (UIElement element : elements) {
            if (element instanceof Clickable) {
                ((Clickable) element).checkClick(clickPosition);
            }
        }
    }

    public void pushElement(UIElement element) {
        elements.add(element);
    }

    public void popElement(UIElement element) {
        elements.remove(element);
    }
}
