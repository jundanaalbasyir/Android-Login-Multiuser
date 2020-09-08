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
import dev.jundana.loginandregistrationmultiuser.helper.SharedPref;
import dev.jundana.loginandregistrationmultiuser.helper.Url;
import dev.jundana.loginandregistrationmultiuser.model.Users;
import dev.jundana.loginandregistrationmultiuser.ui.admin.AdminActivity;
import dev.jundana.loginandregistrationmultiuser.ui.user.UserActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });
    }

    private void checkInput() {
        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            etUsername.setError("Please enter your email");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            etPassword.setError("Enter a password");
            etPassword.requestFocus();
            return;
        }

        UserLogin ul = new UserLogin(
                etUsername.getText().toString(),
                etPassword.getText().toString());
        ul.execute();
    }

    class UserLogin extends AsyncTask<Void, Void, String> {
        String username, password;

        UserLogin(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obj = new JSONObject(s);

                if (!obj.getBoolean("error")) {
                    Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject userJson = obj.getJSONObject("user");

                    Users user = new Users(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email"),
                            userJson.getString("status")
                    );

                    SharedPref.getInstance(LoginActivity.this).setUserLogin(user);
                    Users dataUser = SharedPref.getInstance(LoginActivity.this).getUser();

                    if (dataUser.getStatus().equals("admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivity.this, UserActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            return requestHandler.sendPostRequest(Url.URL_LOGIN, params);
        }
    }
}