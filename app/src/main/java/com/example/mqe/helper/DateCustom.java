package com.example.mqe.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

// ========== CLASSE COM FUNÇÕES DE DATA ============ //

public class DateCustom {

    public static String dataAtual() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return simpleDateFormat.format(calendar.getTime());
    }


    public static String horario() {

        Calendar calendar = Calendar.getInstance();
        // o kk deixa em formato 24 horas
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String ano(String data) {
        // separa a data em um vetor de strings segundo a "/"
        String[] retornoData = data.split("/");
        return retornoData[2];
    }

    public static String mes(String data) {
        String[] retornoData = data.split("/");
        return retornoData[1];
    }

    public static String dia(String data) {
        String[] retornoData = data.split("/");
        return retornoData[0];
    }

    public static String hora(String horario) {
        String[] retornoData = horario.split(":");
        return retornoData[0];
    }

    public static String minuto(String horario) {
        String[] retornoData = horario.split(":");
        return retornoData[1];
    }


}
