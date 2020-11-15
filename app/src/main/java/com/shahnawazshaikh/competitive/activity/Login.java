package com.shahnawazshaikh.competitive.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.Utilities.ApiUrl;
import com.shahnawazshaikh.competitive.models.UserDetails;
import com.shahnawazshaikh.competitive.preferences.UserSession;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
  TextView register_text,forgot_passowrd;
  EditText emailID,passwordID;
  ProgressBar progressBar;
  Button login_button;
  private OkHttpClient okHttpClient;
  private UserSession userSession;
    CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userSession = new UserSession(this);
        register_text=findViewById(R.id.register_text);
        emailID=findViewById(R.id.email);
        passwordID=findViewById(R.id.password);
        progressBar=findViewById(R.id.progressBar);
        login_button=findViewById(R.id.loginButton);
        forgot_passowrd=findViewById(R.id.forgot_passowrd);
        checkbox=findViewById(R.id.checkbox);

        forgot_passowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Recover.class));
            }
        });
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    passwordID.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    passwordID.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginUser();
                //startActivity(new Intent(Login.this, MainActivity.class));
                //finish();
            }
        });
    }

    void loginUser() {
        if (TextUtils.isEmpty(emailID.getText().toString())) {
            Toast.makeText(this, "Email cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(passwordID.getText().toString())) {
            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            login_button.setText("Logging...");
            login_button.setEnabled(false);
            new Thread(() -> {
                try {
                    String response = updateLogin(ApiUrl.LOGIN_USER, new UserDetails(emailID.getText().toString().trim(), passwordID.getText().toString().trim()));
                    Log.d("response", "this is response" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    final boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        runOnUiThread(() -> {
                            try {

                                login_button.setText("Login");
                                progressBar.setVisibility(View.GONE);
                                userSession.createUserLoginSession(jsonObject.optString("email"), jsonObject.optString("name"),
                                        jsonObject.optString("institute"), "true",jsonObject.optString("password"));
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(this,"Something went wrong: "+e.getMessage(),Toast.LENGTH_LONG).show();
                                login_button.setText("Login");
                                login_button.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        });

                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "" + "Invalid email or password", Toast.LENGTH_SHORT).show();
                            login_button.setText("Login");
                            login_button.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this,"Something went wrong: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        login_button.setText("Login");
                        login_button.setEnabled(true);
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
//        if (userSession.isUserLoggedIn()) {
//            finish();
//        }
        //finish();
       finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(userSession.isUserLoggedIn()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}