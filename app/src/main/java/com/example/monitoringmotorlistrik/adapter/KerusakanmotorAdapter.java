package com.example.monitoringmotorlistrik.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.model.Kerusakanmotor;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;

import java.util.ArrayList;
import java.util.Locale;

public class KerusakanmotorAdapter extends RecyclerView.Adapter<KerusakanmotorAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<Kerusakanmotor> kerusakanmotorArrayList;
    ArrayList<Kerusakanmotor> kerusakanmotorArrayListtemp;

    OnEditClickListener onEditClickListener;
    OnDeleteClickListener onDeleteClickListener;

    public KerusakanmotorAdapter(Context context){
        this.context = context;
        kerusakanmotorArrayList = new ArrayList<>();
        kerusakanmotorArrayListtemp = new ArrayList<>();
    }

    public void add(Kerusakanmotor kerusakanmotor){
        kerusakanmotorArrayList.add(kerusakanmotor);
        kerusakanmotorArrayListtemp.add(kerusakanmotor);
        notifyItemInserted(kerusakanmotorArrayList.size() - 1);
        notifyItemInserted(kerusakanmotorArrayListtemp.size() - 1);
    }

    public void addAll(ArrayList<Kerusakanmotor> kerusakanmotors){
        for (Kerusakanmotor kerusakanmotor : kerusakanmotors){
            add(kerusakanmotor);;
        }
    }

    public void clear(){
        kerusakanmotorArrayList.clear();
        kerusakanmotorArrayListtemp.clear();
    }


    public void setOnEditClickListener(OnEditClickListener onEditClickListener){
        this.onEditClickListener = onEditClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public Kerusakanmotor getKerusakanmotor(int position) {
        return kerusakanmotorArrayList.get(position);
    }

    public void removeKerusakanmotor(int position){
        if (position >= 0 && position < kerusakanmotorArrayList.size()){
            kerusakanmotorArrayList.remove(position);
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
        holder.bind(kerusakanmotorArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return kerusakanmotorArrayList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Kerusakanmotor> temp = new ArrayList<>();

                if(charSequence.length() == 0)
                    temp.addAll(kerusakanmotorArrayListtemp);
                else
                {
                    String filtrate = charSequence.toString().toLowerCase().trim();

                    if (filtrate.length() == 0){
                        temp = kerusakanmotorArrayListtemp;
                    }else {
                        for(int count = 0; count < kerusakanmotorArrayListtemp.size(); count++)
                        {
                            if(kerusakanmotorArrayListtemp.get(count).getKodeAlatWo().toLowerCase().contains(filtrate) || kerusakanmotorArrayListtemp.get(count).getKodePemakai().toLowerCase().contains(filtrate) || kerusakanmotorArrayListtemp.get(count).getNomorWo().contains(filtrate))
                                temp.add(kerusakanmotorArrayListtemp.get(count));
                        }
                    }


                }

                results.values = temp;
                results.count = temp.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                kerusakanmotorArrayList = (ArrayList<Kerusakanmotor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


        public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tgl_masuk, kode_alat_wo, kode_pemakai, nomor_wo;
        ImageView imgEdit, imgDelete;
        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.listitem_rv_vp_dashboard, itemView, false));
            initView();
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClickListener.onEditClick(getAdapterPosition());
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        public void initView(){
            tgl_masuk = (TextView) itemView.findViewById(R.id.tvtglmasuk_rv_dashboard);
            kode_alat_wo = (TextView) itemView.findViewById(R.id.tvkodealatwo_rv_dashboard);
            kode_pemakai = (TextView) itemView.findViewById(R.id.tvkodepemakai_rv_dashboard);
            nomor_wo = (TextView) itemView.findViewById(R.id.tvnomorwo_rv_dashboard);
            imgEdit = (ImageView) itemView.findViewById(R.id.ivedit_rv_dashboard);
            imgDelete = (ImageView) itemView.findViewById(R.id.ivdelete_rv_dashboard);
        }

        public void bind(Kerusakanmotor kerusakanmotor){
            tgl_masuk.setText("Tanggal Masuk : " + kerusakanmotor.getTglMasuk());
            kode_alat_wo.setText("Kode Alat WO : " + kerusakanmotor.getKodeAlatWo());
            kode_pemakai.setText("Kode Pemakai : " + kerusakanmotor.getKodePemakai());
            nomor_wo.setText("Nomor WO : " + kerusakanmotor.getNomorWo());
        }
    }
}
