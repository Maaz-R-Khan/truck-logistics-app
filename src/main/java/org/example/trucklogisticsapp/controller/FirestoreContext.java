package org.example.trucklogisticsapp.controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class FirestoreContext {

    public static Firestore getDB() {
        return FirestoreClient.getFirestore();
    }
}