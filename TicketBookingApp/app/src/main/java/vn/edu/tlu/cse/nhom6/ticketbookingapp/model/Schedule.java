package vn.edu.tlu.cse.nhom6.ticketbookingapp.model;

public class Schedule {
    private int scheduleId;
    private String carNumber;    // Biển số xe
    private String startLocation; // Điểm đi
    private String endLocation;   // Điểm đến
    private String departureTime; // Giờ khởi hành
    private String arrivalTime;   // Giờ đến

    public Schedule(int scheduleId, String carNumber, String startLocation, String endLocation, String departureTime, String arrivalTime) {
        this.scheduleId = scheduleId;
        this.carNumber = carNumber;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    // Getter và Setter
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public String getCarNumber() { return carNumber; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }
    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }
    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
}
