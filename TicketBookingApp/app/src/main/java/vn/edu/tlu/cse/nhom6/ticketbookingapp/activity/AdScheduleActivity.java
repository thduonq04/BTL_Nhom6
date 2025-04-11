package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.database.DatabaseHelper;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Car;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Route;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Schedule;

public class AdScheduleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private Spinner spinnerCarNumber; // Thay EditText bằng Spinner
    private EditText etStartLocation, etEndLocation, etDepartureTime, etArrivalTime, etSearch;
    private Button btnUpdate, btnSearch, btnAdd, btnDelete;
    private ListView lvSchedules;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> scheduleAdapter;
    private ArrayAdapter<String> carAdapter; // Adapter cho Spinner
    private List<Schedule> scheduleList;
    private List<String> scheduleDisplayList;
    private int selectedScheduleId = -1;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_schedule);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.AdSchedule);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_qllt);

        spinnerCarNumber = findViewById(R.id.spinner_car_number); // Thay et_BSX
        etStartLocation = findViewById(R.id.et_startLocation);
        etEndLocation = findViewById(R.id.et_endLocation);
        etDepartureTime = findViewById(R.id.et_departureTime);
        etArrivalTime = findViewById(R.id.et_arrivalTime);
        btnUpdate = findViewById(R.id.btn_update);
        btnSearch = findViewById(R.id.btn_search);
        btnAdd = findViewById(R.id.btn_add);
        btnDelete = findViewById(R.id.btn_delete);
        etSearch = findViewById(R.id.et_search);
        lvSchedules = findViewById(R.id.lv_schedules);

        dbHelper = new DatabaseHelper(this);
        scheduleList = new ArrayList<>();
        scheduleDisplayList = new ArrayList<>();
        scheduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scheduleDisplayList);
        lvSchedules.setAdapter(scheduleAdapter);
        loadSchedules();
        loadCarNumbers();

        role = getIntent().getStringExtra("role");
        if (!role.equals("Admin")) {
            navigationView.getMenu().findItem(R.id.nav_staff).setVisible(false);
        }


        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_staff).setVisible(false);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carNumber = spinnerCarNumber.getSelectedItem().toString();
                String startLocation = etStartLocation.getText().toString().trim();
                String endLocation = etEndLocation.getText().toString().trim();
                String departureTime = etDepartureTime.getText().toString().trim();
                String arrivalTime = etArrivalTime.getText().toString().trim();

                if (carNumber.equals("Không có xe nào")) {
                    Toast.makeText(AdScheduleActivity.this, "Không có xe nào để chọn!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (startLocation.isEmpty() || endLocation.isEmpty() || departureTime.isEmpty() || arrivalTime.isEmpty()) {
                    Toast.makeText(AdScheduleActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long id = dbHelper.addSchedule(carNumber, startLocation, endLocation, departureTime, arrivalTime);
                if (id != -1) {
                    Toast.makeText(AdScheduleActivity.this, "Thêm lịch trình thành công!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadSchedules();
                } else {
                    Toast.makeText(AdScheduleActivity.this, "Thêm lịch trình thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Nút Cập nhật lịch trình
        btnUpdate.setOnClickListener(v -> {
            if (selectedScheduleId != -1) {
                String carNumber = spinnerCarNumber.getSelectedItem().toString();
                String startLocation = etStartLocation.getText().toString().trim();
                String endLocation = etEndLocation.getText().toString().trim();
                String departureTime = etDepartureTime.getText().toString().trim();
                String arrivalTime = etArrivalTime.getText().toString().trim();

                if (carNumber.equals("Không có xe nào")) {
                    Toast.makeText(this, "Không có xe nào để chọn!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (startLocation.isEmpty() || endLocation.isEmpty() || departureTime.isEmpty() || arrivalTime.isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = dbHelper.updateSchedule(selectedScheduleId, carNumber, startLocation, endLocation, departureTime, arrivalTime);
                if (success) {
                    Toast.makeText(this, "Sửa lịch trình thành công!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadSchedules();
                    btnAdd.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.GONE);
                    selectedScheduleId = -1;
                } else {
                    Toast.makeText(this, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn lịch trình để sửa!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedScheduleId != -1) {
                    boolean success = dbHelper.deleteSchedule(selectedScheduleId);
                    if (success) {
                        Toast.makeText(AdScheduleActivity.this, "Xóa lịch trình thành công!", Toast.LENGTH_SHORT).show();
                        clearInputs();
                        loadSchedules();
                        selectedScheduleId = -1;
                    } else {
                        Toast.makeText(AdScheduleActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdScheduleActivity.this, "Vui lòng chọn lịch trình để xóa!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnSearch.setOnClickListener(v -> {
            String searchQuery = etSearch.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                scheduleList = dbHelper.getSchedulesByCarNumber(searchQuery);
                updateScheduleList();
            } else {
                loadSchedules();
            }
        });

        lvSchedules.setOnItemClickListener((parent, view, position, id) -> {
            Schedule selectedSchedule = scheduleList.get(position);
            selectedScheduleId = selectedSchedule.getScheduleId();
            spinnerCarNumber.setSelection(((ArrayAdapter<String>) spinnerCarNumber.getAdapter()).getPosition(selectedSchedule.getCarNumber()));
            etStartLocation.setText(selectedSchedule.getStartLocation());
            etEndLocation.setText(selectedSchedule.getEndLocation());
            etDepartureTime.setText(selectedSchedule.getDepartureTime());
            etArrivalTime.setText(selectedSchedule.getArrivalTime());
            btnAdd.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        });

        lvSchedules.setOnItemLongClickListener((parent, view, position, id) -> {
            Schedule selectedSchedule = scheduleList.get(position);
            boolean success = dbHelper.deleteSchedule(selectedSchedule.getScheduleId()); // Sửa deleteStaff thành deleteSchedule
            if (success) {
                Toast.makeText(this, "Xóa lịch trình thành công!", Toast.LENGTH_SHORT).show();
                loadSchedules();
            } else {
                Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
            return true;
        });


    }


    private void loadCarNumbers() {
        List<Car> cars = dbHelper.getAllCars();
        List<String> carNumbers = new ArrayList<>();
        for (Car car : cars) {
            carNumbers.add(car.getCarNumber());
        }

        if (carNumbers.isEmpty()) {
            carNumbers.add("Không có xe nào");
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(false);
        }

        carAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carNumbers);
        carAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarNumber.setAdapter(carAdapter);
    }

    private void loadSchedules() {
        scheduleList = dbHelper.getAllSchedules();
        Log.d("AdScheduleActivity", "Số lịch trình: " + scheduleList.size());
        updateScheduleList();
    }

    // Cập nhật danh sách hiển thị
    private void updateScheduleList() {
        scheduleDisplayList.clear();
        for (Schedule schedule : scheduleList) {
            String displayText = schedule.getCarNumber() + " | " +
                    schedule.getStartLocation() + " -> " +
                    schedule.getEndLocation() + " | " +
                    schedule.getDepartureTime() + " | " +
                    schedule.getArrivalTime();
            scheduleDisplayList.add(displayText);
        }
        scheduleAdapter.notifyDataSetChanged();
    }


    // Xóa nội dung các trường nhập liệu
    private void clearInputs() {
        spinnerCarNumber.setSelection(0); // Đặt về mục đầu tiên trong Spinner
        etStartLocation.setText("");
        etEndLocation.setText("");
        etDepartureTime.setText("");
        etArrivalTime.setText("");
    }

//    private void updateScheduleList() {
//        scheduleDisplayList.clear();
//        for (Schedule sche : scheduleList) {
//            scheduleDisplayList.add(sche.getCarNumber() + " - " + sche.getStartLocation() + " - " + sche.getEndLocation() + " - " + sche.getDepartureTime() + " - " + sche.getArrivalTime());
//        }
//        adapter.notifyDataSetChanged();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadSchedules(); // Làm mới danh sách sau khi thêm
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_customer) {
            Intent intent = new Intent(this, AdCustomerActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_staff) {
            Intent intent = new Intent(this, AdStaffActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        } else if (id == R.id.nav_car) {
            Intent intent = new Intent(this, ManageCarActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        } else if (id==R.id.nav_qlduong) {
            Intent intent = new Intent(this, AdRouteActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}