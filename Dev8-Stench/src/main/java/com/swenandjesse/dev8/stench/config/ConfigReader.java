/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swenandjesse.dev8.stench.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showInputDialog;

/**
 *
 * @author Jesse
 */
public class ConfigReader {

    private Properties prop;
    private InputStream input;
    private OutputStream output;
    private File file;

    public ConfigReader() {
        prop = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource("config.properties").getFile());
    }

    public String getAPIkey() {
        try {
            input = new FileInputStream(file);
            prop.load(input);
            String apikey = prop.getProperty("API-KEY");
            if (apikey == null) {
                apikey = promptAPIKey();
            }
            return apikey;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private String promptAPIKey() {
        try {
            String apikey = showInputDialog("If you have an API key, type it in here so you can use this application");
            output = new FileOutputStream(file);
            prop.setProperty("API-KEY", apikey);
            prop.store(output, file.getPath());
            output.close();
            return apikey;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
