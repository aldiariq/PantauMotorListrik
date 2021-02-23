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
import com.example.monitoringmotorlistrik.model.Alatwo;
import com.example.monitoringmotorlistrik.model.Kerusakanmotor;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;

import java.util.ArrayList;

public class AlatwoAdapter extends RecyclerView.Adapter<AlatwoAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<Alatwo> alatwoArrayList;
    ArrayList<Alatwo> alatwoArrayListtemp;

    OnEditClickListener onEditClickListener;
    OnDeleteClickListener onDeleteClickListener;

    public AlatwoAdapter(Context context) {
        this.context = context;
        alatwoArrayList = new ArrayList<>();
        alatwoArrayListtemp = new ArrayList<>();
    }

    public void add(Alatwo alatwo){
        alatwoArrayList.add(alatwo);
        alatwoArrayListtemp.add(alatwo);
        notifyItemInserted(alatwoArrayList.size() - 1);
        notifyItemInserted(alatwoArrayListtemp.size() - 1);
    }

    public void addAll(ArrayList<Alatwo> alatwos){
        for (Alatwo alatwo : alatwos){
            add(alatwo);;
        }
    }

    public void clear(){
        alatwoArrayList.clear();
        alatwoArrayListtemp.clear();
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener){
        this.onEditClickListener = onEditClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public Alatwo getAlatwo(int position) {
        return alatwoArrayList.get(position);
    }

    public void removeAlatwo(int position){
        if (position >= 0 && position < alatwoArrayList.size()){
            alatwoArrayList.remove(position);
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
        holder.bind(alatwoArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return alatwoArrayList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Alatwo> temp = new ArrayList<>();

                if(charSequence.length() == 0)
                    temp.addAll(alatwoArrayListtemp);
                else
                {
                    String filtrate = charSequence.toString().toLowerCase().trim();

                    if (filtrate.length() == 0){
                        temp = alatwoArrayListtemp;
                    }else {
                        for(int count = 0; count < alatwoArrayListtemp.size(); count++)
                        {
                            if(alatwoArrayListtemp.get(count).getKodeAlatWo().toLowerCase().contains(filtrate) || alatwoArrayListtemp.get(count).getNamaAlatWo().toLowerCase().contains(filtrate))
                                temp.add(alatwoArrayListtemp.get(count));
                        }
                    }


                }

                results.values = temp;
                results.count = temp.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                alatwoArrayList = (ArrayList<Alatwo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kode_alat_wo, nama_alat_wo, tgl_beli, usia_maksimum, usia_servicerekomendasi;
        ImageView ivedit_alat_wo, ivdelete_alat_wo;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.listitem_rv_vp_alatwo, itemView, false));
            initView();

            ivedit_alat_wo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClickListener.onEditClick(getAdapterPosition());
                }
            });

            ivdelete_alat_wo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        public void initView(){
            kode_alat_wo = (TextView) itemView.findViewById(R.id.tvkodealatwo_rv_alatwo);
            nama_alat_wo = (TextView) itemView.findViewById(R.id.tvnamaalatwo_rv_alatwo);
            tgl_beli = (TextView) itemView.findViewById(R.id.tvtglbeli_rv_alatwo);
            usia_maksimum = (TextView) itemView.findViewById(R.id.tvtglbeli_rv_usiamaksimum);
            usia_servicerekomendasi = (TextView) itemView.findViewById(R.id.tvtglbeli_rv_servicerekomendasi);
            ivedit_alat_wo = (ImageView) itemView.findViewById(R.id.ivedit_rv_alatwo);
            ivdelete_alat_wo = (ImageView) itemView.findViewById(R.id.ivdelete_rv_alatwo);
        }

        public void bind(Alatwo alatwo){
            kode_alat_wo.setText("Kode Alat WO : " + alatwo.getKodeAlatWo());
            nama_alat_wo.setText("Nama Alat WO : " + alatwo.getNamaAlatWo());
            tgl_beli.setText("Tanggal Beli : " + alatwo.getTglBeliAlatWo());
            usia_maksimum.setText("Usia Maksimum : " + alatwo.getUsiaMaksimumAlatWo() + " Tahun");
            usia_servicerekomendasi.setText("Usia Service Rekomendasi : " + alatwo.getUsiaServiceRekomendasi() + " Hari");
        }
    }
}
