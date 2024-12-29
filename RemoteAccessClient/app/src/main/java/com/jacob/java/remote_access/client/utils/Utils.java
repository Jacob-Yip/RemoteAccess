package com.jacob.java.remote_access.client.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class
 */
public class Utils {
    /**
     * Print out the message
     * @param message The message to be printed
     */
    public static void log(String message) {
//		System.out.print("[INFO] ");
        System.out.println(message);
    }

    /**
     * Print out the error message
     * @param errMsg The message to be printed
     */
    public static void logError(String errMsg) {
        System.err.print("[ERROR]");
        System.err.println(errMsg);
    }


    /**
     * Print out the error message
     * @param errComment Customised comment for the error message
     * @param errMsg The error message
     */
    public static void logError(String errComment, String errMsg) {
        System.err.print("[ERROR]");
        System.err.print(errComment);
        System.err.print(": ");
        System.err.println(errMsg);
    }

    /**
     * Print \n
     */
    public static void log() {
        log("[INFO]");
    }

    /**
     * Print out the message and the boolean value
     * @param message The message to be printed
     * @param boolValue The boolean valueto be printed
     */
    public static void log(String message, boolean boolValue) {
        System.out.print("[INFO] ");
        System.out.println(message + ": " + boolValue);
    }

    /**
     * Print out the message the Integer value
     * @param message The message to be printed
     * @param value The Integer value to be printed with the message
     */
    public static void log(String message, int value) {
        System.out.print("[INFO] ");
        System.out.println(message + ": " + value);
    }

    /**
     * Print out the message and the double value
     * @param message The message to be printed
     * @param value The Doubler value to be printed with the message
     */
    public static void log(String message, double value) {
        System.out.print("[INFO] ");
        System.out.println(message + ": " + value);
    }

    /**
     * Print out the message and the long value
     * @param message The message to be printed
     * @param value The long value to be printed with the message
     */
    public static void log(String message, long value) {
        System.out.print("[INFO] ");
        System.out.println(message + ": " + String.valueOf(value));
    }

    /**
     * Print out the message
     * @param <T> The type of the elements in list
     * @param listName The name of list
     * @param list The list to be printed
     */
    public static <T> void log(String listName, Collection<T> list) {
        log("Elements in " + listName + ": ");

        for(T t: list) {
            if(t == null) {
                log("- null");
            } else {
                log("- " + t.toString());
            }
        }
    }

    /**
     * Print out the message and the object
     * @param message The message to be printed
     * @param o The object to be printed using toString()
     */
    public static void log(String message, Object o) {
        if(o == null) {
            log("[INFO]" + message + ": null");
        } else {
            log("[INFO]" + message + ": " + o.toString());
        }
    }

    public static void log(String arrayName, Object[] array) {
        log("=======================");

        log("Elements in " + arrayName + ": ");

        for(Object o: array) {
            if(o == null) {
                log("- null");
            } else {
                log("- " + o.toString());
            }
        }

        log("=======================");
    }

    /**
     * Print out the Map
     * @param <K> - The type of the key
     * @param <V> - The type of the value
     * @param mapName - The name of the map
     * @param map - The Map instance
     */
    public static <K, V> void log(String mapName, Map<K, V> map) {
        log("Elements in " + mapName + ": ");
        for(Map.Entry<K, V> entry: map.entrySet()) {
            System.out.println("[INFO]key: " + entry.getKey().toString() + "; value: " + entry.getValue().toString());
        }
    }

    /**
     * Reverse the given Map, e.g. HashMap
     * Assume the map is already bijective
     * @param <K> The type of the key of the old map
     * @param <V> The type of the value of the old map
     * @param oldMap The old Map to be reversed
     * @return newMap The new Map reversed
     */
    public static <K, V> Map<V, K> reverseMap(Map<K, V> oldMap) {
        Map<V, K> newMap = new HashMap<V, K>();

        for(Map.Entry<K, V> entry: oldMap.entrySet()) {
            newMap.put(entry.getValue(), entry.getKey());
        }

        return newMap;
    }


    /**
     * Calculate the final velocity
     * Formula: v = u + at
     *
     * @param initialVelocity u
     * @param acceleration a
     * @param timestamp t in seconds
     * @return v
     */
    public static double getFinalVelocity(double initialVelocity, double acceleration, double timestamp) {
        return (initialVelocity + acceleration * timestamp);
    }

    /**
     * Calculate the displacement
     * Formula: s = ut + (1/2)at^2
     *
     * @param initialVelocity u
     * @param acceleration a
     * @param timestamp t in seconds
     * @return s
     */
    public static double getDisplacement(double initialVelocity, double acceleration, double timestamp) {
        return (initialVelocity * timestamp + 0.5 * acceleration * Math.pow(timestamp, 2));
    }

    /**
     * Round the double to a certain decimal places
     *
     * @param n The number to be rounded
     * @param decimalPlaces The number of decimal places for rounding
     * @return The rounded double
     */
    public static double roundToDecimal(double n, int decimalPlaces) {
        return (double) (Math.floor(n * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces));
    }
}
