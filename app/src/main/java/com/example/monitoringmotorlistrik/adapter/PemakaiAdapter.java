package com.example.monitoringmotorlistrik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.model.Kerusakanmotor;
import com.example.monitoringmotorlistrik.model.Pemakai;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;

import java.util.ArrayList;

public class PemakaiAdapter extends RecyclerView.Adapter<PemakaiAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<Pemakai> pemakaiArrayList;
    ArrayList<Pemakai> pemakaiArrayListtemp;

    OnEditClickListener onEditClickListener;
    OnDeleteClickListener onDeleteClickListener;

    public PemakaiAdapter(Context context) {
        this.context = context;
        pemakaiArrayList = new ArrayList<>();
        pemakaiArrayListtemp = new ArrayList<>();
    }

    public void add(Pemakai pemakai){
        pemakaiArrayList.add(pemakai);
        pemakaiArrayListtemp.add(pemakai);
        notifyItemInserted(pemakaiArrayList.size() - 1);
        notifyItemInserted(pemakaiArrayListtemp.size() - 1);
    }

    public void addAll(ArrayList<Pemakai> pemakais){
        for (Pemakai pemakai : pemakais){
            add(pemakai);
        }
    }

    public void clear(){
        pemakaiArrayList.clear();
        pemakaiArrayListtemp.clear();
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener){
        this.onEditClickListener = onEditClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public Pemakai getPemakai(int position) {
        return pemakaiArrayList.get(position);
    }

    public void removePemakai(int position){
        if (position >= 0 && position < pemakaiArrayList.size()){
            pemakaiArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(pemakaiArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return pemakaiArrayList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Pemakai> temp = new ArrayList<>();

                if(charSequence.length() == 0)
                    temp.addAll(pemakaiArrayListtemp);
                else
                {
                    String filtrate = charSequence.toString().toLowerCase().trim();

                    if (filtrate.length() == 0){
                        temp = pemakaiArrayListtemp;
                    }else {
                        for(int count = 0; count < pemakaiArrayListtemp.size(); count++)
                        {
                            if(pemakaiArrayListtemp.get(count).getKodePemakai().toLowerCase().contains(filtrate))
                                temp.add(pemakaiArrayListtemp.get(count));
                        }
                    }


                }

                results.values = temp;
                results.count = temp.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pemakaiArrayList = (ArrayList<Pemakai>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        notifyDataSetChanged();
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kode_pemakai, keterangan_pemakai;
        ImageView ivedit_pemakai, ivdelete_pemakai;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.listitem_rv_vp_pemakai, itemView, false));
            initView();

            ivedit_pemakai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClickListener.onEditClick(getAdapterPosition());
                }
            });

            ivdelete_pemakai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        private void initView() {
            kode_pemakai = (TextView) itemView.findViewById(R.id.tvkodepemakai_rv_pemakai);
            keterangan_pemakai = (TextView) itemView.findViewById(R.id.tvketeranganpemakai_rv_pemakai);
            ivedit_pemakai = (ImageView) itemView.findViewById(R.id.ivedit_rv_pemakai);
            ivdelete_pemakai = (ImageView) itemView.findViewById(R.id.ivdelete_rv_pemakai);
        }

        public void bind(Pemakai pemakai) {
            kode_pemakai.setText("Kode Pemakai : " + pemakai.getKodePemakai());
            keterangan_pemakai.setText("Keterangan Pemakai : " + pemakai.getKeteranganPemakai());
        }
    }
}
