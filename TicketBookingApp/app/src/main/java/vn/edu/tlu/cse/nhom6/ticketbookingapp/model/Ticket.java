//package vn.edu.tlu.cse.nhom6.ticketbookingapp.model;
//
//public class Ticket {
//    private int ticket_id;
//    private int ticket_user_id;
//    private int ticket_schedule_id;
//    private int seat_number;
//    private int price;
//    private String status;
//
//    public Ticket(int ticket_id, int ticket_user_id, int ticket_schedule_id, int seat_number, int price, String status) {
//        this.ticket_id = ticket_id;
//        this.ticket_user_id = ticket_user_id;
//        this.ticket_schedule_id = ticket_schedule_id;
//        this.seat_number = seat_number;
//        this.price = price;
//        this.status = status;
//    }
//
//    public int getTicket_id() {
//        return ticket_id;
//    }
//
//    public void setTicket_id(int ticket_id) {
//        this.ticket_id = ticket_id;
//    }
//
//    public int getTicket_user_id() {
//        return ticket_user_id;
//    }
//
//    public void setTicket_user_id(int ticket_user_id) {
//        this.ticket_user_id = ticket_user_id;
//    }
//
//    public int getTicket_schedule_id() {
//        return ticket_schedule_id;
//    }
//
//    public void setTicket_schedule_id(int ticket_schedule_id) {
//        this.ticket_schedule_id = ticket_schedule_id;
//    }
//
//    public int getSeat_number() {
//        return seat_number;
//    }
//
//    public void setSeat_number(int seat_number) {
//        this.seat_number = seat_number;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}
package vn.edu.tlu.cse.nhom6.ticketbookingapp.model;

import java.io.Serializable;

public class Ticket implements Serializable {
    private int ticket_id;
    private int ticket_user_id;
    private int ticket_schedule_id;
    private int seat_number;
    private int price;
    private String status;
    private String departureTime; // Thêm trường này
    private String startLocation; // Thêm trường này
    private String endLocation;   // Thêm trường này

    public Ticket(int ticket_id, int ticket_user_id, int ticket_schedule_id, int seat_number, int price, String status) {
        this.ticket_id = ticket_id;
        this.ticket_user_id = ticket_user_id;
        this.ticket_schedule_id = ticket_schedule_id;
        this.seat_number = seat_number;
        this.price = price;
        this.status = status;
    }

    // Getters và Setters
    public int getTicket_id() { return ticket_id; }
    public void setTicket_id(int ticket_id) { this.ticket_id = ticket_id; }
    public int getTicket_user_id() { return ticket_user_id; }
    public void setTicket_user_id(int ticket_user_id) { this.ticket_id = ticket_user_id; }
    public int getTicket_schedule_id() { return ticket_schedule_id; }
    public void setTicket_schedule_id(int ticket_schedule_id) { this.ticket_schedule_id = ticket_schedule_id; }
    public int getSeat_number() { return seat_number; }
    public void setSeat_number(int seat_number) { this.seat_number = seat_number; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }
    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }
}