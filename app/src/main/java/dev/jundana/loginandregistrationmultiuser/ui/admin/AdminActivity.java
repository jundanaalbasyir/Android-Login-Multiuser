package dev.jundana.loginandregistrationmultiuser.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dev.jundana.loginandregistrationmultiuser.R;
import dev.jundana.loginandregistrationmultiuser.helper.SharedPref;
import dev.jundana.loginandregistrationmultiuser.model.Users;

public class AdminActivity extends AppCompatActivity {
    TextView tvUsername, tvEmail, tvStatus;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvStatus = findViewById(R.id.tvStatus);
        btnLogout = findViewById(R.id.btnLogout);

        Users user = SharedPref.getInstance(this).getUser();

        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        tvStatus.setText(user.getStatus());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.getInstance(getApplicationContext()).logout();
                finish();
            }
        });
    }
}