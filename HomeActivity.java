package com.example.spytech;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.annotation.SuppressLint;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private ImageButton btnlogout;
    private ImageButton btnPhoneNo;
    private ImageButton CNICbtn;
    private ImageButton CDRbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        btnlogout = findViewById(R.id.btnLogOut);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        btnPhoneNo = findViewById(R.id.btnPhoneNumber);
        btnPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(HomeActivity.this,PhnNoActivity.class));
                startActivity(new Intent(HomeActivity.this,UserActivity.class));
            }
        });
        CNICbtn = findViewById(R.id.btnCNIC);
        CNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,UserCNICActivity.class));
            }
        });
        CDRbtn = findViewById(R.id.btnCDR_Report);
        CDRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,cdrReportActivity.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}