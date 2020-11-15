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
import android.widget.TextView;
import android.widget.Toast;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.Utilities.ApiUrl;
import com.shahnawazshaikh.competitive.models.UserDetails;

import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
   TextView loginText;
   EditText nameID,emailID,passwordID,cnfPasswordID,instituteID;
   Button signupButton;
   ProgressBar progressBar;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginText=findViewById(R.id.loginText);
        nameID=findViewById(R.id.name);
        emailID=findViewById(R.id.email);
        passwordID=findViewById(R.id.password);
        cnfPasswordID=findViewById(R.id.cnfrmpassword);
        instituteID=findViewById(R.id.institute);
        progressBar=findViewById(R.id.progressBar);
        signupButton=findViewById(R.id.signup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

    }
    private void signUp() {
        if (TextUtils.isEmpty(nameID.getText().toString().trim())) {
            Toast.makeText(this, "Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(emailID.getText().toString().trim())) {
            Toast.makeText(this, "Email cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(passwordID.getText().toString().trim())) {
            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(instituteID.getText().toString().trim())) {
            Toast.makeText(this, "College Name cannot be blank", Toast.LENGTH_SHORT).show();
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
            UserDetails userDetails = new UserDetails(nameID.getText().toString().trim(), emailID.getText().toString().trim(), passwordID.getText().toString().trim(),instituteID.getText().toString().trim());
            progressBar.setVisibility(View.VISIBLE);
            signupButton.setText("Registering...");
            signupButton.setEnabled(false);
            new Thread(() -> {
                try {
                    String response = updateLogin(ApiUrl.SIGN_UP_USER, userDetails);
                    JSONObject jsonObject = new JSONObject(response);
                    final boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, Login.class));
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "" +jsonObject.optString("message") , Toast.LENGTH_SHORT).show();
                            signupButton.setText("Signup");
                            signupButton.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(Register.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                        signupButton.setText("Signup");
                        signupButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    });
                }
            }).start();
        }
    }
    private String updateLogin(String url, UserDetails userDetails) throws Exception {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", userDetails.getEmail())
                .addFormDataPart("password", userDetails.getPassword())
                .addFormDataPart("name", userDetails.getName())
                .addFormDataPart("institute", userDetails.getInstitute())
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Register.this, Login.class));
        finish();

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