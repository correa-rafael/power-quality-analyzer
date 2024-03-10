package com.example.mqe.helper;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

// ============ ESSA CLASSE É USADA PARA FORMATAR OS VALORES NO EIXO X DO GRÁFICO ============= //

public class MyFormatter extends IndexAxisValueFormatter {

    private final String[] mValues;

    public MyFormatter(String[] values) {
        this.mValues = values;
    }


    @Override
    public String getFormattedValue(float value) {
        // o value é o valor do eixo x, ex: return "olá"+ value; -> resultado: olá1, olá2 ...
        // retorna o elemento value (eixo x) do array String[] mValues
        // return mValues[(int) value];

        int index = Math.round(value);

        int mValueCount = mValues.length;

        Log.i("teste", String.valueOf(index));


        if (index < 0 || index >= mValueCount || index != (int) value)
            return "";
        return mValues[index];
        //return String.valueOf(value);
    }
}
