package com.example.mqe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mqe.R;
import com.example.mqe.config.ConfiguracaoFirebase;
import com.example.mqe.helper.Base64Custom;
import com.example.mqe.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Cadastro");

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        Button botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (validarCamposCadastro(textoNome, textoEmail, textoSenha)) {

                    usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    cadastrarUsuario();
                }
            }
        });

    }


    public boolean validarCamposCadastro(String nome, String email, String senha) {
        // validar preenchimento dos campos

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!senha.isEmpty()) {
                    return true;

                } else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha a senha",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(CadastroActivity.this,
                        "Preencha o email",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(CadastroActivity.this,
                    "Preencha o nome",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void cadastrarUsuario() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        // poderia ser passado diretamente com o textoSenha e Email mas é preferível usar o Model

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(CadastroActivity.this,
                            "Sucesso ao cadastrar",
                            Toast.LENGTH_SHORT).show();
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    finish();
                } else {

                    String excecao = "";

                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite senha mais forte ";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um email válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Essa conta ja foi cadastrada";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuario:" + e.getMessage();
                    }

                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
