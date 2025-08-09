package com.example.spytech;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private EditText etMsisdn;
    private Button btnFetch;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private Boolean isSearchBtn;
    public String strCNIC;
    public Boolean isTVCNIC=false;
    //private TextView tvCNIC;
    private static final String NAMESPACE = "http://tempuri.org/"; // Adjust if different in WSDL
    private static final String METHOD_NAME = "GetSubscriberbyId";
    private static final String SOAP_ACTION = "http://tempuri.org/GetSubscriberbyId"; // Typically NAMESPACE + METHOD_NAME
    private static final String URL = "http://192.168.100.2:8083/WebService1.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        etMsisdn = findViewById(R.id.txtPhoneNumber);
        btnFetch = findViewById(R.id.btnSearchPhoneNum);
        recyclerView = findViewById(R.id.recyclerView);
        isSearchBtn=false;
        // Setup RecyclerView
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMsisdn = etMsisdn.getText().toString().trim();
                isSearchBtn=true;
                if (!userMsisdn.isEmpty()) {
                    new FetchDataTask().execute(userMsisdn);
                } else {
                    Toast.makeText(UserActivity.this, "Please enter User ID", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private class FetchDataTask extends AsyncTask<String, Void, List<User>> {

        @Override
        protected List<User> doInBackground(String... params) {
            String userId = params[0];
            List<User> result = new ArrayList<>();

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("MSISDN", userId);

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

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            if (users != null && !users.isEmpty()) {
                adapter.updateList(users);
            } else {
                Toast.makeText(UserActivity.this, "No data found or error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void openCNICActivity(View view) {

           /* setContentView(R.layout.item_layout);
            TextView tvCNIC =findViewById(R.id.txtCnic);
            strCNIC=tvCNIC.getText().toString();
            isTVCNIC=true;
            tvCNIC.setOnClickListener(v -> {
                Intent intent = new Intent(UserActivity.this, UserCNICActivity.class);
                startActivity(intent);
            });*/
    }


}