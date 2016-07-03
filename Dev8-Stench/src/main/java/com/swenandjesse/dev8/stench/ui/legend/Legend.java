/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.ui.legend;

import com.swenandjesse.dev8.stench.models.Rect;
import com.swenandjesse.dev8.stench.models.Vector2;
import com.swenandjesse.dev8.stench.ui.UIElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author swenm_000
 */
public class Legend {
    private PApplet applet;
    private List<LegendItem> items;
    
    private int padding = 8;
    private int offset = 8;
    private int lineSpacing = 4;
    private int itemSize = 24;
    private Rect<Integer> area;

    public Legend(PApplet applet) {
        this.applet = applet;
        items = new ArrayList<>();
        
        area = new Rect<>(applet.width - padding - offset, applet.height - padding - offset, padding * 2, padding * 2);
    }

    public Legend(PApplet applet, List<LegendItem> elements) {
        this.applet = applet;
        this.items = elements;
        
        area = new Rect<>(applet.width - padding - offset - 192, applet.height - padding - offset - itemSize * elements.size(), 192, itemSize * (elements.size() + lineSpacing));
    }

    public void draw() {
        applet.stroke(0);
        applet.fill(255);
        applet.rect(area.getX(), area.getY(), area.getWidth(), area.getHeight());
        
        applet.fill(0);
        applet.textSize(itemSize / 2);
        applet.textAlign(applet.LEFT, applet.TOP);
        for (int i = 0; i < items.size(); i++) {
            LegendItem item = items.get(i);
            applet.image(item.getImage(), area.getX() + padding, area.getY() + padding + (itemSize + lineSpacing) * i, itemSize, itemSize);
            applet.text(item.getText(), area.getX() + padding * 2 + itemSize, area.getY() + padding * 1.5f + (itemSize + lineSpacing) * i);
        }
    }

    public void pushItem(LegendItem element) {
        items.add(element);
        area = new Rect<>(applet.width - padding - offset - 192, applet.height - padding - offset - itemSize / 2 * (items.size() + lineSpacing), 192, itemSize / 2 * (items.size() + lineSpacing) + padding);
    }

    public void popItem(LegendItem element) {
        items.remove(element);
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }
}
