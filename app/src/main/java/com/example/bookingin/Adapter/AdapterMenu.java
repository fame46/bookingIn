package com.example.bookingin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookingin.Model.MenuModel;
import com.example.bookingin.R;

import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MenuViewHolder>{

    private Context mCtx;
    private List<MenuModel> menuModelList;

    public AdapterMenu(Context mCtx, List<MenuModel> menuModelList){
        this.mCtx = mCtx;
        this.menuModelList = menuModelList;
    }

    @NonNull
    @Override
    public AdapterMenu.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.list_menu,parent,false);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMenu.MenuViewHolder holder, int position) {
        MenuModel menuModel =menuModelList.get(position);

        Glide.with(mCtx).load(menuModel.getGambar()).into(holder.imageView);
        holder.tvName.setText(menuModel.getNama_menu());
        holder.tvHarga.setText("Rp. "+String.valueOf(menuModel.getHarga())+",-");
    }

    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName, tvHarga;

        public MenuViewHolder(@NonNull View v) {
            super(v);

            tvName = v.findViewById(R.id.tvNamamenu);
            tvHarga = v.findViewById(R.id.tvHargamenu);
            imageView = v.findViewById(R.id.imageTampil);

        }
    }
}
