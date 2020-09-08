package dev.jundana.loginandregistrationmultiuser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import dev.jundana.loginandregistrationmultiuser.R;
import dev.jundana.loginandregistrationmultiuser.helper.SharedPref;
import dev.jundana.loginandregistrationmultiuser.model.Users;
import dev.jundana.loginandregistrationmultiuser.ui.admin.AdminActivity;
import dev.jundana.loginandregistrationmultiuser.ui.user.UserActivity;

public class MainActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart (){
        super.onStart();
        SharedPref sharedPref = SharedPref.getInstance(MainActivity.this);
        if (sharedPref.isLoggedIn()) {
            Users dataUser = SharedPref.getInstance(this).getUser();
            if (dataUser.getStatus().equals("admin")) {
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                finish();
            }
        }
    }
}