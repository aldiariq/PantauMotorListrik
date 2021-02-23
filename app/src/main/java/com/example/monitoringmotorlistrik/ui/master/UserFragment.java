package com.example.monitoringmotorlistrik.ui.master;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.adapter.PemakaiAdapter;
import com.example.monitoringmotorlistrik.adapter.UserAdapter;
import com.example.monitoringmotorlistrik.model.Pemakai;
import com.example.monitoringmotorlistrik.model.User;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponsePemakai;
import com.example.monitoringmotorlistrik.response.ResponseUmum;
import com.example.monitoringmotorlistrik.response.ResponseUser;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment implements OnEditClickListener, OnDeleteClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;

    private DataService dataService;

    private SharedPreferences sharedPreferences;

    private RecyclerView rvUser;
    private UserAdapter userAdapter;

    private ArrayList<User> users = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Spinner statusaktivasi;
    private EditText cari;

    private String token;

    public UserFragment() {
        // Required empty public constructor
    }
    
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_user, container, false);
            initView(view);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.user_refresh);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            rvUser.setAdapter(userAdapter);
            rvUser.setLayoutManager(new LinearLayoutManager(getContext()));
            cari.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    userAdapter.getFilter().filter(cari.getText().toString());
                }
            });
            loadData();
        }

        return view;
    }

    private void initView(View view){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userAdapter = new UserAdapter(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        rvUser = (RecyclerView) view.findViewById(R.id.fragmenuser_rv);
        userAdapter.setOnEditClickListener(this);
        userAdapter.setOnDeleteClickListener(this);
        cari = view.findViewById(R.id.fragmenuser_cari);
        token = sharedPreferences.getString("token","");
    }

    private void loadData(){
        //Menampung idPengguna yang ada dalam Shared Preference
        users.clear();
        userAdapter.clear();
        Call<ResponseUser<List<User>>> loaduser = dataService.apiLihatdaftaruser(token);
        loaduser.enqueue(new Callback<ResponseUser<List<User>>>() {
            @Override
            public void onResponse(Call<ResponseUser<List<User>>> call, Response<ResponseUser<List<User>>> response) {
                if (response.code() == 200){
                    users.addAll(response.body().getUser());
                    userAdapter.addAll(users);
                    userAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "Gagal Mengambil User", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser<List<User>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil User", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDeleteClick(int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Ingin Menghapus Data ?");
        alertDialogBuilder
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        String token = sharedPreferences.getString("token", "");
                        String iduser = users.get(position).getId();

                        Call<ResponseUmum> callhapusUser = dataService.apiHapususer(token, iduser);
                        callhapusUser.enqueue(new Callback<ResponseUmum>() {
                            @Override
                            public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                if (response.code() == 200){
                                    if (response.body().isBerhasil()){
                                        Toast.makeText(getContext(), "Berhasil Menghapus Data User", Toast.LENGTH_SHORT).show();
                                        loadData();
                                    }else {
                                        Toast.makeText(getContext(), "Gagal Menghapus Data User", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getContext(), "Gagal Menghapus Data User", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseUmum> call, Throwable t) {
                                Toast.makeText(getContext(), "Gagal Menghapus Data User", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onEditClick(int position) {
        String id = users.get(position).getId();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogubahuser = inflater.inflate(R.layout.layout_kelola_user, null);
        dialog.setView(dialogubahuser);
        dialog.setCancelable(true);

        statusaktivasi = dialogubahuser.findViewById(R.id.layoutkelolauser_sp_statusaktivasi);

        Call<ResponseUser<List<User>>> responseUserCall = dataService.apiLihatdaftaruser(token);
        responseUserCall.enqueue(new Callback<ResponseUser<List<User>>>() {
            @Override
            public void onResponse(Call<ResponseUser<List<User>>> call, Response<ResponseUser<List<User>>> response) {
                String arrayaktivasi[] = {"AKTIF", "TIDAKAKTIF"};
                List<String> listSpinner = new ArrayList<String>();

                for (int i=0;i<arrayaktivasi.length;i++){
                    listSpinner.add(arrayaktivasi[i]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statusaktivasi.setAdapter(adapter);
                for (int i=0;i<arrayaktivasi.length;i++){
                    if (arrayaktivasi[i].equalsIgnoreCase(users.get(position).getStatusaktivasi())){
                        statusaktivasi.setSelection(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser<List<User>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Status Aktivasi User", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reqstatusaktivasi = statusaktivasi.getSelectedItem().toString();

                String token = sharedPreferences.getString("token", "");

                if (reqstatusaktivasi.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseUmum> ubahstatususer = dataService.apiUbahstatususer(
                            token,
                            id,
                            reqstatusaktivasi
                    );

                    ubahstatususer.enqueue(new Callback<ResponseUmum>() {
                        @Override
                        public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                            if (response.code() == 200){
                                if (response.body().isBerhasil()){
                                    loadData();
                                    Toast.makeText(getContext(), "Berhasil Mengubah Status User", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), "Gagal Mengubah Status User", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Gagal Mengubah Status User", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUmum> call, Throwable t) {
                            Toast.makeText(getContext(), "Gagal Mengubah Status User", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}