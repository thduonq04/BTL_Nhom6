package vn.edu.tlu.cse.nhom6.ticketbookingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Car;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Route;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Schedule;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Ticket;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    //ten database
    private static final String DATABASE_NAME = "ticketbooking.db";
    private static final int DATABASE_VERSION = 6;

    //bang User:

    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_PHONE_NUMBER = "phone_number";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";
    private static final String COL_FULLNAME = "full_name";

    //bang xe:
    private static final String TABLE_CARS = "cars";
    private static final String COL_CAR_ID = "car_id";
    private static final String COL_CAR_NUMBER = "car_number"; //bien so xe
    private static final String COL_SEAT_COUNT = "seat_count"; //so cho ngoi
    private static final String COL_CAR_PHONE_NUMBER = "car_phone_number"; //So dien thoai cua xe(moi xe co 1 sdt rieng)

    //bang tuyen duong (route):

    private static final String TABLE_ROUTES = "routes";
    private static final String COL_ROUTE_ID = "route_id";
    private static final String COL_START_LOCATION = "start_location"; //diem di
    private static final String COL_END_LOCATION = "end_location";//diem den
    private static final String COL_DISTANCE = "distance"; // quang duong di

    private static final String TABLE_SCHEDULES = "schedules";
    private static final String COL_SCHEDULE_ID = "schedule_id";
    private static final String COL_SCHEDULE_CAR_ID = "car_id";
    private static final String COL_SCHEDULE_ROUTE_ID = "route_id";
    private static final String COL_DEPARTURE_TIME = "departure_time";
    private static final String COL_ARRIVAL_TIME = "arrival_time";

    //bang ve:
    private static final String TABLE_TICKETS = "tickets";
    private static final String COL_TICKET_ID = "ticket_id";
    private static final String COL_TICKET_USER_ID = "user_id";
    private static final String COL_TICKET_SCHEDULE_ID = "schedule_id";
    private static final String COL_TICKET_SEAT_NUMBER = "seat_number"; //so ghe
    private static final String COL_TICKET_PRICE = "price";
    private static final String COL_TICKET_STATUS = "status"; //trang thai: da dat, da huy, da thanh toan

    //bang thanh toan:
    private static final String TABLE_PAYMENTS = "payments";
    private static final String COL_PAYMENT_ID = "payment_id";
    private static final String COL_PAYMENT_TICKET_ID = "ticket_id";

    private static final String COL_PAYMENT_METHOD = "method"; //phuong thuc thanh toan: tien mat, chuyen khoan
    private static final String COL_PAYMENT_STATUS = "status"; //trang thai thanh toan: thanh cong, that bai

    //bang danh gia:
    private static final String TABLE_REVIEWS = "reviews";
    private static final String COL_REVIEW_ID = "review_id";
    private static final String COL_REVIEW_USER_ID = "user_id";
    private static final String COL_REVIEW_RATING = "rating";
    private static final String COL_REVIEW_COMMENT = "comment";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PHONE_NUMBER + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_ROLE + " TEXT," +
                COL_FULLNAME + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_CARS + " (" +
                COL_CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CAR_NUMBER + " TEXT," +
                COL_SEAT_COUNT + " INTEGER," +
                COL_CAR_PHONE_NUMBER + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ROUTES + " (" +
                COL_ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_START_LOCATION + " TEXT," +
                COL_END_LOCATION + " TEXT," +
                COL_DISTANCE + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_SCHEDULES + " (" +
                COL_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SCHEDULE_CAR_ID + " INTEGER," +
                COL_SCHEDULE_ROUTE_ID + " INTEGER," +
                COL_DEPARTURE_TIME + " DATE," +
                COL_ARRIVAL_TIME + " DATE, " +
                "FOREIGN KEY (" + COL_SCHEDULE_CAR_ID + ") REFERENCES " + TABLE_CARS + "(" + COL_CAR_ID + ")," +
                "FOREIGN KEY (" + COL_SCHEDULE_ROUTE_ID + ") REFERENCES " + TABLE_ROUTES + "(" + COL_ROUTE_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_TICKETS + " (" +
                COL_TICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TICKET_USER_ID + " INTEGER," +
                COL_TICKET_SCHEDULE_ID + " INTEGER," +
                COL_TICKET_SEAT_NUMBER + " INTEGER," +
                COL_TICKET_PRICE + " INTEGER," +
                COL_TICKET_STATUS + " TEXT," +
                "FOREIGN KEY (" + COL_TICKET_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + ")," +
                "FOREIGN KEY (" + COL_TICKET_SCHEDULE_ID + ") REFERENCES " + TABLE_SCHEDULES + "(" + COL_SCHEDULE_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_PAYMENTS + " (" +
                COL_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PAYMENT_TICKET_ID + " INTEGER," +
                COL_PAYMENT_METHOD + " TEXT," +
                COL_PAYMENT_STATUS + " TEXT," +
                "FOREIGN KEY (" + COL_PAYMENT_TICKET_ID + ") REFERENCES " + TABLE_TICKETS + "(" + COL_TICKET_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_REVIEWS + " (" +
                COL_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REVIEW_USER_ID + " INTEGER," +
                COL_REVIEW_RATING + " INTEGER," +
                COL_REVIEW_COMMENT + " TEXT," +
                "FOREIGN KEY (" + COL_REVIEW_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");

        //chèn dữ liệu mẫu:
        insertUser(db, "0364422876", "admin1@gmail.com", "123456", "Admin", "Admin1");
        insertUser(db, "0364422877", "admin2@gmail.com", "123abc", "Admin", "Admin2");
        insertUser(db, "0364422866", "admin3@gmail.com", "123xyz", "Admin", "Admin3");
        insertUser(db, "0364433788", "staff1@gmail.com", "456789", "Nhân viên", "Staff1");
        insertUser(db, "0364433755", "staff2@gmail.com", "456bcd", "Nhân viên", "Staff2");
        insertUser(db, "0912345678", "customer1@gmail.com", "pass123", "Khách hàng", "Customer One");
        insertUser(db, "0912345679", "customer2@gmail.com", "pass456", "Khách hàng", "Customer Two");
        insertUser(db, "0912345680", "customer3@gmail.com", "pass789", "Khách hàng", "Customer Three");


        db.execSQL("INSERT INTO " + TABLE_CARS + " (" + COL_CAR_NUMBER + ", " + COL_SEAT_COUNT + ", " + COL_CAR_PHONE_NUMBER + ") VALUES ('29A-12345', 40, '0909123456')");
        db.execSQL("INSERT INTO " + TABLE_CARS + " (" + COL_CAR_NUMBER + ", " + COL_SEAT_COUNT + ", " + COL_CAR_PHONE_NUMBER + ") VALUES ('29B-12335', 50, '0909123455')");
        db.execSQL("INSERT INTO " + TABLE_ROUTES + " (" + COL_START_LOCATION + ", " + COL_END_LOCATION + ", " + COL_DISTANCE + ") VALUES ('Hà Nội', 'Hải Phòng', 120)");
        db.execSQL("INSERT INTO " + TABLE_ROUTES + " (" + COL_START_LOCATION + ", " + COL_END_LOCATION + ", " + COL_DISTANCE + ") VALUES ('Hà Nam', 'Hải Phòng', 100)");
        db.execSQL("INSERT INTO " + TABLE_SCHEDULES + " (" + COL_SCHEDULE_CAR_ID + ", " + COL_SCHEDULE_ROUTE_ID + ", " + COL_DEPARTURE_TIME + ", " + COL_ARRIVAL_TIME + ") VALUES (1, 1, '2025-04-10 08:00', '2025-04-10 10:00')");
        db.execSQL("INSERT INTO " + TABLE_SCHEDULES + " (" + COL_SCHEDULE_CAR_ID + ", " + COL_SCHEDULE_ROUTE_ID + ", " + COL_DEPARTURE_TIME + ", " + COL_ARRIVAL_TIME + ") VALUES (2, 2, '2025-04-11 09:00', '2025-04-11 11:00')");



        // Dữ liệu mẫu cho bảng Tickets
        insertTicket(db, 6, 1, 5, 120000, "Đã đặt");
        insertTicket(db, 7, 2, 10, 90000, "Đã thanh toán");
        insertTicket(db, 8, 2, 1, 150000, "Đã hủy");

        // Dữ liệu mẫu cho bảng Payments
        insertPayment(db, 1, "Tiền mặt", "Thành công");
        insertPayment(db, 2, "Chuyển khoản", "Thành công");
        insertPayment(db, 3, "Tiền mặt", "Thất bại");

        // Dữ liệu mẫu cho bảng Reviews
        insertReview(db, 6, 5, "Dịch vụ rất tốt");
        insertReview(db, 7, 4, "Chuyến đi khá thoải mái");
        insertReview(db, 8, 2, "Xe chạy hơi nhanh");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        onCreate(db);
    }

    //add customer:
    public Boolean insertCustomer(String phone, String email, String password, String full_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone_number", phone);
        values.put("email", email);
        values.put("password", password);
        values.put("role", "Khách hàng");
        values.put("full_name", full_name);

        long insert = db.insert("users", null, values);
        db.close();
        if (insert == -1) {
            return false;
        }
        return true;

    }

    //    kiem tra dang nhap:
    public boolean checkUser2(String phone_number, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_PHONE_NUMBER + " = ? AND " + COL_PASSWORD + " = ?", new String[]{phone_number, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;

    }


    //check đăng kí
    public boolean checkUser1(String phone_number) {
        if (phone_number == null || phone_number.isEmpty()) {
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                    COL_PHONE_NUMBER + " = ? ", new String[]{phone_number});
            exists = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return exists;
    }


    public String checkRole(String phone_number, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_PHONE_NUMBER + " = ? AND " + COL_PASSWORD + " = ?", new String[]{phone_number, password});
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)); //lay vai tro nguoi dung thong qua index
            cursor.close();
            db.close();
            return role;
        }
        cursor.close();
        db.close();
        return null;
    }

    private void insertUser(SQLiteDatabase db, String phone_number, String email, String password, String role, String full_name) {
        ContentValues values = new ContentValues();
        values.put(COL_PHONE_NUMBER, phone_number);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);
        values.put(COL_FULLNAME, full_name);
        db.insert(TABLE_USERS, null, values);
    }

    private void insertCar(SQLiteDatabase db, String carNumber, int seatCount, String phone) {
        ContentValues values = new ContentValues();
        values.put(COL_CAR_NUMBER, carNumber);
        values.put(COL_SEAT_COUNT, seatCount);
        values.put(COL_CAR_PHONE_NUMBER, phone);
        db.insert(TABLE_CARS, null, values);
    }

    private void insertRoute(SQLiteDatabase db, String startLocation, String endLocation, int distance) {
        ContentValues values = new ContentValues();
        values.put(COL_START_LOCATION, startLocation);
        values.put(COL_END_LOCATION, endLocation);
        values.put(COL_DISTANCE, distance);
        db.insert(TABLE_ROUTES, null, values);
    }

    private void insertSchedule(SQLiteDatabase db, int carId, int routeId, String departure, String arrival) {
        ContentValues values = new ContentValues();
        values.put(COL_SCHEDULE_CAR_ID, carId);
        values.put(COL_SCHEDULE_ROUTE_ID, routeId);
        values.put(COL_DEPARTURE_TIME, departure);
        values.put(COL_ARRIVAL_TIME, arrival);
        db.insert(TABLE_SCHEDULES, null, values);
    }

    private void insertTicket(SQLiteDatabase db, int userId, int scheduleId, int seat, int price, String status) {
        ContentValues values = new ContentValues();
        values.put(COL_TICKET_USER_ID, userId);
        values.put(COL_TICKET_SCHEDULE_ID, scheduleId);
        values.put(COL_TICKET_SEAT_NUMBER, seat);
        values.put(COL_TICKET_PRICE, price);
        values.put(COL_TICKET_STATUS, status);
        db.insert(TABLE_TICKETS, null, values);
    }

    private void insertPayment(SQLiteDatabase db, int ticketId, String method, String status) {
        ContentValues values = new ContentValues();
        values.put(COL_PAYMENT_TICKET_ID, ticketId);
        values.put(COL_PAYMENT_METHOD, method);
        values.put(COL_PAYMENT_STATUS, status);
        db.insert(TABLE_PAYMENTS, null, values);
    }

    private void insertReview(SQLiteDatabase db, int userId, int rating, String comment) {
        ContentValues values = new ContentValues();
        values.put(COL_REVIEW_USER_ID, userId);
        values.put(COL_REVIEW_RATING, rating);
        values.put(COL_REVIEW_COMMENT, comment);
        db.insert(TABLE_REVIEWS, null, values);
    }


    //quan ly Staff
    public long addStaff(String phoneNumber, String email, String password, String fullname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PHONE_NUMBER, phoneNumber);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, "Nhân viên"); // Vai trò mặc định là Nhân viên
        values.put(COL_FULLNAME, fullname);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean updateStaff(int userId, String phoneNumber, String email, String password, String fullname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PHONE_NUMBER, phoneNumber);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, "Nhân viên");
        values.put(COL_FULLNAME, fullname);

        int rowsAffected = db.update(TABLE_USERS, values, COL_USER_ID + " = ? AND " + COL_ROLE + " = ?",
                new String[]{String.valueOf(userId), "Nhân viên"});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteStaff(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USERS, COL_USER_ID + " = ? AND " + COL_ROLE + " = ?",
                new String[]{String.valueOf(userId), "Nhân viên"});
        db.close();
        return rowsAffected > 0;
    }

    public List<User> getAllStaff() {
        List<User> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_ROLE + " = ?",
                new String[]{"Nhân viên"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User employee = new User();
                employee.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
                employee.setPhone_number(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBER)));
                employee.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                employee.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
                employee.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                employee.setFull_name(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)));
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return employeeList;
    }

    public List<User> getStaffByFullname(String fullname) {
        List<User> employeeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_FULLNAME + " LIKE ? AND " + COL_ROLE + " = ?",
                new String[]{"%" + fullname + "%", "Nhân viên"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User employee = new User();
                employee.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
                employee.setPhone_number(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBER)));
                employee.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                employee.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
                employee.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                employee.setFull_name(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)));
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return employeeList;
    }


    //quan ly customer
    public long addCustomer(String phoneNumber, String email, String password, String fullname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PHONE_NUMBER, phoneNumber);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, "Khách hàng"); // Vai trò mặc định là Khách hàng
        values.put(COL_FULLNAME, fullname);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean updateCustomer(int userId, String phoneNumber, String email, String password, String fullname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PHONE_NUMBER, phoneNumber);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, "Khách hàng");
        values.put(COL_FULLNAME, fullname);

        int rowsAffected = db.update(TABLE_USERS, values, COL_USER_ID + " = ? AND " + COL_ROLE + " = ?",
                new String[]{String.valueOf(userId), "Khách hàng"});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteCustomer(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USERS, COL_USER_ID + " = ? AND " + COL_ROLE + " = ?",
                new String[]{String.valueOf(userId), "Khách hàng"});
        db.close();
        return rowsAffected > 0;
    }

    public List<User> getAllCustomers() {
        List<User> customerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_ROLE + " = ?",
                new String[]{"Khách hàng"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User customer = new User();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
                customer.setPhone_number(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBER)));
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                customer.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
                customer.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                customer.setFull_name(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)));
                customerList.add(customer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return customerList;
    }

    public List<User> getCustomersByFullname(String fullname) {
        List<User> customerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_FULLNAME + " LIKE ? AND " + COL_ROLE + " = ?",
                new String[]{"%" + fullname + "%", "Khách hàng"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User customer = new User();
                customer.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
                customer.setPhone_number(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBER)));
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                customer.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
                customer.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                customer.setFull_name(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)));
                customerList.add(customer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return customerList;
    }

    //quan ly xe
    public List<Car> getAllCars() {
        List<Car> carList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CARS, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Car car = new Car(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_NUMBER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEAT_COUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_PHONE_NUMBER))
                );
                carList.add(car);
            } while (cursor.moveToNext());
            cursor.close();

        }
        return carList;
    }

    public boolean insertCar(String carNumber, int seatCount, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CAR_NUMBER, carNumber);
        values.put(COL_SEAT_COUNT, seatCount);
        values.put(COL_CAR_PHONE_NUMBER, phoneNumber);
        long result = db.insert(TABLE_CARS, null, values);
        return result != -1;
    }

    public boolean updateCar(int id, String carNumber, int seatCount, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CAR_NUMBER, carNumber);
        values.put(COL_SEAT_COUNT, seatCount);
        values.put(COL_CAR_PHONE_NUMBER, phoneNumber);
        int result = db.update(TABLE_CARS, values, COL_CAR_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteCar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CARS, COL_CAR_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public List<Car> searchCars(String query) {
        List<Car> carList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CARS + " WHERE " +
                        COL_CAR_NUMBER + " LIKE ? OR " + COL_CAR_PHONE_NUMBER + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Car car = new Car(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_CAR_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_NUMBER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEAT_COUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_PHONE_NUMBER))
                );
                carList.add(car);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return carList;
    }

    //quan ly tuyen duong
    public long addRoute(String startLocation, String endLocation, int distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_START_LOCATION, startLocation);
        values.put(COL_END_LOCATION, endLocation);
        values.put(COL_DISTANCE, distance);

        long id = db.insert(TABLE_ROUTES, null, values);
        db.close();
        return id;
    }

    public List<Route> getAllRoute() {
        List<Route> routeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ROUTES, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Route route = new Route(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROUTE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_START_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_END_LOCATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_DISTANCE))
                );
                routeList.add(route);
            } while (cursor.moveToNext());
            cursor.close();

        }
        return routeList;
    }

    public boolean updateRoute(int id, String startLocation, String endLocation, int distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_START_LOCATION, startLocation);
        values.put(COL_END_LOCATION, endLocation);
        values.put(COL_DISTANCE, distance);
        int result = db.update(TABLE_ROUTES, values, COL_ROUTE_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public List<Route> getRouteByStartLocation(String startLocation) {
        List<Route> routeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROUTES, null, COL_START_LOCATION + " LIKE ? ",
                new String[]{"%" + startLocation}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Route route = new Route(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROUTE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_START_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_END_LOCATION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_DISTANCE))
                );
                routeList.add(route);
            } while (cursor.moveToNext());
            cursor.close();

        }
        return routeList;
    }

    public boolean deleteRoute(int routeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USERS, COL_USER_ID + " = ?",
                new String[]{String.valueOf(routeId)});
        db.close();
        return rowsAffected > 0;
    }


    //quan li lich trinh:
    // Lấy danh sách lịch trình của một xe dựa trên biển số xe
    public List<Schedule> getSchedulesByCarNumber(String carNumber) {
        List<Schedule> scheduleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn SQL để liên kết bảng schedules, cars và routes
        String query = "SELECT " +
                TABLE_SCHEDULES + "." + COL_SCHEDULE_ID + ", " +
                TABLE_CARS + "." + COL_CAR_NUMBER + ", " +
                TABLE_ROUTES + "." + COL_START_LOCATION + ", " +
                TABLE_ROUTES + "." + COL_END_LOCATION + ", " +
                TABLE_SCHEDULES + "." + COL_DEPARTURE_TIME + ", " +
                TABLE_SCHEDULES + "." + COL_ARRIVAL_TIME +
                " FROM " + TABLE_SCHEDULES +
                " INNER JOIN " + TABLE_CARS + " ON " + TABLE_SCHEDULES + "." + COL_SCHEDULE_CAR_ID + " = " + TABLE_CARS + "." + COL_CAR_ID +
                " INNER JOIN " + TABLE_ROUTES + " ON " + TABLE_SCHEDULES + "." + COL_SCHEDULE_ROUTE_ID + " = " + TABLE_ROUTES + "." + COL_ROUTE_ID +
                " WHERE " + TABLE_CARS + "." + COL_CAR_NUMBER + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{carNumber});

        if (cursor.moveToFirst()) {
            do {
                int scheduleId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCHEDULE_ID));
                String carNum = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_NUMBER));
                String startLocation = cursor.getString(cursor.getColumnIndexOrThrow(COL_START_LOCATION));
                String endLocation = cursor.getString(cursor.getColumnIndexOrThrow(COL_END_LOCATION));
                String departureTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTURE_TIME));
                String arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARRIVAL_TIME));

                Schedule schedule = new Schedule(scheduleId, carNum, startLocation, endLocation, departureTime, arrivalTime);
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return scheduleList;
    }

    // Hiển thị tất cả lịch trình
    public List<Schedule> getAllSchedules() {
        List<Schedule> scheduleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                TABLE_SCHEDULES + "." + COL_SCHEDULE_ID + ", " +
                TABLE_CARS + "." + COL_CAR_NUMBER + ", " +
                TABLE_ROUTES + "." + COL_START_LOCATION + ", " +
                TABLE_ROUTES + "." + COL_END_LOCATION + ", " +
                TABLE_SCHEDULES + "." + COL_DEPARTURE_TIME + ", " +
                TABLE_SCHEDULES + "." + COL_ARRIVAL_TIME +
                " FROM " + TABLE_SCHEDULES +
                " INNER JOIN " + TABLE_CARS + " ON " + TABLE_SCHEDULES + "." + COL_SCHEDULE_CAR_ID + " = " + TABLE_CARS + "." + COL_CAR_ID +
                " INNER JOIN " + TABLE_ROUTES + " ON " + TABLE_SCHEDULES + "." + COL_SCHEDULE_ROUTE_ID + " = " + TABLE_ROUTES + "." + COL_ROUTE_ID;

        Cursor cursor = db.rawQuery(query, null);
        Log.d("DatabaseHelper", "Số dòng trong schedules: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                int scheduleId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCHEDULE_ID));
                String carNum = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_NUMBER));
                String startLocation = cursor.getString(cursor.getColumnIndexOrThrow(COL_START_LOCATION));
                String endLocation = cursor.getString(cursor.getColumnIndexOrThrow(COL_END_LOCATION));
                String departureTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTURE_TIME));
                String arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARRIVAL_TIME));

                Schedule schedule = new Schedule(scheduleId, carNum, startLocation, endLocation, departureTime, arrivalTime);
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return scheduleList;
    }

    // Lấy lịch trình dựa trên schedule_id
    public Schedule getScheduleById(int scheduleId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                TABLE_SCHEDULES + "." + COL_SCHEDULE_ID + ", " +
                TABLE_CARS + "." + COL_CAR_NUMBER + ", " +
                TABLE_ROUTES + "." + COL_START_LOCATION + ", " +
                TABLE_ROUTES + "." + COL_END_LOCATION + ", " +
                TABLE_SCHEDULES + "." + COL_DEPARTURE_TIME + ", " +
                TABLE_SCHEDULES + "." + COL_ARRIVAL_TIME +
                " FROM " + TABLE_SCHEDULES +
                " INNER JOIN " + TABLE_CARS + " ON " + TABLE_SCHEDULES + "." + COL_SCHEDULE_CAR_ID + " = " + TABLE_CARS + "." + COL_CAR_ID +
                " INNER JOIN " + TABLE_ROUTES + " ON " + TABLE_SCHEDULES + "." + COL_SCHEDULE_ROUTE_ID + " = " + TABLE_ROUTES + "." + COL_ROUTE_ID +
                " WHERE " + TABLE_SCHEDULES + "." + COL_SCHEDULE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(scheduleId)});

        if (cursor.moveToFirst()) {
            int schId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCHEDULE_ID));
            String carNum = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_NUMBER));
            String startLocation = cursor.getString(cursor.getColumnIndexOrThrow(COL_START_LOCATION));
            String endLocation = cursor.getString(cursor.getColumnIndexOrThrow(COL_END_LOCATION));
            String departureTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTURE_TIME));
            String arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARRIVAL_TIME));

            Schedule schedule = new Schedule(schId, carNum, startLocation, endLocation, departureTime, arrivalTime);
            cursor.close();
            db.close();
            return schedule;
        }

        cursor.close();
        db.close();
        return null;
    }

    //Add lịch trình:
    public long addSchedule(String carNumber, String startLocation, String endLocation, String departureTime, String arrivalTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        long scheduleId = -1;

        try {
            // Kiểm tra xem biển số xe có tồn tại trong bảng cars không
            Cursor carCursor = db.rawQuery("SELECT " + COL_CAR_ID + " FROM " + TABLE_CARS +
                    " WHERE " + COL_CAR_NUMBER + " = ?", new String[]{carNumber});

            if (carCursor != null && carCursor.moveToFirst()) {
                int carId = carCursor.getInt(carCursor.getColumnIndexOrThrow(COL_CAR_ID));
                carCursor.close();

                // Kiểm tra hoặc thêm tuyến đường nếu chưa tồn tại
                int routeId = -1;
                Cursor routeCursor = db.rawQuery("SELECT " + COL_ROUTE_ID + " FROM " + TABLE_ROUTES +
                                " WHERE " + COL_START_LOCATION + " = ? AND " + COL_END_LOCATION + " = ?",
                        new String[]{startLocation, endLocation});

                if (routeCursor.moveToFirst()) {
                    routeId = routeCursor.getInt(routeCursor.getColumnIndexOrThrow(COL_ROUTE_ID));
                } else {
                    // Nếu tuyến đường chưa tồn tại, thêm mới (distance mặc định là 0, có thể yêu cầu người dùng nhập sau)
                    ContentValues routeValues = new ContentValues();
                    routeValues.put(COL_START_LOCATION, startLocation);
                    routeValues.put(COL_END_LOCATION, endLocation);
                    routeValues.put(COL_DISTANCE, 0); // Giá trị mặc định, có thể thay đổi sau
                    routeId = (int) db.insert(TABLE_ROUTES, null, routeValues);
                }
                routeCursor.close();

                if (routeId == -1) {
                    Log.e("DatabaseHelper", "Failed to add or find route: " + startLocation + " to " + endLocation);
                    return -1;
                }

                // Thêm lịch trình
                ContentValues scheduleValues = new ContentValues();
                scheduleValues.put(COL_SCHEDULE_CAR_ID, carId);
                scheduleValues.put(COL_SCHEDULE_ROUTE_ID, routeId);
                scheduleValues.put(COL_DEPARTURE_TIME, departureTime);
                scheduleValues.put(COL_ARRIVAL_TIME, arrivalTime);

                scheduleId = db.insert(TABLE_SCHEDULES, null, scheduleValues);

                if (scheduleId == -1) {
                    Log.e("DatabaseHelper", "Failed to add schedule for car: " + carNumber);
                } else {
                    Log.d("DatabaseHelper", "Schedule added successfully with ID: " + scheduleId);
                }
            } else {
                Log.e("DatabaseHelper", "Car with number " + carNumber + " not found in database");
                if (carCursor != null) carCursor.close();
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error adding schedule: " + e.getMessage());
        } finally {
            db.close();
        }

        return scheduleId;
    }

    // Cập nhật lịch trình
    public boolean updateSchedule(int scheduleId, String carNumber, String startLocation, String endLocation, String departureTime, String arrivalTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Lấy car_id từ car_number
        int carId = -1;
        Cursor carCursor = db.rawQuery("SELECT " + COL_CAR_ID + " FROM " + TABLE_CARS + " WHERE " + COL_CAR_NUMBER + " = ?", new String[]{carNumber});
        if (carCursor.moveToFirst()) {
            carId = carCursor.getInt(0);
        }
        carCursor.close();
        if (carId == -1) return false;

        // Kiểm tra hoặc thêm route nếu chưa tồn tại
        int routeId = -1;
        Cursor routeCursor = db.rawQuery("SELECT " + COL_ROUTE_ID + " FROM " + TABLE_ROUTES + " WHERE " +
                COL_START_LOCATION + " = ? AND " + COL_END_LOCATION + " = ?", new String[]{startLocation, endLocation});
        if (routeCursor.moveToFirst()) {
            routeId = routeCursor.getInt(0);
        } else {
            ContentValues newRoute = new ContentValues();
            newRoute.put(COL_START_LOCATION, startLocation);
            newRoute.put(COL_END_LOCATION, endLocation);
            routeId = (int) db.insert(TABLE_ROUTES, null, newRoute);
        }
        routeCursor.close();
        if (routeId == -1) return false;

        // Cập nhật schedule
        ContentValues scheduleValues = new ContentValues();
        scheduleValues.put(COL_SCHEDULE_CAR_ID, carId);
        scheduleValues.put(COL_SCHEDULE_ROUTE_ID, routeId);
        scheduleValues.put(COL_DEPARTURE_TIME, departureTime);
        scheduleValues.put(COL_ARRIVAL_TIME, arrivalTime);

        int result = db.update(TABLE_SCHEDULES, scheduleValues, COL_SCHEDULE_ID + "=?", new String[]{String.valueOf(scheduleId)});
        db.close();
        return result > 0;
    }
    public boolean deleteSchedule(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SCHEDULES, COL_SCHEDULE_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    //Đặt vé, hủy vé, thanh toán



    // Kiểm tra ghế đã được đặt chưa
    public boolean isSeatBooked(int scheduleId, int seatNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TICKETS,
                new String[]{COL_TICKET_ID},
                COL_TICKET_SCHEDULE_ID + "=? AND " + COL_TICKET_SEAT_NUMBER + "=? AND " + COL_TICKET_STATUS + "!=?",
                new String[]{String.valueOf(scheduleId), String.valueOf(seatNumber), "Hủy"},
                null, null, null);
        boolean isBooked = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isBooked;
    }
    // Đặt vé
    public boolean bookTicket(int userId, int scheduleId, int seatNumber, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TICKET_USER_ID, userId);
        values.put(COL_TICKET_SCHEDULE_ID, scheduleId);
        values.put(COL_TICKET_SEAT_NUMBER, seatNumber);
        values.put(COL_TICKET_PRICE, price);
        values.put(COL_TICKET_STATUS, "Đặt");
        long result = db.insert(TABLE_TICKETS, null, values);
        db.close();
        return result != -1;
    }


    // Lấy danh sách vé của người dùng
    public Cursor getTicketsByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.*, s." + COL_DEPARTURE_TIME + ", r." + COL_START_LOCATION + ", r." + COL_END_LOCATION +
                " FROM " + TABLE_TICKETS + " t" +
                " JOIN " + TABLE_SCHEDULES + " s ON t." + COL_TICKET_SCHEDULE_ID + " = s." + COL_SCHEDULE_ID +
                " JOIN " + TABLE_ROUTES + " r ON s." + COL_SCHEDULE_ROUTE_ID + " = r." + COL_ROUTE_ID +
                " WHERE t." + COL_TICKET_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});

    }

    // Hủy vé
    public boolean cancelTicket(int ticketId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TICKET_STATUS, "Hủy");
        int result = db.update(TABLE_TICKETS, values, COL_TICKET_ID + "=?", new String[]{String.valueOf(ticketId)});
        db.close();
        return result > 0;
    }
    public boolean confirmTicket(int ticketId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TICKET_STATUS, "Đã xác nhận");
        int result = db.update(TABLE_TICKETS, values, COL_TICKET_ID + "=?", new String[]{String.valueOf(ticketId)});
        db.close();
        return result > 0;
    }
    public boolean deleteTicket(int ticketId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TICKETS, COL_TICKET_ID + "=?", new String[]{String.valueOf(ticketId)});
        db.close();
        return result > 0;
    }

    // Lấy thông tin người dùng dựa trên phoneNumber
    public User getUserByPhoneNumber(String phone_number) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_PHONE_NUMBER + " = ?",
                new String[]{phone_number});
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setPhone_number(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBER)));
            user.setFull_name(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULLNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
            cursor.close();
        }
        db.close();
        return user;
    }


//    public Cursor getAllTickets() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT * FROM " + TABLE_TICKETS, null);
//    }

    public Cursor getAllTickets() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.*, s." + COL_DEPARTURE_TIME + ", r." + COL_START_LOCATION + ", r." + COL_END_LOCATION +
                " FROM " + TABLE_TICKETS + " t" +
                " JOIN " + TABLE_SCHEDULES + " s ON t." + COL_TICKET_SCHEDULE_ID + " = s." + COL_SCHEDULE_ID +
                " JOIN " + TABLE_ROUTES + " r ON s." + COL_SCHEDULE_ROUTE_ID + " = r." + COL_ROUTE_ID;
        return db.rawQuery(query, null);
    }






}