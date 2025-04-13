package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
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
    private boolean hasSubscription = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_xe);

        txtThongTinXe = findViewById(R.id.txtThongTinXe);
        btnTraXe = findViewById(R.id.btnTraXe);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnTraXe.setEnabled(false);

        kiemTraXeDangThue();

        kiemTraGoiHienTai();

        btnTraXe.setOnClickListener(v -> {
            if (hasSubscription) {

                Toast.makeText(this, "Bạn đã có vé tháng, không cần thanh toán!", Toast.LENGTH_SHORT).show();
                traXe();
            } else {

                traXe();
            }
        });
    }

    private void kiemTraGoiHienTai() {
        db.collection("subscriptions")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        hasSubscription = true;
                        Toast.makeText(this, "Bạn đã có vé tháng, không cần thanh toán khi trả xe!", Toast.LENGTH_SHORT).show();
                    } else {
                        hasSubscription = false;
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi kiểm tra vé tháng", Toast.LENGTH_SHORT).show();
                    hasSubscription = false;
                });
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

                        txtThongTinXe.setText("Xe: " + bikeName + "\nGiá: " + price + " đ/giờ\nNgày thuê: " + date);
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

        long endTime = System.currentTimeMillis();

        db.collection("history").document(historyId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long startTime = documentSnapshot.getLong("startTime");
                        String priceStr = documentSnapshot.getString("price");

                        if (startTime == null || priceStr == null) {
                            Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        long durationMillis = endTime - startTime;
                        long hours = durationMillis / (1000 * 60 * 60);
                        if (hours == 0) hours = 1;

                        long pricePerHour = Long.parseLong(priceStr);
                        long totalAmount = hours * pricePerHour;

                        if (hasSubscription) {
                            updateHistoryAndReturnBike(endTime, 0);
                        } else {
                            trừTiềnVàoVí(totalAmount, endTime);
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy dữ liệu thuê xe", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi đọc dữ liệu thuê xe", Toast.LENGTH_SHORT).show();
                });
    }

    private void trừTiềnVàoVí(long totalAmount, long endTime) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(userDoc -> {
            Long currentWallet = userDoc.getLong("wallet");
            if (currentWallet == null) currentWallet = 0L;

            long remaining = currentWallet - totalAmount;

            if (remaining < 0) {
                Toast.makeText(this, "Số dư không đủ để trả xe!", Toast.LENGTH_SHORT).show();
                return;
            }

            userRef.update("wallet", remaining)
                    .addOnSuccessListener(aVoid -> {
                        // Cập nhật lịch sử và trả xe
                        updateHistoryAndReturnBike(endTime, totalAmount);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi cập nhật ví", Toast.LENGTH_SHORT).show();
                    });

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Không lấy được thông tin ví", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateHistoryAndReturnBike(long endTime, long totalAmount) {
        db.collection("history").document(historyId)
                .update(
                        "ended", true,
                        "endTime", endTime,
                        "totalAmount", totalAmount
                )
                .addOnSuccessListener(aVoid -> {
                    db.collection("bike").document(bikeIdDangThue)
                            .update("status", "available")
                            .addOnSuccessListener(aVoid2 -> {
                                Toast.makeText(this, "Trả xe thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi cập nhật trạng thái xe", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi cập nhật lịch sử", Toast.LENGTH_SHORT).show();
                });
    }
}
