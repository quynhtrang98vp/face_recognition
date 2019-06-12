package com.example.diem_danh_sv;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.diem_danh_sv.detection.detection;
import com.example.diem_danh_sv.fragment.fragment_account_lecturer;
import com.example.diem_danh_sv.fragment.fragment_gioi_thieu;
import com.example.diem_danh_sv.fragment.fragment_phan_hoi;
import com.example.diem_danh_sv.fragment.fragment_thoi_khoa_bieu_gv;
import com.example.diem_danh_sv.fragment.fragment_thong_ke_gv;
import com.example.diem_danh_sv.fragment.fragment_thong_ke_sv;
import com.example.diem_danh_sv.fragment.fragment_tro_giup;


public class LoginLecturer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    // khai bao doi tuong account
    private Account account;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.login_lecturer);

        // them toolbar
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bat su kien cho fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // them drawer layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout_lecturer);
        //them leftmenu
        NavigationView navigationView = findViewById(R.id.nav_view_lecturer);
        //tao va them thanh mo left menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /*
    nhan intent tu MainActivity
     */
    private void receive(){
        Intent intent = getIntent();
        account = new Account();
        account = (Account) intent.getSerializableExtra("login_lecturer");
    }

    /*
    menu them vao khi khoi dong
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout_lecturer);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    /*
    bat su kien cho navigation
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        // neu click item
       int id= item.getItemId();
        //fragment
        Fragment fragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(id == R.id.Tai_khoan_gv){
            receive();
            //tao bundle de gui du lieu
            Bundle bundle = new Bundle();
            bundle.putString("Email_id",account.getToEmail_id ());
            bundle.putString("Name",account.getToName());
            bundle.putString("Vien",account.getToVien());
            bundle.putString("Gmail",account.getToMail());
            bundle.putString("Chuyen_Nganh",account.getToChuyen_nganh());
            bundle.putString("Dia_chi",account.getToDia_chi());

            // tao fragment moi
            fragment = new fragment_account_lecturer();

            //thay the va truyen du lieu qua fragment
            ft.replace(R.id.frag_tai_khoan, fragment);
            ft.commit();
            fragment.setArguments(bundle);

        } else if(id == R.id.Diem_danh_gv){

            receive();
            Intent intent_diem_danh = new Intent(LoginLecturer.this, detection.class);
            Bundle bundle = new Bundle();
            bundle.putString("email_id", account.getToEmail_id ());
            intent_diem_danh.putExtras ( bundle );
            startActivity(intent_diem_danh);

        } else if(id == R.id.thoi_khoa_bieu_gv){

            receive();
            Bundle bundle = new Bundle();
            bundle.putString("email_id", account.getToEmail_id ());
            fragment = new fragment_thoi_khoa_bieu_gv ();
            ft.replace(R.id.frag_tai_khoan, fragment);
            ft.commit();
            fragment.setArguments(bundle);

        } else if(id == R.id.thong_ke_gv){

            receive();
            Bundle bundle = new Bundle();
            bundle.putString("email_id_gv", account.getToEmail_id ());
            fragment = new fragment_thong_ke_gv ();
            ft.replace(R.id.frag_tai_khoan, fragment);
            ft.commit();
            fragment.setArguments(bundle);

        } else if(id == R.id.tro_giup_gv){
            // tao fragment moi
            fragment = new fragment_tro_giup();

            //thay fragment moi
            ft.replace(R.id.frag_tai_khoan, fragment);
            ft.commit();

        }else if(id == R.id.phan_hoi_gv){
            // tao fragment moi
            fragment = new fragment_phan_hoi();

            //thay fragment moi
            ft.replace(R.id.frag_tai_khoan, fragment);
            ft.commit();
        }else if(id == R.id.gioi_thieu_gv){
            // tao fragment moi
            fragment = new fragment_gioi_thieu();

            //thay fragment moi
            ft.replace(R.id.frag_tai_khoan, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_lecturer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

