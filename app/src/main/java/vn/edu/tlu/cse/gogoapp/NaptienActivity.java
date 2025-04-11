package vn.edu.tlu.cse.gogoapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NaptienActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;
    //
    private EditText edtSotien;
    private Button btnTiennap;
    private TextView txtSotien;

    int tien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naptien);

        txtSotien = findViewById(R.id.balance);
        edtSotien = findViewById(R.id.edtSotien);
        btnTiennap = findViewById(R.id.btnTiennap);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadSoTienConLai();
        btnTiennap.setOnClickListener(v -> naptien());

    }

    private void loadSoTienConLai() {
        db.collection("users")
                .whereEqualTo("sotien", userId)
                .whereEqualTo("ended", false)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        String tien = doc.getString("sotien");
                        txtSotien.setText(tien);
                    } else {
                       // txtThongTinXe.setText("Bạn hiện không có xe nào đang thuê.");
                    }
                })
                .addOnFailureListener(e -> {
                    //txtThongTinXe.setText("Lỗi khi kiểm tra thông tin xe.");
                   // btnTraXe.setEnabled(false);
                });
    }

    private void naptien() {
        String sotien = edtSotien.getText().toString().trim();
        /*if (bikeIdDangThue == null || historyId == null) {
            Toast.makeText(this, "Không có xe để trả", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // Cập nhật ended = true trong history
        db.collection("user").document("sotien")
                .update("ended", true);
    }
}