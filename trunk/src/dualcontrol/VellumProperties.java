/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.util.Properties;

/**
 *
 * @author evans
 */
public class VellumProperties {

    public static final VellumProperties systemProperties = 
            new VellumProperties(System.getProperties());
    
    Properties properties;

    public VellumProperties(Properties properties) {
        this.properties = properties;
    }
        
    public String getString(String propertyName) {
        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new RuntimeException("Missing -D property: " + propertyName);
        }
        return propertyValue;
    } 

    public String getString(String propertyName, String defaultValue) {
        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            return defaultValue;
        }
        return propertyValue;
    } 
    
    public int getInt(String propertyName) {
        String propertyString = properties.getProperty(propertyName);
        if (propertyString == null) {
            throw new RuntimeException("Missing -D property: " + propertyName);
        }
        return Integer.parseInt(propertyString);
    }
    
    public int getInt(String propertyName, int defaultValue) {
        String propertyString = properties.getProperty(propertyName);
        if (propertyString == null) {
            return defaultValue;
        }
        return Integer.parseInt(propertyString);
    }
    
    public boolean getBoolean(String propertyName, boolean defaultValue) {
        String string = properties.getProperty(propertyName);
        if (string == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(string);
    }    
    
    public char[] getPassword(String propertyName, char[] defaultValue) {
        String passwordString = properties.getProperty(propertyName);
        if (passwordString == null) {
            return defaultValue;
        }
        return passwordString.toCharArray();
    }
}