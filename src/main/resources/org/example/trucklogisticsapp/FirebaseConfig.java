package org.example.trucklogisticsapp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {

    private static boolean initialized = false;

    public static void initializeFirebase() {
        if (initialized) return; // Prevent re-initialization

        try (InputStream serviceAccount =
                     FirebaseConfig.class.getResourceAsStream("/key.json")) {

            if (serviceAccount == null) {
                throw new IOException("key.json not found in resources folder!");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            initialized = true;

            System.out.println("Firebase initialized successfully.");

        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase:");
            e.printStackTrace();
        }
    }
}