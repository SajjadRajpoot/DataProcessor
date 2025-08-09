package com.example.spytech;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class SoapClient {
    private static final String NAMESPACE = "http://tempuri.org/"; // Adjust if different in WSDL
    private static final String METHOD_NAME = "GetSubscriberbyId";
    private static final String SOAP_ACTION = "http://tempuri.org/GetSubscriberbyId"; // Typically NAMESPACE + METHOD_NAME
    private static final String URL = "http://192.168.100.2:8083/WebService1.asmx";


    public static Subscriber getSubscriberById(String subscriberId) {
        String name;
        String company;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("MSISDN", subscriberId);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true; // Required for .NET-based services
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject subscriberObject = (SoapObject) response.getProperty(0);
             //Extract values from the response
            String id = subscriberObject.getProperty(0).toString();
             name = subscriberObject.getProperty(1).toString();
            String cnic = subscriberObject.getProperty(2).toString();
            String address = subscriberObject.getProperty(3).toString();
             company = subscriberObject.getProperty(4).toString();

            return new Subscriber(id, name, cnic, address, company);
            //return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
