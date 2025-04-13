package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChiTietThueXeActivity extends AppCompatActivity {

    TextView txtBikeName, txtPrice, txtStatus;
    Button btnThuongThue;
    FirebaseFirestore db;

    String bikeId, name, pricePerHour, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thue_xe);

        txtBikeName = findViewById(R.id.txtBikeName);
        txtPrice = findViewById(R.id.txtPrice);
        txtStatus = findViewById(R.id.txtStatus);
        btnThuongThue = findViewById(R.id.btnThuongThue);
        db = FirebaseFirestore.getInstance();

        bikeId = getIntent().getStringExtra("bikeId");

        loadBikeDetails();
    }

    private void loadBikeDetails() {
        db.collection("bike").document(bikeId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        name = documentSnapshot.getString("name");
                        pricePerHour = documentSnapshot.getString("price_per_hour");
                        status = documentSnapshot.getString("status");

                        txtBikeName.setText(name);
                        txtPrice.setText("Giá thuê: " + pricePerHour + " VNĐ/giờ");
                        txtStatus.setText("Trạng thái: " + status);

                        if ("available".equals(status)) {
                            btnThuongThue.setEnabled(true);
                            btnThuongThue.setOnClickListener(v -> checkUserHasActiveRental());
                        } else {
                            btnThuongThue.setEnabled(false);
                            Toast.makeText(this, "Xe đã được thuê!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserHasActiveRental() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("history")
                .whereEqualTo("userId", userId)
                .whereEqualTo("ended", false)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        Toast.makeText(this, "Bạn đang có xe chưa trả!", Toast.LENGTH_SHORT).show();
                    } else {
                        rentBike(userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kiểm tra trạng thái thuê xe", Toast.LENGTH_SHORT).show();
                });
    }

    private void rentBike(String userId) {
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Map<String, Object> historyData = new HashMap<>();
        historyData.put("userId", userId);
        historyData.put("bikeId", bikeId);
        historyData.put("bikeName", name);
        historyData.put("price", pricePerHour);
        historyData.put("date", date);
        historyData.put("startTime", System.currentTimeMillis());
        historyData.put("ended", false);

        db.collection("bike").document(bikeId)
                .update("status", "rented")
                .addOnSuccessListener(aVoid -> {
                    db.collection("history")
                            .add(historyData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Thuê xe thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi khi lưu lịch sử thuê", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi cập nhật trạng thái xe", Toast.LENGTH_SHORT).show();
                });
    }
}
