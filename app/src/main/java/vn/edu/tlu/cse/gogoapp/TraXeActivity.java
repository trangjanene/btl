package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class TraXeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;

    private TextView txtThongTinXe;
    private Button btnTraXe;

    private String bikeIdDangThue = null;
    private String historyId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_xe);

        txtThongTinXe = findViewById(R.id.txtThongTinXe);
        btnTraXe = findViewById(R.id.btnTraXe);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnTraXe.setEnabled(false); // Tạm tắt nút cho đến khi kiểm tra xong

        kiemTraXeDangThue();

        btnTraXe.setOnClickListener(v -> traXe());
    }

    private void kiemTraXeDangThue() {
        db.collection("history")
                .whereEqualTo("userId", userId)
                .whereEqualTo("ended", false)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        bikeIdDangThue = doc.getString("bikeId");
                        historyId = doc.getId();

                        String bikeName = doc.getString("bikeName");
                        String price = doc.getString("price");
                        String date = doc.getString("date");

                        txtThongTinXe.setText("Xe: " + bikeName + "\nGiá: " + price + "\nNgày thuê: " + date);
                        btnTraXe.setEnabled(true);
                    } else {
                        txtThongTinXe.setText("Bạn hiện không có xe nào đang thuê.");
                        btnTraXe.setEnabled(false);
                    }
                })
                .addOnFailureListener(e -> {
                    txtThongTinXe.setText("Lỗi khi kiểm tra thông tin xe.");
                    btnTraXe.setEnabled(false);
                });
    }

    private void traXe() {
        if (bikeIdDangThue == null || historyId == null) {
            Toast.makeText(this, "Không có xe để trả", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật ended = true trong history
        db.collection("history").document(historyId)
                .update("ended", true)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật trạng thái xe thành available
                    db.collection("bike").document(bikeIdDangThue)
                            .update("status", "available")
                            .addOnSuccessListener(aVoid2 -> {
                                Toast.makeText(this, "Trả xe thành công!", Toast.LENGTH_SHORT).show();
                                finish(); // Quay về màn hình trước
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi cập nhật trạng thái xe", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi cập nhật lịch sử thuê", Toast.LENGTH_SHORT).show();
                });
    }
}
