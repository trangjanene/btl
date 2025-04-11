package vn.edu.tlu.cse.gogoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import vn.edu.tlu.cse.gogoapp.R;
import vn.edu.tlu.cse.gogoapp.models.Bike;
import vn.edu.tlu.cse.gogoapp.ThanhToanActivity;




import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.BikeViewHolder> {

    private List<Bike> bikeList;
    private Context context;
    private FirebaseFirestore db;

    public BikeAdapter(List<Bike> bikeList, Context context) {
        this.bikeList = bikeList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }
//
    @Override
    public BikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bike, parent, false);
        return new BikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BikeViewHolder holder, int position) {
        Bike bike = bikeList.get(position);
        holder.txtBikeName.setText(bike.getName());
        holder.txtPrice.setText("Giá thuê: " + bike.getPricePerHour() + " VNĐ/giờ");

        holder.btnSelect.setOnClickListener(v -> {
            // Cập nhật trạng thái xe thành "rented"
            db.collection("bike").document(bike.getId())
                    .update("status", "rented")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Đã chọn xe " + bike.getName() + " để thuê!", Toast.LENGTH_SHORT).show();
                        // Mở màn hình thanh toán hoặc tính tiền
                        Intent intent = new Intent(context, ThanhToanActivity.class);
                        intent.putExtra("bikeId", bike.getId());
                        context.startActivity(intent);
                    });
        });
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public class BikeViewHolder extends RecyclerView.ViewHolder {
        TextView txtBikeName, txtPrice;
        Button btnSelect;

        public BikeViewHolder(View itemView) {
            super(itemView);
            txtBikeName = itemView.findViewById(R.id.txtBikeName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnSelect = itemView.findViewById(R.id.btnSelect);
        }
    }
}
