package com.example.diem_danh_sv.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.diem_danh_sv.LoginLecturer;
import com.example.diem_danh_sv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fragment_thoi_khoa_bieu_gv extends Fragment {

    TextView tvMon1, tvMalop1, tvThoigian1;
    TextView tvMon2, tvMalop2, tvThoigian2;

    String URL_TKB_GV = "https://diemdanhlophoc.000webhostapp.com/thoi_khoa_bieu_gv.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.thoi_khoa_bieu, container, false);

        tvMon1 = (TextView) view.findViewById ( R.id.tvMon1 );
        tvMon2 = (TextView) view.findViewById ( R.id.tvMon2 );
        tvMalop1 = (TextView) view.findViewById ( R.id.tvMalop1 );
        tvMalop2 = (TextView) view.findViewById ( R.id.tvMalop2 );
        tvThoigian1 = (TextView) view.findViewById ( R.id.tvThoiGian1 );
        tvThoigian2 = (TextView) view.findViewById ( R.id.tvThoiGian2 );


        Bundle bundle = getArguments();
        if(bundle != null) {
            String email_id = bundle.getString ( "email_id" );
            Thongtin (email_id , URL_TKB_GV );
        }

        return view;
    }

    private void Thongtin (final String email_id,  final String url) {
        StringRequest stringRequest = new StringRequest( Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray array = jsonObject.getJSONArray( "data" );
                    for (int i = 0; i <array.length(); i++){
                        JSONObject object = array.getJSONObject( i );
                        String monhoc = object.getString( "monhoc" );
                        String malop = object.getString( "lop" );
                        String thoigian = object.getString( "thoigian" );
                        if(i == 0){
                            tvMon1.setText( monhoc );
                            tvMalop1.setText( malop );
                            tvThoigian1.setText( thoigian);
                        }
                        else if(i == 1){
                            tvMon2.setText( monhoc );
                            tvMalop2.setText( malop );
                            tvThoigian2.setText( thoigian);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d ( "AAA", "error:" + e.getMessage ());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } )
        {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap <> (  );
                params.put( "email_id", email_id);
                return params;
            }
        };

        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue( getActivity().getApplicationContext());
        requestQueue.add( stringRequest );
    }
}

