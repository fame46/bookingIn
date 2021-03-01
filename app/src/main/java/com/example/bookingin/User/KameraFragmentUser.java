package com.example.bookingin.User;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bookingin.Adapter.AdapterMenu;
import com.example.bookingin.Model.MenuModel;
import com.example.bookingin.R;
import com.example.bookingin.Utils.Preferences;
import com.example.bookingin.Utils.RecyclerItemClickListener;
import com.example.bookingin.Utils.URLs;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KameraFragmentUser extends Fragment {

    ImageView imageView;
    TextView tvnama_menu, tvhargaMenu, tvjumlah, tvtotalHarga, tvTanggal, btnMin, btnPlus, btnMinlama, btnPluslama, tvjumlahlama, tvtotalHargalama;
    Button btnPesan;

    List<MenuModel> menuModelList;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private DividerItemDecoration did;
    private RecyclerView.Adapter adapter;

    private View BottomSheet;
    private BottomSheetBehavior SheetBehavior;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_kamera_user, container, false);

        preferences = new Preferences(getContext());

        recyclerView = v.findViewById(R.id.recyclerView);
        imageView = v.findViewById(R.id.imageViewBottom);
        tvnama_menu = v.findViewById(R.id.tvNamaMenuBottom);
        tvhargaMenu = v.findViewById(R.id.tvHargaMenuBottom);
        tvjumlah = v.findViewById(R.id.tvJumlah);
        tvjumlahlama = v.findViewById(R.id.tvJumlahlama);
        tvtotalHarga = v.findViewById(R.id.tvJumlahharga);
        tvtotalHargalama = v.findViewById(R.id.tvJumlahhargatotal);
        btnMin = v.findViewById(R.id.btnMin);
        btnPlus = v.findViewById(R.id.btnPlus);
        btnMinlama = v.findViewById(R.id.btnMinlama);
        btnPluslama = v.findViewById(R.id.btnPluslama);
        btnPesan = v.findViewById(R.id.btnPesan);
        tvTanggal = v.findViewById(R.id.tvTanggal);

        menuModelList = new ArrayList<>();
        adapter = new AdapterMenu(getContext(),menuModelList);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        did = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);
        recyclerView.setAdapter(adapter);

        loadData();

        BottomSheet = v.findViewById(R.id.bottomSheet);
        SheetBehavior = BottomSheetBehavior.from(BottomSheet);
        SheetBehavior.setHideable(true);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String dateTime = simpleDateFormat.format(calendar.getTime());
        tvTanggal.setText(dateTime);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final MenuModel menuModel = menuModelList.get(position);
                if(SheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    SheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Glide.with(getActivity()).load(menuModel.getGambar()).into(imageView);
                    tvnama_menu.setText(menuModel.getNama_menu());
                    tvhargaMenu.setText("Rp. "+String.valueOf(menuModel.getHarga())+" ,-");
                    tvtotalHarga.setText(String.valueOf(menuModel.getHarga()*Integer.parseInt(tvjumlah.getText().toString())));
                    tvtotalHargalama.setText(String.valueOf(Integer.parseInt(tvtotalHarga.getText().toString()) * Integer.parseInt(tvjumlahlama.getText().toString())));
                    btnMin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(Integer.parseInt(tvjumlah.getText().toString()) == 0){

                            }else {
                                int jumlah = Integer.parseInt(tvjumlah.getText().toString()) - 1;
                                int jumlahHarga = menuModel.getHarga() * jumlah;
                                int jumlahHarga2 = menuModel.getHarga() * jumlah * Integer.parseInt(tvjumlahlama.getText().toString());
                                tvjumlah.setText(String.valueOf(jumlah));
                                tvtotalHarga.setText(String.valueOf(jumlahHarga));
                                tvtotalHargalama.setText(String.valueOf(jumlahHarga2));
                            }
                        }
                    });

                    btnPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int jumlah = Integer.parseInt(tvjumlah.getText().toString()) + 1;
                            int jumlahHarga = menuModel.getHarga() * jumlah ;
                            int jumlahHarga2 = menuModel.getHarga() * jumlah * Integer.parseInt(tvjumlahlama.getText().toString());
                            tvjumlah.setText(String.valueOf(jumlah));
                            tvtotalHarga.setText(String.valueOf(jumlahHarga));
                            tvtotalHargalama.setText(String.valueOf(jumlahHarga2));
                        }
                    });

                    btnPesan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cart(menuModel.getHarga());
//                            Toast.makeText(getContext(), ""+String.valueOf(menuModel.getHarga()), Toast.LENGTH_SHORT).show();
                            SheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });

                    tvTanggal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDateDialogDari();
                        }
                    });

                    btnMinlama.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(Integer.parseInt(tvjumlahlama.getText().toString()) == 0){

                            }else {
                                int jumlah = Integer.parseInt(tvjumlahlama.getText().toString()) - 1;
                                int jumlahHarga = Integer.parseInt(tvtotalHarga.getText().toString()) * jumlah;
                                tvjumlahlama.setText(String.valueOf(jumlah));
                                tvtotalHargalama.setText(String.valueOf(jumlahHarga));
                            }
                        }
                    });

                    btnPluslama.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int jumlah = Integer.parseInt(tvjumlahlama.getText().toString()) + 1;
                            int jumlahHarga = Integer.parseInt(tvtotalHarga.getText().toString()) * jumlah;
                            tvjumlahlama.setText(String.valueOf(jumlah));
                            tvtotalHargalama.setText(String.valueOf(jumlahHarga));
                        }
                    });
                }else {
                    SheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Glide.with(getActivity()).load(menuModel.getGambar()).into(imageView);
                    tvnama_menu.setText(menuModel.getNama_menu());
                    tvhargaMenu.setText("Rp. "+String.valueOf(menuModel.getHarga())+" ,-");
                    tvtotalHarga.setText(String.valueOf(menuModel.getHarga()*Integer.parseInt(tvjumlah.getText().toString())));
                    tvtotalHargalama.setText(String.valueOf(Integer.parseInt(tvtotalHarga.getText().toString()) * Integer.parseInt(tvjumlahlama.getText().toString())));
                    btnMin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(Integer.parseInt(tvjumlah.getText().toString()) == 0){

                            }else {
                                int jumlah = Integer.parseInt(tvjumlah.getText().toString()) - 1;
                                int jumlahHarga = menuModel.getHarga() * jumlah;
                                int jumlahHarga2 = menuModel.getHarga() * jumlah * Integer.parseInt(tvjumlahlama.getText().toString());
                                tvjumlah.setText(String.valueOf(jumlah));
                                tvtotalHarga.setText(String.valueOf(jumlahHarga));
                                tvtotalHargalama.setText(String.valueOf(jumlahHarga2));
                            }
                        }
                    });

                    btnPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int jumlah = Integer.parseInt(tvjumlah.getText().toString()) + 1;
                            int jumlahHarga = menuModel.getHarga() * jumlah ;
                            int jumlahHarga2 = menuModel.getHarga() * jumlah * Integer.parseInt(tvjumlahlama.getText().toString());
                            tvjumlah.setText(String.valueOf(jumlah));
                            tvtotalHarga.setText(String.valueOf(jumlahHarga));
                            tvtotalHargalama.setText(String.valueOf(jumlahHarga2));
                        }
                    });

                    btnPesan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cart(menuModel.getHarga());
//                            Toast.makeText(getContext(), ""+String.valueOf(menuModel.getHarga()), Toast.LENGTH_SHORT).show();
                            SheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });

                    tvTanggal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDateDialogDari();
                        }
                    });

                    btnMinlama.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(Integer.parseInt(tvjumlahlama.getText().toString()) == 0){

                            }else {
                                int jumlah = Integer.parseInt(tvjumlahlama.getText().toString()) - 1;
                                int jumlahHarga = Integer.parseInt(tvtotalHarga.getText().toString()) * jumlah;
                                tvjumlahlama.setText(String.valueOf(jumlah));
                                tvtotalHargalama.setText(String.valueOf(jumlahHarga));
                            }
                        }
                    });

                    btnPluslama.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int jumlah = Integer.parseInt(tvjumlahlama.getText().toString()) + 1;
                            int jumlahHarga = Integer.parseInt(tvtotalHarga.getText().toString()) * jumlah;
                            tvjumlahlama.setText(String.valueOf(jumlah));
                            tvtotalHargalama.setText(String.valueOf(jumlahHarga));
                        }
                    });
                }
            }
        }));

        return v;
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        menuModelList.clear();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URLs.GETKAMERA, new Response.Listener<String>() {
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
                                MenuModel menuModel = new MenuModel();
                                menuModel.setId(object.getInt("id"));
                                menuModel.setNama_menu(object.getString("nama_menu"));
                                menuModel.setHarga(object.getInt("harga"));
                                menuModel.setKategori(object.getString("kategori"));
                                menuModel.setGambar(object.getString("gambar"));
                                menuModelList.add(menuModel);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void showDateDialogDari(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(getContext(),R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                tvTanggal.setText(dateFormatter.format(newDate.getTime()));
//                if(a == 0){
//                    loadPengadaanData();
//                }else {
//                    loadPengadaanData2();
//                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

    private void cart(final int a){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CREATEPESANAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(getActivity(), "Sending to Cart", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Failed to Sending order "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Failed to Sending order "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("nama_menu", tvnama_menu.getText().toString());
                params.put("harga", String.valueOf(a));
                params.put("jumlah",tvjumlah.getText().toString());
                params.put("lama", tvjumlahlama.getText().toString());
                params.put("tanggal",tvTanggal.getText().toString());
                params.put("totalharga",tvtotalHargalama.getText().toString());
                params.put("id_user",preferences.getSpId());
                params.put("nama_user",preferences.getSPNama());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}