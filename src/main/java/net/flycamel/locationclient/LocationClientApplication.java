package net.flycamel.locationclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LocationClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationClientApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        new Client().start();
    }
}
