package vn.edu.tlu.cse.nhom6.ticketbookingapp.model;

public class Payment {
    private int payment_id;
    private int ticket_id;
    private String method;
    private String status;

    public Payment(int payment_id, int ticket_id, String method, String status) {
        this.payment_id = payment_id;
        this.ticket_id = ticket_id;
        this.method = method;
        this.status = status;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
