package com.example.bookingin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingin.Model.TransaksiModel;
import com.example.bookingin.R;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryViewHolder>{

    private Context mCtx;
    private List<TransaksiModel> transaksiModelList;

    public AdapterHistory(List<TransaksiModel> transaksiModelList) {
        this.transaksiModelList = transaksiModelList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        int sub = transaksiModelList.get(position).getJumlah() * transaksiModelList.get(position).getHarga();
        holder.jumlah.setText(String.valueOf(transaksiModelList.get(position).getJumlah()) + "X");
        holder.namamenu.setText(transaksiModelList.get(position).getNama_menu());
        holder.hargamenu.setText("Rp. "+String.valueOf(transaksiModelList.get(position).getHarga())+",-");
        holder.lama.setText(String.valueOf(transaksiModelList.get(position).getLama()) + " hari");
        holder.subtotal.setText("Rp. "+String.valueOf(sub)+",-");
        if(transaksiModelList.get(position).getStatus() == 1){
            holder.status.setText("menunggu");
            holder.status.setTextColor(Color.parseColor("#FF5722"));
        }else if(transaksiModelList.get(position).getStatus() == 2){
            holder.status.setText("diterima");
            holder.status.setTextColor(Color.GREEN);
        }else if(transaksiModelList.get(position).getStatus() == 3){
            holder.status.setText("ditolak");
            holder.status.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return transaksiModelList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView jumlah, namamenu, hargamenu, lama, subtotal, status;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            jumlah = itemView.findViewById(R.id.tvQty);
            namamenu = itemView.findViewById(R.id.tvNamamenu);
            hargamenu = itemView.findViewById(R.id.textViewHarga);
            lama = itemView.findViewById(R.id.tvlama);
            subtotal = itemView.findViewById(R.id.textViewSubtotal);
            status = itemView.findViewById(R.id.textViewStatusIsi);
        }
    }
}
