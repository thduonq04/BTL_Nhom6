package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.adapter.TicketAdapter;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.database.DatabaseHelper;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Ticket;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.User;




public class ViewTicketsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Spinner spinnerCustomers;
    private RecyclerView rvTickets;
    private DatabaseHelper dbHelper;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList;
    private User user;
    private String role;
    private List<User> customerList;
    private ArrayAdapter<String> customerAdapter;
    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tickets);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.ViewTickets);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_ticketView);

        rvTickets = findViewById(R.id.rvTickets);
        spinnerCustomers = findViewById(R.id.spinnerCustomers);
        dbHelper = new DatabaseHelper(this);
        ticketList = new ArrayList<>();

        role = getIntent().getStringExtra("role");
        phone_number = getIntent().getStringExtra("phoneNumber");
        user = dbHelper.getUserByPhoneNumber(phone_number);
        if (user == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!role.equals("Nhân viên") && !role.equals("Admin")) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_customer).setVisible(false);
            menu.findItem(R.id.nav_qllt).setVisible(false);
            menu.findItem(R.id.nav_qlduong).setVisible(false);
            menu.findItem(R.id.nav_car).setVisible(false);
            menu.findItem(R.id.nav_staff).setVisible(false);
        } else if (!role.equals("Admin")) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_staff).setVisible(false);
            menu.findItem(R.id.nav_ticket).setVisible(false);
            menu.findItem(R.id.nav_review).setVisible(false);
        } else if (role.equals("Admin")) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_ticket).setVisible(false);
            menu.findItem(R.id.nav_review).setVisible(false);
            menu.findItem(R.id.nav_staff).setVisible(true);
        }

        ticketAdapter = new TicketAdapter(this, ticketList, role.equals("Nhân viên") || role.equals("Admin"), new TicketAdapter.OnTicketActionListener() {
            @Override
            public void onCancel(Ticket ticket) {
                if (dbHelper.cancelTicket(ticket.getTicket_id())) {
                    Toast.makeText(ViewTicketsActivity.this, "Hủy vé thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewTicketsActivity.this, "Hủy vé thất bại", Toast.LENGTH_SHORT).show();
                }
                loadTickets(ticket.getTicket_user_id());
            }


            // Đây lắng nghe sự kiện bấm vào xác nhận
            @Override
            public void onConfirm(Ticket ticket) {
                if (dbHelper.confirmTicket(ticket.getTicket_id())) {
                    Toast.makeText(ViewTicketsActivity.this, "Xác nhận vé thành công", Toast.LENGTH_SHORT).show();
                    loadCustomers();
                } else {
                    Toast.makeText(ViewTicketsActivity.this, "Xác nhận vé thất bại", Toast.LENGTH_SHORT).show();
                    loadCustomers();
                }
                loadTickets(ticket.getTicket_user_id());
            }

            @Override
            public void onDelete(Ticket ticket) {
                if (dbHelper.deleteTicket(ticket.getTicket_id())) {
                    Toast.makeText(ViewTicketsActivity.this, "Xóa vé thành công", Toast.LENGTH_SHORT).show();
                    loadCustomers();
                } else {
                    Toast.makeText(ViewTicketsActivity.this, "Xóa vé thất bại", Toast.LENGTH_SHORT).show();
                    loadCustomers();
                }
                loadTickets(ticket.getTicket_user_id());
            }
        });
        rvTickets.setLayoutManager(new LinearLayoutManager(this));//thiet lap hien thi bo cuc layout cho recyclerview
        rvTickets.setAdapter(ticketAdapter);

        if (role.equals("Admin") || role.equals("Nhân viên")) {
            spinnerCustomers.setVisibility(View.VISIBLE);
            loadCustomers();
            spinnerCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    User selectedUser = customerList.get(position);
                    if (selectedUser.getId() == -1) {
                        loadAllTickets(); //chon muc "tat ca khach hang"thi no load het tat ca
                    } else {
                        loadTickets(selectedUser.getId()); //khong thi dua ra theo tung cus
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không làm gì nếu không chọn
                }
            });
        } else {
            loadTickets(user.getId()); //khach nao xem cua ng do
        }
    }

    private void showReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đánh giá ứng dụng");
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        builder.setView(dialogView);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            if (fromUser) {
                Toast.makeText(this, "Bạn đã chọn: " + rating + " sao", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Gửi", (dialog, which) -> {
            float rating = ratingBar.getRating();
            if (rating == 0) {
                Toast.makeText(this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Cảm ơn bạn đã đánh giá " + rating + " sao!", Toast.LENGTH_SHORT).show();
                openGooglePlayForReview();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void openGooglePlayForReview() {
        String packageName = getPackageName();
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void loadCustomers() { //load ds va dua vao spinnerCtomers
        customerList = dbHelper.getAllCustomers();
        List<String> customerNames = new ArrayList<>();
        customerList.add(0, new User(-1, "Tất cả", "", "", "", ""));
        customerNames.add("Tất cả khách hàng");

        for (int i = 1; i < customerList.size(); i++) {
            User customer = customerList.get(i);
            customerNames.add(customer.getFull_name() + " - " + customer.getPhone_number());
        }

        customerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, customerNames);
        spinnerCustomers.setAdapter(customerAdapter);
    }

    private void loadAllTickets() {
        ticketList.clear();
        Cursor cursor = dbHelper.getAllTickets();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ticket ticket = new Ticket(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ticket_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("seat_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status"))
                );
                ticket.setDepartureTime(cursor.getString(cursor.getColumnIndexOrThrow("departure_time")));
                ticket.setStartLocation(cursor.getString(cursor.getColumnIndexOrThrow("start_location")));
                ticket.setEndLocation(cursor.getString(cursor.getColumnIndexOrThrow("end_location")));
                ticketList.add(ticket);
            } while (cursor.moveToNext());
            cursor.close();
        }
        ticketAdapter.notifyDataSetChanged();
    }

    private void loadTickets(int userId) {
        ticketList.clear();
        Cursor cursor = dbHelper.getTicketsByUser(userId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ticket ticket = new Ticket(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ticket_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("seat_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status"))
                );
                ticket.setDepartureTime(cursor.getString(cursor.getColumnIndexOrThrow("departure_time")));
                ticket.setStartLocation(cursor.getString(cursor.getColumnIndexOrThrow("start_location")));
                ticket.setEndLocation(cursor.getString(cursor.getColumnIndexOrThrow("end_location")));
                ticketList.add(ticket);
            } while (cursor.moveToNext());
            cursor.close();
        }
        ticketAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (role.equals("Admin") || role.equals("Nhân viên")) {
            loadCustomers();
            if (!customerList.isEmpty() && phone_number != null) {
                for (int i = 0; i < customerList.size(); i++) {
                    if (customerList.get(i).getPhone_number().equals(phone_number)) {
                        spinnerCustomers.setSelection(i);
                        loadTickets(customerList.get(i).getId());
                        break;
                    }
                }
            }
        } else {
            loadTickets(user.getId());
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_ticket) {
            Intent intent = new Intent(this, BookTicketActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("phoneNumber", phone_number);

            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_customer) {
            Intent intent = new Intent(this, AdCustomerActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("phoneNumber", phone_number);

            startActivity(intent);
        } else if (id == R.id.nav_qllt) {
            Intent intent = new Intent(this, AdScheduleActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("phoneNumber", phone_number);
            startActivity(intent);
        } else if (id == R.id.nav_qlduong) {
            Intent intent = new Intent(this, AdRouteActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("phoneNumber", phone_number);
            startActivity(intent);
        } else if (id == R.id.nav_car) {
            Intent intent = new Intent(this, ManageCarActivity.class);
            intent.putExtra("role", role);
            intent.putExtra("phoneNumber", phone_number);
            startActivity(intent);
        }
        else if (id == R.id.nav_review) {
            showReviewDialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}