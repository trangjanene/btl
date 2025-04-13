package vn.edu.tlu.cse.gogoapp.models;

public class RentalHistory {
    private String bikeName;  // Tên xe
    private String price;     // Giá thuê
    private Long startTime;   // Thời gian bắt đầu thuê
    private Long endTime;     // Thời gian kết thúc thuê
    private Long totalAmount; // Tổng số tiền thanh toán

    // Constructor
    public RentalHistory(String bikeName, String price, Long startTime, Long endTime, Long totalAmount) {
        this.bikeName = bikeName;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
    }

    // Getter và Setter cho các thuộc tính
    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
