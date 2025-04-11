package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.database.DatabaseHelper;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Schedule;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.User;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.res.ColorStateList;

import com.google.android.material.navigation.NavigationView;


public class BookTicketActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Spinner spinnerSchedule;
    private Spinner spinnerPaymentMethod;
    private EditText etPrice;
    private Button btnBook;
    private DatabaseHelper dbHelper;
    private User user;
    private List<Schedule> scheduleList;
    private Map<Integer, Button> seatButtons;
    private int selectedSeat = -1;
    private int selectedScheduleId = -1;
    private String selectedPaymentMethod;
    private String role, phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.BookTicket);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_ticket);

        spinnerSchedule = findViewById(R.id.spinnerSchedule);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        etPrice = findViewById(R.id.etPrice);
        btnBook = findViewById(R.id.btnBook);
        dbHelper = new DatabaseHelper(this);
        user = (User) getIntent().getSerializableExtra("user");

        role = getIntent().getStringExtra("role");
        phone_number = getIntent().getStringExtra("phoneNumber");

        if(!role.equals("Nhân viên")&&!role.equals("Admin")){
            navigationView.getMenu().findItem(R.id.nav_customer).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_qllt).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_qlduong).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_car).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_staff).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_review).setVisible(false);
        }
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_staff).setVisible(false);
        menu.findItem(R.id.nav_customer).setVisible(false);
        menu.findItem(R.id.nav_qllt).setVisible(false);
        menu.findItem(R.id.nav_qlduong).setVisible(false);
        menu.findItem(R.id.nav_car).setVisible(false);
        menu.findItem(R.id.nav_review).setVisible(false);

        if (user == null) {
            if (phone_number != null) {
                user = dbHelper.getUserByPhoneNumber(phone_number);
            }
        }

        if (user == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        seatButtons = new HashMap<>();

        // Khởi tạo các nút ghế
        initializeSeatButtons();

        // Load danh sách lịch trình
        loadSchedules();

        // Load danh sách phương thức thanh toán
        loadPaymentMethods();

        // Xử lý chọn lịch trình
        spinnerSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedScheduleId = scheduleList.get(position).getScheduleId();
                updateSeatStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedScheduleId = -1;
                selectedSeat = -1;
                etPrice.setText("");
            }
        });

        // Xử lý chọn phương thức thanh toán
        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPaymentMethod = null;
            }
        });

        // Xử lý nút đặt vé
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedScheduleId == -1) {
                    Toast.makeText(BookTicketActivity.this, "Vui lòng chọn lịch trình", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedSeat == -1) {
                    Toast.makeText(BookTicketActivity.this, "Vui lòng chọn ghế", Toast.LENGTH_SHORT).show();
                    return;
                }
                String priceStr = etPrice.getText().toString().trim();
                if (priceStr.isEmpty()) {
                    Toast.makeText(BookTicketActivity.this, "Vui lòng nhập giá vé", Toast.LENGTH_SHORT).show();
                    return;
                }
                int price;
                try {
                    price = Integer.parseInt(priceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(BookTicketActivity.this, "Giá vé không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedPaymentMethod == null) {
                    Toast.makeText(BookTicketActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Đặt vé
                boolean success = dbHelper.bookTicket(user.getId(), selectedScheduleId, selectedSeat, price);
                if (success) {
                    Toast.makeText(BookTicketActivity.this, "Đặt vé thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookTicketActivity.this, ViewTicketsActivity.class);
                    intent.putExtra("role", role); // Truyền role sang
                    String phoneNumber = getIntent().getStringExtra("phoneNumber");
                    intent.putExtra("phoneNumber", phoneNumber);// Truyền role sang
                    startActivity(intent);

                } else {
                    Toast.makeText(BookTicketActivity.this, "Đặt vé thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getLastTicketId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(ticket_id) FROM tickets", null);
        int ticketId = -1;
        if (cursor.moveToFirst()) {
            ticketId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return ticketId;
    }

    private void initializeSeatButtons() {
        seatButtons.put(1, findViewById(R.id.btnSeat1));
        seatButtons.put(2, findViewById(R.id.btnSeat2));
        seatButtons.put(3, findViewById(R.id.btnSeat3));
        seatButtons.put(4, findViewById(R.id.btnSeat4));
        seatButtons.put(5, findViewById(R.id.btnSeat5));
        seatButtons.put(6, findViewById(R.id.btnSeat6));
        seatButtons.put(7, findViewById(R.id.btnSeat7));
        seatButtons.put(8, findViewById(R.id.btnSeat8));
        seatButtons.put(9, findViewById(R.id.btnSeat9));
        seatButtons.put(10, findViewById(R.id.btnSeat10));
        seatButtons.put(11, findViewById(R.id.btnSeat11));
        seatButtons.put(12, findViewById(R.id.btnSeat12));
        seatButtons.put(13, findViewById(R.id.btnSeat13));
        seatButtons.put(14, findViewById(R.id.btnSeat14));
        seatButtons.put(15, findViewById(R.id.btnSeat15));

        for (final Map.Entry<Integer, Button> entry : seatButtons.entrySet()) {
            entry.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedScheduleId == -1) {
                        Toast.makeText(BookTicketActivity.this, "Vui lòng chọn lịch trình trước", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int seatNumber = entry.getKey();
                    if (dbHelper.isSeatBooked(selectedScheduleId, seatNumber)) {
                        Toast.makeText(BookTicketActivity.this, "Ghế đã được đặt", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Reset màu của ghế trước đó
                    if (selectedSeat != -1) {
                        seatButtons.get(selectedSeat).setBackgroundTintList(ColorStateList.valueOf(0xFF00BCD4)); // Trống
                    }
                    // Cập nhật ghế được chọn
                    selectedSeat = seatNumber;
                    entry.getValue().setBackgroundTintList(ColorStateList.valueOf(0xFFFFB300)); // Đặt
                    // Tính và điền giá vé tự động
                    updatePriceBasedOnSeat(seatNumber);
                }
            });
        }
    }

    private void loadSchedules() {
        scheduleList = dbHelper.getAllSchedules(); // Lấy danh sách từ DatabaseHelper
        List<String> scheduleDisplayList =  new ArrayList<>();
        scheduleDisplayList.clear(); // Xóa danh sách hiển thị cũ

        for (Schedule schedule : scheduleList) {
            String display = schedule.getCarNumber() + ": " +
                    schedule.getStartLocation() + " -> " +
                    schedule.getEndLocation() + " (" +
                    schedule.getDepartureTime() + ")";
            scheduleDisplayList.add(display);
        }

        //scheduleAdapter.notifyDataSetChanged();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scheduleDisplayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSchedule.setAdapter(adapter);// Cập nhật ListView
    }

    private void loadPaymentMethods() {
        List<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("Tiền mặt");
        paymentMethods.add("Chuyển khoản");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(adapter);
    }

    private void updateSeatStatus() {
        for (Map.Entry<Integer, Button> entry : seatButtons.entrySet()) {
            int seatNumber = entry.getKey();
            Button button = entry.getValue();
            if (dbHelper.isSeatBooked(selectedScheduleId, seatNumber)) {
                button.setBackgroundTintList(ColorStateList.valueOf(0xFFFFCDD2)); // Bán
            } else {
                button.setBackgroundTintList(ColorStateList.valueOf(0xFF00BCD4)); // Trống
            }
        }
        selectedSeat = -1;
        etPrice.setText("");
    }

    private void updatePriceBasedOnSeat(int seatNumber) {
        int price;
        if (seatNumber >= 1 && seatNumber <= 2) { // Hàng 1
            price = 150000;
        } else if (seatNumber >= 12 && seatNumber <= 15) { // Hàng cuối
            price = 150000;
        } else { // Các hàng giữa
            price = 200000;
        }
        etPrice.setText(String.valueOf(price));
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_ticketView) {
            Intent intent = new Intent(this, ViewTicketsActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            intent.putExtra("user", user);
            intent.putExtra("phoneNumber", phone_number);
            finish();
            startActivity(intent);
        }
        else if (id == R.id.nav_review) {
            Intent intent = new Intent(this,CustomerMainActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            intent.putExtra("user", user);
            startActivity(intent);
        }

        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}