package com.example.spytech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExcelUploadActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private String filePath;
    private Button btnSelect, btnUpload;
    private TextView txtStatus;
    private File selectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cdr_report);
        btnSelect = findViewById(R.id.btn_select);
        btnUpload = findViewById(R.id.btn_upload);
        txtStatus = findViewById(R.id.txt_status);

        btnSelect.setOnClickListener(v -> openFileChooser());

        btnUpload.setOnClickListener(v -> {
            if (selectedFile != null) {
                uploadFile(selectedFile);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(Intent.createChooser(intent, "Select Excel File"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            filePath = FileUtils.getPath(this, fileUri); // See FileUtils.java below
            selectedFile = new File(filePath);
            txtStatus.setText("Selected: " + selectedFile.getName());
            btnUpload.setEnabled(true);
        }
    }

    private void uploadFile(File file) {
        txtStatus.setText("Uploading...");
        new Thread(() -> {
            try {
                String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                //URL url = new URL("http://YOUR_SERVER_ADDRESS/api/upload");
                URL url = new URL("http://192.168.100.2:8086/api/FileUpload/upload");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", file.getName());

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                FileInputStream fis = new FileInputStream(file);
                int bytesAvailable = fis.available();
                int bufferSize = Math.min(bytesAvailable, 1024 * 1024);
                byte[] buffer = new byte[bufferSize];

                int bytesRead;
                while ((bytesRead = fis.read(buffer, 0, bufferSize)) > 0) {
                    dos.write(buffer, 0, bytesRead);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                int serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                runOnUiThread(() -> txtStatus.setText("Server Response: " + serverResponseMessage + " (" + serverResponseCode + ")"));

                fis.close();
                dos.flush();
                dos.close();

            } catch (Exception e) {
                runOnUiThread(() -> txtStatus.setText("Upload error: " + e.getMessage()));
            }
        }).start();
    }
}
