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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valor = findViewById(R.id.etImporte);
        resultado = findViewById(R.id.etResultado);

        queue = Volley.newRequestQueue(this);
    }

    public void getConversion(View view){
        Spinner spDivisaIn = (Spinner) findViewById(R.id.spMoneda1);
        Spinner spDivisaOut = (Spinner) findViewById(R.id.spMoneda2);

        divisaIn = spDivisaIn.getSelectedItem().toString();
        divisaOut = spDivisaOut.getSelectedItem().toString();

        String conversor = divisaIn + "_" + divisaOut;
        Log.d("divisaIn", divisaIn);
        String urlDivisas = "https://free.currconv.com/api/v7/convert?q=" + conversor + "&compact=ultra&apiKey=51f15bfbcb6f5d386ff0";
        Log.d("url", urlDivisas);
        getDivisas(urlDivisas, conversor);
    }

    private void getDivisas(String url, String conversion) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data = response.getString(conversion);
                    Log.d("Lista", data);
                    Float factor = Float.parseFloat(data);
                    Float res = Float.valueOf(factor * Float.parseFloat(valor.getText().toString()));
                    resultado.setText(res.toString());
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