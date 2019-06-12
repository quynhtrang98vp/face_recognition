package com.example.diem_danh_sv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diem_danh_sv.R;

public class fragment_account_lecturer extends Fragment {
    private TextView ho_ten;
    private TextView vien;
    private TextView gmail;
    private TextView dia_chi;
    private TextView chuyen_nganh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contanier, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tai_khoan, contanier, false);

        //tham chieu den id cua tai_khoan_gv
        ho_ten = (TextView) view.findViewById(R.id.ho_ten_gv_data);
        vien =(TextView) view.findViewById(R.id.Vien_data);
        gmail =(TextView) view.findViewById(R.id.Gmail_data);
        dia_chi =(TextView) view.findViewById(R.id.phong_lam_viec_data);
        chuyen_nganh =(TextView) view.findViewById(R.id.chuyen_nganh_data);

        // nhan du lieu tu login_lecturer
        Bundle bundle = getArguments();
        if(bundle != null) {
            String toName = bundle.getString("Name");
            String toMail = getArguments().getString("Gmail");
            String toVien = getArguments().getString("Vien");
            String toDia_chi = getArguments().getString("Dia_chi");
            String toChuyen_nganh = getArguments().getString("Chuyen_Nganh");

            //truyen du lieu vao o TextView
            ho_ten.setText(toName);
            vien.setText(toVien);
            gmail.setText(toMail);
            dia_chi.setText(toDia_chi);
            chuyen_nganh.setText(toChuyen_nganh);
        }

        return view;
    }
}
