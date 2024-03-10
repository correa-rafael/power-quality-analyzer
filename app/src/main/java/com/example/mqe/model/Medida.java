package com.example.mqe.model;

import com.example.mqe.config.ConfiguracaoFirebase;
import com.example.mqe.helper.DateCustom;
import com.google.firebase.database.DatabaseReference;

// =============== ACTIVITY USADA APENAS PARA TESTES ================ //

public class Medida {

    private String data, horario, local;
    private double thdv, thdi, fp, freq;

    public Medida() {
    }

    public void salvar(String data, String horario) {

        String dia = DateCustom.dia(data);
        String mes = DateCustom.mes(data);
        String ano = DateCustom.ano(data);
        String hora = DateCustom.hora(horario);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabase();
        reference.child("medida")
                .child("centro_politecnico")
                .child(ano)
                .child(mes)
                .child(dia)
                .push()
                .setValue(this);

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public double getThdv() {
        return thdv;
    }

    public void setThdv(double thdv) {
        this.thdv = thdv;
    }

    public double getThdi() {
        return thdi;
    }

    public void setThdi(double thdi) {
        this.thdi = thdi;
    }

    public double getFp() {
        return fp;
    }

    public void setFp(double fp) {
        this.fp = fp;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }
}
