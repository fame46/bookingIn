package com.example.bookingin.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookingin.Adapter.AdapterDetailPesanan;
import com.example.bookingin.Model.TransaksiModel;
import com.example.bookingin.R;
import com.example.bookingin.Utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LaporanDetailActivity extends AppCompatActivity {
    TextView tvTotalBayar;

    List<TransaksiModel> transaksiModelList;

    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private DividerItemDecoration did;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail);

        recyclerView = findViewById(R.id.recyclerView);
        tvTotalBayar = findViewById(R.id.tvTotalBayar);

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
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        transaksiModelList.clear();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URLs.GETLAPORANDETAIL+"?id_user="+getIntent().getStringExtra("id_user")+"&tanggal="+getIntent().getStringExtra("tanggal"), new Response.Listener<String>() {
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
        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,URLs.GETTOTALBAYAR2+"?id_user="+getIntent().getStringExtra("id_user")+"&tanggal="+getIntent().getStringExtra("tanggal"),
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
}