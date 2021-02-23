package com.example.monitoringmotorlistrik.ui.master;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.adapter.AlatwoAdapter;
import com.example.monitoringmotorlistrik.model.Alatwo;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponseAlatwo;
import com.example.monitoringmotorlistrik.response.ResponseUmum;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlatwoFragment extends Fragment implements OnEditClickListener, OnDeleteClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String tanggalbeli;

    private DataService dataService;

    private SharedPreferences sharedPreferences;

    private RecyclerView rvAlatwo;
    private AlatwoAdapter alatwoAdapter;
    private FloatingActionButton floatingActionButton;

    private ArrayList<Alatwo> alatwos = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private EditText kodealatwo;
    private EditText namaalatwo;
    private EditText keteranganalatwo;
    private TextView tglbeli;
    private EditText usiaalatwo;
    private EditText usiarekomendasialatwo;

    private EditText cari;

    // TODO: Rename and change types and number of parameters
    public static AlatwoFragment newInstance(String param1, String param2) {
        AlatwoFragment fragment = new AlatwoFragment();
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
            view = inflater.inflate(R.layout.fragment_alatwo, container, false);
            initView(view);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.alatwo_refresh);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            rvAlatwo.setAdapter(alatwoAdapter);
            rvAlatwo.setLayoutManager(new LinearLayoutManager(getContext()));
            loadData();

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogtambahalatwo = inflater.inflate(R.layout.layout_kelola_alat_wo, null);
                    dialog.setView(dialogtambahalatwo);
                    dialog.setCancelable(true);

                    kodealatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_kode_alat);
                    namaalatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_nama_alat);
                    keteranganalatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_keterangan);
                    tglbeli = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_tv_tglbeli);
                    usiaalatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_usia_maksimum);
                    usiarekomendasialatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_usia_rekomendasi);

                    dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                    tglbeli.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDateDialog();
                        }
                    });


                    dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String reqkodealatwo = kodealatwo.getText().toString();
                            String reqnamaalatwo = namaalatwo.getText().toString();
                            String reqketeranganalatwo = keteranganalatwo.getText().toString();
                            String requsiaalatwo = usiaalatwo.getText().toString();
                            String requsiarekomendasialatwo = usiarekomendasialatwo.getText().toString();
                            String reqidpengguna = sharedPreferences.getString("id", "1");

                            String token = sharedPreferences.getString("token", "");

                            if (tanggalbeli.equalsIgnoreCase("") || reqkodealatwo.equalsIgnoreCase("") ||
                                    reqnamaalatwo.equalsIgnoreCase("") || reqketeranganalatwo.equalsIgnoreCase("") ||
                                    requsiaalatwo.equalsIgnoreCase("") || requsiarekomendasialatwo.equalsIgnoreCase("")
                            ){
                                Toast.makeText(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT).show();
                            }else {
                                Call<ResponseUmum> tambahalatwo = dataService.apiTambahalatwo(
                                        token,
                                        reqkodealatwo,
                                        reqnamaalatwo,
                                        reqketeranganalatwo,
                                        tanggalbeli,
                                        requsiaalatwo,
                                        requsiarekomendasialatwo,
                                        reqidpengguna
                                );

                                tambahalatwo.enqueue(new Callback<ResponseUmum>() {
                                    @Override
                                    public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                        if (response.code() == 200){
                                            if (response.body().isBerhasil()){
                                                loadData();
                                                Toast.makeText(getContext(), "Berhasil Menambahkan Alat WO", Toast.LENGTH_SHORT).show();
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
                    alatwoAdapter.getFilter().filter(cari.getText().toString());
                }
            });
        }

        return view;
    }

    private void initView(View view){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        alatwoAdapter = new AlatwoAdapter(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        rvAlatwo = (RecyclerView) view.findViewById(R.id.fragmenalatwo_rv);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabAlatwo);
        alatwoAdapter.setOnEditClickListener(this);
        alatwoAdapter.setOnDeleteClickListener(this);
        tanggalbeli = "0000-00-00";
        cari = view.findViewById(R.id.fragmenalatwo_cari);
    }

    private void loadData(){
        //Menampung idPengguna yang ada dalam Shared Preference
        alatwos.clear();
        alatwoAdapter.clear();
        String token = sharedPreferences.getString("token","");
        Call<ResponseAlatwo<List<Alatwo>>> loadAlatwo = dataService.apiLihatalatwo(token);
        loadAlatwo.enqueue(new Callback<ResponseAlatwo<List<Alatwo>>>() {

            @Override
            public void onResponse(Call<ResponseAlatwo<List<Alatwo>>> call, Response<ResponseAlatwo<List<Alatwo>>> response) {
                if (response.code() == 200){
                    alatwos.addAll(response.body().getAlatwo());
                    alatwoAdapter.addAll(alatwos);
                    alatwoAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "Gagal Mengambil Alat WO", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAlatwo<List<Alatwo>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Alat WO", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */

                tanggalbeli = dateFormatter.format(newDate.getTime()).toString();
                tglbeli.setText(tanggalbeli);
                Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalbeli, Toast.LENGTH_SHORT).show();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
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
                        String idalatwo = alatwos.get(position).getId();

                        Call<ResponseUmum> callhapusalatwo = dataService.apiHapusalatwo(token, idalatwo);
                        callhapusalatwo.enqueue(new Callback<ResponseUmum>() {
                            @Override
                            public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                                if (response.code() == 200){
                                    if (response.body().isBerhasil()){
                                        Toast.makeText(getContext(), "Berhasil Menghapus Data WO", Toast.LENGTH_SHORT).show();
                                        loadData();
                                    }else {
                                        Toast.makeText(getContext(), "Gagal Menghapus Data WO", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getContext(), "Gagal Menghapus Data WO", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseUmum> call, Throwable t) {
                                Toast.makeText(getContext(), "Gagal Menghapus Data WO", Toast.LENGTH_SHORT).show();
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
        String id = alatwos.get(position).getId();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogtambahalatwo = inflater.inflate(R.layout.layout_kelola_alat_wo, null);
        dialog.setView(dialogtambahalatwo);
        dialog.setCancelable(true);

        kodealatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_kode_alat);
        namaalatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_nama_alat);
        keteranganalatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_keterangan);
        tglbeli = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_tv_tglbeli);
        usiaalatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_usia_maksimum);
        usiarekomendasialatwo = dialogtambahalatwo.findViewById(R.id.layoutkelolaalatwo_et_usia_rekomendasi);

        kodealatwo.setText(alatwos.get(position).getKodeAlatWo());
        namaalatwo.setText(alatwos.get(position).getNamaAlatWo());
        keteranganalatwo.setText(alatwos.get(position).getKeteranganAlatWo());
        tanggalbeli = alatwos.get(position).getTglBeliAlatWo();
        tglbeli.setText(alatwos.get(position).getTglBeliAlatWo());
        usiaalatwo.setText(alatwos.get(position).getUsiaMaksimumAlatWo());
        usiarekomendasialatwo.setText(alatwos.get(position).getUsiaServiceRekomendasi());

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        tglbeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });


        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reqkodealatwo = kodealatwo.getText().toString();
                String reqnamaalatwo = namaalatwo.getText().toString();
                String reqketeranganalatwo = keteranganalatwo.getText().toString();
                String requsiaalatwo = usiaalatwo.getText().toString();
                String requsiarekomendasialatwo = usiarekomendasialatwo.getText().toString();
                String reqidpengguna = sharedPreferences.getString("id", "1");

                String token = sharedPreferences.getString("token", "");

                if (tanggalbeli.equalsIgnoreCase("") || reqkodealatwo.equalsIgnoreCase("") ||
                        reqnamaalatwo.equalsIgnoreCase("") || reqketeranganalatwo.equalsIgnoreCase("") ||
                        requsiaalatwo.equalsIgnoreCase("") || requsiarekomendasialatwo.equalsIgnoreCase("")
                ){
                    Toast.makeText(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseUmum> ubahalatwo = dataService.apiUbahalatwo(
                            token,
                            id,
                            reqkodealatwo,
                            reqnamaalatwo,
                            reqketeranganalatwo,
                            tanggalbeli,
                            requsiaalatwo,
                            requsiarekomendasialatwo,
                            reqidpengguna
                    );

                    ubahalatwo.enqueue(new Callback<ResponseUmum>() {
                        @Override
                        public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                            if (response.code() == 200){
                                if (response.body().isBerhasil()){
                                    loadData();
                                    Toast.makeText(getContext(), "Berhasil Menambahkan Alat WO", Toast.LENGTH_SHORT).show();
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