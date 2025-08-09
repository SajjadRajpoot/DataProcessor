package com.example.spytech;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class PhnNoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubscriberAdapter adapter;
    private List<Subscriber> subscriberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phn_no);
        EditText edit_text= findViewById(R.id.txtPhoneNumber);
        Button btnSearch =  findViewById(R.id.btnSearchPhoneNum);
        edit_text.requestFocus();
        edit_text.setTextSize(24);
        String phnNo;
        // Show the keyboard


        btnSearch.setOnFocusChangeListener((view, hasFocus) -> {

        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetSubscriberTask().execute(edit_text.getText().toString());

//                recyclerView = findViewById(R.id.recyclerView);
//                //recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//                // Populate list using for loop
//                subscriberList = new ArrayList<>();
//                for (int i = 1; i <= 10; i++) {
//                    subscriberList.add(new Subscriber("MSISDN " + i, "Name " + i, "CNIC " + i,"Address " + i,"Company " + i));
//                }
//
//                adapter = new SubscriberAdapter(subscriberList);
//                recyclerView.setAdapter(adapter);
            }


        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private class GetSubscriberTask extends AsyncTask<String, Void, Subscriber> {
        @Override
        protected Subscriber doInBackground(String... params) {
            return SoapClient.getSubscriberById(params[0]);
        }

        @Override
        protected void onPostExecute(Subscriber result) {
            TextView txtview2 = findViewById(R.id.textView2);
            if (result != null) {


                txtview2.setText(result.toString());
            } else {
                txtview2.setText("Failed to fetch subscriber details.");
            }
        }
    }
}