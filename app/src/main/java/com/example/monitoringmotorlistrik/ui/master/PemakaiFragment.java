package com.example.monitoringmotorlistrik.ui.master;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.adapter.PemakaiAdapter;
import com.example.monitoringmotorlistrik.model.Pemakai;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponsePemakai;
import com.example.monitoringmotorlistrik.response.ResponseUmum;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemakaiFragment extends Fragment implements OnEditClickListener, OnDeleteClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;

    private DataService dataService;

    private SharedPreferences sharedPreferences;

    private RecyclerView rvPemakai;
    private PemakaiAdapter pemakaiAdapter;
    private FloatingActionButton floatingActionButton;

    private ArrayList<Pemakai> pemakais = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private EditText kodepemakai;
    private EditText keteranganpemakai;

    private EditText cari;

    public PemakaiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PemakaiFragment newInstance(String param1, String param2) {
        PemakaiFragment fragment = new PemakaiFragment();
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
            view = inflater.inflate(R.layout.fragment_pemakai, container, false);
            initView(view);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pemakai_refresh);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            rvPemakai.setAdapter(pemakaiAdapter);
            rvPemakai.setLayoutManager(new LinearLayoutManager(getContext()));
            loadData();

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogtambahpemakai = inflater.inflate(R.layout.layout_kelola_pemakai, null);
                    dialog.setView(dialogtambahpemakai);
                    dialog.setCancelable(true);

                    kodepemakai = dialogtambahpemakai.findViewById(R.id.layoutkelolapemakai_et_kode_pemakai);
                    keteranganpemakai = dialogtambahpemakai.findViewById(R.id.layoutkelolapemakai_et_keterangan_pemakai);

                    dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String reqkodepemakai = kodepemakai.getText().toString();
                            String reqketeranganpemakai = keteranganpemakai.getText().toString();

                            String reqidpengguna = sharedPreferences.getString("id", "1");

                            String token = sharedPreferences.getString("token", "");

                            if (reqkodepemakai.equalsIgnoreCase("") || reqketeranganpemakai.equalsIgnoreCase("")){
                                Toast.makeText(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT).show();
                            }else {
                                Call<ResponseUmum> tambahpemakai = dataService.apiTambahpemakai(
                                        token,
                                        reqkodepemakai,
                                        reqketeranganpemakai,
                                        reqidpengguna
                                );

                                tambahpemakai.enqueue(new Callback<ResponseUmum>() {
                                    @Override
                                    public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                        if (response.code() == 200){
                                            if (response.body().isBerhasil()){
                                                loadData();
                                                Toast.makeText(getContext(), "Berhasil Menambahkan Pemakai", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getContext(), "Pastikan Inputan Benar", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(getContext(), "Pastikan Inputan Benar", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseUmum> call, Throwable t) {
                                        Toast.makeText(getContext(), "Pastikan Inputan Benar", Toast.LENGTH_SHORT).show();
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
            });

            cari.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    pemakaiAdapter.getFilter().filter(cari.getText().toString());
                }
            });
        }

        return view;
    }

    private void initView(View view){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        pemakaiAdapter = new PemakaiAdapter(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        rvPemakai = (RecyclerView) view.findViewById(R.id.fragmenpemakai_rv);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabPemakai);
        pemakaiAdapter.setOnEditClickListener(this);
        pemakaiAdapter.setOnDeleteClickListener(this);
        cari = view.findViewById(R.id.fragmenpemakai_cari);
    }

    private void loadData(){
        //Menampung idPengguna yang ada dalam Shared Preference
        pemakais.clear();
        pemakaiAdapter.clear();
        String token = sharedPreferences.getString("token","");
        Call<ResponsePemakai<List<Pemakai>>> loadpemakai = dataService.apiLihatpemakai(token);
        loadpemakai.enqueue(new Callback<ResponsePemakai<List<Pemakai>>>() {
            @Override
            public void onResponse(Call<ResponsePemakai<List<Pemakai>>> call, Response<ResponsePemakai<List<Pemakai>>> response) {
                if (response.code() == 200){
                    pemakais.addAll(response.body().getPemakai());
                    pemakaiAdapter.addAll(pemakais);
                    pemakaiAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "Gagal Mengambil Pemakai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePemakai<List<Pemakai>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Pemakai", Toast.LENGTH_SHORT).show();
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
                        String idpemakai = pemakais.get(position).getId();

                        Call<ResponseUmum> callhapuspemakai = dataService.apiHapuspemakai(token, idpemakai);
                        callhapuspemakai.enqueue(new Callback<ResponseUmum>() {
                            @Override
                            public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                if (response.code() == 200){
                                    if (response.body().isBerhasil()){
                                        Toast.makeText(getContext(), "Berhasil Menghapus Data Pemakai", Toast.LENGTH_SHORT).show();
                                        loadData();
                                    }else {
                                        Toast.makeText(getContext(), "Gagal Menghapus Data Pemakai", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getContext(), "Gagal Menghapus Data Pemakai", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseUmum> call, Throwable t) {
                                Toast.makeText(getContext(), "Gagal Menghapus Data Pemakai", Toast.LENGTH_SHORT).show();
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
        String id = pemakais.get(position).getId();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogubahpemakai = inflater.inflate(R.layout.layout_kelola_pemakai, null);
        dialog.setView(dialogubahpemakai);
        dialog.setCancelable(true);

        kodepemakai = dialogubahpemakai.findViewById(R.id.layoutkelolapemakai_et_kode_pemakai);
        keteranganpemakai = dialogubahpemakai.findViewById(R.id.layoutkelolapemakai_et_keterangan_pemakai);

        kodepemakai.setText(pemakais.get(position).getKodePemakai());
        keteranganpemakai.setText(pemakais.get(position).getKeteranganPemakai());

        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reqkodepemakai = kodepemakai.getText().toString();
                String reqketeranganpemakai = keteranganpemakai.getText().toString();

                String reqidpengguna = sharedPreferences.getString("id", "1");

                String token = sharedPreferences.getString("token", "");

                if (reqkodepemakai.equalsIgnoreCase("") || reqketeranganpemakai.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseUmum> ubahpemakai = dataService.apiUbahpemakai(
                            token,
                            id,
                            reqkodepemakai,
                            reqketeranganpemakai,
                            reqidpengguna
                    );

                    ubahpemakai.enqueue(new Callback<ResponseUmum>() {
                        @Override
                        public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                            if (response.code() == 200){
                                if (response.body().isBerhasil()){
                                    loadData();
                                    Toast.makeText(getContext(), "Berhasil Menambahkan Pemakai", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), "Pastikan Inputan Benar", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Pastikan Inputan Kode Benar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUmum> call, Throwable t) {
                            Toast.makeText(getContext(), "Pastikan Inputan Kode Benar", Toast.LENGTH_SHORT).show();
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