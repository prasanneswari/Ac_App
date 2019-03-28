package com.example.ac_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ac_app.Status.inputB;
import static com.example.ac_app.Status.operateB;
import static com.example.ac_app.Status.powerB;
import static com.example.ac_app.Users.uservaluespos;

public class Units extends AppCompatActivity {

    ListView unitslst;
     String[] macid;
     String[] premise;
     String[] changedate;
   static String[] status;

    RequestQueue sch_RequestQueue;
    private ProgressDialog dialog_progress ;
    AlertDialog.Builder builderLoading;
    List<String> macid_idL,premiseL,change_dateL,statusL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);

        unitslst=(ListView) findViewById(R.id.unitslst);
        dialog_progress = new ProgressDialog(Units.this);
        builderLoading = new AlertDialog.Builder(Units.this);
        getunits();

    }

    public void getunits(){

        if (inputB==true) {

            String clientsS = "{\"username\":\"admin\",\"password\":\"admin\",\"user_desc\":\"" + uservaluespos + "\"}";
            Log.d("sending string is :", clientsS.toString());
            Log.d("jsnresponse clientsS", "---" + clientsS);
            String getclients_url = "http://123.176.44.5:5904/GenericACApp/get_iunits/";
            JSONObject lstrmdt = null;

            try {
                lstrmdt = new JSONObject(clientsS);
                Log.d("jsnresponse....", "---" + clientsS);
                // dialog_progress.setMessage("connecting ...");
                dialog_progress.show();

                JSONSenderVolleyclient(getclients_url, lstrmdt);

            } catch (JSONException e) {

            }
        }else if (operateB==true){

            String clientsS = "{\"username\":\"admin\",\"password\":\"admin\",\"user_desc\":\"" + uservaluespos + "\"}";
            Log.d("sending string is :", clientsS.toString());
            Log.d("jsnresponse clientsS", "---" + clientsS);
            String getclients_url = "http://123.176.44.5:5904/GenericACApp/get_opunits/";
            JSONObject lstrmdt = null;

            try {
                lstrmdt = new JSONObject(clientsS);
                Log.d("jsnresponse....", "---" + clientsS);
                // dialog_progress.setMessage("connecting ...");
                dialog_progress.show();

                JSONSenderVolleyclient(getclients_url, lstrmdt);

            } catch (JSONException e) {

            }
        }else if (powerB==true){

            String clientsS = "{\"username\":\"admin\",\"password\":\"admin\",\"user_desc\":\"" + uservaluespos + "\"}";
            Log.d("sending string is :", clientsS.toString());
            Log.d("jsnresponse clientsS", "---" + clientsS);
            String getclients_url = "http://123.176.44.5:5904/GenericACApp/get_punits/";
            JSONObject lstrmdt = null;

            try {
                lstrmdt = new JSONObject(clientsS);
                Log.d("jsnresponse....", "---" + clientsS);
                // dialog_progress.setMessage("connecting ...");
                dialog_progress.show();

                JSONSenderVolleyclient(getclients_url, lstrmdt);

            } catch (JSONException e) {

            }
        }

    }
    public void JSONSenderVolleyclient(String getclients_url, final JSONObject json)
    {
        Log.d("---clientsS url-----", "---"+getclients_url);
        Log.d("555555", "clientsS"+json.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                getclients_url, json,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(" ", response.toString());
                        Log.d("----JSONOrguser volly--", "---"+response.toString());
                         macid_idL = new ArrayList<String>();
                         premiseL = new ArrayList<String>();
                         change_dateL = new ArrayList<String>();
                         statusL = new ArrayList<String>();

                        if (response!=null){
                            if (response.has("error_code")){
                                try {
                                    String errorCode = response.getString("error_code");
                                    if (errorCode.contentEquals("0")){
                                        //Toast.makeText(getApplicationContext(), "Response=successfully added", Toast.LENGTH_LONG).show();
                                        dialog_progress.dismiss();


                                        JSONArray dataArray = new JSONArray(response.getString("get_units"));

                                        if (dataArray.length()!=0) {

                                            for (int i = 0; i < dataArray.length(); i++) {
                                                Log.d("array length---", "---" + i);


                                                JSONObject dataObject = new JSONObject(dataArray.get(i).toString());

                                                String macid_id = dataObject.getString("macid_id");
                                                String premise = dataObject.getString("premise");
                                                String change_date = dataObject.getString("change_date");
                                                String statusc = dataObject.getString("status");

                                                Log.d("macid_id....", "---" + macid_id);

                                                macid_idL.add(macid_id);
                                                premiseL.add(premise);
                                                change_dateL.add(change_date);
                                                statusL.add(statusc);

                                            }
                                            macid = new String[macid_idL.size()];
                                            premise = new String[premiseL.size()];
                                            changedate = new String[change_dateL.size()];
                                            status = new String[statusL.size()];


                                            for (int l = 0; l < statusL.size(); l++) {
                                                macid[l] = macid_idL.get(l);
                                                premise[l] = premiseL.get(l);
                                                changedate[l] = change_dateL.get(l);
                                                status[l] = statusL.get(l);

                                                Log.d("macid ", macid[l]);
                                                Log.d("premise ", premise[l]);
                                                Log.d("changedate ", changedate[l]);
                                                Log.d("status ", status[l]);


                                            }
                                            Adapter_Users adapter = new Adapter_Users(Units.this, macid,premise,changedate);
                                            unitslst.setAdapter(adapter);
                                            macid_idL.clear();
                                            premiseL.clear();
                                            change_dateL.clear();
                                            statusL.clear();
                                            inputB=false;
                                            operateB=false;
                                            powerB=false;




                                        }
                                        else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Units.this);
                                            LayoutInflater inflater = (LayoutInflater) Units.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
                                            View dialogLayout = inflater.inflate(R.layout.popup_back,
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
                                            Button cancel = (Button) dialogLayout.findViewById(R.id.okbtn);
                                            TextView textpopup = (TextView) dialogLayout.findViewById(R.id.popuptxt);

                                            textpopup.setText("Unable to get the data");

                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    // TODO Auto-generated method stub
                                                    //delgroup(grpnm);

                                                    dialog.dismiss();
                                                    dialog_progress.dismiss();
                                                }
                                            });
                                            dialog.show();
                                        }

                                    }
                                    else {
                                        // Toast.makeText(getApplicationContext(), "Not added the notes", Toast.LENGTH_LONG).show();

                                        String msg;
                                        if(response.has("Response")){
                                            msg=response.getString("Response");
                                        }
                                        else{
                                            msg="No Units Found !";
                                        }
                                        Log.d("errorCode", "" + errorCode);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Units.this);
                                        LayoutInflater inflater = (LayoutInflater) Units.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
                                        View dialogLayout = inflater.inflate(R.layout.popup_back,
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
                                        Button cancel = (Button) dialogLayout.findViewById(R.id.okbtn);
                                        TextView textpopup = (TextView) dialogLayout.findViewById(R.id.popuptxt);

                                        textpopup.setText(msg);

                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method stub
                                                //delgroup(grpnm);

                                                dialog.dismiss();
                                                dialog_progress.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Units.this);
                                builder.setMessage("Reasponse=Unable to get teh data");

                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface1, int i) {

                                        dialog_progress.dismiss();
                                        dialogInterface1.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(" Error---------", "shulamithi: " + String.valueOf(error));
                Log.d("my test error-----","shulamithi: " + String.valueOf(error));
                Toast.makeText(getApplicationContext(), "connection error ", Toast.LENGTH_LONG).show();
                dialog_progress.dismiss();
/*
                final AlertDialog.Builder builder = new AlertDialog.Builder(List_places.this);
                builder.setTitle("Info");
                builder.setMessage(String.valueOf(error));
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface1, int i) {

                        dialog_progress.dismiss();
                        dialogInterface1.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
*/
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept","application/json");
                //headers.put("Content-Type","application/json");
                headers.put("Content-Type", "application/json; charset=utf-8");
                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };

        jsonObjReq.setTag("");
        addToRequestQueue(jsonObjReq);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        if (sch_RequestQueue == null) {
            sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        sch_RequestQueue.add(req);
    }
}
