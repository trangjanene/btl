package vn.edu.tlu.cse.gogoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class KhachHangHomeActivity extends AppCompatActivity {

    private Button btnThueXe, btnDangKyVeThang, btnDangXuat, btnLichSu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang_home);

        btnThueXe = findViewById(R.id.btnThueXe);
        btnDangKyVeThang = findViewById(R.id.btnVeThang);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnLichSu = findViewById(R.id.btnLichSu); // Gán nút lịch sử

        // Khi người dùng bấm thuê xe
        btnThueXe.setOnClickListener(v -> {
            Intent intent = new Intent(KhachHangHomeActivity.this, ThueXeActivity.class);
            startActivity(intent);
        });

        // Khi người dùng bấm đăng ký vé tháng
        btnDangKyVeThang.setOnClickListener(v -> {
            Intent intent = new Intent(KhachHangHomeActivity.this, DangKyVeThangActivity.class);
            startActivity(intent);
        });

        // Khi người dùng bấm xem lịch sử thuê xe
        btnLichSu.setOnClickListener(v -> {
            Intent intent = new Intent(KhachHangHomeActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Đăng xuất
        btnDangXuat.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(KhachHangHomeActivity.this, LoginActivity.class));
            finish();
        });
    }
}
