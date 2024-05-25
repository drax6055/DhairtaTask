package com.dhairya.dhairtatask.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dhairya.dhairtatask.Model.LoginRequest;
import com.dhairya.dhairtatask.Model.LoginResponse;
import com.dhairya.dhairtatask.databinding.ActivityLoginBinding;
import com.dhairya.dhairtatask.utiles.ApiService;
import com.dhairya.dhairtatask.utiles.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_activity extends AppCompatActivity {
    ActivityLoginBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Check if user is already logged in
        if (sharedPreferences.contains(KEY_TOKEN)) {
            redirectToMainActivity();
        }

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
                    // Save login token in SharedPreferences
                    editor.putString(KEY_TOKEN, response.body().getToken());
                    editor.apply();

                    Toast.makeText(Login_activity.this, "Login Successful! Token: " + response.body().getToken(), Toast.LENGTH_SHORT).show();
                    redirectToMainActivity();
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

    private void redirectToMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    private void logout() {
        editor.clear();
        editor.apply();
        Toast.makeText(Login_activity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to login activity
        startActivity(new Intent(getApplicationContext(), Login_activity.class));
        finish();
    }
}