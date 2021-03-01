package com.example.bookingin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookingin.Utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etusername, etpassword, etAlamat, etNomorHP;
    Button save;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etusername = findViewById(R.id.etUsername);
        etpassword = findViewById(R.id.etPassword);
        etAlamat = findViewById(R.id.etAlamat);
        etNomorHP = findViewById(R.id.etnomorHP);
        save = findViewById(R.id.btnSaveRegister);
        loading = findViewById(R.id.loading);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpandata();
            }
        });

    }

    private void simpandata(){
        loading.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URLs.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject2 = new JSONObject(response);
                            String success = jsonObject2.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Create User Success !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), "Create User Failed !", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            save.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Failed to Upload data " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Failed to Upload data " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", etusername.getText().toString());
                params.put("password", etpassword.getText().toString());
                params.put("alamat", etAlamat.getText().toString());
                params.put("nomorhp", etNomorHP.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(stringRequest2);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}