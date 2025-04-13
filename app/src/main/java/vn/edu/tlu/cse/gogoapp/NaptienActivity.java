package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class NaptienActivity extends AppCompatActivity {

    private TextView balanceTextView;
    private EditText edtSotien;
    private Button btnTiennap;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naptien);

        // Khởi tạo các view
        balanceTextView = findViewById(R.id.balance);
        edtSotien = findViewById(R.id.edtSotien);
        btnTiennap = findViewById(R.id.btnTiennap);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadBalance();

        btnTiennap.setOnClickListener(v -> napTien());
    }

    private void loadBalance() {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long balance = documentSnapshot.getLong("wallet");
                        if (balance == null) {
                            balance = 0L;
                        }
                        // Hiển thị số dư
                        balanceTextView.setText(balance + " đ");
                    } else {
                        Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải số dư: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void napTien() {
        String amountStr = edtSotien.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        long amount = Long.parseLong(amountStr);

        if (amount <= 0) {
            Toast.makeText(this, "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long currentBalance = documentSnapshot.getLong("wallet");
                        if (currentBalance == null) {
                            currentBalance = 0L;
                        }

                        long newBalance = currentBalance + amount;

                        db.collection("users").document(userId)
                                .update("wallet", newBalance)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Nạp tiền thành công!", Toast.LENGTH_SHORT).show();
                                    loadBalance();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Lỗi khi nạp tiền: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi lấy thông tin ví: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
