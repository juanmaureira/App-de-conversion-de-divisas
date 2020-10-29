package cl.maureira.appdivisas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    public EditText valor;
    public EditText resultado;
    public String divisaIn;
    public String divisaOut;
    //public String divisaInAnterior=null, divisaOutAnterior=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valor = findViewById(R.id.etImporte);
        resultado = findViewById(R.id.etResultado);

        queue = Volley.newRequestQueue(this);
    }

    public void getConversion(View view){
        String valor1, valor2,conversor;

        Spinner spDivisaIn = (Spinner) findViewById(R.id.spMoneda1);
        Spinner spDivisaOut = (Spinner) findViewById(R.id.spMoneda2);

        divisaIn = spDivisaIn.getSelectedItem().toString();
        divisaOut = spDivisaOut.getSelectedItem().toString();

        //divisaInAnterior = divisaIn;
        //divisaOutAnterior = divisaOut;

        valor1 = valor.getText().toString();
        valor2 = resultado.getText().toString();

        if(valor1.isEmpty() && valor2.isEmpty()){
            Toast.makeText(this, "Ingrese Valor a Convertir", Toast.LENGTH_LONG).show();
        }else if(!valor1.isEmpty() && valor2.isEmpty()) {
            conversor = divisaIn + "_" + divisaOut;
            Toast.makeText(this, "Conversion " + divisaIn + " -> " + divisaOut, Toast.LENGTH_LONG).show();
            String urlDivisas = "https://free.currconv.com/api/v7/convert?q=" + conversor + "&compact=ultra&apiKey=51f15bfbcb6f5d386ff0";
            getDivisas(urlDivisas, conversor, false);
        }else if(valor1.isEmpty() && !valor2.isEmpty()){
            conversor = divisaOut + "_" + divisaIn;
            Toast.makeText(this, "Conversion " + divisaOut + " -> " + divisaIn, Toast.LENGTH_LONG).show();
            String urlDivisas = "https://free.currconv.com/api/v7/convert?q=" + conversor + "&compact=ultra&apiKey=51f15bfbcb6f5d386ff0";
            getDivisas(urlDivisas, conversor, true);
        }
    }

    private void getDivisas(String url, String conversion, boolean InOut) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data = response.getString(conversion);
                    Log.d("Lista", data);
                    Float factor = Float.parseFloat(data);
                    if(!InOut){
                        resultado.setText("");
                        Float res = Float.valueOf(factor * Float.parseFloat(valor.getText().toString()));
                        resultado.setText(res.toString());
                        Log.d("Ida",res.toString());
                    }else{
                        valor.setText("");
                        Float res = Float.valueOf(factor * Float.parseFloat(resultado.getText().toString()));
                        valor.setText(res.toString());
                        Log.d("Benida",res.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

}