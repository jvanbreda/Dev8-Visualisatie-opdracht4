/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.ui.legend;

import processing.core.PImage;

/**
 *
 * @author swenm_000
 */
public class LegendItem {
    private PImage image;
    private String text;

    public LegendItem(PImage image, String text) {
        this.image = image;
        this.text = text;
    }

    public PImage getImage() {
        return image;
    }

    public void setImage(PImage image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
