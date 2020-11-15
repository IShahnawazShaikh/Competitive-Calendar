package com.shahnawazshaikh.competitive.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.Utilities.ApiUrl;
import com.shahnawazshaikh.competitive.preferences.UserSession;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_EMAIL;
import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_INSTITUTE;
import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_NAME;
import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_PASSWORD;

public class Edit extends Fragment {
    View view;
    private HashMap<String, String> userDetails;
    private UserSession userSession;
    EditText nameID,emailID,instituteID,passwordID,PasswordID;
    Button updateButton;
    ProgressBar progressBar;
    private OkHttpClient okHttpClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_edit, container, false);
        nameID = view.findViewById(R.id.name);
        emailID = view.findViewById(R.id.email);
        instituteID = view.findViewById(R.id.institute);
        passwordID = view.findViewById(R.id.password);
        updateButton = view.findViewById(R.id.update);
        progressBar=view.findViewById(R.id.progressBar);


        setUserDetails();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        return  view;
    }

    private void updateUser() {
        userSession = new UserSession(getContext());
        userDetails = userSession.getUserDetails();

        if (TextUtils.isEmpty(nameID.getText().toString().trim())) {
            Toast.makeText(getContext(), "Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(emailID.getText().toString().trim())) {
            Toast.makeText(getContext(), "Email cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(passwordID.getText().toString().trim())) {
            Toast.makeText(getContext(), "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(instituteID.getText().toString().trim())) {
            Toast.makeText(getContext(), "College Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        else if(!validEmail(emailID.getText().toString().trim())){
            Toast.makeText(getContext(),"Enter valid e-mail!",Toast.LENGTH_LONG).show();
        }
        else if(!isValidPassword(passwordID.getText().toString().trim())){
            Toast.makeText(getContext(), "password must contain atleast one digit, one small character,one capital character", Toast.LENGTH_SHORT).show();
        }
        else if(userDetails.get(KEY_NAME).trim().equals(nameID.getText().toString().trim()) &&
                userDetails.get(KEY_EMAIL).trim().equals(emailID.getText().toString().trim()) &&
                userDetails.get(KEY_PASSWORD).trim().equals(passwordID.getText().toString().trim()) &&
                userDetails.get(KEY_INSTITUTE).trim().equals(instituteID.getText().toString().trim())){
            Toast.makeText(getContext(), "Nothing has Changed", Toast.LENGTH_SHORT).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            updateButton.setText("Updating...");
            updateButton.setEnabled(false);
            new Thread(() -> {
                try {
                    String response = updateDetail(ApiUrl.UPDATE_USER);
                    JSONObject jsonObject = new JSONObject(response);
                    final boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            updateButton.setText("Update");
                            progressBar.setVisibility(View.GONE);
                            updateButton.setEnabled(true);

                            userSession.createUserLoginSession(jsonObject.optString("email"), jsonObject.optString("name"),
                                    jsonObject.optString("institute"), "true",jsonObject.optString("password"));

                            //Refresh
                            setUserDetails();

                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "" + "Something went wrong !!!", Toast.LENGTH_SHORT).show();
                            updateButton.setText("Upadte");
                            updateButton.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(),"catch",Toast.LENGTH_SHORT).show();
                    getActivity().runOnUiThread(() -> {
                        updateButton.setText("Update");
                        updateButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    });
                }
            }).start();
            //update
        }
    }
    private String updateDetail(String url) throws Exception {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key_email", userDetails.get(KEY_EMAIL))
                .addFormDataPart("email", emailID.getText().toString())
                .addFormDataPart("name", nameID.getText().toString())
                .addFormDataPart("password", passwordID.getText().toString())
                .addFormDataPart("institute", instituteID.getText().toString())
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

    private void setUserDetails() {
        userSession = new UserSession(getContext());
        userDetails = userSession.getUserDetails();
        nameID.setText(userDetails.get(KEY_NAME));
        emailID.setText(userDetails.get(KEY_EMAIL));
        instituteID.setText(userDetails.get(KEY_INSTITUTE));
        passwordID.setText(userDetails.get(KEY_PASSWORD));
       // Toast.makeText(getContext(),"@@@@@@@"+userDetails.get(KEY_PASSWORD),Toast.LENGTH_LONG).show();
    }
}