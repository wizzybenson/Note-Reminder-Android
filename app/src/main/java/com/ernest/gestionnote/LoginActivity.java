package com.ernest.gestionnote;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private RetrofitApiInterface retrofitApiInstance;
    private final int _SUCCESS_ = 200;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cookie = preferences.getString("reminderCookie", "");
        if (!"".equals(cookie)) {
            Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
            //finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        retrofitApiInstance = RetrofitApiClass.getClient();



        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();

        retrofitApiInstance.connect(name,password).enqueue(new Callback<ApiGenericGetResponse>() {
            @Override
            public void onResponse(Call<ApiGenericGetResponse> call, Response<ApiGenericGetResponse> response) {

                try {
                    if (!(response.errorBody()==null))
                        Log.d("requesterror", response.errorBody().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!(response.body() == null)) {

                    ApiGenericGetResponse apiResponse = response.body();

                    Log.d("CALLOBJECT", response.body().toString());

                    if (apiResponse.getCode() == _SUCCESS_) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("reminderCookie",response.headers().get("Set-Cookie"));
                        editor.apply();
                        Log.d("COOKIE", response.headers().get("Set-Cookie"));
                        startActivity(new Intent(LoginActivity.this, NotesActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        _loginButton.setEnabled(true);}
                } else  Log.d("responserror", response.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiGenericGetResponse> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        //moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty()) {
            _nameText.setError("enter a valid name");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("enter your password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}