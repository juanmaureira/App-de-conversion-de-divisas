package cl.maureira.appdivisas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private TextView text;
    String urlDivisas = "https://free.currconv.com/api/v7/currencies?apiKey=51f15bfbcb6f5d386ff0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.tvMonedas);
        queue = Volley.newRequestQueue(this);
        getDivisas();

    }

    private void getDivisas() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlDivisas,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String data = response.getString("results");
                    //char[] dataArray = data.toCharArray();
                    text.setText(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                text.setText(error.toString());
                Toast.makeText(MainActivity.this, "ERROR" + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

}