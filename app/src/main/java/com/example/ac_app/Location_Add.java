package com.example.ac_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Location_Add extends AppCompatActivity {

    ListView locationlst;
    Button addbtn;
    DatabaseHelper myDb;
    List<String> macidL;
    List<String> locationL;
    static List<String>idL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__add);
        locationlst=(ListView)findViewById(R.id.locationaddlst);
        addbtn=(Button) findViewById(R.id.addbtn);
        myDb = new DatabaseHelper(this);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationadd_popup();
            }
        });
        viewAll();


    }

    public void viewAll() {
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            // show message
            Toast.makeText(Location_Add.this,"Nothing found", Toast.LENGTH_LONG).show();

            // showMessage("Error","Nothing found");
            return;
        }
        idL = new ArrayList<String>();
        macidL = new ArrayList<String>();
        locationL = new ArrayList<String>();

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            /*buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Name :"+ res.getString(1)+"\n");
            buffer.append("Surname :"+ res.getString(2)+"\n");
            buffer.append("Marks :"+ res.getString(3)+"\n\n");
            buffer.append("Marks :"+ res.getString(4)+"\n\n");
            Log.d("log","id value"+res.getString(0));
            Log.d("log","barcodevalue value"+res.getString(1));
          */
            idL.add(res.getString(0));
            Log.d("log","id value"+res.getString(0));

            macidL.add(res.getString(1));
            locationL.add(res.getString(2));
        }

        Adapter_Location_Add adapter = new Adapter_Location_Add(Location_Add.this, macidL, locationL);
        locationlst.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Toast.makeText(Location_Add.this, "Get the Data", Toast.LENGTH_LONG).show();

        // Show all data
        // showMessage("Data",buffer.toString());

    }
   public void locationadd_popup(){

       AlertDialog.Builder builder = new AlertDialog.Builder(Location_Add.this);
       LayoutInflater inflater = (LayoutInflater) Location_Add.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
       View dialogLayout = inflater.inflate(R.layout.locationadd_popup,
               null);
       final AlertDialog dialog = builder.create();
       dialog.getWindow().setSoftInputMode(
               WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       dialog.setView(dialogLayout, 0, 0, 0, 0);
       dialog.setCanceledOnTouchOutside(true);
       dialog.setCancelable(true);
       WindowManager.LayoutParams wlmp = dialog.getWindow()
               .getAttributes();
       wlmp.gravity = Gravity.CENTER;
       Button addbtn = (Button) dialogLayout.findViewById(R.id.addpopup);
       final EditText macidE = (EditText) dialogLayout.findViewById(R.id.macidpopup);
       final EditText locationE = (EditText) dialogLayout.findViewById(R.id.locationpopup);


       addbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               //delgroup(grpnm);
               String macicS=macidE.getText().toString();
               String locationS=locationE.getText().toString();
               boolean isInserted = myDb.insertData(macicS, locationS);
               if (isInserted == true) {
                   Toast.makeText(Location_Add.this, "Data Inserted", Toast.LENGTH_LONG).show();
                   Intent intent = new Intent(Location_Add.this, Location_Add.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
               }
               else
                   Toast.makeText(Location_Add.this, "Data not Inserted", Toast.LENGTH_LONG).show();

               dialog.dismiss();
           }
       });
       dialog.show();
   }

}
