package com.example.spytech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText userNameEdt, passwordEdt;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        userNameEdt = findViewById(R.id.txtUserName);
        passwordEdt = findViewById(R.id.txtPassword);
        loginBtn = findViewById(R.id.login);

        userNameEdt.requestFocus();

        // Show the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userNameEdt, InputMethodManager.SHOW_IMPLICIT);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = "admin";//userNameEdt.getText().toString();
                String password = "admin";//passwordEdt.getText().toString();

                // checking if the entered text is empty or not.
                if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
                }
                else if (userName.equals("admin") && password.equals("admin")) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    Toast.makeText(MainActivity.this, "Home Activity Starts", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Wrong userName or Password", Toast.LENGTH_SHORT).show();
                }

                // calling a method to login our user.

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}