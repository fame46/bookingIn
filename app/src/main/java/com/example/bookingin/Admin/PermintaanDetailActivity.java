package com.example.bookingin.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookingin.Adapter.AdapterDetailPesanan;
import com.example.bookingin.Model.TransaksiModel;
import com.example.bookingin.R;
import com.example.bookingin.Utils.Preferences;
import com.example.bookingin.Utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermintaanDetailActivity extends AppCompatActivity {

    TextView tvTotalBayar;
    Button btnTerima, btnTolak;

    List<TransaksiModel> transaksiModelList;

    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private DividerItemDecoration did;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permintaan_detail);

        recyclerView = findViewById(R.id.recyclerView);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);
        btnTerima = findViewById(R.id.btnTerima);
        btnTolak = findViewById(R.id.btnTolak);

        transaksiModelList = new ArrayList<>();
        adapter = new AdapterDetailPesanan(transaksiModelList);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        did = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.setAdapter(adapter);

        loadData();
        sum();

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfirmasiterima();
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfirmasitolak();
            }
        });
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        transaksiModelList.clear();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URLs.GETPERMINTAANDETAIL+"?id_user="+getIntent().getStringExtra("id_user"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Aaaa",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                TransaksiModel transaksiModel = new TransaksiModel();
                                transaksiModel.setId(object.getInt("id"));
                                transaksiModel.setNama_menu(object.getString("nama_menu"));
                                transaksiModel.setHarga(object.getInt("harga"));
                                transaksiModel.setJumlah(object.getInt("jumlah"));
                                transaksiModel.setLama(object.getInt("lama"));
                                transaksiModel.setTanggal(object.getString("tanggal"));
                                transaksiModel.setTotalharga(object.getInt("totalharga"));
                                transaksiModel.setStatus(object.getInt("status"));
                                transaksiModel.setId_user(object.getInt("id_user"));
                                transaksiModel.setNama_user(object.getString("nama_user"));
                                transaksiModelList.add(transaksiModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void sum(){
        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,URLs.GETTOTALBAYAR2+"?id_user="+getIntent().getStringExtra("id_user"),
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        Log.e("Aaaa",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String result = jsonObject.getString("result");

                            tvTotalBayar.setText("Rp. "+result+",-");

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void konfirmasiterima(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.EDITSTATUSTERIMA,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.e("responsecart",response);
                        startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PermintaanDetailActivity.this,"Gagal melakukan pesanan "+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String id = "";
                for(int i=0;i<transaksiModelList.size();i++){
                    id += String.valueOf(transaksiModelList.get(i).getId())+",";
                }
                params.put("id_user", getIntent().getStringExtra("id_user"));
                params.put("count",String.valueOf(transaksiModelList.size()));
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void konfirmasitolak(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.EDITSTATUSTOLAK,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.e("responsecart",response);
                        startActivity(new Intent(getApplicationContext(), HomeAdmin.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PermintaanDetailActivity.this,"Gagal melakukan pesanan "+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String id = "";
                for(int i=0;i<transaksiModelList.size();i++){
                    id += String.valueOf(transaksiModelList.get(i).getId())+",";
                }
                params.put("id_user", getIntent().getStringExtra("id_user"));
                params.put("count",String.valueOf(transaksiModelList.size()));
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}