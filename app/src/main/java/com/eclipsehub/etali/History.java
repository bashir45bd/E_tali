package com.eclipsehub.etali;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class History extends AppCompatActivity {

    ImageView back;
    RecyclerView list,list2;
    LinearLayout receivable,payable;
    BottomNavigationView his_nav;
    DB_helper dbHelper;

    HashMap<String,String> hashMap;
    ArrayList< HashMap<String,String> > arrayList;
    HashMap<String,String> hashMap1;
    ArrayList< HashMap<String,String> > arrayList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        back=findViewById(R.id.back);
        list=findViewById(R.id.rec_list);
        list2=findViewById(R.id.pay_list);
        receivable=findViewById(R.id.receivable);
        payable=findViewById(R.id.payable);
        his_nav=findViewById(R.id.his_nav);
        dbHelper=new DB_helper(History.this);

        list.addItemDecoration(new DividerItemDecoration(History.this,
                DividerItemDecoration.VERTICAL));


        Bundle bun=getIntent().getExtras();
        int user_id =bun.getInt("user_id");

        String key = String.valueOf(user_id);
        rec_his(dbHelper.get_history_rec(key));
        pay_his(dbHelper.get_history_pay(key));

        his_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId()==R.id.receivable){

                    receivable.setVisibility(View.VISIBLE);
                    payable.setVisibility(View.GONE);


                }
              else {
                    payable.setVisibility(View.VISIBLE);
                    receivable.setVisibility(View.GONE);
                   // Toast.makeText(History.this, "payable", Toast.LENGTH_LONG).show();
                }


                return true;
            }
        });

        back.setOnClickListener(v -> {
            onBackPressed();
        });


    }

     public void rec_his(Cursor cursor){

        if (cursor!=null && cursor.getCount()>0){
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){

                int id = cursor.getInt(0);
                double amount= cursor.getDouble(1);
                String reason =cursor.getString(2);
                double time =cursor.getDouble(4);



                hashMap=new HashMap<>();
                hashMap.put("id",""+id);
                hashMap.put("amount",""+amount);
                hashMap.put("reason",reason);
                hashMap.put("time",""+time);
                arrayList.add(hashMap);

            }
            rec_list adapter = new rec_list();
            list.setAdapter(adapter);
            list.setLayoutManager(new LinearLayoutManager(History.this));

        }
        else {


        }

    }

    public void pay_his(Cursor cursor){


        if (cursor!=null && cursor.getCount()>0){
            arrayList1 = new ArrayList<>();
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                double amount= cursor.getDouble(1);
                String reason =cursor.getString(2);
                double time =cursor.getDouble(4);



                hashMap1=new HashMap<>();
                hashMap1.put("id",""+id);
                hashMap1.put("amount",""+amount);
                hashMap1.put("reason",reason);
                hashMap1.put("time",""+time);
                arrayList1.add(hashMap1);

            }
            pay_list adapter = new pay_list();
            list2.setAdapter(adapter);
            list2.setLayoutManager(new LinearLayoutManager(History.this));

        }
        else {
        }

    }

    public class rec_list extends RecyclerView.Adapter<rec_list.viewholder>{

        public class viewholder extends RecyclerView.ViewHolder{

            TextView amount,reason,time;
            LinearLayout item;

            public viewholder(@NonNull View itemView) {
                super(itemView);

                amount=itemView.findViewById(R.id.amount);
                reason= itemView.findViewById(R.id.reason);
                time= itemView.findViewById(R.id.time);
                item = itemView.findViewById(R.id.item);
            }
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.history_item,parent,false);

            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {

            HashMap<String,String>hashMap=arrayList.get(position);
            String fid = hashMap.get("id");
            String f_amount = hashMap.get("amount");
            String f_reason = hashMap.get("reason");
            String f_time = hashMap.get("time");

            holder.amount.setText(f_amount+"৳");
            holder.amount.setTextColor(Color.parseColor("#DE0000"));
            holder.reason.setText(f_reason);
            double a_time = Double.parseDouble(f_time);
            SimpleDateFormat simpletimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat simpledateFormat = new SimpleDateFormat("dd MMM yyyy ", Locale.getDefault());

            String stime = simpletimeFormat.format(a_time);
            String date = simpledateFormat.format(a_time);

            holder.time.setText(stime+" "+date);

            holder.item.setOnClickListener(v -> {

                Bundle bun=getIntent().getExtras();
                int user_id =bun.getInt("user_id");
                up_and_delete_item1(fid,f_amount,f_reason,user_id);
            });


        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }



    }

    public class pay_list extends RecyclerView.Adapter<pay_list.view>{


        public class view extends RecyclerView.ViewHolder{

            TextView amount,reason,time;
            LinearLayout item;

            public view(@NonNull View itemView) {
                super(itemView);
                amount=itemView.findViewById(R.id.amount);
                reason= itemView.findViewById(R.id.reason);
                time= itemView.findViewById(R.id.time);
                item = itemView.findViewById(R.id.item);
            }
        }


        @NonNull
        @Override
        public view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.history_item,parent,false);

            return new view(view);
        }

        @Override
        public void onBindViewHolder(@NonNull view holder, int position) {

            HashMap<String,String>hashMap=arrayList1.get(position);
            String fid = hashMap.get("id");
            String f_amount = hashMap.get("amount");
            String f_reason = hashMap.get("reason");
            String f_time = hashMap.get("time");

            holder.amount.setText(f_amount+"৳");
            holder.reason.setText(f_reason);
            double a_time = Double.parseDouble(f_time);
            SimpleDateFormat simpletimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat simpledateFormat = new SimpleDateFormat("dd MMM yyyy ", Locale.getDefault());

            String stime = simpletimeFormat.format(a_time);
            String date = simpledateFormat.format(a_time);

            holder.time.setText(stime+" "+date);

            holder.item.setOnClickListener(v -> {

                   Bundle bun=getIntent().getExtras();
                   int user_id =bun.getInt("user_id");
                   up_and_delete_item2(fid,f_amount,f_reason,user_id);
            });

        }

        @Override
        public int getItemCount() {
            return arrayList1.size();
        }


    }

    private void up_and_delete_item1(String id,String amount, String title,int user_id) {

        Context context = History.this;
        BottomSheetDialog dialog = new BottomSheetDialog(context);

        // Inflate the layout for the BottomSheetDialog
        View view = getLayoutInflater().inflate(R.layout.up_his_item, null);

        // Set the layout for the BottomSheetDialog
        dialog.setContentView(view);

        EditText amount_up = view.findViewById(R.id.amount_up);
        EditText reason_up = view.findViewById(R.id.reason_up);
        ImageView submit_data= view.findViewById(R.id.submit_data);
        ImageView delete= view.findViewById(R.id.user_item_delete);

        amount_up.setText(amount);
        reason_up.setText(title);


        submit_data.setOnClickListener(v -> {

            if (amount_up.length()>0 && reason_up.length()>0){


                String amount_d = amount_up.getText().toString().trim();
                Double dd = Double.parseDouble(amount_d);
                String title_S = reason_up.getText().toString().trim();
                dbHelper.up_rec(id,dd,title_S,user_id);
                dialog.dismiss();
                ui_update();


            } else {
                amount_up.setError("Enter Number");
                reason_up.setError("Enter text");

            }




        });

        delete.setOnClickListener(v -> {



            AlertDialog alertDialog = new AlertDialog.Builder(History.this)
                    .setIcon(R.drawable.alert)
                    .setTitle("Delete!")
                    .setMessage("Are you sure?")

                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dbHelper.delete_rec(id);
                            ui_update();


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            dialog.dismiss();
        });


        dialog.show();

    }

   private void up_and_delete_item2(String id,String amount, String title,int user_id) {

        Context context = History.this;
        BottomSheetDialog dialog = new BottomSheetDialog(context);

        // Inflate the layout for the BottomSheetDialog
        View view = getLayoutInflater().inflate(R.layout.up_his_item, null);
        // Set the layout for the BottomSheetDialog
        dialog.setContentView(view);

        EditText amount_e = view.findViewById(R.id.amount_up);
        EditText title_a = view.findViewById(R.id.reason_up);
        ImageView submit= view.findViewById(R.id.submit_data);
        ImageView delete= view.findViewById(R.id.user_item_delete);


        amount_e.setText(amount);
        title_a.setText(title);


        submit.setOnClickListener(v -> {

            if (amount_e.length()>0 && title_a.length()>0){


                String amount_d = amount_e.getText().toString().trim();
                Double dd = Double.parseDouble(amount_d);
                String title_S = title_a.getText().toString().trim();
                dbHelper.up_pay(id,dd,title_S,user_id);
                dialog.dismiss();
                ui_update();


            } else {
                amount_e.setError("Enter Number");
                title_a.setError("Enter text");

            }




        });

        delete.setOnClickListener(v -> {



            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.alert)
                    .setTitle("Delete!")
                    .setMessage("Are you sure?")

                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dbHelper.delete_pay(id);
                            ui_update();


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            dialog.dismiss();
        });


        dialog.show();

    }

    private void ui_update(){

        finish();
        startActivity(getIntent());

    }


}