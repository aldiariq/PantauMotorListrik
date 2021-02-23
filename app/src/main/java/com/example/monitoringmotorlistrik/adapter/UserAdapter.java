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
import com.example.monitoringmotorlistrik.model.User;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<User> userArrayList;
    ArrayList<User> userArrayListtemp;

    OnEditClickListener onEditClickListener;
    OnDeleteClickListener onDeleteClickListener;

    public UserAdapter(Context context) {
        this.context = context;
        userArrayList = new ArrayList<>();
        userArrayListtemp = new ArrayList<>();
    }

    public void add(User user){
        userArrayList.add(user);
        userArrayListtemp.add(user);
        notifyItemInserted(userArrayList.size() - 1);
        notifyItemInserted(userArrayListtemp.size() - 1);
    }

    public void addAll(ArrayList<User> users){
        for (User user : users){
            add(user);
        }
    }

    public void clear(){
        userArrayList.clear();
        userArrayListtemp.clear();
    }

    public void setOnEditClickListener(OnEditClickListener onEditClickListener){
        this.onEditClickListener = onEditClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public User getUser(int position) {
        return userArrayList.get(position);
    }

    public void removeUser(int position){
        if (position >= 0 && position < userArrayList.size()){
            userArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserAdapter.ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        holder.bind(userArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<User> temp = new ArrayList<>();

                if(charSequence.length() == 0)
                    temp.addAll(userArrayListtemp);
                else
                {
                    String filtrate = charSequence.toString().toLowerCase().trim();

                    if (filtrate.length() == 0){
                        temp = userArrayListtemp;
                    }else {
                        for(int count = 0; count < userArrayListtemp.size(); count++)
                        {
                            if(userArrayListtemp.get(count).getNip().toLowerCase().contains(filtrate) || userArrayListtemp.get(count).getNama().toLowerCase().contains(filtrate))
                                temp.add(userArrayListtemp.get(count));
                        }
                    }


                }

                results.values = temp;
                results.count = temp.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userArrayList = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nip_user, nama_user, status_user;
        ImageView ivedit_user, ivdelete_user;

        public ViewHolder(ViewGroup itemView) {
            super(LayoutInflater.from(itemView.getContext()).inflate(R.layout.listitem_rv_vp_user, itemView, false));
            initView();

            ivedit_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClickListener.onEditClick(getAdapterPosition());
                }
            });

            ivdelete_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
        }

        private void initView() {
            nip_user = (TextView) itemView.findViewById(R.id.tvnip_rv_user);
            nama_user = (TextView) itemView.findViewById(R.id.tvnama_rv_user);
            status_user = (TextView) itemView.findViewById(R.id.tvstatus_rv_user);
            ivedit_user = (ImageView) itemView.findViewById(R.id.ivedit_rv_user);
            ivdelete_user = (ImageView) itemView.findViewById(R.id.ivdelete_rv_user);
        }

        public void bind(User user) {
            nip_user.setText("NIP : " + user.getNip());
            nama_user.setText("Nama : " + user.getNama());
            status_user.setText("Status : " + user.getStatusaktivasi());
        }
    }
}
