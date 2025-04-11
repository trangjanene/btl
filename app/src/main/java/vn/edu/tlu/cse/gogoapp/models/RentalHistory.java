package vn.edu.tlu.cse.gogoapp.models;

public class RentalHistory {
    private String bikeName;
    private String price;
    private long startTime;

    public RentalHistory(String bikeName, String price, long startTime) {
        this.bikeName = bikeName;
        this.price = price;
        this.startTime = startTime;
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

}
