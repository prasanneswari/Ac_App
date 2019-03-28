package com.example.ac_app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.ac_app.Units.status;


public class Adapter_Users extends ArrayAdapter<String> {

    private Context context;
    private String[] macid;
    private String[] premise;
    private String[] changedate;

    public static String lcusname;
    public static String lfullname;
    public static String lcuscontact;
    public static String lapntdate;
    public static String lvisitdate;
    static int posval;
    TextView macidT,premiseT,changedateT;
    LinearLayout linearid;

    public Adapter_Users(Context context, String[] macidS, String[] premiseS ,String[] changedateS) {

        super(context, R.layout.activity_users, macidS);

        //Assinging the 'RequisitionData' array values to the local arrays inside adapter

        this.context = context;
        this.macid = macidS;
        this.premise = premiseS;
        this.changedate = changedateS;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_adapter__users, parent, false);  //Setting content view of xml
        // ImageView imageView=(ImageView)rowView.findViewById(R.id.image);
        //Assigning IDs from xml
        posval=position;
        macidT = (TextView) rowView.findViewById(R.id.macid);
        premiseT = (TextView) rowView.findViewById(R.id.premise);
        changedateT = (TextView) rowView.findViewById(R.id.changedate);
        linearid = (LinearLayout) rowView.findViewById(R.id.linarid);

        try {

            //Assigning values from array to individual layouts in list view
            macidT.setText(macid[position]);
            premiseT.setText(premise[position]);
            changedateT.setText(changedate[position]);

            String statuspos=status[posval];
            Log.d("statuspos....", "---" + statuspos);

            if (statuspos.equals("Active")){
                linearid.setBackgroundColor(Color.parseColor("#87F803"));

            }else {
                linearid.setBackgroundColor(Color.parseColor("#F71505"));

            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        /*rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Userview_task.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ltcame = tcname[position];
                ltstatusname = tstatusname[position];
                lDtapntdate = tapntdate[position];
                lStcmpltdate = tcmpltdate[position];
                ltapnttype = tapnttype[position];

                Log.d("Location" ," ltcame1111 :" + ltcame);
                Log.d("Location" ," ltstatusname2222 :" + ltstatusname);
                Log.d("Location" ," lDtapntdate33333 :" + lDtapntdate);
                Log.d("Location" ," lStcmpltdate44444 :" + lStcmpltdate);
                Log.d("Location" ," ltapnttype55555 :" + ltapnttype);

            }
        });*/
        return rowView;
    }

}
