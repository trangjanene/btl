package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChiTietThueXeActivity extends AppCompatActivity {

    TextView txtBikeName, txtPrice, txtStatus;
    Button btnThuongThue;
    FirebaseFirestore db;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thue_xe);

        txtBikeName = findViewById(R.id.txtBikeName);
        txtPrice = findViewById(R.id.txtPrice);
        txtStatus = findViewById(R.id.txtStatus);
        btnThuongThue = findViewById(R.id.btnThuongThue);
        db = FirebaseFirestore.getInstance();

        String bikeId = getIntent().getStringExtra("bikeId");

        // Lấy thông tin xe từ Firestore
        db.collection("bike").document(bikeId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String pricePerHour = documentSnapshot.getString("price_per_hour");
                        String status = documentSnapshot.getString("status");

                        txtBikeName.setText(name);
                        txtPrice.setText("Giá thuê: " + pricePerHour + " VNĐ/giờ");
                        txtStatus.setText("Trạng thái: " + status);

                        // Nếu xe có trạng thái "available", cho phép thuê
                        if (status.equals("available")) {
                            btnThuongThue.setEnabled(true);
                            btnThuongThue.setOnClickListener(v -> {
                                // Thực hiện thuê xe
                                Toast.makeText(ChiTietThueXeActivity.this, "Bạn đã thuê xe thành công!", Toast.LENGTH_SHORT).show();
                                // Cập nhật trạng thái xe sau khi thuê
                                db.collection("bike").document(bikeId).update("status", "rented");
                                finish(); // Quay lại màn hình trước
                            });
                        } else {
                            btnThuongThue.setEnabled(false);
                            Toast.makeText(ChiTietThueXeActivity.this, "Xe đã được thuê!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
