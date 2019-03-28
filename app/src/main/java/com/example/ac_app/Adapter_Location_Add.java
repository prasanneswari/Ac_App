package com.example.ac_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.ac_app.Clients.clientidpos;
import static com.example.ac_app.Location_Add.idL;
import static com.example.ac_app.Units.status;
import static java.sql.DriverManager.println;

public class Adapter_Location_Add extends ArrayAdapter<String> implements MqttCallback {

    private Context context;
    private List<String> macid;
    private List<String> location;

    public static String lmacid;
    public static String llocation;
    public static String lcuscontact;
    public static String lapntdate;
    public static String lvisitdate;
    static int posval;
    TextView macidT,locationT;
    Button pingcheck;
    DatabaseHelper myDb;
    int id;

    private ProgressDialog dialog_progress ;
    AlertDialog.Builder builderLoading;

    private static String TAG = "MQTT_android";
    String payload = "the payload";
    static MqttAndroidClient client;
    MqttConnectOptions options = new MqttConnectOptions();

    String topic;




    public Adapter_Location_Add(Context context, List<String> macidS, List<String> locationS ) {

        super(context, R.layout.activity_location__add, macidS);

        //Assinging the 'RequisitionData' array values to the local arrays inside adapter

        this.context = context;
        this.macid = macidS;
        this.location = locationS;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_adapter__location__add, parent, false);  //Setting content view of xml
        // ImageView imageView=(ImageView)rowView.findViewById(R.id.image);
        //Assigning IDs from xml
        posval=position;
        myDb = new DatabaseHelper(context);

        macidT = (TextView) rowView.findViewById(R.id.macid);
        locationT = (TextView) rowView.findViewById(R.id.location);
        pingcheck = (Button) rowView.findViewById(R.id.pingckeck);
        dialog_progress = new ProgressDialog(context);
        builderLoading = new AlertDialog.Builder(context);
        pingcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Location" ," entering to button :");
                subscribe_scada();
            }
        });

        try {

            //Assigning values from array to individual layouts in list view
            macidT.setText(macid.get(position));
            locationT.setText(location.get(position));


        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lmacid = macid.get(position);
                llocation = location.get(position);

                Log.d("Location" ," lmacid1111 :" + lmacid);
                Log.d("Location" ," llocation2222 :" + llocation);
                updatepop();

            }
        });
        return rowView;
    }
    public void updatepop(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.locationupdate_popup,
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
        Button update = (Button) dialogLayout.findViewById(R.id.updatepopup);
        final EditText macidE = (EditText) dialogLayout.findViewById(R.id.macidpopup);
        final EditText locationE = (EditText) dialogLayout.findViewById(R.id.locationpopup);

        macidE.setText(lmacid);
        locationE.setText(llocation);
         id = Integer.parseInt(idL.get(posval));
        Log.d("log","id value"+id);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String macidU=macidE.getText().toString();
                String locationU=locationE.getText().toString();
                Log.d("log","macidU value"+macidU);


                boolean isUpdate = myDb.updateData(id,macidU,locationU);

                if(isUpdate == true) {
                    Toast.makeText(context, "Data Update", Toast.LENGTH_LONG).show();

                    // dialog.dismiss();
                    context.startActivity(new Intent(context, Location_Add.class));


                    /*Intent intent = new Intent(context, Viewjava.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }
                else
                    Toast.makeText(context,"Data not Updated",Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    public void subscribe_scada() {
        Log.d("Enetered ", "in sub func ");
        //Bundle b = getIntent().getExtras();
        String clientId = MqttClient.generateClientId();
        //topic = "jts/dtd/response";
        //String server_ip = "tcp://jtha.in:1883";
        topic="jts/dcUnit/v_0_0_2/dcRes/"+macid.get(posval);
        Log.d("Enetered ", "topic"+topic);

        String server_ip = "tcp://cld003.jts-prod.in:1883";
        Log.d("Enetered ", "subscribeScada");
        client = new MqttAndroidClient(context, server_ip,
                clientId);

        Log.d("Enetered ", "subscribeScada1");
        try {
            options.setUserName("esp");
            options.setPassword("ptlesp01".toCharArray());
            IMqttToken token = client.connect(options);
            Log.d("Enetered ", "subscribeScada2");
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //t.cancel();
                    Log.d("Enetered ", "subscribeScada3");
                    client.setCallback(Adapter_Location_Add.this);
                    int qos = 2;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos);
                        Log.d("Enetered ", "subscribeScada4");
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // successfully subscribed
                                //tv.setText("Successfully subscribed to: " + topic);
                                Log.d("success", "came here");

                                topic="jts/dcUnit/v_0_0_2/dcCmd/"+macid.get(posval);
                                Log.d("success", "publish here"+topic);

                                String result1 = "{\"dc_cmd\":{\"opp\":\"list_nodes\"}}";
                                Log.d("success", "message here"+result1);
                                //String message = "message";

                                result1=result1.replaceAll(" ","");
                                result1 = result1.trim();

                                byte[] encodedPayload = new byte[0];
                                try {
                                    encodedPayload = result1.getBytes("UTF-8");
                                    MqttMessage message = new MqttMessage(encodedPayload);
                                    dialog_progress.setMessage("connecting ...");
                                    dialog_progress.show();
                                    try {

                                        client.publish(topic, message);
                                        Log.d(">>>publish enetr>>>","enter%%%%%%");

                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                // Toast.makeText(MainActivity.this, "Couldn't subscribe to: " + topic, Toast.LENGTH_SHORT).show();
                                Log.d("failure", "came here");
                                //tv.setText("Couldn't subscribe to: " + topic);
                            }
                        });
                        Log.d(TAG, "here we are");
                    } catch (MqttException e) {
                        e.printStackTrace();
                        Log.d("error", "!");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.d("error", "2");
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d(TAG, "onFailure");
        }
    }
    @Override
    public void connectionLost(Throwable cause) {

    }
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        Log.d("messege arriv", "login"+message);
        dialog_progress.dismiss();


        JSONObject json = null;  //your response
        try {
            json = new JSONObject(String.valueOf(message));
           // JSONArray dc_cmd = json.getJSONArray("dc_cmd");

/*
            String dc_cmd = json.getString("dc_cmd");
            Log.d("messege arriv", "dc_cmd"+dc_cmd);
*/

            JSONObject jObject  = new JSONObject(String.valueOf(json));
            JSONObject  menu = jObject.getJSONObject("dc_cmd");
            Map<String,String> map = new HashMap<String,String>();
            Iterator iter = menu.keys();
            while(iter.hasNext()){
                String key = (String)iter.next();
                String value = menu.getString(key);
                map.put(key,value);
                jsondata(key,value);

            }



           /* for (int i = 0; i < dc_cmd.length(); i++) {

                JSONObject jsonobj = dc_cmd.getJSONObject(i);

                String opp = jsonobj.getString("opp");

                println( ", " + opp);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void jsondata(String dc_cmd,String value){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

        textpopup.setText(dc_cmd + ":" +value);

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
