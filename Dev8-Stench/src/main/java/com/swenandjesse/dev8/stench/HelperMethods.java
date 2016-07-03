/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench;

import com.swenandjesse.dev8.stench.models.Vector2;

/**
 *
 * @author Jesse
 */
public class HelperMethods {
    
    public static Vector2 convertFromDecimalToRd(Vector2 coordinateDecimal){
        double referenceWgs84X = 52.15517;
        double referenceWgs84Y = 5.387206;
        
        double dX = 0.36 * ((double) coordinateDecimal.getX() - referenceWgs84X);
        double dY = 0.36 * ((double) coordinateDecimal.getY() - referenceWgs84Y);
        
        int referenceRdX = 155000;
        int referenceRdY = 463000; 
        
        double sumX = (190094.945 * dY) + (-11832.228 * dX * dY) + (-114.221 * Math.pow(dX, 2) * dY) + (-32.391 * Math.pow(dY, 3) + (-0.705) * dX) + (-2.340 * Math.pow(dX, 3) * dY) + (-0.608 * dX * Math.pow(dY, 3) + (-0.008) * Math.pow(dY, 2) + 0.148 * Math.pow(dX, 2) * Math.pow(dY, 3));
        double sumY = (309056.544 * dX) + (3638.893 * Math.pow(dY, 2)) + (73.077 * Math.pow(dX, 2)) + (-157.984 * dX * Math.pow(dY, 2)) + (59.788 * Math.pow(dX, 3)) + (0.433 * dY) + (-6.439 * Math.pow(dX, 2) * Math.pow(dY, 2)) + (-0.032 * dX * dY) + (0.092 * Math.pow(dY, 4)) + (-0.054 * dX * Math.pow(dY, 4));
        
        long rdX = referenceRdX + (long) sumX;
        long rdY = referenceRdY + (long) sumY;
        
        return new Vector2(rdX, rdY);
    }
    
}
