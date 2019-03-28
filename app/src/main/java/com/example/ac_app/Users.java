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

import static com.example.ac_app.Clients.clientidpos;

public class Users extends AppCompatActivity {

    ListView userslst;
    String [] uservalues;
    String [] userid;


    RequestQueue sch_RequestQueue;
    private ProgressDialog dialog_progress ;
    AlertDialog.Builder builderLoading;
    static String useridpos,uservaluespos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        userslst=(ListView)findViewById(R.id.userslst);
        dialog_progress = new ProgressDialog(Users.this);
        builderLoading = new AlertDialog.Builder(Users.this);

        getusers();
    }
    public void getusers(){

        String unitsS = "{\"username\":\"admin\",\"password\":\"admin\",\"ClientId\":\"" + clientidpos + "\"}";
        Log.d("sending string is :", unitsS.toString());
        Log.d("jsnresponse unitsS", "---" + unitsS);
        String getusers_url = "http://123.176.44.5:5904/GenericACApp/get_users/";
        JSONObject lstrmdt = null;

        try {
            lstrmdt = new JSONObject(unitsS);
            Log.d("jsnresponse....", "---" + unitsS);
            // dialog_progress.setMessage("connecting ...");
            dialog_progress.show();

            JSONSenderVolleyusers(getusers_url, lstrmdt);

        } catch (JSONException e) {

        }

    }
    public void JSONSenderVolleyusers(String getusers_url, final JSONObject json)
    {

        Log.d("---unitsS url-----", "---"+getusers_url);
        Log.d("555555", "unitsS"+json.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                getusers_url, json,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(" ", response.toString());
                        Log.d("----JSONOrguser volly--", "---"+response.toString());
                        List<String> user_idL = new ArrayList<String>();
                        List<String> user_descL = new ArrayList<String>();


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

                                                String user_id = dataObject.getString("user_id");
                                                String user_desc = dataObject.getString("user_desc");

                                                Log.d("user_desc....", "---" + user_desc);

                                                user_idL.add(user_id);
                                                user_descL.add(user_desc);
                                            }
                                            userid = new String[user_idL.size()];
                                            uservalues = new String[user_descL.size()];

                                            for (int l = 0; l < user_descL.size(); l++) {
                                                userid[l] = user_idL.get(l);
                                                uservalues[l] = user_descL.get(l);

                                                Log.d("userid ", userid[l]);
                                                Log.d("uservalues ", uservalues[l]);

                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Users.this, R.layout.clientlst_back, R.id.clienttext, uservalues);
                                            userslst.setAdapter(adapter);
                                            userslst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                public void onItemClick(AdapterView<?> parent, View v,
                                                                        int position, long id) {
                                                    String clickpos = parent.getAdapter().getItem(position).toString();
                                                    int index = position;
                                                    useridpos = userid[index];
                                                    uservaluespos = uservalues[index];
                                                    //gridclickpos= index;
                                                    Log.d("uservaluespos", "--------------" + uservaluespos);
                                                    //Log.d("statuspos ing444", "--------------" +unitid[Integer.parseInt(clickpos)]);

                                                    Intent intent = new Intent(Users.this, Status.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    // Toast.makeText(getApplicationContext(), clickpos,Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Users.this);
                                            LayoutInflater inflater = (LayoutInflater) Users.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
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
                                            msg="No Users Data";
                                        }
                                        Log.d("errorCode", "" + errorCode);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Users.this);
                                        LayoutInflater inflater = (LayoutInflater) Users.this.getSystemService(getApplication().LAYOUT_INFLATER_SERVICE);
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
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Users.this);
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
