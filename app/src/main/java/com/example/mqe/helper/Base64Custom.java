package com.example.mqe.helper;
// necessário para poder usar o email no firebase, que não aceita carcteres especiais

// ========== ACTIVITY PARA CODIFICAR O EMAIL DO USUARIO EM BASE64 NO MOMENTO DO CADSTRO ========== //

import android.util.Base64;

public class Base64Custom {

    // quando fica estático n precis instanciar (global, fica igual p/ todos os obj da classe)
    public static String codificarBase64(String texto) {
        // substituir caracteres inválidos por vazio
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("([\\n\\r])", "");

    }

    public static String decodificarBase64(String textoCodificado) {
        //return Base64.decode(textoCodificado, Base64.DEFAULT).toString();
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
