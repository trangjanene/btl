package vn.edu.tlu.cse.gogoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class KhachHangHomeActivity extends AppCompatActivity {
//
    private Button btnNaptien, btnThueXe, btnDangKyVeThang, btnDangXuat, btnLichSu;
    private void checkIfUserHasReturnedBike() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("history")
                .whereEqualTo("userId", userId)
                .whereEqualTo("ended", false) // xe chưa được trả
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        // Không có xe nào đang thuê => cho vào lịch sử
                        startActivity(new Intent(KhachHangHomeActivity.this, HistoryActivity.class));
                    } else {
                        Toast.makeText(this, "Bạn cần trả xe trước khi xem lịch sử.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kiểm tra trạng thái thuê xe", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang_home);

        btnNaptien = findViewById(R.id.btnNaptien);
        btnThueXe = findViewById(R.id.btnThueXe);
        btnDangKyVeThang = findViewById(R.id.btnVeThang);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnLichSu = findViewById(R.id.btnLichSu); // Gán nút lịch sử

        //Khi người dùng bấm nạp tiền
        btnNaptien.setOnClickListener(v -> {
            Intent intent = new Intent(KhachHangHomeActivity.this, NaptienActivity.class);
            startActivity(intent);
        });

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

        btnLichSu = findViewById(R.id.btnLichSu);

        btnLichSu.setOnClickListener(v -> {
            checkIfUserHasReturnedBike();
        });

        Button btnTraXe = findViewById(R.id.btnTraXe);
        btnTraXe.setOnClickListener(v -> {
            Intent intent = new Intent(KhachHangHomeActivity.this, TraXeActivity.class);
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
