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
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.User;

public class AdCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private EditText etFullname, etPhone, etEmail, etPassword, etSearch;
    private Button btnAdd, btnUpdate, btnSearch;
    private ListView lvCustomers;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<User> customerList;
    private List<String> customerDisplayList;
    private int selectedCustomerId = -1;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ad_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.AdCustomer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_customer);

        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        }

        role = getIntent().getStringExtra("role");
        if (!role.equals("Admin")) {
            navigationView.getMenu().findItem(R.id.nav_staff).setVisible(false);
        }


        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_staff).setVisible(false);

        etFullname = findViewById(R.id.et_fullname);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etSearch = findViewById(R.id.et_search);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnSearch = findViewById(R.id.btn_search);
        lvCustomers = findViewById(R.id.lv_customers);

        dbHelper = new DatabaseHelper(this);
        customerList = new ArrayList<>();
        customerDisplayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerDisplayList);
        lvCustomers.setAdapter(adapter);

        loadCustomers();


        btnAdd.setOnClickListener(v -> {
            String fullname = etFullname.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!fullname.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                long id = dbHelper.addCustomer(phone, email, password, fullname);
                if (id != -1) {
                    Toast.makeText(this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadCustomers();
                } else {
                    Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sửa nhân viên
        btnUpdate.setOnClickListener(v -> {
            if (selectedCustomerId != -1) {
                String fullname = etFullname.getText().toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!fullname.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    boolean success = dbHelper.updateCustomer(selectedCustomerId, phone, email, password, fullname);
                    if (success) {
                        Toast.makeText(this, "Sửa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                        clearInputs();
                        loadCustomers();
                        btnAdd.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        selectedCustomerId = -1;
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
                customerList = dbHelper.getCustomersByFullname(searchQuery);
                updateCustomerList();
            } else {
                loadCustomers();
            }
        });

        // Chọn nhân viên từ danh sách để sửa hoặc xóa
        lvCustomers.setOnItemClickListener((parent, view, position, id) -> {
            User selectedCustomer = customerList.get(position);
            selectedCustomerId = selectedCustomer.getId();
            etFullname.setText(selectedCustomer.getFull_name());
            etPhone.setText(selectedCustomer.getPhone_number());
            etEmail.setText(selectedCustomer.getEmail());
            etPassword.setText(selectedCustomer.getPassword());
            btnAdd.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        });

        lvCustomers.setOnItemLongClickListener((parent, view, position, id) -> {
            User selectedCustomer = customerList.get(position);
            boolean success = dbHelper.deleteCustomer(selectedCustomer.getId());
            if (success) {
                Toast.makeText(this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                loadCustomers();
            } else {
                Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void loadCustomers() {
        customerList = dbHelper.getAllCustomers();
        updateCustomerList();
    }


    private void updateCustomerList() {
        customerDisplayList.clear();
        for (User emp : customerList) {
            customerDisplayList.add(emp.getFull_name() + " - " + emp.getPhone_number());
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

        if (id == R.id.nav_qlduong) {
            Intent intent = new Intent(this, AdRouteActivity.class);
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
        } else if (id == R.id.nav_qllt) {
            Intent intent = new Intent(this, AdScheduleActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        } else if (id == R.id.nav_car) {
            Intent intent = new Intent(this, ManageCarActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}