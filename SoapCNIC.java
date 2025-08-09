package com.example.spytech;

import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class SoapCNIC {
    private static final String NAMESPACE = "http://tempuri.org/"; // Adjust if different in WSDL
    private static final String METHOD_NAME = "GetSubscriberbyCNIC";
    private static final String SOAP_ACTION = "http://tempuri.org/GetSubscriberbyCNIC"; // Typically NAMESPACE + METHOD_NAME
    private static final String URL = "http://192.168.100.2:8083/WebService1.asmx";

    public static List<User> getUserByCNIC(String userId) {
        List<User> result = new ArrayList<>();

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("CNIC", userId);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            // Parse the SOAP response
            for (int i = 0; i < response.getPropertyCount(); i++) {
                SoapObject userObject = (SoapObject) response.getProperty(i);
                String msisdn = userObject.getProperty("MSISDN").toString();
                String username = userObject.getProperty("NAME").toString();
                String cnic = userObject.getProperty("CNIC").toString();
                String address = userObject.getProperty("ADDRESS").toString();
                String company = userObject.getProperty("COMPANY").toString();

                result.add(new User(msisdn, username, cnic, address, company));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



}

