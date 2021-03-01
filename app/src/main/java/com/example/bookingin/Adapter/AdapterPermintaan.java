package com.example.bookingin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingin.Model.TransaksiModel;
import com.example.bookingin.R;

import java.util.List;

public class AdapterPermintaan extends RecyclerView.Adapter<AdapterPermintaan.PermintaanViewHolder> {

    private List<TransaksiModel> transaksiModelList;

    public AdapterPermintaan(List<TransaksiModel> transaksiModelList) {
        this.transaksiModelList = transaksiModelList;
    }

    @NonNull
    @Override
    public PermintaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_permintaan, parent, false);
        PermintaanViewHolder permintaanViewHolder = new PermintaanViewHolder(view);
        return permintaanViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PermintaanViewHolder holder, int position) {
        holder.user.setText(transaksiModelList.get(position).getNama_user());
        holder.nomorHP.setText(transaksiModelList.get(position).getNomorHP());
        holder.alamat.setText(transaksiModelList.get(position).getAlamat());
    }

    @Override
    public int getItemCount() {
        return transaksiModelList.size();
    }

    public class PermintaanViewHolder extends RecyclerView.ViewHolder {
        TextView user, nomorHP, alamat;
        public PermintaanViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.tv);
            nomorHP = itemView.findViewById(R.id.tv2);
            alamat = itemView.findViewById(R.id.tv3);
        }
    }
}
