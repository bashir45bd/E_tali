package com.eclipsehub.etali;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;


public class Userinfo extends AppCompatActivity {

      ImageView back,dots,call,msg,whatapps_msg;
      TextView user_name,tv_total;
      public static String name="";
      public static String display="";

      LinearLayout rec_page,pay_page,color1,color2;
      TextInputEditText rec_amount,rec_dec,pay_amount,pay_dec;
      CardView rec_add,pay_add,rec_submit,pay_submit;
      Animation item_anim;
      boolean color=true;
      boolean color3=true;
      DB_helper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        user_name=findViewById(R.id.user_title);
        tv_total=findViewById(R.id.tv_total);
        user_name.setText(name);
        tv_total.setText(display);
        rec_page=findViewById(R.id.rec_page);
        pay_page=findViewById(R.id.pay_page);
        rec_amount=findViewById(R.id.rec_amount);
        rec_dec=findViewById(R.id.rec_dec);
        pay_amount=findViewById(R.id.pay_amount);
        pay_dec=findViewById(R.id.pay_dec);
        rec_add=findViewById(R.id.rec_add);
        pay_add=findViewById(R.id.pay_add);
        pay_submit=findViewById(R.id.pay_submit);
        rec_submit=findViewById(R.id.rec_submit);
        back=findViewById(R.id.back);
        color1=findViewById(R.id.color1);
        color2=findViewById(R.id.color2);
        dots=findViewById(R.id.dots);
        call=findViewById(R.id.call);
        msg=findViewById(R.id.msg);
        whatapps_msg=findViewById(R.id.whatapps_msg);
        dbHelper=new DB_helper(Userinfo.this);

        Bundle bun=getIntent().getExtras();
        int user_id =bun.getInt("user_id");
        String number =bun.getString("phone");
        double taka =bun.getDouble("final_amount");


        call.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+"01"+number));
            startActivity(intent);
        });

        msg.setOnClickListener(v -> {

            if (taka>0){

                Uri uri = Uri.parse("smsto:01"+number+"");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "আপনার কাছে "+taka+"৳ পাওনা আছে।\nঅনুগ্রহ করে টাকা পরিশোধ করুন।\n\nধন্যবাদ।");
                startActivity(intent);
            }
            else if (taka<0) {
                double f_taka= taka*-1;
                Uri uri = Uri.parse("smsto:01"+number+"");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "আপনি আমার নিকট "+f_taka+"৳ পাবেন।\nখুব দ্রুত আপনার টাকা পরিশোধ করব।\n\nধন্যবাদ।");
                startActivity(intent);
            }

            else if (taka==0) {

                Uri uri = Uri.parse("smsto:01"+number+"");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "আপনার সাথে কোনো দেনা পাওনা নেই।\n\nধন্যবাদ।");
                startActivity(intent);
            }


        });

        whatapps_msg.setOnClickListener(v -> {


            if (taka>0){

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+8801"+number+" &text=আপনার কাছে "+taka+"৳ পাওনা আছে।\nঅনুগ্রহ করে টাকা পরিশোধ করুন।\n\nধন্যবাদ।"));
                startActivity(intent);
            }
            else if (taka<0) {
                double f_taka= taka*-1;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+8801"+number+" &text=আপনি আমার নিকট "+f_taka+"৳ পাবেন।\nখুব দ্রুত আপনার টাকা পরিশোধ করব।\n\nধন্যবাদ।"));
                startActivity(intent);
            }

            else if (taka==0) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+8801"+number+" &text=আপনার সাথে কোনো দেনা পাওনা নেই।\n\nধন্যবাদ।"));
                startActivity(intent);
            }


        });




         rec_submit.setOnClickListener(v -> {


            if (rec_amount.length()>0){

                if(rec_dec.length()==0)
                {
                    rec_dec.setText("No reason");
                }
                String rec_amount_a = rec_amount.getText().toString();
                String rec_dec_a = rec_dec.getText().toString();
                double am = Double.parseDouble(rec_amount_a);
                dbHelper.add_rec(am,rec_dec_a,user_id);

                rec_amount.setText("");
                rec_dec.setText("");
                finish();
                Toast.makeText(Userinfo.this, "Successfully added", Toast.LENGTH_LONG).show();




            } else {
                rec_amount.setError("Enter Number");

            }

        });

        pay_submit.setOnClickListener(v -> {

            if (pay_amount.length()>0){

                if(pay_dec.length()==0)
                {
                    pay_dec.setText("No reason");
                }
                String pay_amount_a = pay_amount.getText().toString();
                String pay_dec_a = pay_dec.getText().toString();
                double pay = Double.parseDouble(pay_amount_a);

                dbHelper.add_pay(pay,pay_dec_a,user_id);

                pay_amount.setText("");
                pay_dec.setText("");
                finish();
                Toast.makeText(Userinfo.this, "Successfully added", Toast.LENGTH_LONG).show();



            } else {
                pay_amount.setError("Enter Number");

            }



        });

       dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_menu(v);
            }
        });

        back.setOnClickListener(v -> {
            onBackPressed();
        });

        rec_add.setOnClickListener(v -> {
            rec_page.setVisibility(View.VISIBLE);
            pay_page.setVisibility(View.GONE);
            if (color==true)
            {
                color1.setBackgroundColor(Color.parseColor("#dc1e76"));
                color=false;
            }
            else{
                color1.setBackgroundColor(Color.parseColor("#dc1e76"));
                color2.setBackgroundColor(Color.parseColor("#1abe75"));
                color=true;
            }


        });

        pay_add.setOnClickListener(v -> {
            rec_page.setVisibility(View.GONE);
            pay_page.setVisibility(View.VISIBLE);
            if (color3==true)
            {
                color2.setBackgroundColor(Color.parseColor("#dc1e76"));
                color1.setBackgroundColor(Color.parseColor("#1abe75"));
                color=false;
            }
            else {
                color2.setBackgroundColor(Color.parseColor("#1abe75"));
                color1.setBackgroundColor(Color.parseColor("#dc1e76"));
                color3=true;
            }



        });




    }

    public void user_menu(View view) {
        // Create a ContextThemeWrapper with the custom style
        ContextThemeWrapper ctw = new ContextThemeWrapper(Userinfo.this, R.style.CustomPopupMenu);
        // Create the PopupMenu with the custom context
        PopupMenu popupMenu = new PopupMenu(ctw, view);

        // Inflate the menu resource into the PopupMenu
        popupMenu.getMenuInflater().inflate(R.menu.user, popupMenu.getMenu());

        // Set a click listener for menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId()==R.id.user_delete1){

                    AlertDialog alertDialog = new AlertDialog.Builder(Userinfo.this)
                            .setIcon(R.drawable.alert)
                            .setTitle("Delete!")
                            .setMessage("Are you sure?")

                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                   Bundle bun=getIntent().getExtras();
                                   int user_id =bun.getInt("user_id");
                                   String ssd= String.valueOf(user_id);
                                   dbHelper.delete_user(ssd);
                                   finish();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }

                else if (item.getItemId()==R.id.user_edit1){
                   Bundle bun=getIntent().getExtras();
                   int user_id =bun.getInt("user_id");
                   String dd = String.valueOf(user_id);
                   String name = bun.getString("name");
                   String address = bun.getString("phone");
                   user_up(Userinfo.this,dd,name,address);
                }

                else if (item.getItemId()==R.id.user_report){

                    Bundle bun=getIntent().getExtras();
                    int user_id =bun.getInt("user_id");
                    Intent nextActivity = new Intent(Userinfo.this,History.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",user_id);
                    nextActivity.putExtras(bundle);
                    startActivity(nextActivity);

                }
                return false;
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.show();
    }

    public void user_up (Context context, String id, String name, String address) {

        BottomSheetDialog dialog = new BottomSheetDialog(this);

        // Inflate the layout for the BottomSheetDialog
        View view = getLayoutInflater().inflate(R.layout.add_user, null);

        // Set the layout for the BottomSheetDialog
        dialog.setContentView(view);

        TextView title = view.findViewById(R.id.title);
        EditText name1 = view.findViewById(R.id.name);
        EditText address1 = view.findViewById(R.id.phone);

        title.setText("Update User Info");
        name1.setText(name);
        address1.setText(address);


        view.findViewById(R.id.submit).setOnClickListener(v -> {


            if (name1.length()>0 && address1.length()==9){

                String name_a = name1.getText().toString();
                String address_a = address1.getText().toString();
                dbHelper.update_user(id,name_a,address_a);
                dialog.dismiss();
                finish();

            } else {
                name1.setError("Enter Number");
                address1.setError("Enter text");

            }



        });

        dialog.show();

    }

}