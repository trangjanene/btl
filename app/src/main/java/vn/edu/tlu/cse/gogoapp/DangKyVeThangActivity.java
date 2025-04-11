package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import vn.edu.tlu.cse.gogoapp.models.Subscription;

public class DangKyVeThangActivity extends AppCompatActivity {

    Spinner spinnerGoi;
    Button btnDangKy;
    TextView txtGoiHienTai;

    FirebaseFirestore db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_ve_thang);

        spinnerGoi = findViewById(R.id.spinnerGoi);
        btnDangKy = findViewById(R.id.btnDangKy);
        txtGoiHienTai = findViewById(R.id.txtGoiHienTai);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        List<String> goiList = Arrays.asList("1 tháng", "3 tháng", "6 tháng");
        spinnerGoi.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, goiList));

        btnDangKy.setOnClickListener(v -> dangKy());

        kiemTraGoiHienTai();
    }

    private void dangKy() {
        String goi = spinnerGoi.getSelectedItem().toString();
        long ngayDangKy = System.currentTimeMillis();

        Subscription sub = new Subscription(uid, goi, ngayDangKy);

        db.collection("subscriptions")
                .document(uid)
                .set(sub)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Đăng ký vé tháng thành công!", Toast.LENGTH_SHORT).show();
                    kiemTraGoiHienTai();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void kiemTraGoiHienTai() {
        db.collection("subscriptions")
                .document(uid)
                .get()//
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String goi = doc.getString("goi");
                        txtGoiHienTai.setText("Gói hiện tại: " + goi);
                    } else {
                        txtGoiHienTai.setText("Chưa đăng ký gói nào.");
                    }
                });
    }
}
