package com.mimi.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {

    private static final String LOG_FILE = "data/application.log";

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(LOG_FILE, true))) {

            String timeStamp = LocalDateTime.now().format(formatter);
            writer.write(timeStamp + " | " + message);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Logging failed: " + e.getMessage());
        }
    }
}
