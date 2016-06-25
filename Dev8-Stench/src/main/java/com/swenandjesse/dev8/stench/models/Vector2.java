/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.models;

/**
 *
 * @author swenm_000
 */
public class Vector2<T> {
    private T x;
    private T y;

    public Vector2() { }

    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }
    
    // Java doesn't support operator overriding >:(
    public static <T extends Number> Vector2 Sum(Vector2<T> v1, Vector2<T> v2) {
        return new Vector2<>(v1.getX().floatValue() + v2.getX().floatValue(), v1.getY().floatValue() + v2.getY().floatValue());
    }
    
    public static <T extends Number> Vector2 Subtract(Vector2<T> v1, Vector2<T> v2) {
        return new Vector2<>(v1.getX().floatValue() - v2.getX().floatValue(), v1.getY().floatValue() - v2.getY().floatValue());
    }
    
    public static <T extends Number> Vector2 Divide(Vector2<T> v1, float number) {
        return new Vector2<>(v1.getX().floatValue() / number, v1.getY().floatValue() / number);
    }
    
    @Override
    public String toString() {
        return "Vector2[x: " + x + "; y: " + y + "]";
    }
}
