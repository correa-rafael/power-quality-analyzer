package com.example.mqe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mqe.config.ConfiguracaoFirebase;
import com.example.mqe.helper.DateCustom;
import com.example.mqe.model.Medida;
import com.example.mqe.model.TempoReal;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mqe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// ========= ESSA É A ACTIVITY PRINCIPAL, FOI ESCOLHIDO UM TEMPLATE DE BASIC ACTIVITY ========= //

public class PrincipalActivity extends AppCompatActivity {

    private final DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference tempoRealRef;
    private ValueEventListener valueEventListenerTempoReal;
    private ValueEventListener valueEventListenerHistorico;
    private ChildEventListener childEventListenerHistorico;

    private String local = "centro_politecnico"; // p/ n deixar vazio na primeira execução
    private final Double thdv = 0.0;
    private final Double thdi = 0.0;
    private final Double fp = 0.0;
    private final Double freq = 0.0;

    // para teste
    private TextView textLocal;
    private Button buttonThdv, buttonThdi, buttonFp, buttonFreq;
    private TextView textData, textHorario;

    // parâmetros para recuperação do histórico
    // Lista contendo as diversas medidas
    private final List<Medida> medidasHistorico = new ArrayList<>();
    // Referencia para o JSON do firebase
    private DatabaseReference medidaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // teste
        textLocal = findViewById(R.id.textLocal);
        // para que na primeira execução o campo não esteja vazio
        textLocal.setText(local);


        buttonThdv = findViewById(R.id.buttonThdv);
        buttonThdi = findViewById(R.id.buttonThdi);
        buttonFp = findViewById(R.id.buttonFp);
        buttonFreq = findViewById(R.id.buttonFreq);
        textLocal = findViewById(R.id.textLocal);
        textData = findViewById(R.id.textData);
        textHorario = findViewById(R.id.textHorario);


        FloatingActionMenu floatingActionMenu = findViewById(R.id.menu);
        // Tornar o floating action menu invisível, caso queira usar, basta comentar
        floatingActionMenu.setVisibility(View.GONE);

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

         */
    }

    // essa função foi feita apenas para a interface de teste, para passar "local" de "MedidasActivity" para a Activity principal
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                local = data.getStringExtra("local");
                String[] localFormatado = local.split("_");
                String local1 = localFormatado[0];
                local1 = local1.substring(0, 1).toUpperCase() + local1.substring(1).toLowerCase();
                String local2 = localFormatado[1];
                local2 = local2.substring(0, 1).toUpperCase() + local2.substring(1).toLowerCase();

                textLocal.setText(local1 + " " + local2);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.menuSobre:
                startActivity(new Intent(this, SobreActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void graficoThdi(View view) {
        startActivity(new Intent(this, GraficoThdiActivity.class));
    }

    public void graficoThdv(View view) {
        startActivity(new Intent(this, GraficoThdvActivity.class));
    }

    public void graficoPF(View view) {
        startActivity(new Intent(this, GraficoPfActivity.class));
    }

    public void graficoFreq(View view) {
        startActivity(new Intent(this, GraficoFreqActivity.class));
    }


    // método usado para adicionar testes, isso foi feito para testar o envio de dados diretamente pelo celular, antes do ESP32 ter sido configurado para
    // envio de dados para o firebase
    public void adicionarMedida(View view) {
        Intent intent = new Intent(this, MedidasActivity.class);
        startActivityForResult(intent, 1);
        //startActivity(new Intent(this, MedidasActivity.class));
    }

    // recuperar a medida para tempo real
    public void recuperarMedida() {

        // o local é passado da activity de medidas
        tempoRealRef = reference.child("tempo_real").child(local);
        valueEventListenerTempoReal = tempoRealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Medida medida = snapshot.getValue(Medida.class);
                buttonThdv.setText(medida.getThdv() + "%");
                buttonThdi.setText(medida.getThdi() + "%");
                buttonFp.setText(medida.getFp() + "");
                buttonFreq.setText(medida.getFreq() + " Hz");
                textData.setText(medida.getData());
                textHorario.setText(medida.getHorario());
                //Log.i("live", medida.getHorario());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.i("Evento", "Evento adicionado");

    }

    // método para recuperar medidas históricas em 24 horas, essa é a lógica utilizada, pode ser usado para testes, mas para a versão final não tem utilidade
    /*
    public void recuperarMedidasHistorico() {

        // caminho até os dados desejados no firebase
        medidaRef = reference.child("medida")
                .child("centro_politecnico")
                .child("2021")
                .child("01")
                .child("13");


        valueEventListenerHistorico = medidaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                medidasHistorico.clear();
                // o get value retorna um obj, o get children retorna todos os filhos
                Log.i("teste", "antes do for");


                for (DataSnapshot dados : snapshot.getChildren()) {

                    //Log.i("dados", "retorno" + dados.toString());
                    // a cada iteração será recuperado um objeto filho
                    Medida medida = dados.getValue(Medida.class);
                    //medidasHistorico.add(medida);
                    Log.i("dados", "retorno " + medida.getFp());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */

    // o método onStart é chamado quando a activity inicia
    @Override
    protected void onStart() {
        // super chama a função original da classe pai
        super.onStart();
        recuperarMedida();
        Log.i("teste", "chamando recuperar historico");
        //recuperarMedidasHistorico();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Evento", "evento removido");
        // remover os listeners quando a activity for encerrada para não haver gasto de energia desnecessário
        tempoRealRef.removeEventListener(valueEventListenerTempoReal);
        //medidaRef.removeEventListener(valueEventListenerHistorico);
    }

}