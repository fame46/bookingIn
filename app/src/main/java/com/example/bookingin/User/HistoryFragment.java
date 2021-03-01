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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookingin.Adapter.AdapterDetailPesanan;
import com.example.bookingin.Adapter.AdapterHistory;
import com.example.bookingin.Model.TransaksiModel;
import com.example.bookingin.R;
import com.example.bookingin.Utils.Preferences;
import com.example.bookingin.Utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

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
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        preferences = new Preferences(getContext());

        recyclerView = v.findViewById(R.id.recyclerViewCart);

        transaksiModelList = new ArrayList<>();
        adapter = new AdapterHistory(transaksiModelList);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        did = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.setAdapter(adapter);

        loadData();

        return v;
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        transaksiModelList.clear();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URLs.GETHISTORY+"?id_user="+preferences.getSpId(), new Response.Listener<String>() {
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
}