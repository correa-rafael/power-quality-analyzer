package com.example.mqe.model;

// ========== ACTIVITY MODELO COM OS DADOS DE TEMPO REAL =========== //

public class TempoReal {

    private double thdv = 0.00;
    private double thdi = 0.00;
    private double fp = 0.00;
    private double freq = 0.00;
    private String horario, local, data;

    public TempoReal() {
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
