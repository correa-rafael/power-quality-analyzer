package com.example.mqe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mqe.R;
import com.example.mqe.config.ConfiguracaoFirebase;
import com.example.mqe.helper.DateCustom;
import com.example.mqe.model.Medida;
import com.example.mqe.model.TempoReal;
import com.example.mqe.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MedidasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoHorario, campoThdv, campoThdi, campoFp, campoFreq, campoLocal;
    private final DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabase();
    private double thdv;
    private double thdi;
    private double fp;
    private double freq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidas);

        campoData = findViewById(R.id.editData);
        campoHorario = findViewById(R.id.editHorario);
        campoThdv = findViewById(R.id.editThdv);
        campoThdi = findViewById(R.id.editThdi);
        campoFp = findViewById(R.id.editFp);
        campoFreq = findViewById(R.id.editFreq);
        campoLocal = findViewById(R.id.editLocal);

        // preenche o campo data e hora com informações atualizadas
        campoData.setText(DateCustom.dataAtual());
        campoHorario.setText(DateCustom.horario());
        // preenche o campo local com o padrão
        String localDefault = "centro_politecnico";
        campoLocal.setText(localDefault);
        //recuperarTempoReal();
    }

    public void salvarMedida(View view) {

        if (validarMedida()) {

            Medida medida = new Medida();

            String data = Objects.requireNonNull(campoData.getText()).toString();
            String horario = Objects.requireNonNull(campoHorario.getText()).toString();
            String local = Objects.requireNonNull(campoLocal.getText()).toString();
            double thdvAtualizada = Double.parseDouble(Objects.requireNonNull(campoThdv.getText()).toString());
            double thdiAtualizada = Double.parseDouble(Objects.requireNonNull(campoThdi.getText()).toString());
            double fpAtualizada = Double.parseDouble(Objects.requireNonNull(campoFp.getText()).toString());
            double freqAtualizada = Double.parseDouble(Objects.requireNonNull(campoFreq.getText()).toString());

            medida.setThdv(thdvAtualizada);
            medida.setThdi(thdiAtualizada);
            medida.setFp(fpAtualizada);
            medida.setFreq(freqAtualizada);
            medida.setData(data);
            medida.setHorario(horario);
            medida.setLocal(local);

            // o esp fará duas coisas:
            // 1) salvar no banco de dados histórico
            medida.salvar(data, horario);
            // 2) salvar no local de dados em tempo real
            atualizarMedida(thdvAtualizada, thdiAtualizada, fpAtualizada, freqAtualizada, horario, data);
            enviarLocal();
            finish();


        }

        /*

        Log.i("horario", DateCustom.hora(horario) );
        Log.i("horario", DateCustom.ano(data) );
        Log.i("horario", DateCustom.mes(data) );
        Log.i("horario", DateCustom.dia(data) );

        String horario1 = DateCustom.horario();
        Log.i("horario", horario1);
        String data2 = DateCustom.dataAtual();
        Log.i("horario", data2);
        */
    }

    public boolean validarMedida() {

        String data = Objects.requireNonNull(campoData.getText()).toString();
        String horario = Objects.requireNonNull(campoHorario.getText()).toString();
        String local = Objects.requireNonNull(campoLocal.getText()).toString();
        String thdv = Objects.requireNonNull(campoThdv.getText()).toString();
        String thdi = Objects.requireNonNull(campoThdi.getText()).toString();
        String fp = Objects.requireNonNull(campoFp.getText()).toString();
        String freq = Objects.requireNonNull(campoFreq.getText()).toString();

        if (!data.isEmpty()) {
            if (!horario.isEmpty()) {
                if (!local.isEmpty()) {
                    if ((!thdv.isEmpty())) {
                        if (!thdi.isEmpty()) {
                            if (!fp.isEmpty()) {
                                if (!freq.isEmpty()) {
                                    return true;
                                } else {
                                    Toast.makeText(MedidasActivity.this,
                                            "Preencha a freq",
                                            Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                            } else {
                                Toast.makeText(MedidasActivity.this,
                                        "Preencha a fp",
                                        Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }

                    } else {
                        Toast.makeText(MedidasActivity.this,
                                "Preencha a thdi",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } else {
                    Toast.makeText(MedidasActivity.this,
                            "Preencha a thdv",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(MedidasActivity.this,
                        "Preencha a hora",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;
        } else {
            Toast.makeText(MedidasActivity.this,
                    "Preencha a data",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /*
    public void recuperarTempoReal(){
        String local = campoLocal.getText().toString();
        DatabaseReference tempoRealRef = reference.child("tempo_real").child(local);

        tempoRealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TempoReal tempoReal = snapshot.getValue(TempoReal.class);
                thdv = tempoReal.getThdv();
                thdi = tempoReal.getThdi();
                fp = tempoReal.getFp();
                freq = tempoReal.getFreq();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */

    public void atualizarMedida(Double thdv, Double thdi, Double fp, Double freq, String horario, String data) {

        String local = Objects.requireNonNull(campoLocal.getText()).toString();
        DatabaseReference tempoRealRef = reference.child("tempo_real").child(local);
        tempoRealRef.child("thdv").setValue(thdv);
        tempoRealRef.child("thdi").setValue(thdi);
        tempoRealRef.child("fp").setValue(fp);
        tempoRealRef.child("freq").setValue(freq);
        tempoRealRef.child("horario").setValue(horario);
        tempoRealRef.child("local").setValue(local);
        tempoRealRef.child("data").setValue(data);


    }

    public void enviarLocal() {

        String local = Objects.requireNonNull(campoLocal.getText()).toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("local", local);
        setResult(RESULT_OK, resultIntent);
    }

}