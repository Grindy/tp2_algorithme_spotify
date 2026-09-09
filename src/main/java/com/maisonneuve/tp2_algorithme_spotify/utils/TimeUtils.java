package com.maisonneuve.tp2_algorithme_spotify.utils;

import java.text.ParseException;

public class TimeUtils {

    public static String msToMinutes (int time) {
        int secondesTotales = time / 1000;
        int minutes = secondesTotales / 60;
        int seconds = secondesTotales % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static Integer MinToSecondes (String time) throws ParseException {
        if (!estFormatTempsValide(time)) throw new ParseException("Format invalide de temps", 0);
        int minutes = Integer.parseInt(time.split(":")[0]);
        int seconds = Integer.parseInt(time.split(":")[1]);
        return seconds + (minutes * 60);
    }

    public static Integer MinToMs (String time) throws ParseException {
        if (!estFormatTempsValide(time)) throw new ParseException("Format invalide de temps", 0);

        int minutes = Integer.parseInt(time.split(":")[0]);
        int seconds = Integer.parseInt(time.split(":")[1]);
        return (seconds + (minutes * 60)) * 1000;
    }

    public static boolean estFormatTempsValide (String time) {
        if (time == null || time.trim().isEmpty()) return false;

        try {
            if (time.contains(":")) {
                String[] tempsSepare = time.split(":");
                if (tempsSepare.length != 2) return false;
                int minutes = Integer.parseInt(tempsSepare[0]);
                int seconds = Integer.parseInt(tempsSepare[1]);

                if (minutes < 0) return false;
                return seconds >= 0 && seconds <= 59;
            } else {
                int secondes = Integer.parseInt(time);
                return secondes >= 0;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
