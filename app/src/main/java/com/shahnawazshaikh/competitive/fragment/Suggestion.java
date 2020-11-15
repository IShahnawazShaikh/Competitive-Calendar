package com.shahnawazshaikh.competitive.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.Utilities.ApiUrl;
import com.shahnawazshaikh.competitive.activity.Login;
import com.shahnawazshaikh.competitive.activity.Register;
import com.shahnawazshaikh.competitive.models.UserDetails;
import com.shahnawazshaikh.competitive.preferences.UserSession;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_EMAIL;
import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_INSTITUTE;
import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_NAME;

public class Suggestion extends Fragment {
    View view;
    EditText messageID;
    Button send;
    private HashMap<String, String> userDetails;
    private UserSession userSession;
    ProgressBar progressBar;
    private OkHttpClient okHttpClient;
    public Suggestion() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_suggestion, container, false);
        messageID=view.findViewById(R.id.message);
        send=view.findViewById(R.id.send);
        progressBar=view.findViewById(R.id.progressBar);
        userSession = new UserSession(getContext());
        userDetails = userSession.getUserDetails();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sendSuggestion();
            }
        });

      return view;
    }
    private void sendSuggestion() {

        if (TextUtils.isEmpty(messageID.getText().toString().trim())) {
            Toast.makeText(getContext(), "Suggestion Box cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
           // UserDetails userDetails = new UserDetails(nameID.getText().toString().trim(), emailID.getText().toString().trim(), passwordID.getText().toString().trim(),instituteID.getText().toString().trim());
            progressBar.setVisibility(View.VISIBLE);
            send.setText("Sending...");
            send.setEnabled(false);
            new Thread(() -> {
                try {
                    String response = insertSuggestion(ApiUrl.SUGGESTION_USER);
                    //     Log.d("response", "this is response" + response);
                    JSONObject jsonObject = new JSONObject(response);

                    final boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Submitted Successfully", Toast.LENGTH_SHORT).show();
                            send.setEnabled(true);
                            send.setText("Send");
                            progressBar.setVisibility(View.GONE);
                            messageID.setText("");
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "" + "Something went wrong !!!", Toast.LENGTH_SHORT).show();
                            send.setText("Send");
                            send.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(),"catch",Toast.LENGTH_SHORT).show();
                   getActivity().runOnUiThread(() -> {
                        send.setText("Send");
                        send.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    });
                }
            }).start();
        }
    }
    private String insertSuggestion(String url) throws Exception {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", userDetails.get(KEY_EMAIL))
                .addFormDataPart("name", userDetails.get(KEY_NAME))
                .addFormDataPart("message", messageID.getText().toString())
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















}