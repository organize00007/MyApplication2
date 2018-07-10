package com.example.user.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.example.user.myapplication.MainActivity.INSERT;

public class InsertActivity extends AppCompatActivity {

    private EditText edt_f1, edt_f2, edt_f3;
    private Button btn_insert, btn_cancel;

    private static final String NAMESPACE = "http://tempuri.org/" ;
    private static final String URL = "http://10.0.2.2/WS_Server/WebService1.asmx";
    //private static final String SOAP_ACTION = "http://tempuri.org/SNQuery";
    //private static final String INSERT = "insert";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    Toast.makeText(InsertActivity.this,"不可輸入空白",Toast.LENGTH_LONG).show();
                    return "";
                } else return null;
            }
        };

        edt_f1 = findViewById(R.id.edt_f1);
        edt_f2 = findViewById(R.id.edt_f2);
        edt_f3 = findViewById(R.id.edt_f3);
        btn_insert = findViewById(R.id.btn_insert);
        btn_cancel = findViewById(R.id.btn_cancel);


        edt_f1.setFilters(new InputFilter[]{filter});
        edt_f2.setFilters(new InputFilter[]{filter});
        edt_f3.setFilters(new InputFilter[]{filter});


        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final SoapObject request = new SoapObject(NAMESPACE, INSERT);
                            request.addProperty("f1", edt_f1.getText().toString());
                            request.addProperty("f2", edt_f2.getText().toString());
                            request.addProperty("f3", edt_f3.getText().toString());

                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;//若WS有輸入參數必須要加這一行否則WS沒反應
                            envelope.setOutputSoapObject(request);
                            HttpTransportSE ht = new HttpTransportSE(URL);
                            ht.call(NAMESPACE + INSERT, envelope);
                            //final SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                            //final SoapObject soapObject = (SoapObject) envelope.getResponse();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                finish();
            }

        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

