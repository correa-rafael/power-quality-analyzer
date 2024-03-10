package com.example.mqe.model;

import com.example.mqe.config.ConfiguracaoFirebase;
import com.example.mqe.helper.Base64Custom;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String local; // deve ser removido posteriormente, deve vir do esp 32


    public Usuario() {
    }

    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase(); // pega a referência para usar o banco de dados
        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);    // salvando o objeto usuário
    }

    @Exclude
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
