package com.shahnawazshaikh.competitive.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.Utilities.ApiUrl;
import com.shahnawazshaikh.competitive.models.UserDetails;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Recover extends AppCompatActivity {
   Button resetButton;
    ProgressBar progressBar;
    private OkHttpClient okHttpClient;
    EditText emailID,passwordID,cnfPasswordID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        emailID=findViewById(R.id.email);
        passwordID=findViewById(R.id.password);
        cnfPasswordID=findViewById(R.id.cnfrmpassword);
        resetButton=findViewById(R.id.resetButton);
        progressBar=findViewById(R.id.progressBar);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });


    }


    private void resetPassword() {

        if (TextUtils.isEmpty(emailID.getText().toString().trim())) {
            Toast.makeText(this, "Email cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(passwordID.getText().toString().trim())) {
            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        else if(!passwordID.getText().toString().trim().equals(cnfPasswordID.getText().toString().trim())){
            Toast.makeText(this, "Password not matching...", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!validEmail(emailID.getText().toString().trim())){
            Toast.makeText(this,"Enter valid e-mail!",Toast.LENGTH_LONG).show();
        }
        else if(!isValidPassword(passwordID.getText().toString().trim())){
            Toast.makeText(this, "password must contain atleast one digit, one small character,one capital character", Toast.LENGTH_SHORT).show();
        }
        else {

            progressBar.setVisibility(View.VISIBLE);
            resetButton.setText("Registering...");
            resetButton.setEnabled(false);
            new Thread(() -> {
                try {

                    String response = sendData(ApiUrl.RESET_PASSWORD);

                    JSONObject jsonObject = new JSONObject(response);

                    final boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, ""+jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, Login.class));
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "" +jsonObject.optString("message") , Toast.LENGTH_SHORT).show();
                            resetButton.setText("Signup");
                            resetButton.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                } catch (Exception e) {

                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        resetButton.setText("Signup");
                        resetButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    });
                }
            }).start();

        }
    }
    private String sendData(String url) throws Exception {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", emailID.getText().toString())
                .addFormDataPart("password",passwordID.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }
    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}