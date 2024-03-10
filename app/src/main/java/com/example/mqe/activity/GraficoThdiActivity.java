package com.example.mqe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.example.mqe.R;
import com.example.mqe.config.ConfiguracaoFirebase;
import com.example.mqe.helper.DateCustom;
import com.example.mqe.helper.MyFormatter;
import com.example.mqe.model.Medida;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

// ========== ACTIVITY QUE EXIBE OS GRÁFICOS EM TEMPO REAL E HISTÓRICO PARA A THDI (A LÓGICA É A MESMA PARA A THDV, O PF E A FREQ) ============ //

public class GraficoThdiActivity extends AppCompatActivity {

    private LineChart mChart;
    private LineChart mChart2;
    private final DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference chartRef;
    private DatabaseReference chartRef2;
    private ValueEventListener valueEventListenerTempoRealThdi;
    private ValueEventListener valueEventListenerHistoricoThdi;


    // string[] com as labels desejadas no eixo
    String[] values = {"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30",
            "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
            "13:30", "14:00", "14:30", "15:00", "16:00", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30",
            "22:00", "22:30", "23:00", "23:30"};


    ArrayList<Entry> dataVals = new ArrayList<>();
    // ArrayList<Entry> dataVals2 = new ArrayList<>();
    LineDataSet lineDataSet = new LineDataSet(null, null);
    LineDataSet lineDataSet2 = new LineDataSet(null, null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    ArrayList<ILineDataSet> iLineDataSets2 = new ArrayList<>();
    LineData lineData;
    LineData lineData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_thdi);

        Objects.requireNonNull(getSupportActionBar()).hide();

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mChart = findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setTextColor(Color.WHITE);
        description.setText("");
        mChart.setDescription(description);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        // xAxis.setDrawGridLines(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setTextColor(Color.WHITE);
        // leftAxis.setDrawGridLines(false);

        // função para formatar as labels do eixo x
        MyFormatter myFormatter1 = new MyFormatter(values);
        xAxis.setValueFormatter(myFormatter1);
        // step mínimo com zoom (pelo que eu entendi)
        xAxis.setGranularity(1f);

        mChart2 = findViewById(R.id.linechart2);
        mChart2.setDragEnabled(true);
        mChart2.setScaleEnabled(false);
        mChart2.setDrawGridBackground(false);
        mChart2.setPinchZoom(true);
        mChart2.getLegend().setEnabled(false);
        mChart2.setDescription(description);

        XAxis xAxis2 = mChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTextColor(Color.WHITE);
        xAxis2.setAxisLineColor(Color.WHITE);
        xAxis2.setDrawGridLines(false);
        YAxis leftAxis2 = mChart2.getAxisLeft();
        leftAxis2.setDrawGridLines(true);
        YAxis rightAxis2 = mChart2.getAxisRight();
        rightAxis2.setEnabled(false);
        leftAxis2.setTextColor(Color.WHITE);
        // função para formatar as labels do eixo x
        MyFormatter myFormatter2 = new MyFormatter(values);
        xAxis2.setValueFormatter(myFormatter2);
        xAxis2.setGranularity(1f);
        // step mínimo com zoom (pelo que eu entendi)

        /*
        ArrayList<Entry> dataVals3 = new ArrayList<>();
        for(float i = 0; i<=100;  i = i + 0.01f){
            dataVals3.add(new Entry(i, i*100));
        }
        Log.i("dadosLista", String.valueOf(dataVals3));
        showChartHistorico(dataVals3);

        dataVals3.add(new Entry(27.65435656f,0));
        dataVals3.add(new Entry(27.99876987f,2));
        dataVals3.add(new Entry(27.809809f,6));
        dataVals3.add(new Entry(27.0990909f,8));
        dataVals3.add(new Entry(27.3565656f,2));
        dataVals3.add(new Entry(27.345635635f,3));
        dataVals3.add(new Entry(27.055656f,100));
        Log.i("dadosLista", String.valueOf(dataVals3));

         */


        insertData();
        inserirDataHistorico();
    }

    // inserir dados em gráfico de tempo real
    private void insertData() {
        String local = "centro_politecnico";
        chartRef = reference.child("tempo_real").child(local);
        valueEventListenerTempoRealThdi = chartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Medida medida = snapshot.getValue(Medida.class);
                assert medida != null;
                double thdi = medida.getThdi();
                String horario = medida.getHorario();
                double newHorario = formatarHorario(horario);
                Log.i("chartTest", String.valueOf(thdi));
                Log.i("chartTest", horario);
                Log.i("chartTest", String.valueOf(formatarHorario(horario)));
                dataVals.add(new Entry((float) newHorario, (float) thdi));
                showChart(dataVals, (float) newHorario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showChart(ArrayList<Entry> dataVals, float xPosition) {
        lineDataSet.setValues(dataVals);
        lineDataSet.setLineWidth(3);
        //lineDataSet.setLabel("DataSet1");
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(true);
        lineDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setValueTextColor(Color.WHITE);
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        mChart.clear();
        mChart.setData(lineData);
        mChart.setVisibleXRange(0.5f, 0.5f);
        mChart.setScaleMinima(50f, 1f);
        mChart.moveViewToX(xPosition);
        mChart.notifyDataSetChanged();
        mChart.invalidate();

        //Log.i("teste", lineDataSet.toString());
    }

    private void inserirDataHistorico() {
        // variável data que será dividda para selecionar corretamente o ano, mes e dia
        String[] date = DateCustom.dataAtual().split("/");
        chartRef2 = reference.child("medida").child("centro_politecnico").child(date[2]).child(date[1]).child(date[0]);
        //chartRef2 = reference.child("medida").child("centro_politecnico").child(date[2]).child("01").child("27");
        valueEventListenerHistoricoThdi = chartRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Entry> dataVals2 = new ArrayList<>();
                if (snapshot.hasChildren()) {
                    //dataVals2.clear();
                    for (DataSnapshot dados : snapshot.getChildren()) {
                        // a cada iteração será recuperado um objeto filho
                        Medida medida = dados.getValue(Medida.class);
                        assert medida != null;
                        double thdi = medida.getThdi();
                        String horario = medida.getHorario();
                        double newHorario = formatarHorario(horario);
                        dataVals2.add(new Entry((float) newHorario, (float) thdi));
                        //Log.i("dadosChart", "retorno " + medida.getThdi());
                        //Log.i("dadosChart", "retorno " + newHorario);
                    }
                    Log.i("dadosLista", String.valueOf(dataVals2));
                    showChartHistorico(dataVals2);

                } else {
                    mChart2.clear();
                    mChart2.invalidate();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showChartHistorico(ArrayList<Entry> dataVals) {
        lineDataSet2.setValues(dataVals);
        lineDataSet2.setLineWidth(3);
        //lineDataSet2.setLabel("DataSet2");
        lineDataSet2.setColors(Color.WHITE);
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setDrawValues(false);
        lineDataSet2.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient);   // isso é para preencher a parte de baixo do gráfico com o gradiente
        lineDataSet2.setFillDrawable(drawable);
        //lineDataSet2.setFillColor(Color.WHITE);
        iLineDataSets2.clear();
        iLineDataSets2.add(lineDataSet2);
        lineData2 = new LineData(iLineDataSets2);
        mChart2.clear();
        mChart2.setData(lineData2);
        //mChart2.setVisibleXRange(2, 2);
        //mChart2.setVisibleXRangeMinimum(1);
        mChart2.setAutoScaleMinMaxEnabled(true);
        //mChart2.setScaleMinima(50f,1f);
        //mChart.moveViewToX(xPosition);
        mChart2.notifyDataSetChanged();
        mChart2.invalidate();
        //Log.i("teste", lineDataSet.toString());
    }


    private double formatarHorario(String horario) {
        String[] manipulacao = horario.split(":");
        int hora = Integer.parseInt(manipulacao[0]);
        double minuto = Double.parseDouble(manipulacao[1]) / 60;
        double segundo = Double.parseDouble(manipulacao[2]) / 3600;
        // é multiplicado por 2 pois a escala é de meia em meia hora, isto é, 1 hora equivale a 2 unidades,
        // além disso, deve-se retirar 2 unidades pois a hora 00 vale 2 unidades
        // apenas pensar em uma conversão de escalas de inteiro para horas, em que uma hora equivale a duas unidades
        return (((hora + minuto + segundo) * 2) - 2);
    }


    protected void onStop() {
        super.onStop();
        // removendo listener para não consumir recursos desnecessários
        chartRef.removeEventListener(valueEventListenerTempoRealThdi);
        chartRef2.removeEventListener(valueEventListenerHistoricoThdi);
    }
}