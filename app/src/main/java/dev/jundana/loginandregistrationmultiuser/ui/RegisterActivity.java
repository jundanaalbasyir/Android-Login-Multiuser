package dev.jundana.loginandregistrationmultiuser.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import dev.jundana.loginandregistrationmultiuser.R;
import dev.jundana.loginandregistrationmultiuser.helper.RequestHandler;
import dev.jundana.loginandregistrationmultiuser.helper.Url;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    EditText etEmail, etUsername, etPassword, etStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etStatus = findViewById(R.id.etStatus);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });
    }

    private void checkInput() {
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            etUsername.setError("Please enter username");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            etPassword.setError("Enter a password");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etStatus.getText().toString().trim())) {
            etStatus.setError("Enter a status");
            etStatus.requestFocus();
            return;
        }

        RegisterUser ru = new RegisterUser(
                etEmail.getText().toString(),
                etUsername.getText().toString(),
                etPassword.getText().toString(),
                etStatus.getText().toString());
        ru.execute();
    }

    private class RegisterUser extends AsyncTask<Void, Void, String> {
        private String username, email, password, status;

        RegisterUser(String username, String email, String password, String status) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.status = status;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("email", email);
            params.put("password", password);
            params.put("status", status);

            return requestHandler.sendPostRequest(Url.URL_REGISTER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obj = new JSONObject(s);
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}