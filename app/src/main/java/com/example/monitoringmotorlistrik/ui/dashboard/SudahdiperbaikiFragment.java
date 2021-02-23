package com.example.monitoringmotorlistrik.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.adapter.KerusakanmotorAdapter;
import com.example.monitoringmotorlistrik.model.Alatwo;
import com.example.monitoringmotorlistrik.model.Kerusakanmotor;
import com.example.monitoringmotorlistrik.model.Pemakai;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.Server;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;
import com.example.monitoringmotorlistrik.response.ResponseAlatwo;
import com.example.monitoringmotorlistrik.response.ResponseKerusakanmotor;
import com.example.monitoringmotorlistrik.response.ResponsePemakai;
import com.example.monitoringmotorlistrik.response.ResponseUmum;
import com.example.monitoringmotorlistrik.utils.OnDeleteClickListener;
import com.example.monitoringmotorlistrik.utils.OnEditClickListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SudahdiperbaikiFragment extends Fragment implements OnEditClickListener, OnDeleteClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private DataService dataService;
    private RecyclerView rvSudahdiperbaiki;
    private KerusakanmotorAdapter kerusakanmotorAdapter;
    private SharedPreferences sharedPreferences;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<Kerusakanmotor> kerusakanmotors = new ArrayList<>();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String tanggalmasuk;
    private String tanggalselesai;
    private String tanggalkeluar;

    private ImageView ivfotosebelum;
    private ImageView ivfotosesudah;
    private TextView tglmasuk;
    private TextView tglselesai;
    private TextView tglkeluar;
    private Spinner kodealatwo;
    private Spinner kodepemakai;
    private EditText materialbearing;
    private EditText materialkawatkg;
    private EditText nomorwo;
    private EditText keterangan;
    private EditText dll;

    private EditText cari;

    private String token;

    public SudahdiperbaikiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SudahdiperbaikiFragment newInstance(String param1, String param2) {
        SudahdiperbaikiFragment fragment = new SudahdiperbaikiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loadData();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sudahdiperbaiki, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sudahdiperbaiki_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        initView(view);
        rvSudahdiperbaiki.setAdapter(kerusakanmotorAdapter);
        rvSudahdiperbaiki.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                kerusakanmotorAdapter.getFilter().filter(cari.getText().toString());
            }
        });

        return view;
    }

    private void initView(View view){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        kerusakanmotorAdapter = new KerusakanmotorAdapter(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        rvSudahdiperbaiki = (RecyclerView) view.findViewById(R.id.fragmensudahdiperbaiki_rv);
        kerusakanmotorAdapter.setOnEditClickListener(this);
        kerusakanmotorAdapter.setOnDeleteClickListener(this);
        tanggalmasuk = "0000-00-00";
        tanggalselesai = "0000-00-00";
        tanggalkeluar = "0000-00-00";
        cari = view.findViewById(R.id.fragmensudahdiperbaiki_cari);
        token = sharedPreferences.getString("token", "");
    }

    @Override
    public void onDeleteClick(int position) {
        String token = sharedPreferences.getString("token", "");
        String id = kerusakanmotors.get(position).getId();

        Call<ResponseUmum> callhapuskerusakan = dataService.apiHapuskerusakanmotor(token, id);
        callhapuskerusakan.enqueue(new Callback<ResponseUmum>() {
            @Override
            public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                if (response.code() == 200){
                    if (response.body().isBerhasil()){
                        Toast.makeText(getContext(), "Berhasil Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
                        loadData();
                    }else {
                        Toast.makeText(getContext(), "Gagal Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Gagal Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUmum> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Menghapus Kerusakan Motor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(int position) {
        String id = kerusakanmotors.get(position).getId();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogtambahkerusakan = inflater.inflate(R.layout.layout_kelola_motor_rusak, null);
        dialog.setView(dialogtambahkerusakan);
        dialog.setCancelable(true);

        ivfotosebelum = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_iv_fotosebelum);
        ivfotosesudah = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_iv_fotosesudah);
        tglmasuk = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_tv_tglmasuk);
        tglselesai = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_tv_tglselesai);
        tglkeluar = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_tv_tglkeluar);
        kodealatwo = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_sp_kode_alat);
        kodepemakai = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_sp_kode_pemakai);
        materialbearing = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_material_bearing);
        materialkawatkg = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_material_kawat_kg);
        nomorwo = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_nomor_wo);
        keterangan = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_keterangan);
        dll = dialogtambahkerusakan.findViewById(R.id.layoutkelolarusak_et_dll);

        if (kerusakanmotors.get(position).getFoto_sebelum() != null){
            Picasso.get().load(Server.BASE_URL + "FileUpload/Fotokerusakan/" + kerusakanmotors.get(position).getFoto_sebelum()).into(ivfotosebelum);
        }
        if (kerusakanmotors.get(position).getFoto_sesudah() != null){
            Picasso.get().load(Server.BASE_URL + "FileUpload/Fotokerusakan/" + kerusakanmotors.get(position).getFoto_sesudah()).into(ivfotosesudah);
        }

        Call<ResponseAlatwo<List<Alatwo>>> responseAlatwoCall = dataService.apiLihatalatwo(token);
        responseAlatwoCall.enqueue(new Callback<ResponseAlatwo<List<Alatwo>>>() {
            @Override
            public void onResponse(Call<ResponseAlatwo<List<Alatwo>>> call, Response<ResponseAlatwo<List<Alatwo>>> response) {
                List<Alatwo> alatwoArrayList = response.body().getAlatwo();
                List<String> listSpinner = new ArrayList<String>();
                for (int i=0;i<alatwoArrayList.size();i++){
                    listSpinner.add(alatwoArrayList.get(i).getKodeAlatWo());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kodealatwo.setAdapter(adapter);
                for (int i=0;i<alatwoArrayList.size();i++){
                    if (alatwoArrayList.get(i).getKodeAlatWo().equalsIgnoreCase(kerusakanmotors.get(position).getKodeAlatWo())){
                        kodealatwo.setSelection(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAlatwo<List<Alatwo>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Kode Alat", Toast.LENGTH_SHORT).show();
            }
        });

        Call<ResponsePemakai<List<Pemakai>>> responsePemakaiCall = dataService.apiLihatpemakai(token);
        responsePemakaiCall.enqueue(new Callback<ResponsePemakai<List<Pemakai>>>() {
            @Override
            public void onResponse(Call<ResponsePemakai<List<Pemakai>>> call, Response<ResponsePemakai<List<Pemakai>>> response) {
                List<Pemakai> pemakaiList = response.body().getPemakai();
                Log.i("BESARPEMAKAI", String.valueOf(pemakaiList.size()));
                List<String> listSpinner = new ArrayList<String>();
                for (int i=0;i<pemakaiList.size();i++){
                    listSpinner.add(pemakaiList.get(i).getKodePemakai());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                kodepemakai.setAdapter(adapter);
                for (int i=0;i<pemakaiList.size();i++){
                    if (pemakaiList.get(i).getKodePemakai().equalsIgnoreCase(kerusakanmotors.get(position).getKodePemakai())){
                        kodepemakai.setSelection(i);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePemakai<List<Pemakai>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil Kode Alat", Toast.LENGTH_SHORT).show();
            }
        });

        tanggalmasuk = kerusakanmotors.get(position).getTglMasuk();
        tglmasuk.setText(kerusakanmotors.get(position).getTglMasuk());
        tglselesai.setText(kerusakanmotors.get(position).getTglSelesai());
        tglkeluar.setText(kerusakanmotors.get(position).getTglKeluar());
        kodealatwo.setEnabled(false);
        kodepemakai.setEnabled(false);
        materialbearing.setText(kerusakanmotors.get(position).getMaterialBearing());
        materialbearing.setEnabled(false);
        materialkawatkg.setText(kerusakanmotors.get(position).getMaterialKawatKg());
        materialkawatkg.setEnabled(false);
        nomorwo.setText(kerusakanmotors.get(position).getNomorWo());
        nomorwo.setEnabled(false);
        keterangan.setText(kerusakanmotors.get(position).getKeterangan());
        keterangan.setEnabled(false);
        dll.setText(kerusakanmotors.get(position).getDll());
        dll.setEnabled(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        tglkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(3);
            }
        });

        dialog.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reqkodealatwo = kodealatwo.getSelectedItem().toString();
                String reqkodepemakai = kodepemakai.getSelectedItem().toString();
                String reqmaterialbearing = materialbearing.getText().toString();
                String reqmateialkawat = materialkawatkg.getText().toString();
                String reqnomorwo = nomorwo.getText().toString();
                String reqketerangan = keterangan.getText().toString();
                String reqdll = dll.getText().toString();
                String reqidpengguna = sharedPreferences.getString("id", "1");

                if (tanggalmasuk.equalsIgnoreCase("") || tanggalselesai.equalsIgnoreCase("") || tanggalkeluar.equalsIgnoreCase("")
                        || reqkodealatwo.equalsIgnoreCase("") || reqkodepemakai.equalsIgnoreCase("") || reqmaterialbearing.equalsIgnoreCase("")
                        || reqmateialkawat.equalsIgnoreCase("") || reqnomorwo.equalsIgnoreCase("") || reqketerangan.equalsIgnoreCase("")
                        || reqdll.equalsIgnoreCase("") || reqidpengguna.equalsIgnoreCase("")
                ){
                    Toast.makeText(getContext(), "Mohon Lengkapi Inputan", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseUmum> callKonfirmasipengambilan = dataService.apiKonfirmasipengambilan(
                            token,
                            id,
                            tanggalkeluar,
                            reqidpengguna
                    );

                    callKonfirmasipengambilan.enqueue(new Callback<ResponseUmum>() {
                        @Override
                        public void onResponse(Call<ResponseUmum> call, Response<ResponseUmum> response) {
                            if (response.code() == 200){
                                if (response.body().isBerhasil()){
                                    Toast.makeText(getContext(), "Berhasil Mengkonfirmasi Pengambilan", Toast.LENGTH_SHORT).show();
                                    loadData();
                                }else {
                                    Toast.makeText(getContext(), "Gagal Mengkonfirmasi Pengambilan", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Gagal Mengkonfirmasi Pengambilan", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUmum> call, Throwable t) {
                            Toast.makeText(getContext(), "Gagal Mengkonfirmasi Pengambilan", Toast.LENGTH_SHORT).show();
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

    private void loadData(){
        //Menampung idPengguna yang ada dalam Shared Preference
        kerusakanmotors.clear();
        kerusakanmotorAdapter.clear();
        String token = sharedPreferences.getString("token","");
        Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> loadKerusakanmotor = dataService.apiLihatkerusakanmotorsudah(token);
        try {
            loadKerusakanmotor.enqueue(new Callback<ResponseKerusakanmotor<List<Kerusakanmotor>>>() {

                @Override
                public void onResponse(Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> call, Response<ResponseKerusakanmotor<List<Kerusakanmotor>>> response) {
                    if (response.code() == 200){
                        kerusakanmotors.addAll(response.body().getKerusakanmotor());
                        kerusakanmotorAdapter.addAll(kerusakanmotors);
                        kerusakanmotorAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getContext(), "Gagal Mengambil Kerusakan Motor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseKerusakanmotor<List<Kerusakanmotor>>> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal Mengambil Kerusakan Motor", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "Gagal Mengambil Kerusakan Motor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        loadData();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showDateDialog(int angka){

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

                if (angka == 1){
                    tanggalmasuk = dateFormatter.format(newDate.getTime()).toString();
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalmasuk, Toast.LENGTH_SHORT).show();
                }else if(angka == 2){
                    tanggalselesai = dateFormatter.format(newDate.getTime()).toString();
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalselesai, Toast.LENGTH_SHORT).show();
                }else if (angka == 3){
                    tanggalkeluar = dateFormatter.format(newDate.getTime()).toString();
                    tglkeluar.setText(tanggalkeluar);
                    Toast.makeText(getContext(), "Tanggal Dipilih : " + tanggalkeluar, Toast.LENGTH_SHORT).show();
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }

}