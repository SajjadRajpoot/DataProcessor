package com.example.spytech;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.net.Uri;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;
import android.provider.OpenableColumns;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class cdrReportActivity extends AppCompatActivity {
    private Button btnSelect, btnUpload;
    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private String filePath, FileName;
    private TextView txtStatus;
    private File selectedFile;
    private static final int FILE_SELECT_CODE = 0;
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cdr_report);
        btnSelect = findViewById(R.id.btn_select);
        btnUpload = findViewById(R.id.btn_upload);
        txtStatus = findViewById(R.id.txt_status);


        btnSelect.setOnClickListener(v -> {
            openFileChooser();

        });

        btnUpload.setOnClickListener(v -> {
            if (  fileUri != null) {
                uploadExcelFile(fileUri);

            }
        });
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        String fileName = getFileNameFromUri(uri);
                        String filePath = uri.getPath(); // May not be full physical path
                        selectedFile = new File(filePath);
                        //TextView fileNameTextView = findViewById(R.id.tvFileName);
                        //TextView filePathTextView = findViewById(R.id.tvFilePath);

                        txtStatus.setText(fileName);
                        //txtStatus.setText(filePath);
                        fileUri=uri;
                        //uploadExcelFile(uri);


                    }
                }
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select Excel File"));

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

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
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

    private void uploadExcelFile(Uri fileUri) {
        try {
            String fileName = getFileName(fileUri);
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            byte[] fileBytes = getBytes(inputStream);

            RequestBody fileBody = RequestBody.create(fileBytes, MediaType.parse("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fileName, fileBody);

            OkHttpClient client = new OkHttpClient();

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName, fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.100.2:8086/api/FileUpload/upload") // Replace with your server URL
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> Toast.makeText(cdrReportActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(cdrReportActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(cdrReportActivity.this, "Upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    response.close(); // Important to avoid memory leak
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }
}