package vn.edu.tlu.cse.gogoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import vn.edu.tlu.cse.gogoapp.R;
import vn.edu.tlu.cse.gogoapp.models.RentalHistory;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.ViewHolder> {

    private List<RentalHistory> rentalList;

    public RentalAdapter(List<RentalHistory> rentalList) {
        this.rentalList = rentalList;
    }
//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rental_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RentalHistory rental = rentalList.get(position);
        holder.txtBikeName.setText("Xe: " + rental.getBikeName());
        holder.txtPrice.setText("Giá: " + rental.getPrice());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.txtDate.setText("Ngày: " + sdf.format(new Date(rental.getStartTime())));

    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBikeName, txtPrice, txtDate;

        public ViewHolder(View itemView) {
            super(itemView);
            txtBikeName = itemView.findViewById(R.id.txtBikeName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
