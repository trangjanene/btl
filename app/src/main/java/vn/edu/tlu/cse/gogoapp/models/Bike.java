package vn.edu.tlu.cse.gogoapp.models;

import java.io.Serializable;

public class Bike implements Serializable {
    private String id; // Thêm thuộc tính id
    private String name;
    private String pricePerHour;
    private String status;

    public Bike(String id, String name, String pricePerHour, String status) {
        this.id = id; // Khởi tạo id
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.status = status;
    }

    // Getter và Setter cho id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Các getter và setter khác
    public String getName() {
        return name;
    }

    public String getPricePerHour() {
        return pricePerHour;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name + " - " + pricePerHour + " VNĐ/giờ";
    }
}
