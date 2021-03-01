package com.example.bookingin.User;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class KeranjangFragment extends Fragment {

    TextView tvTotalBayar;
    Button btnKonfirmasi;

    List<TransaksiModel> transaksiModelList;

    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private DividerItemDecoration did;
    private RecyclerView.Adapter adapter;

    Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_keranjang, container, false);

        preferences = new Preferences(getContext());

        recyclerView = v.findViewById(R.id.recyclerViewCart);
        tvTotalBayar = v.findViewById(R.id.tvTotalBayar);
        btnKonfirmasi = v.findViewById(R.id.btnAjukan);

        transaksiModelList = new ArrayList<>();
        adapter = new AdapterDetailPesanan(transaksiModelList);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        did = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.setAdapter(adapter);

        loadData();
        sum();

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfirmasi();
                loadData();
                tvTotalBayar.setText("0");
            }
        });


        return v;
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        transaksiModelList.clear();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URLs.GETCART+"?id_user="+preferences.getSpId(), new Response.Listener<String>() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    public void sum(){
        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.GET,URLs.GETTOTALBAYAR+"?id_user="+preferences.getSpId(),
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void konfirmasi(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.EDITSTATUSCART,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.e("responsecart",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Gagal melakukan pesanan "+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String id = "";
                for(int i=0;i<transaksiModelList.size();i++){
                    id += String.valueOf(transaksiModelList.get(i).getId())+",";
                }
                params.put("id_user", preferences.getSpId());
                params.put("count",String.valueOf(transaksiModelList.size()));
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}