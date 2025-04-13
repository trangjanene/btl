package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.edu.tlu.cse.gogoapp.models.Bike;

public class ThueXeActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Bike> bikeAdapter;
    private List<Bike> bikeList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thue_xe);

        listView = findViewById(R.id.listView);
        db = FirebaseFirestore.getInstance();

        loadAvailableBikes();
    }

    private void loadAvailableBikes() {
        db.collection("bike").get()
                .addOnSuccessListener(querySnapshot -> {
                    bikeList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String pricePerHour = document.getString("price_per_hour");
                        String status = document.getString("status");

                        Log.d("DEBUG_XE", "Tìm thấy xe: " + name + " - Trạng thái: " + status);

                        if ("available".equals(status)) {
                            bikeList.add(new Bike(id, name, pricePerHour, status));
                        }
                    }

                    bikeAdapter = new ArrayAdapter<>(ThueXeActivity.this, android.R.layout.simple_list_item_1, bikeList);
                    listView.setAdapter(bikeAdapter);

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        Bike selectedBike = bikeList.get(position);
                        checkUserHasActiveRental(selectedBike);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ThueXeActivity.this, "Lỗi tải dữ liệu xe", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUserHasActiveRental(Bike bike) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("history")
                .whereEqualTo("userId", userId)
                .whereEqualTo("ended", false)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        Toast.makeText(ThueXeActivity.this, "Bạn đang có xe chưa trả!", Toast.LENGTH_SHORT).show();
                    } else {
                        proceedToRent(bike);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ThueXeActivity.this, "Lỗi kiểm tra trạng thái thuê xe", Toast.LENGTH_SHORT).show();
                });
    }

    private void proceedToRent(Bike bike) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String bikeName = bike.getName();
        String price = bike.getPricePerHour();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Map<String, Object> historyData = new HashMap<>();
        historyData.put("userId", userId);
        historyData.put("bikeId", bike.getId());
        historyData.put("bikeName", bikeName);
        historyData.put("price", price);
        historyData.put("date", date);
        historyData.put("startTime", System.currentTimeMillis());
        historyData.put("ended", false);

        db.collection("bike").document(bike.getId())
                .update("status", "rented")
                .addOnSuccessListener(aVoid -> {
                    db.collection("history")
                            .add(historyData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(ThueXeActivity.this, "Thuê xe thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ThueXeActivity.this, "Lỗi khi lưu lịch sử thuê", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ThueXeActivity.this, "Lỗi cập nhật trạng thái xe", Toast.LENGTH_SHORT).show();
                });
    }
}
