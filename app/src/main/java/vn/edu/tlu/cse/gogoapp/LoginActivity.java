package vn.edu.tlu.cse.gogoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

public class LoginActivity extends AppCompatActivity {
//
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtToRegister, txtForgotPassword;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtToRegister = findViewById(R.id.txtToRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword); // THÊM DÒNG NÀY

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> loginUser());

        txtToRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        txtForgotPassword.setOnClickListener(v ->  // THÊM DÒNG NÀY
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class))
        );
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = auth.getCurrentUser().getUid();
                    db.collection("users").document(uid).get()
                            .addOnSuccessListener(snapshot -> {
                                if (snapshot.exists()) {
                                    String role = snapshot.getString("role");
                                    if ("khachhang".equals(role)) {
                                        startActivity(new Intent(LoginActivity.this, KhachHangHomeActivity.class));
                                    } else if ("nhanvien".equals(role)) {
                                        startActivity(new Intent(LoginActivity.this, NhanVienHomeActivity.class));
                                    } else {
                                        Toast.makeText(this, "Không xác định vai trò!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(this, "Không tìm thấy dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi đăng nhập: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
