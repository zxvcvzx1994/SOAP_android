package com.cvcompany.vo.myapplication;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG =MainActivity.class.getSimpleName() ;
    private final String SOAP_ACTION = "http://threepin.org/findUserById";
    private final String METHOD_NAME="findUserById";
    private final String NAME_SPACE= "http://threepin.org/";
    private final String SOAP_URL = "http://192.168.1.10/MyWebService.asmx";
    private Handler handler = new Handler();
    @BindView(R.id.etText)
    EditText etText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn)
    public void btn(){
        String id  =etText.getText().toString().trim();
        if(id.length()==0){
            Toast.makeText(this, "Please input data", Toast.LENGTH_SHORT).show();
            return;
        }else

        new FindViewById().execute(id);
    }

    private class FindViewById extends AsyncTask<String, Void, String>{


        private Object soapPrimitive;

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "doInBackground: enter");
            SoapObject soapObject = new SoapObject(NAME_SPACE, METHOD_NAME);
//            soapObject.addProperty("id", "1");
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName("id");
            propertyInfo.setValue(Integer.parseInt( params[0]));
            propertyInfo.setType(Integer.class);
            soapObject.addProperty(propertyInfo);
            SoapSerializationEnvelope soapSerializationEnvelope  = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapSerializationEnvelope.dotNet= true;
            soapSerializationEnvelope.setOutputSoapObject(soapObject);
            HttpTransportSE httpTransportSE = new HttpTransportSE(SOAP_URL);
            try {
                httpTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);
                soapPrimitive =soapSerializationEnvelope.getResponse();

            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            return soapPrimitive.toString();
        }

        @Override
        protected void onPostExecute( final String aVoid) {
            super.onPostExecute(aVoid);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "onPostExecute: "+aVoid);
                }
            });

        }
    }
}
