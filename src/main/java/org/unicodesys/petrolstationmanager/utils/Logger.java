package org.unicodesys.petrolstationmanager.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] [" + level + "] " + message);
    }

    public void success(String message) {
        log("SUCCESS", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void warning(String message) {
        log("WARNING", message);
    }
}