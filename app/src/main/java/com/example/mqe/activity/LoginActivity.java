package com.example.mqe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mqe.R;
import com.example.mqe.config.ConfiguracaoFirebase;
import com.example.mqe.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");


        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        Button botaoEntrar = findViewById(R.id.buttonEntrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (validarCamposLogin(textoEmail, textoSenha)) {
                    usuario = new Usuario();
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    login();
                }

            }
        });
    }

    public boolean validarCamposLogin(String email, String senha) {

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {
                return true;

            } else {
                Toast.makeText(LoginActivity.this,
                        "Preencha a senha",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(LoginActivity.this,
                    "Preencha o email",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void login() {

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            "Sucesso no login",
                            Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();

                } else {

                    String excecao = "";

                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Usuario e senha inválidos";
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não está cadastrado.";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuario:" + e.getMessage();
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish(); // fecha activity de login
    }


}