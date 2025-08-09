package com.example.spytech;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
public class SoapRecyclerView {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "GetSubscriberbyId"; // Replace with your SOAP method
    private static final String SOAP_ACTION = "http://tempuri.org/GetSubscriberbyId";
    private static final String URL = "http://192.168.100.2:8083/WebService1.asmx"; // Replace with your service URL

    public static List<String> fetchData() {
        List<String> dataList = new ArrayList<>();

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            // Loop through the response to extract data
            for (int i = 0; i < response.getPropertyCount(); i++) {
                dataList.add(response.getProperty(i).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
