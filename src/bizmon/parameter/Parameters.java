/*
 * Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package bizmon.parameter;

import common.exception.Exceptions;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author evanx
 */
public class Parameters {

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty() || list.get(0) == null;
    }

    public static Entry<String, String> parseEntry(String string) {
        int index = string.indexOf("=");
        if (index > 1 && index + 1 < string.length()) {
            return new Entry(string.substring(0, index), string.substring(index + 1));
        }
        return null;
    }

    public static <T> T get(Class<T> type) {
        T object;
        try {
            object = (T) type.newInstance();
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
        return object;
    }

    public static String getString(String[] args, String name) {
        for (String arg : args) {
            Entry entry = parseEntry(arg);
            if (entry != null && entry.getKey().equals(name)) {
                return (String) entry.getValue();
            }
        }
        return null;
    }

    public static String getString(String defaultValue, String name, String[] args, int index) {
        if (args.length > index) {
            return args[index];
        }
        return defaultValue;
    }
   
    public static String getString(String name, String[] args, int index) {
        if (args.length > index) {
            return args[index];
        }
        throw new IllegalArgumentException(name);
    }

    public static boolean getBoolean(String name, String[] args) {
        for (String arg : args) {
            if (arg.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static String getString(String defaultValue, String name, String[] args) {
        for (String arg : args) {
            Entry entry = parseEntry(arg);
            if (entry != null && entry.getKey().equals(name)) {
                return (String) entry.getValue();
            }
        }
        return defaultValue;
    }

    public static List<String> getList(String string) {
        List<String> list = new ArrayList();
        if (string == null || string.isEmpty()) return list;
        int index0 = string.indexOf('\"');
        if (index0 >= 0) {
            index0++;
            int index1 = string.indexOf('\"', index0);
            if (index1 > 0) {
                list.add(string.substring(index0, index1));
                index1++;
                if (index1 == string.length()) return list;
                string = string.substring(index1);
            }
        }
        for (String item : string.split("\\s")) {
            list.add(item);
        }
        return list;
    }

    public static Map<String, String> createMap(String[] args) {
        Map<String, String> map = new HashMap();
        for (String arg : args) {
            Entry<String, String> entry = parseEntry(arg);
            if (entry != null) {
                map.put(entry.getKey(), entry.getValue());
            } else {
                map.put(arg, null);
            }
        }
        return map;
    }

    public static void addString(List<String> list, String name, String[] args) {
        for (String arg : args) {
            if (arg.startsWith(name) && arg.length() > name.length() + 2 && arg.charAt(name.length()) == '=') {
                String value = arg.substring(name.length() + 1);
                list.add(value);
            }
        }
    }

    public static boolean isProperty(String name, String[] args) {
        for (String arg : args) {
            if (arg.startsWith(name) && arg.length() > name.length() + 2 && arg.charAt(name.length()) == '=') {
                return true;
            }
        }
        return false;
    }

    public static String getHostName() {
        String hostName = System.getProperty("hostName");
        if (hostName == null) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        int index = hostName.indexOf(".");
        if (index > 0) {
            hostName = hostName.substring(0, index);
        }
        return hostName;
    }

}
