package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;


import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.adapter.CarAdapter;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.database.DatabaseHelper;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Car;


public class ManageCarActivity extends AppCompatActivity implements CarAdapter.OnCarClickListener, NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private CarAdapter carAdapter;
    private List<Car> carList;
    private DatabaseHelper databaseHelper;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.MangeCar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_qllt);

        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        databaseHelper = new DatabaseHelper(this);
        carList = new ArrayList<>();
        carAdapter = new CarAdapter(this, carList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carAdapter);

        loadCars();

        fabAdd.setOnClickListener(v -> showAddEditDialog(null));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                searchCars(s.toString());
            }
        });

    }

    private void loadCars() {
        carList.clear();
        carList.addAll(databaseHelper.getAllCars());
        carAdapter.notifyDataSetChanged();
    }

    private void searchCars(String query) {
        carList.clear();
        carList.addAll(databaseHelper.searchCars(query));
        carAdapter.notifyDataSetChanged();
    }

    private void showAddEditDialog(Car car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_edit_car, null);
        builder.setView(view);

        EditText etCarNumber = view.findViewById(R.id.etCarNumber);
        EditText etSeatCount = view.findViewById(R.id.etSeatCount);
        EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        Button btnSave = view.findViewById(R.id.btnSave);

        if (car != null) {
            etCarNumber.setText(car.getCarNumber());
            etSeatCount.setText(String.valueOf(car.getSeatCount()));
            etPhoneNumber.setText(car.getPhoneNumber());
            builder.setTitle("Sửa thông tin xe");
        } else {
            builder.setTitle("Thêm xe mới");
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String carNumber = etCarNumber.getText().toString().trim();
            String seatCountStr = etSeatCount.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            if (carNumber.isEmpty() || seatCountStr.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int seatCount = Integer.parseInt(seatCountStr);
            boolean success;
            if (car == null) {
                success = databaseHelper.insertCar(carNumber, seatCount, phoneNumber);
            } else {
                success = databaseHelper.updateCar(car.getId(), carNumber, seatCount, phoneNumber);
            }

            if (success) {
                Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                loadCars();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void onEditClick(Car car) {
        showAddEditDialog(car);
    }

    @Override
    public void onDeleteClick(Car car) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa xe " + car.getCarNumber() + "?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (databaseHelper.deleteCar(car.getId())) {
                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        loadCars();
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
