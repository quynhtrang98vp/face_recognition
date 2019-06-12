package com.example.diem_danh_sv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diem_danh_sv.R;

public class fragment_phan_hoi  extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contanier, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phan_hoi, contanier, false);

        return view;
    }
}
