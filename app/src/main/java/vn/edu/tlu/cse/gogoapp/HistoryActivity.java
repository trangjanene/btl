package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.gogoapp.adapters.RentalAdapter;
import vn.edu.tlu.cse.gogoapp.models.RentalHistory;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RentalAdapter rentalAdapter;
    private List<RentalHistory> rentalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_thue); // Layout bạn đã tạo

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rentalList = new ArrayList<>();
        rentalAdapter = new RentalAdapter(rentalList);
        recyclerView.setAdapter(rentalAdapter);

        loadRentalHistory();
    }

    private void loadRentalHistory() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("history")
                .whereEqualTo("userId", userId)
                .whereEqualTo("ended", true)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    rentalList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String bikeName = doc.getString("bikeName");
                        String price = doc.getString("price");
                        Long startTime = doc.getLong("startTime");

                        if (bikeName != null && price != null && startTime != null) {
                            rentalList.add(new RentalHistory(bikeName, price, startTime));
                        }
                    }
                    rentalAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải lịch sử: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
