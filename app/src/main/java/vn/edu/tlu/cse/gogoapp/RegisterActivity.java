package vn.edu.tlu.cse.gogoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
//
    EditText edtHoTen, edtEmail, edtMatKhau, edtSoDienThoai;
    RadioGroup radioGroupRole;
    RadioButton rbNhanVien, rbKhachHang;
    Button btnDangKy;
    TextView txtToLogin;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        rbNhanVien = findViewById(R.id.rbNhanVien);
        rbKhachHang = findViewById(R.id.rbKhachHang);
        btnDangKy = findViewById(R.id.btnDangKy);
        txtToLogin = findViewById(R.id.txtToLogin);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnDangKy.setOnClickListener(v -> dangKyNguoiDung());

        txtToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void dangKyNguoiDung() {
        String hoTen = edtHoTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String sdt = edtSoDienThoai.getText().toString().trim();
        String role = rbNhanVien.isChecked() ? "nhanvien" : "khachhang";
        String sotien = "10";
        if (TextUtils.isEmpty(hoTen) || TextUtils.isEmpty(email) || TextUtils.isEmpty(matKhau) || TextUtils.isEmpty(sdt)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, matKhau)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = auth.getCurrentUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("hoTen", hoTen);
                        user.put("email", email);
                        user.put("sdt", sdt);
                        user.put("role", role);
                        user.put("sotien", sotien);
                        db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi lưu dữ liệu!", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
