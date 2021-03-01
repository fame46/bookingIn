package com.example.bookingin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.bookingin.Admin.HomeAdmin;
import com.example.bookingin.User.HomeUserActivity;
import com.example.bookingin.Utils.Preferences;
import com.example.bookingin.Utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnRegister;
    ProgressBar loading;

    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = new Preferences(this);

        etUsername = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        loading = findViewById(R.id.loading);

        if(preferences.getSPSudahLogin() == true){
            Log.d("TAG", "onCreate: "+preferences.getSpLevelId());
            if(preferences.getSpLevelId().equals("1")){
                startActivity(new Intent(LoginActivity.this, HomeAdmin.class));
            }else if(preferences.getSpLevelId().equals("2")){
                startActivity(new Intent(LoginActivity.this, HomeUserActivity.class));
            }

        }else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cekLogin(etUsername.getText().toString(),etPassword.getText().toString());
                }
            });
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void cekLogin(final String usernameS, final  String passwordS){
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        if (TextUtils.isEmpty(usernameS)) {
            etUsername.setError("Masukkan Username");
            etUsername.requestFocus();
            loading.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(passwordS)) {
            etPassword.setError("Masukkan Password");
            etPassword.requestFocus();
            loading.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            return;
        }

        StringRequest arrayRequest = new StringRequest(Request.Method.POST, URLs.LOGIN,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("volley", "response : " + response.toString());
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getInt("success")==1) {
//                        Toast.makeText(getApplicationContext(),"Anda Masuk Sebagai "+ object.getString("user_id"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        //storing the user in shared preferences
                        preferences.saveSPString(Preferences.SP_NAMA,  object.getString("username"));
                        preferences.saveSPString(Preferences.SP_ALAMAT,  object.getString("alamat"));
                        preferences.saveSPString(Preferences.SP_NOMORHP,  object.getString("nomorhp"));
                        preferences.saveSPString(Preferences.SP_LEVELID,  String.valueOf(object.getString("level")));
                        preferences.saveSPString(Preferences.SP_ID,  String.valueOf(object.getString("id")));
                        // Shared Pref ini berfungsi untuk menjadi trigger session login
                        preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                        if(object.getInt("level")==1){
                            startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                        }else if(object.getInt("level")==2){
                            startActivity(new Intent(getApplicationContext(), HomeUserActivity.class));
                        }

                    } else {
                        loading.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "username atau password Salah !", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "login error : "+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                Log.d("volley", "error : " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", usernameS);
                params.put("password", passwordS);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(arrayRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}