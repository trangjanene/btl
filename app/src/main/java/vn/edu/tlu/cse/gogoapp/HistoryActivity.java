package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import vn.edu.tlu.cse.gogoapp.models.RentalHistory;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listViewHistory;
    private ArrayAdapter<RentalHistory> historyAdapter;
    private List<RentalHistory> historyList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_thue);

        listViewHistory = findViewById(R.id.listViewHistory);
        historyList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("history")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String bikeId = doc.getString("bikeId"); // <-- bạn cần đảm bảo có trường này trong Firestore
                        String bikeName = doc.getString("bikeName");
                        String price = doc.getString("price");
                        long startTime = doc.getLong("startTime");

                        historyList.add(new RentalHistory(bikeId, bikeName, price, startTime));

                    }

                    historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
                    listViewHistory.setAdapter(historyAdapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải lịch sử thuê", Toast.LENGTH_SHORT).show();
                });
    }
}
