package com.example.csis3175_project_rims;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class functionPanel extends AppCompatActivity {

    FirebaseAuth auth;
    TextView txtUserInfo;
    Button btnLogout, btnDashboard, btnStocktaking, btnSKUManage;
    FirebaseUser user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_function_panel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        txtUserInfo = findViewById(R.id.txtUserInfo);
        btnLogout = findViewById(R.id.btnLogout);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnStocktaking = findViewById(R.id.btnStocktaking);
        btnSKUManage = findViewById(R.id.btnSKUManage);
        user = auth.getCurrentUser();

        // Set button click listeners
        btnDashboard.setOnClickListener(v -> replaceFragment(new DashboardFragment()));
        btnSKUManage.setOnClickListener(v -> replaceFragment(new SkuManageFragment()));
        btnStocktaking.setOnClickListener(v -> replaceFragment(new StocktakingFragment()));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Replace fragment in the container
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Optional: enables back navigation
        transaction.commit();
    }
}