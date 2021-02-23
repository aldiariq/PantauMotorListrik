package com.example.monitoringmotorlistrik.ui.master;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.monitoringmotorlistrik.R;
import com.example.monitoringmotorlistrik.network.DataService;
import com.example.monitoringmotorlistrik.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

public class MasterFragment extends Fragment {

    private ViewPager viewPager;

    private DataService dataService;

    private SharedPreferences sharedPreferences;

    private View view;

    private ImageView ivmundur, ivmaju;

    public MasterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MasterFragment newInstance(String param1, String param2) {
        MasterFragment fragment = new MasterFragment();
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
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_master, container, false);
            initView(view);
            ivmundur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
                }
            });
            ivmaju.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                }
            });
            setupViewPager(viewPager);
        }

        return view;
    }

    private void initView(View view){
        viewPager = (ViewPager) view.findViewById(R.id.vp_master);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        ivmundur = view.findViewById(R.id.iv_mundur_master);
        ivmaju = view.findViewById(R.id.iv_maju_master);
    }

    private void setupViewPager(ViewPager viewPager) {
        if (sharedPreferences.getString("status", "").equalsIgnoreCase("ADMIN")){
            MasterFragment.Adapter adapter = new Adapter(getChildFragmentManager());
            adapter.addFragment(new AlatwoFragment());
            adapter.addFragment(new PemakaiFragment());
            adapter.addFragment(new UserFragment());
            viewPager.setAdapter(adapter);
        }else {
            MasterFragment.Adapter adapter = new Adapter(getChildFragmentManager());
            adapter.addFragment(new AlatwoFragment());
            adapter.addFragment(new PemakaiFragment());
            viewPager.setAdapter(adapter);
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public Adapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}