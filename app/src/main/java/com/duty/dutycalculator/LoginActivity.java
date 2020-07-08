package com.duty.dutycalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duty.dutycalculator.api.ApiService;
import com.duty.dutycalculator.api.LoginResponseModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText medittext_login_username;
    EditText medittext_login_password;
    Button mlogin_button;
    TextView mtxtRegister;
    MyApplication myApplication = MyApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (myApplication.isLogin(LoginActivity.this)) {
            startActivity(new Intent(LoginActivity.this, CalculateDutyActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        medittext_login_username = (EditText) findViewById(R.id.edittext_login_username);
        medittext_login_password = (EditText) findViewById(R.id.edittext_login_password);
        mlogin_button = (Button) findViewById(R.id.login_button);
        mtxtRegister = (TextView) findViewById(R.id.txtRegister);

        mlogin_button.setOnClickListener(this);
        mtxtRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String username = medittext_login_username.getText().toString();
        String password = medittext_login_password.getText().toString();
        String type = "login";

        switch (view.getId()) {
            case R.id.login_button:
                if (myApplication.isEmptyText(username))
                    medittext_login_username.setError("Please enter username");
                else if (myApplication.isEmptyText(password))
                    medittext_login_username.setError("Please enter password");
                else
                    apiCallLogin();


                break;

            case R.id.txtRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void apiCallLogin() {
        // create an instance of the ApiService
        ApiService apiService = MyApplication.getInstance().getRetrofit().create(ApiService.class);
        apiService.getLogin(medittext_login_username.getText().toString(), medittext_login_password.getText().toString()).
                enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                        Log.i("==", "==onResponse==response" + response.message());

                        LoginResponseModel loginResponseModel = response.body();
                        if (loginResponseModel == null) {
                            Toast.makeText(LoginActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                        }
                        if (loginResponseModel.msg != null && (loginResponseModel.msg).equalsIgnoreCase("Login Failed")) {
                            Toast.makeText(LoginActivity.this, "" + loginResponseModel.msg, Toast.LENGTH_SHORT).show();
                        } else if (!myApplication.isEmptyText(loginResponseModel.userID)) {
                            startActivity(new Intent(LoginActivity.this, CalculateDutyActivity.class));
                            finish();
                        } else {
                            if (myApplication.isEmptyText(loginResponseModel.msg)) {
                                Toast.makeText(LoginActivity.this, "" + loginResponseModel.msg, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(LoginActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                        Log.i("==", "==onFailure==response" + t);
                        Toast.makeText(LoginActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
