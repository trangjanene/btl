package vn.edu.tlu.cse.gogoapp.models;

public class Subscription {
    private String uid;
    private String goi;
    private long ngayDangKy;

    public Subscription() {}

    public Subscription(String uid, String goi, long ngayDangKy) {
        this.uid = uid;
        this.goi = goi;
        this.ngayDangKy = ngayDangKy;
    }

    public String getUid() { return uid; }
    public String getGoi() { return goi; }
    public long getNgayDangKy() { return ngayDangKy; }

    public void setUid(String uid) { this.uid = uid; }
    public void setGoi(String goi) { this.goi = goi; }
    public void setNgayDangKy(long ngayDangKy) { this.ngayDangKy = ngayDangKy; }
}
