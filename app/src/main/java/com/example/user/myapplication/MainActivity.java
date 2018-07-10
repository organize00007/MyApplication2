package com.example.user.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button btn_insert,btn_getdata;
    private ListView listView;
    private EditText edt_insert;

    private static final String NAMESPACE = "http://tempuri.org/" ;
    private static final String URL = "http://10.0.2.2/WS_Server/WebService1.asmx";
    //private static final String SOAP_ACTION = "http://tempuri.org/SNQuery";
    private static final String GETDATA = "getdata";

    //private static final String INSERT_SOAP_ACTION = "http://tempuri.org/insert";
    public static final String INSERT = "insert";
    private static final String DELETE = "delete";
    private static final String UPDATE = "update";
    private static final String SEARCH = "search";

    private ArrayList<HashMap<String, Object>> gdata=new ArrayList<>();
    private ListAdapter listAdapter;
    private ProgressBar progressBar;

    private void get_data()
    {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gdata.clear();
                    final SoapObject request = new SoapObject(NAMESPACE, GETDATA);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;//若WS有輸入參數必須要加這一行否則WS沒反應Log.d("nick", "error");
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE ht = new HttpTransportSE(URL);
                    ht.call(NAMESPACE+GETDATA, envelope);
                    //final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();

                    final SoapObject soapObject = (SoapObject) envelope.getResponse();

                    Log.d("nick",soapObject+"");

                    for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                        HashMap<String, Object> item = new HashMap<>();
                        //Log.d("nick",soapObject.getProperty(i).toString().split(" ")[2]+"");
                        item.put("a", soapObject.getProperty(i).toString().split(" ")[0]);
                        item.put("b", soapObject.getProperty(i).toString().split(" ")[1]);
                        item.put("c", soapObject.getProperty(i).toString().split(" ")[2]);
                        //Log.d("nick",mlist+"");

                        gdata.add(item);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d("nick", "end");
                            listAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e) {
                    Log.d("nick", "error");
                    Log.d("nick", "exception", e);
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void delete(final String f1)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SoapObject request = new SoapObject(NAMESPACE, DELETE);
                    request.addProperty("f1", f1);


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;//若WS有輸入參數必須要加這一行否則WS沒反應
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE ht = new HttpTransportSE(URL);
                    ht.call(NAMESPACE+DELETE, envelope);
                    //final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                    //final SoapObject soapObject = (SoapObject) envelope.getResponse();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            get_data();
                        }
                    });
                }catch (Exception e) {
                    Log.d("nick", "error");
                    Log.d("nick", "exception", e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_insert = (Button) findViewById(R.id.btn_insert);
        listView = (ListView) findViewById(R.id.listview);
        btn_getdata = (Button) findViewById(R.id.btn_getdata);
        edt_insert = (EditText) findViewById(R.id.edt_insert);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        listAdapter = new ListAdapter(this, gdata);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int ii, long l) {
                Log.d("nick",listAdapter.getData(ii));
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(DELETE)
                        .setMessage("REALLY DELETE?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(listAdapter.getData(ii));
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
               // Toast.makeText(MainActivity.this, listAdapter.getItem(ii).toString(),Toast.LENGTH_LONG).show();
            }
        });

        get_data();

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final SoapObject request = new SoapObject(NAMESPACE, INSERT);
                            request.addProperty("f1", edt_insert.getText().toString());
                            request.addProperty("f2", edt_insert.getText().toString());
                            request.addProperty("f3", edt_insert.getText().toString());
                            edt_insert.setText("");

                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;//若WS有輸入參數必須要加這一行否則WS沒反應
                            envelope.setOutputSoapObject(request);
                            HttpTransportSE ht = new HttpTransportSE(URL);
                            ht.call(NAMESPACE+INSERT, envelope);
                            //final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                            //final SoapObject soapObject = (SoapObject) envelope.getResponse();

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                get_data();*/
                startActivity(new Intent(MainActivity.this,InsertActivity.class));
            }
        });

        btn_getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handler.post(thread1);
                if(edt_insert.getText().length()==0)
                {
                    get_data();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                gdata.clear();
                                final SoapObject request = new SoapObject(NAMESPACE, SEARCH);
                                request.addProperty("f1", edt_insert.getText().toString());

                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                                envelope.dotNet = true;//若WS有輸入參數必須要加這一行否則WS沒反應Log.d("nick", "error");
                                envelope.setOutputSoapObject(request);
                                HttpTransportSE ht = new HttpTransportSE(URL);
                                ht.call(NAMESPACE+SEARCH, envelope);

                                final SoapObject soapObject = (SoapObject) envelope.getResponse();

                                Log.d("nick",soapObject.getPropertyCount()+"");



                                Log.d("nick",soapObject+"");

                                //Log.d("nick",soapObject.getProperty(i).toString().split(" ")[2]+"");
                                if(soapObject.getPropertyCount()==0)
                                {
                                    HashMap<String, Object> item = new HashMap<>();
                                    item.put("a", "not found");
                                    item.put("b", "");
                                    item.put("c", "");
                                    gdata.add(item);
                                } else {

                                    for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                                        HashMap<String, Object> item = new HashMap<>();
                                        //Log.d("nick",soapObject.getProperty(i).toString().split(" ")[2]+"");
                                        item.put("a", soapObject.getProperty(i).toString().split(" ")[0]);
                                        item.put("b", soapObject.getProperty(i).toString().split(" ")[1]);
                                        item.put("c", soapObject.getProperty(i).toString().split(" ")[2]);
                                        //Log.d("nick",mlist+"");

                                        gdata.add(item);
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Log.d("nick", "end");
                                        listAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                        listView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }catch (Exception e) {
                                Log.d("nick", "error");
                                Log.d("nick", "exception", e);
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        get_data();
    }


}
