package com.example.mqe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mqe.R;
import com.example.mqe.activity.CadastroActivity;
import com.example.mqe.activity.LoginActivity;
import com.example.mqe.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

// ========== ACTIVITY DE APRESENTAÇÃO ========== //

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);


        setButtonBackVisible(false);
        setButtonNextVisible(false);

        // para add um slide
        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .fragment(R.layout.intro_1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .fragment(R.layout.intro_2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .fragment(R.layout.intro_3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .fragment(R.layout.intro_4)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());

    }

    // está no onStart pois após o cadastro o login deve ser validado
    public void onStart() {
        super.onStart();
        verificarLogin();
    }

    public void btLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void verificarLogin() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        //autenticacao.signOut();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
    }


}