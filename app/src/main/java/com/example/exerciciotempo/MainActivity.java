package com.example.exerciciotempo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tempAtual;
    TextView tempMin;
    TextView tempMax, umidade, chanceChu;
    Button button;
    EditText editCidade, editPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempAtual = findViewById(R.id.tempAtual);
        tempMin = findViewById(R.id.tempMin);
        tempMax = findViewById(R.id.tempMax);
        umidade = findViewById(R.id.umidade);
        chanceChu = findViewById(R.id.chanceChu);
        button = findViewById(R.id.button5);
        editCidade = findViewById(R.id.editCidade);
        editPais = findViewById(R.id.editPais);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(editCidade.getWindowToken(), 0);
                findWeather();
            }
        });
    }

    public void findWeather() {
        String cidade = editCidade.getText().toString();
        String pais = editPais.getText().toString();
        if(editCidade.length() == 0 || cidade.equals("Cidade")){
            Toast toast = Toast.makeText(getApplicationContext(), "Digite a cidade",Toast.LENGTH_SHORT);
            toast.show();
        }else if (editPais.length() == 0 || pais.equals("País")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Digite o pais", Toast.LENGTH_SHORT);
            toast.show();
        }else {

            String urlPrincipal = "http://api.openweathermap.org/data/2.5/weather?q=";
            String cidadeEstado = cidade + "," + pais;
            String urlFinal = "&appid=8582b752414824683c950ef9fbf09d19&units=metric";
            String urlCompleta = urlPrincipal + cidadeEstado + urlFinal;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCompleta, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject json = new JSONObject(response);

                        JSONObject aux = json.getJSONObject("main");
                        double tempAux = aux.getDouble("temp");
                        tempAtual.setText("Temperatura\n Atual\n" + tempAux + "C");

                        JSONObject aux2 = json.getJSONObject("main");
                        double tempMinAux = aux2.getDouble("temp_min");
                        tempMin.setText("Temperatura Minima\n" + tempMinAux + "C");

                        JSONObject aux3 = json.getJSONObject("main");
                        double tempMaxAux = aux3.getDouble("temp_max");
                        tempMax.setText("Temperatura Maxima\n" + tempMaxAux + "C");

                        JSONObject aux4 = json.getJSONObject("main");
                        double umidadeAux = aux4.getDouble("humidity");
                        umidade.setText("Umidade\n" + umidadeAux + "%");

                        JSONArray jsonArray = json.getJSONArray("weather");
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String chuvaAux = obj.getString("main");
                        if (chuvaAux.equals("Rain")) {
                            JSONObject aux5 = json.getJSONObject("rain");
                            double chanceChuAux = aux5.getDouble("1h");
                            chanceChu.setText("Chuva\n" + chanceChuAux + "mm");
                        } else {
                            chanceChu.setText("Sem chuva");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Cidade ou Pais não encontrado", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    public void clearCidade(View view){
        editCidade.setText("");
        editPais.setText("");
    }

    public void clearPais(View view){
        editPais.setText("");
    }

}