package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.User;

public class AdStaffActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private EditText etFullname, etPhone, etEmail, etPassword, etSearch;
    private Button btnAdd, btnUpdate, btnSearch;
    private ListView lvEmployees;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<User> employeeList;
    private List<String> employeeDisplayList;
    private int selectedEmployeeId = -1;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ad_staff);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.AdStaff);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_staff);

        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            // này chắc bỏ á m :))) cop từ nav vè mà 0 cần đến hay sao á
        }

        role = getIntent().getStringExtra("role");
        Menu menu = navigationView.getMenu();
        if (!"Admin".equals(role)) {
            menu.findItem(R.id.nav_staff).setVisible(false);
        } else {
            menu.findItem(R.id.nav_staff).setVisible(true);
        }





        etFullname = findViewById(R.id.et_fullname);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etSearch = findViewById(R.id.et_search);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnSearch = findViewById(R.id.btn_search);
        lvEmployees = findViewById(R.id.lv_employees);

        dbHelper = new DatabaseHelper(this);
        employeeList = new ArrayList<>();
        employeeDisplayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeDisplayList);
        lvEmployees.setAdapter(adapter);

        loadEmployees();



        role = getIntent().getStringExtra("role");
        Menu menu1 = navigationView.getMenu();
        if(role.equals("Admin")){
            menu1.findItem(R.id.nav_staff).setVisible(true);
            menu1.findItem(R.id.nav_logout).setVisible(true);
            menu1.findItem(R.id.nav_customer).setVisible(true);
            menu1.findItem(R.id.nav_car).setVisible(true);
            menu1.findItem(R.id.nav_qlduong).setVisible(true);
            menu1.findItem(R.id.nav_qllt).setVisible(true);
            menu1.findItem(R.id.nav_ticket).setVisible(false);
            menu1.findItem(R.id.nav_review).setVisible(true);
            menu1.findItem(R.id.nav_review).setVisible(false);
        }
//        if (role.equals("Admin")) {
//            navigationView.getMenu().findItem(R.id.nav_staff).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_customer).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_car).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_qlduong).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_qllt).setVisible(true);
//        }
//
//
//        Menu menu = navigationView.getMenu();
//        menu.findItem(R.id.nav_staff).setVisible(true);
//        menu.findItem(R.id.nav_logout).setVisible(true);
//        menu.findItem(R.id.nav_customer).setVisible(true);
//        menu.findItem(R.id.nav_car).setVisible(true);
//        menu.findItem(R.id.nav_qlduong).setVisible(true);
//        menu.findItem(R.id.nav_qllt).setVisible(true);

        btnAdd.setOnClickListener(v -> {
            String fullname = etFullname.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!fullname.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                long id = dbHelper.addStaff(phone, email, password, fullname);
                if (id != -1) {
                    Toast.makeText(this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadEmployees();
                } else {
                    Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sửa nhân viên
        btnUpdate.setOnClickListener(v -> {
            if (selectedEmployeeId != -1) {
                String fullname = etFullname.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!fullname.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    boolean success = dbHelper.updateStaff(selectedEmployeeId, phone, email, password, fullname);
                    if (success) {
                        Toast.makeText(this, "Sửa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                        clearInputs();
                        loadEmployees();
                        btnAdd.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        selectedEmployeeId = -1;
                    } else {
                        Toast.makeText(this, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tìm kiếm nhân viên
        btnSearch.setOnClickListener(v -> {
            String searchQuery = etSearch.getText().toString();
            if (!searchQuery.isEmpty()) {
                employeeList = dbHelper.getStaffByFullname(searchQuery);
                updateEmployeeList();
            } else {
                loadEmployees();
            }
        });

        // Chọn nhân viên từ danh sách để sửa hoặc xóa
        lvEmployees.setOnItemClickListener((parent, view, position, id) -> {
            User selectedEmployee = employeeList.get(position);
            selectedEmployeeId = selectedEmployee.getId();
            etFullname.setText(selectedEmployee.getFull_name());
            etPhone.setText(selectedEmployee.getPhone_number());
            etEmail.setText(selectedEmployee.getEmail());
            etPassword.setText(selectedEmployee.getPassword());
            btnAdd.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        });

        lvEmployees.setOnItemLongClickListener((parent, view, position, id) -> {
            User selectedEmployee = employeeList.get(position);
            boolean success = dbHelper.deleteStaff(selectedEmployee.getId());
            if (success) {
                Toast.makeText(this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                loadEmployees();
            } else {
                Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void loadEmployees() {
        employeeList = dbHelper.getAllStaff();
        updateEmployeeList();
    }


    private void updateEmployeeList() {
        employeeDisplayList.clear();
        for (User emp : employeeList) {
            employeeDisplayList.add(emp.getFull_name() + " - " + emp.getPhone_number());
        }
        adapter.notifyDataSetChanged();
    }

    private void clearInputs() {
        etFullname.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etPassword.setText("");
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
            intent.putExtra("role", role); // Truyền role sang
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
        else if(id == R.id.nav_qllt){
            Intent intent = new Intent(this, AdScheduleActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }
        else if (id == R.id.nav_ticketView) {
            Intent intent = new Intent(this, ViewTicketsActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            String phoneNumber = getIntent().getStringExtra("phoneNumber");
            intent.putExtra("phoneNumber", phoneNumber);// Truyền role sang
            startActivity(intent);
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}