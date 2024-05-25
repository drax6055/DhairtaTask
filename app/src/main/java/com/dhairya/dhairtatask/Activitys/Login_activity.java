package com.dhairya.dhairtatask.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dhairya.dhairtatask.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_activity extends AppCompatActivity {
ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.editUsername.getText().toString().trim();
                String password = binding.editPasswod.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    login(username, password);
                } else {
                    Toast.makeText(Login_activity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login(String username, String password) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Login_activity.this, "Login Successful! Token: " + response.body().getToken(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                } else {
                    Toast.makeText(Login_activity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login_activity.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}