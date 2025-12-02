package com.nextstep.rest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class AutoOpenBrowser implements CommandLineRunner {

    @Override
    public void run(String... args) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
            } else {
                System.out.println("Desktop not supported. Open browser manually.");
            }
        } catch (Exception e) {
            System.out.println("Could not open browser automatically: " + e.getMessage());
        }
    }
}
