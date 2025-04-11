package vn.edu.tlu.cse.gogoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnDangNhap, btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);

        btnDangNhap.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        btnDangKy.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }
}