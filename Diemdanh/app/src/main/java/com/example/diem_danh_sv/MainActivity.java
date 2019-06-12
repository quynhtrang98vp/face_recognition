package com.example.diem_danh_sv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // khai bao editext, button va hop thoai dialod
    private EditText edtemail;
    private EditText edtpassword;
    private Button sign_in;
    private RadioGroup radioGroup;
    private ProgressDialog pDialog;

    //TAG
    public static final String TAG = MainActivity.class.getSimpleName();
    //url
    private static final String url_student = "https://diemdanhlophoc.000webhostapp.com/login_student.php";
    private static final String url_lecturer = "https://diemdanhlophoc.000webhostapp.com/login_lecturer.php";


    //parmas key
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
            tham chieu va bat su kien
         */
        //tham chieu den cac id trong activity_main.xml
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        sign_in = (Button) findViewById(R.id.sign_in);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        //bat su kien cho button
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input du lieu vao bien
                String email = edtemail.getText().toString().trim();
                String password = edtpassword.getText().toString().trim();

                //check email va password
                if(checkEditText(edtemail) && checkEditText(edtpassword)) {
                    //check id group va login du lieu
                    int idChecked = radioGroup.getCheckedRadioButtonId();
                    if (idChecked != R.id.student && idChecked != R.id.lecturer) {
                        String message = "Hay check sign in as";
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    } else if (idChecked == R.id.student) {
                        loginAccountStudent(email, password);
                    } else {
                        loginAccountLecturer(email, password);
                    }
                }
            }
        });

    }

    /*
        reuquestque chua request
     */
    public RequestQueue newRequestQueue(Context context){
        Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue queue = new RequestQueue(cache, network);
        queue.start();
        return queue;
    }

    /*
        checkEditText de check du lieu trong o
     */
    private  boolean checkEditText (EditText editText){
        if (editText.getText().toString().trim().length()>0)
            return true;
        else {
            editText.setError("Vui long nhap vao day");
        }
        return false;
    }

    /*
        hop thoai dialog
     */
    private void diaplayLoader(){
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Logging In ... Please Wait ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /*
        method login
        xu ly dang nhap
     */

    // xu ly dang nhap voi student
    private void loginAccountStudent(final String email, final String password){
        // khoi tao requestqueue chua request
        RequestQueue queue = Volley.newRequestQueue(this);
        // mohop thoai thong bao
        diaplayLoader();
        //xu ly dang nhap
        StringRequest requestLogin = new StringRequest(Request.Method.POST, url_student,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                String message = "";
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("success") == 1){
                        //truyen du lieu vao account
                        Account account = new Account();
                        account.setToEmail_id ( jsonObject.getString ( "Email_ID" ) );
                        account.setToMail(jsonObject.getString("Gmail"));
                        account.setToName(jsonObject.getString("Ho_Ten"));
                        account.setToVien(jsonObject.getString("Vien"));
                        account.setToDia_chi(jsonObject.getString("Dia_chi"));
                        account.setToChuyen_nganh(jsonObject.getString("Chuyen_Nganh"));

                        //gui thong bao dang nhap thanh cong
                        message = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                        //thay doi giao dien va gui du lieu di
                        Intent intent = new Intent(MainActivity.this,LoginStudent.class);
                        intent.putExtra("login_student",account);
                        startActivity(intent);
                    }
                    else{
                        message = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                return params;
            }
        };
        // them request
        queue.add(requestLogin);
    }

    // xu ly dang nhap voi lecturer
    private void loginAccountLecturer(final String email, final String password){
        // khoi tao requestqueue chua request
        RequestQueue queue = Volley.newRequestQueue(this);
        //mo hop thoai thong bao
        diaplayLoader();
        // xu ly dang nhap
        StringRequest requestLogin = new StringRequest(Request.Method.POST, url_lecturer,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                String message = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("success") == 1) {
                        //truyen du lieu vao account
                        Account account = new Account();
                        account.setToEmail_id ( jsonObject.getString ( "Email_ID" ) );
                        account.setToMail(jsonObject.getString("Gmail"));
                        account.setToName(jsonObject.getString("Ho_Ten"));
                        account.setToVien(jsonObject.getString("Vien"));
                        account.setToDia_chi(jsonObject.getString("Dia_chi"));
                        account.setToChuyen_nganh(jsonObject.getString("Chuyen_Nganh"));

                        //gui thong bao dang nhap thanh cong
                        message = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                        //thay doi giao dien va gui du lieu di
                        Intent intent = new Intent(MainActivity.this, LoginLecturer.class);
                        intent.putExtra("login_lecturer", account);
                        startActivity(intent);
                    } else {
                        message = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                return params;
            }
        };
        // them request
        queue.add(requestLogin);
    }

    /*
        them menu vao khi khoi dong
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
        bat su kien nut back tren actonbar
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
}
