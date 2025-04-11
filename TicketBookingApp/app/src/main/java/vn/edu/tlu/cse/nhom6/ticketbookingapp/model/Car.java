package vn.edu.tlu.cse.nhom6.ticketbookingapp.model;

public class Car {
    private int id;
    private String carNumber;
    private int seatCount;
    private String phoneNumber;

    public Car(int id, String carNumber, int seatCount, String phoneNumber) {
        this.id = id;
        this.carNumber = carNumber;
        this.seatCount = seatCount;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}