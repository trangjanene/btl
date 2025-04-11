package vn.edu.tlu.cse.gogoapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RentalHistory {
    private String bikeId;
    private String bikeName;
    private String price;
    private long startTime;

    public RentalHistory(String bikeId, String bikeName, String price, long startTime) {
        this.bikeId = bikeId;
        this.bikeName = bikeName;
        this.price = price;
        this.startTime = startTime;
    }

    public String getBikeId() {
        return bikeId;
    }

    public String getBikeName() {
        return bikeName;
    }

    public String getPrice() {
        return price;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateStr = sdf.format(new Date(startTime));
        return "Xe: " + bikeName + "\nGiá: " + price + "\nNgày: " + dateStr;
    }
}
