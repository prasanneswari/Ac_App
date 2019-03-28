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

public class Clients extends AppCompatActivity {

    ListView clientlst;
    String [] clientvalues;
    String [] clientid;


    RequestQueue sch_RequestQueue;
    private ProgressDialog dialog_progress ;
    AlertDialog.Builder builderLoading;
    static String clientidpos,clientvaluespos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        clientlst=(ListView)findViewById(R.id.clientlst);

        dialog_progress = new ProgressDialog(Clients.this);
        builderLoading = new AlertDialog.Builder(Clients.this);
        getclients();

    }
    public void getclients(){

        String clientsS = "{\"username\":\"admin\",\"password\":\"admin\"}";
        Log.d("sending string is :", clientsS.toString());
        Log.d("jsnresponse clientsS", "---" + clientsS);
        String getclients_url = "http://123.176.44.5:5904/GenericACApp/get_clients/";
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
                        List<String> client_idL = new ArrayList<String>();
                        List<String> client_descL = new ArrayList<String>();


                        if (response!=null){
                            if (response.has("error_code")){
                                try {
                                    String errorCode = response.getString("error_code");
                                    if (errorCode.contentEquals("0")){
                                        //Toast.makeText(getApplicationContext(), "Response=successfully added", Toast.LENGTH_LONG).show();
                                        dialog_progress.dismiss();

                                        JSONArray dataArray = new JSONArray(response.getString("get_clients"));

                                        if (dataArray.length()!=0) {

                                            for (int i = 0; i < dataArray.length(); i++) {
                                                Log.d("array length---", "---" + i);


                                                JSONObject dataObject = new JSONObject(dataArray.get(i).toString());

                                                String client_id = dataObject.getString("client_id");
                                                String client_desc = dataObject.getString("client_desc");

                                                Log.d("client_desc....", "---" + client_desc);

                                                client_idL.add(client_id);
                                                client_descL.add(client_desc);
                                            }
                                            clientid = new String[client_idL.size()];
                                            clientvalues = new String[client_descL.size()];

                                                for (int l = 0; l < client_descL.size(); l++) {
                                                    clientid[l] = client_idL.get(l);
                                                    clientvalues[l] = client_descL.get(l);

                                                    Log.d("clientid ", clientid[l]);
                                                    Log.d("clientvalues ", clientvalues[l]);

                                                }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Clients.this, R.layout.clientlst_back, R.id.clienttext, clientvalues);
                                            clientlst.setAdapter(adapter);
                                            clientlst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                public void onItemClick(AdapterView<?> parent, View v,
                                                                        int position, long id) {
                                                    String clickpos = parent.getAdapter().getItem(position).toString();
                                                    int index = position;
                                                     clientidpos = clientid[index];
                                                     clientvaluespos = clientvalues[index];
                                                    //gridclickpos= index;
                                                    Log.d("statuspos", "--------------" + clientidpos);
                                                    //Log.d("statuspos ing444", "--------------" +unitid[Integer.parseInt(clickpos)]);

                                                    Intent intent = new Intent(Clients.this, Users.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);

                                                    // Toast.makeText(getApplicationContext(), clickpos,Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Clients.this);
                                            LayoutInflater inflater = (LayoutInflater) Clients.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
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
                                            msg="No Client Data";
                                        }
                                        Log.d("errorCode", "" + errorCode);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Clients.this);
                                        LayoutInflater inflater = (LayoutInflater) Clients.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
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
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Clients.this);
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
