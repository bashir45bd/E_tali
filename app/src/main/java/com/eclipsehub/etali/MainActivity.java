package com.eclipsehub.etali;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DrawerLayout main;
    MaterialToolbar app_bar;
    NavigationView nev_view;
    ActionBarDrawerToggle toggle;
    RecyclerView user_list;
    ExtendedFloatingActionButton fav;
    LottieAnimationView loti;
    TextInputEditText search;

    DB_helper dbHelper;
    TextView rec,pay;

    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;

    boolean backpress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new DB_helper(MainActivity.this);

        main=findViewById(R.id.main);
        app_bar=findViewById(R.id.app_bar);
        nev_view=findViewById(R.id.nav_View);
        user_list = findViewById(R.id.user_list);
        fav = findViewById(R.id.fav);
        loti=findViewById(R.id.loti);
        rec =findViewById(R.id.rec);
        pay=findViewById(R.id.pay);
        search=findViewById(R.id.search);


        tali_total(dbHelper.get_user());
        get_tali_user(dbHelper.get_user());


        fav.setOnClickListener(v -> {
            add_user(MainActivity.this);
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String key44 = search.getText().toString();
                get_tali_user(dbHelper.search(key44));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        toggle = new ActionBarDrawerToggle(MainActivity.this, main,app_bar, R.string.open, R.string.close);
        main.addDrawerListener(toggle);


        nev_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId()==R.id.shareapp){

                    share_method(MainActivity.this);

                    main.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.rateapp){

                    Context context=MainActivity.this;
                    final String apppn = context.getPackageName();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+apppn));
                    startActivity(intent);

                    try {

                    } catch (ActivityNotFoundException e){

                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("https://play.google.com/store/apps/details?id="+apppn));
                        startActivity(intent1);

                    }

                    main.closeDrawer(GravityCompat.START);
                }



                else if (item.getItemId()==R.id.policy){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                   intent.setData(Uri.parse("https://sites.google.com/view/eclipsehub-etali"));
                   startActivity(intent);



                    main.closeDrawer(GravityCompat.START);
                }




                else if (item.getItemId()==R.id.more){

                    String devmane = "EclipsHub Soft";
                    try {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub: "+devmane)));

                    } catch (ActivityNotFoundException e){

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id= "+devmane)));

                    }

                    main.closeDrawer(GravityCompat.START);
                }

                return false;
            }
        });



    }//==================================================

    public void add_user(Context context) {


        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View view = getLayoutInflater().inflate(R.layout.add_user, null);

        // Set the layout for the BottomSheetDialog
        dialog.setContentView(view);

        view.findViewById(R.id.submit).setOnClickListener(v -> {

            EditText name = view.findViewById(R.id.name);
            EditText phone = view.findViewById(R.id.phone);

            if (name.length() > 0 && phone.length()==9) {

                String name_a = name.getText().toString();
                String phone_a = phone.getText().toString();
                dbHelper.add_user(name_a, phone_a);
                get_tali_user(dbHelper.get_user());
                dialog.dismiss();

            } else {
                name.setError("Enter name");
                phone.setError("Enter number");

            }


        });

        dialog.show();

    }

    public void get_tali_user(Cursor cursor) {


        if (cursor != null && cursor.getCount() > 0) {
            loti.setVisibility(View.GONE);
            arrayList = new ArrayList<>();
            user_list.setVisibility(View.VISIBLE);
            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                double time = cursor.getDouble(3);

                hashMap = new HashMap<>();
                hashMap.put("id", "" + id);
                hashMap.put("name", name);
                hashMap.put("phone", phone);
                hashMap.put("time", "" + time);
                arrayList.add(hashMap);

            }

            user_list adapter = new user_list();
            user_list.setAdapter(adapter);
            user_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        } else {
           loti.setVisibility(View.VISIBLE);
            user_list.setVisibility(View.GONE);
        }

    }

    public void tali_total(Cursor cursor) {

        double total=0;
        double total1=0;

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);

                double ff = dbHelper.cal_rec(String.valueOf(id)) - dbHelper.cal_pay(String.valueOf(id));
                if (ff>0){
                    total=total+ff;
                    rec.setText(""+total+"৳");

                }
                else if(ff<0){
                    double taka= ff*-1;
                    total1=total1+taka;
                    pay.setText(""+total1+"৳");
                }
                else {
                    rec.setText(""+total+"৳");
                    pay.setText(""+total1+"৳");
                }

            }

        }
    }

    private class user_list extends RecyclerView.Adapter<user_list.view_holder>{


        private class view_holder extends RecyclerView.ViewHolder{

            TextView name, phone, time,imageView;
            LinearLayout item;
            LinearLayout anim1;

            public view_holder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                phone = itemView.findViewById(R.id.phone);
                item = itemView.findViewById(R.id.item);
                imageView = itemView.findViewById(R.id.list_image);
            //    anim1 = itemView.findViewById(R.id.income_item);

            }
        }


        @NonNull
        @Override
        public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.item, parent, false);

            return new view_holder(view) ;
        }

        @Override
        public void onBindViewHolder(@NonNull view_holder holder, int position) {

            HashMap<String, String> hashMap = arrayList.get(position);
            String fid = hashMap.get("id");
            String f_name = hashMap.get("name");
            String f_phone = hashMap.get("phone");


            Random mRandom = new Random();
            int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
            ((GradientDrawable) holder.imageView.getBackground()).setColor(color);
            holder.imageView.setText(f_name.substring(0, 1));


            holder.name.setText(f_name);
           double ff = dbHelper.cal_rec(fid) - dbHelper.cal_pay(fid);



           if (ff > 0) {
                holder.phone.setText("Receivable " + ff + "৳");

            } else if (ff == 0) {
                holder.phone.setText(ff + "৳");
            } else {
                double f_taka= ff*-1;
                holder.phone.setText("Payable " + f_taka + "৳");
                holder.phone.setTextColor(Color.parseColor("#DE0000"));

            }


           holder.item.setOnClickListener(v -> {

                Userinfo.name = f_name;
                if (ff > 0) {

                    Userinfo.display = "Receivable " + ff + "৳";
                } else if (ff == 0) {
                    Userinfo.display = "Total " + ff + "৳";
                } else {
                    double f_taka= ff*-1;
                    Userinfo.display = "Payable " + f_taka + "৳";
                }
                Integer ss = Integer.parseInt(fid);
                Intent nextActivity = new Intent(MainActivity.this, Userinfo.class);
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", ss);
                bundle.putString("phone", f_phone);
                bundle.putString("name", f_name);
                bundle.putDouble("final_amount", ff);
                nextActivity.putExtras(bundle);
                startActivity(nextActivity);

            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


    }







    private void share_method (Context context){

        final String appname = context.getPackageName();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Download Now: https://play.google.com/store/apps/details?id="+appname);
        intent.setType("text/plain");
        context.startActivity(intent);

    }


    @Override
    public void onBackPressed() {


        if(backpress){
            super.onBackPressed();
        }

        else if (this.backpress=true) {


            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    backpress=false;

                }
            },2000);

        }
}


    @Override
    protected void onPostResume() {

        tali_total(dbHelper.get_user());
        get_tali_user(dbHelper.get_user());
        super.onPostResume();
    }

}