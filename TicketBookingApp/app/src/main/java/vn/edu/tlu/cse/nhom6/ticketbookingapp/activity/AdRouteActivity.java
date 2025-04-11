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
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.Route;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.User;

public class AdRouteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private EditText et_startLocation, et_endLocation, et_distance, et_search;
    private Button btnAdd, btnUpdate, btnSearch, btnDelete;
    private ListView lvRoutes;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Route> routeList;
    private List<String> routeDisplayList;
    private int selectedRouteId = -1;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_route);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.AdRoute);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_qlduong);


        et_startLocation = findViewById(R.id.et_startLocation);
        et_endLocation = findViewById(R.id.et_endLocation);
        et_distance = findViewById(R.id.et_distance);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnSearch = findViewById(R.id.btn_search);
        btnDelete = findViewById(R.id.btn_delete);
        lvRoutes = findViewById(R.id.lv_routes);
        et_search = findViewById(R.id.et_search);
        dbHelper = new DatabaseHelper(this);
        routeList = new ArrayList<>();
        routeDisplayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, routeDisplayList);
        lvRoutes.setAdapter(adapter);
        loadRoutes();

        role = getIntent().getStringExtra("role");
        Menu menu = navigationView.getMenu();
        if (!role.equals("Admin")) {
            menu.findItem(R.id.nav_staff).setVisible(false);
            menu.findItem(R.id.nav_ticket).setVisible(false);
            menu.findItem(R.id.nav_review).setVisible(false);
        }
        else{
            menu.findItem(R.id.nav_ticket).setVisible(false);
            menu.findItem(R.id.nav_review).setVisible(false);
        }




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startLocation = et_startLocation.getText().toString().trim();
                String endLocation = et_endLocation.getText().toString().trim();
                String distanceStr = et_distance.getText().toString().trim();

                // Kiểm tra rỗng
                if (startLocation.isEmpty() || endLocation.isEmpty() || distanceStr.isEmpty()) {
                    Toast.makeText(AdRouteActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int distance;
                try {
                    distance = Integer.parseInt(distanceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(AdRouteActivity.this, "Khoảng cách không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (distance <= 0) {
                    Toast.makeText(AdRouteActivity.this, "Khoảng cách phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long id = dbHelper.addRoute(startLocation, endLocation, distance);
                if (id != -1) {
                    Toast.makeText(AdRouteActivity.this, "Thêm tuyến đường thành công!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadRoutes();
                } else {
                    Toast.makeText(AdRouteActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(v -> {
            if (selectedRouteId != -1) {
                String startLocation = et_startLocation.getText().toString();
                String endLocation = et_endLocation.getText().toString();
                int distance = Integer.parseInt(et_distance.getText().toString());

                if (!startLocation.isEmpty() && !endLocation.isEmpty() && distance != 0) {
                    boolean success = dbHelper.updateRoute(selectedRouteId, startLocation, endLocation, distance);
                    if (success) {
                        Toast.makeText(this, "Sửa tuyến  đường thành công!", Toast.LENGTH_SHORT).show();
                        clearInputs();
                        loadRoutes();
                        btnAdd.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        selectedRouteId = -1;
                    } else {
                        Toast.makeText(this, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDelete.setOnClickListener(v -> {
            if (selectedRouteId != -1) {
                boolean success = dbHelper.deleteStaff(selectedRouteId);
                if (success) {
                    Toast.makeText(this, "Xóa tuyến đường thành công!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    loadRoutes();
                    selectedRouteId = -1;
                } else {
                    Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn tuyến đường để xóa!", Toast.LENGTH_SHORT).show();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = et_search.getText().toString();
                if (!searchQuery.isEmpty()) {
                    routeList = dbHelper.getRouteByStartLocation(searchQuery);
                    updateRouteList();
                } else {
                    loadRoutes();
                }
            }
        });
        lvRoutes.setOnItemClickListener((parent, view, position, id) -> {
            Route selectedRoute = routeList.get(position);
            selectedRouteId = selectedRoute.getRoute_id();
            et_startLocation.setText(selectedRoute.getStart_location());
            et_endLocation.setText(selectedRoute.getEnd_location());
            et_distance.setText(String.valueOf(selectedRoute.getDistance()));
            btnAdd.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        });

    }

    private void loadRoutes() {
        routeList = dbHelper.getAllRoute();
        updateRouteList();
    }


    private void updateRouteList() {
        routeDisplayList.clear();
        for (Route rou : routeList) {
            routeDisplayList.add(rou.getStart_location() + " - " + rou.getEnd_location() + "-" + rou.getDistance());
        }
        adapter.notifyDataSetChanged();
    }

    private void clearInputs() {
        et_startLocation.setText("");
        et_endLocation.setText("");
        et_distance.setText("");
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
        } else if (id == R.id.nav_qllt) {
            Intent intent = new Intent(this, AdScheduleActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        } else if (id == R.id.nav_car) {
            Intent intent = new Intent(this, ManageCarActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        } else if (id == R.id.nav_ticketView) {
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