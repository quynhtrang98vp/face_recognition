package com.example.diem_danh_sv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.diem_danh_sv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fragment_thong_ke_sv extends Fragment {
    TextView tvMon1, tvMalop1, tvSolan1;
    TextView tvMon2, tvMalop2, tvSolan2;

    String URL_THONGKE = "https://diemdanhlophoc.000webhostapp.com/thong_ke_diem_danh_sv.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.thong_ke, container, false);

        tvMon1 = (TextView) view.findViewById ( R.id.tvMon1 );
        tvMon2 = (TextView) view.findViewById ( R.id.tvMon2 );
        tvMalop1 = (TextView) view.findViewById ( R.id.tvMalop1 );
        tvMalop2 = (TextView) view.findViewById ( R.id.tvMalop2 );
        tvSolan1 = (TextView) view.findViewById ( R.id.tvSolan1 );
        tvSolan2 = (TextView) view.findViewById ( R.id.tvSolan2 );


        Bundle bundle = getArguments();
        if(bundle != null) {

            String email_id = bundle.getString ( "email_id" );
            ThongKe (email_id , URL_THONGKE );
        }

        return view;
    }

    private void ThongKe (final String email_id,  final String url) {
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
                        String solan  = object.getString( "Solan" );
                        if(i == 0){
                            tvMon1.setText( monhoc );
                            tvMalop1.setText( malop );
                            tvSolan1.setText( solan);
                        }
                        else if(i == 1){
                            tvMon2.setText( monhoc );
                            tvMalop2.setText( malop );
                            tvSolan2.setText( solan);
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
