package com.ernest.gestionnote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private RetrofitApiInterface retrofitApiInstance;
    private final String _KEY_ = "emiage2018-1";
    private final int _SUCCESS_ = 201;
    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_key) EditText _keyText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        retrofitApiInstance = RetrofitApiClass.getClient();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString().trim();
        String key = _keyText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

        retrofitApiInstance.register(name,password,key).enqueue(new Callback<ApiGenericGetResponse>() {
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
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        progressDialog.dismiss();
                    }
                } else  Log.d("responserror", response.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiGenericGetResponse> call, Throwable t) {

            }
        });


    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String key = _keyText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty()) {
            _nameText.setError("at least 1 character");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (!_KEY_.equals(key)) {
            _keyText.setError("supply the correct key");
            valid = false;
        } else {
            _keyText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("at least 3 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
