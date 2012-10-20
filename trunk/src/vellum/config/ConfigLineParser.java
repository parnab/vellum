/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.config;

import vellum.util.Args;

/**
 *
 * @author evan
 */
public class ConfigLineParser {
    String key;
    String value; 
    
    public ConfigLineParser() {
    }

    public boolean parse(String line) {
        int index = line.indexOf(":");
        if (index > 0) {
            key = line.substring(0, index).trim();
            if (line.length() > index + 1) {
                value = line.substring(index + 1).trim();
                return true;
            }
        }
        return false;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Args.format(key, value);
    }
    
    
            
}
