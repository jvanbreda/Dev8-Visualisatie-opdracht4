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
public class Crematoria {
    private String name;
    private String address;
    private String city;
    private int rdX;
    private int rdY;

    public Crematoria() { }

    public Crematoria(String name, String address, String city, int rdX, int rdY) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.rdX = rdX;
        this.rdY = rdY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getRdX() {
        return rdX;
    }

    public void setRdX(int rdX) {
        this.rdX = rdX;
    }

    public int getRdY() {
        return rdY;
    }

    public void setRdY(int rdY) {
        this.rdY = rdY;
    }
}
