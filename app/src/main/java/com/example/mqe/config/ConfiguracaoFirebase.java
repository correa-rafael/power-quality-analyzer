package com.example.mqe.config;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference firebaseDatabase;

    // retorna a isntancia do firebase
    // o estático permite usar o método direto, sem instanciar previamente a classe, isso faz com que todas as instancias de ConfiguracaoFirebase
    // tenham os mesmos atributos firebaseauth e databasereference
    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static DatabaseReference getFirebaseDatabase() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return firebaseDatabase;
    }
}
