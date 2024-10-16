package com.example.csis3175_project_rims;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Stocktaking extends AppCompatActivity {

    FirebaseAuth auth;
    TextView textView;
    Button btnLogout2, btnDashboard3, btnSKUManage3, btnFinalizeStocktake;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stocktaking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        //txtUserInfo = findViewById(R.id.txtUserInfo);
        btnLogout2 = findViewById(R.id.btnLogout2);
        btnDashboard3 = findViewById(R.id.btnDashboard3);
        btnSKUManage3 = findViewById(R.id.btnSKUManage3);
        btnFinalizeStocktake = findViewById(R.id.btnFinalizeStocktake);
        user = auth.getCurrentUser();


        btnLogout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDashboard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        btnSKUManage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SKU_Manage.class);
                startActivity(intent);
                finish();
            }
        });

        btnFinalizeStocktake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Stocktaking under development",Toast.LENGTH_LONG).show();
            }
        });
    }
}