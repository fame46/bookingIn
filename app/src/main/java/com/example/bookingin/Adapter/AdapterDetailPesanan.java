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

public class AdapterDetailPesanan extends RecyclerView.Adapter<AdapterDetailPesanan.DetailPesananViewHolder>{

    private List<TransaksiModel> transaksiModelList;

    public AdapterDetailPesanan(List<TransaksiModel> transaksiModelList) {
        this.transaksiModelList = transaksiModelList;
    }

    @NonNull
    @Override
    public DetailPesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart, parent, false);
        DetailPesananViewHolder detailPesananViewHolder = new DetailPesananViewHolder(view);
        return detailPesananViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPesananViewHolder holder, int position) {
        int sub = transaksiModelList.get(position).getJumlah() * transaksiModelList.get(position).getHarga();
        holder.jumlah.setText(String.valueOf(transaksiModelList.get(position).getJumlah()) + "X");
        holder.namamenu.setText(transaksiModelList.get(position).getNama_menu());
        holder.hargamenu.setText("Rp. "+String.valueOf(transaksiModelList.get(position).getHarga())+",-");
        holder.lama.setText(String.valueOf(transaksiModelList.get(position).getLama()) + " hari");
        holder.subtotal.setText("Rp. "+String.valueOf(sub)+",-");
    }

    @Override
    public int getItemCount() {
        return transaksiModelList.size();
    }

    public class DetailPesananViewHolder extends RecyclerView.ViewHolder {
        TextView jumlah, namamenu, hargamenu, lama, subtotal;
        public DetailPesananViewHolder(@NonNull View itemView) {
            super(itemView);
            jumlah = itemView.findViewById(R.id.tvQty);
            namamenu = itemView.findViewById(R.id.tvNamamenu);
            hargamenu = itemView.findViewById(R.id.textViewHarga);
            lama = itemView.findViewById(R.id.tvlama);
            subtotal = itemView.findViewById(R.id.textViewSubtotal);
        }
    }
}
