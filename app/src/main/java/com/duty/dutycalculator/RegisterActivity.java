package com.duty.dutycalculator;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mFirstName;
    EditText mSurname;
    EditText mEmail;
    EditText mPhone;
    EditText mUsername;
    EditText mPassword;
    EditText mConfirmPassword;
    Button mRegister;
    TextView mSignin;

    MyApplication myApplication = MyApplication.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstName = (EditText)findViewById(R.id.edittext_register_firstname);
        mSurname = (EditText)findViewById(R.id.edittext_register_surname);
        mEmail = (EditText)findViewById(R.id.edittext_register_email);
        mPhone = (EditText)findViewById(R.id.edittext_register_phoneNumber);
        mUsername = (EditText)findViewById(R.id.edittext_register_username);
        mPassword = (EditText)findViewById(R.id.edittext_register_password);
        mConfirmPassword = (EditText)findViewById(R.id.edittext_register_confirm_password);
        mRegister = (Button)findViewById(R.id.register_button);
        mSignin = (TextView)findViewById(R.id.txtSignIn);

        mRegister.setOnClickListener(this);
        mSignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        String firstName = mFirstName.getText().toString();
        String lastName = mSurname.getText().toString();
        String username = mUsername.getText().toString();
        String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        String type = "register";

        switch (view.getId())
        {
            case R.id.register_button:
                if (myApplication.isEmptyText(firstName))
                    mUsername.setError("Please enter Firstname");
                else if (myApplication.isEmptyText(lastName))
                    mUsername.setError("Please enter Surename");
                else if (myApplication.isEmptyText(username))
                    mUsername.setError("Please enter username");
                else if (myApplication.isEmptyText(email))
                    mEmail.setError("Please enter email");
                else if (myApplication.isEmptyText(phone))
                    mEmail.setError("Please enter PhoneNumber");
                else if (myApplication.isEmptyText(password))
                    mPassword.setError("Please enter password");
                else if (!confirmPassword.equals(password))
                    mPassword.setError("confirm password not match");
                else
                    apiCallRegister();

                break;

            case R.id.txtSignIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }


    private void apiCallRegister() {
        // create an instance of the ApiService
        String userFullName = mFirstName.getText().toString() + " " + mSurname.getText().toString();
        ApiService apiService = MyApplication.getInstance().getRetrofit().create(ApiService.class);
        apiService.getSignup(mUsername.getText().toString(), userFullName, mEmail.getText().toString(),
                mPhone.getText().toString(), mPassword.getText().toString()).
                enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                        Log.i("==", "==onResponse==response" + response.message());

                        LoginResponseModel loginResponseModel = response.body();
                        if (loginResponseModel == null) {
                            Toast.makeText(RegisterActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                        }

                        if (loginResponseModel.msg!=null && (loginResponseModel.msg).equalsIgnoreCase("Login Failed")) {
                            Toast.makeText(RegisterActivity.this, "" + loginResponseModel.msg, Toast.LENGTH_SHORT).show();
                        }else if (!myApplication.isEmptyText(loginResponseModel.userID)) {

                            startActivity(new Intent(RegisterActivity.this, CalculateDutyActivity.class));
                        } else {
                            if (myApplication.isEmptyText(loginResponseModel.msg)) {
                                Toast.makeText(RegisterActivity.this, "" + loginResponseModel.msg, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(RegisterActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                        Log.i("==", "==onFailure==response" + t);
                        Toast.makeText(RegisterActivity.this, "Something went wrong please try again!!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
