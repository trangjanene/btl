package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.Date;

public class TraXeActivity extends AppCompatActivity {

    TextView txtThongTinXe;
    Button btnTraXe;

    FirebaseFirestore db;
    String uid;
    DocumentSnapshot rentalDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_xe);

        txtThongTinXe = findViewById(R.id.txtThongTinXe);
        btnTraXe = findViewById(R.id.btnTraXe);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        kiemTraXeDangThue();

        btnTraXe.setOnClickListener(v -> thucHienTraXe());
    }

    private void kiemTraXeDangThue() {
        db.collection("rentals")
                .whereEqualTo("uid", uid)
                .whereEqualTo("isActive", true)
                .limit(1)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        rentalDoc = query.getDocuments().get(0);
                        String bikeId = rentalDoc.getString("bikeId");
                        long start = rentalDoc.getLong("startTime");

                        txtThongTinXe.setText("Bạn đang thuê xe: " + bikeId + "\nBắt đầu: " + new Date(start));
                        btnTraXe.setEnabled(true);
                    } else {
                        txtThongTinXe.setText("Bạn hiện không thuê xe nào.");
                        btnTraXe.setEnabled(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kiểm tra thuê xe", Toast.LENGTH_SHORT).show();
                });
    }

    private void thucHienTraXe() {
        if (rentalDoc == null) return;

        String bikeId = rentalDoc.getString("bikeId");
        String rentalId = rentalDoc.getId();

        // 1. Cập nhật trạng thái bản ghi rental
        DocumentReference rentalRef = db.collection("rentals").document(rentalId);
        rentalRef.update(
                "isActive", false,
                "returnTime", new Date().getTime()
        );

        // 2. Cập nhật xe thành available
        DocumentReference bikeRef = db.collection("bikes").document(bikeId);
        bikeRef.update("status", "available");

        Toast.makeText(this, "Trả xe thành công!", Toast.LENGTH_SHORT).show();
        finish(); // quay về màn hình trước
    }
}
