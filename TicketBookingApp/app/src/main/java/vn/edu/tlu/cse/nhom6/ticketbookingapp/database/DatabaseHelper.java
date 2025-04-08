package vn.edu.tlu.cse.nhom6.ticketbookingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //ten database
    private static final String DATABASE_NAME = "ticketbooking.db";
    private static final int DATABASE_VERSION = 2;

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
                COL_DEPARTURE_TIME + " TEXT," +
                COL_ARRIVAL_TIME + " TEXT, " +
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

    //kiem tra dang nhap:
//    public boolean checkUser(String phone_number, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
//                COL_PHONE_NUMBER + " = ? AND " + COL_PASSWORD + " = ?", new String[]{phone_number, password});
//        boolean exists = cursor.getCount() > 0;
//        cursor.close();
//        db.close();
//        return exists;
//
//    }
    public boolean checkUser(String phone_number) {
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


    public String checkRole(String phone_number, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_PHONE_NUMBER + " = ? AND " + COL_PASSWORD + " = ?", new String[]{phone_number, password});
        if(cursor !=null && cursor.moveToFirst()){
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)); //lay vai tro nguoi dung thong qua index
            cursor.close();
            db.close();
            return role;
        }
        cursor.close();
        db.close();
        return null;
    }
    private void insertUser
}