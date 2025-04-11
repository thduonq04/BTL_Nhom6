package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.model.User;

public class StaffMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private String role;
    private String phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.Adminmain);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        role = getIntent().getStringExtra("role");
        phone_number = getIntent().getStringExtra("phone_number");
        Menu menu = navigationView.getMenu();

        if (!"Admin".equals(role)) {
            menu.findItem(R.id.nav_staff).setVisible(false);
            menu.findItem(R.id.nav_ticket).setVisible(false);
            menu.findItem(R.id.nav_review).setVisible(false);
        } else {
            menu.findItem(R.id.nav_staff).setVisible(true);
            menu.findItem(R.id.nav_ticket).setVisible(false);
            menu.findItem(R.id.nav_review).setVisible(false);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_qlduong) {
            Intent intent = new Intent(this, AdRouteActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            intent.putExtra("phoneNumber", phone_number);
            startActivity(intent);
        }
        else{
            if (id == R.id.nav_staff) {
                Intent intent = new Intent(this, AdStaffActivity.class);
                intent.putExtra("role", role); // Truyền role sang
                intent.putExtra("phoneNumber", phone_number);
                startActivity(intent);
            } else if (id == R.id.nav_customer) {
                Intent intent = new Intent(this, AdCustomerActivity.class);
                intent.putExtra("role", role); // Truyền role sang
                intent.putExtra("phoneNumber", phone_number);
                startActivity(intent);
            }
            else if (id == R.id.nav_logout) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            else if (id == R.id.nav_qllt) {
                Intent intent = new Intent(this, AdScheduleActivity.class);
                intent.putExtra("role", role); // Truyền role sang
                intent.putExtra("phoneNumber", phone_number);
                startActivity(intent);
            }
            else if (id == R.id.nav_car) {
                    Intent intent = new Intent(this, ManageCarActivity.class);
                    intent.putExtra("role", role); // Truyền role sang
                    intent.putExtra("phoneNumber", phone_number);
                    startActivity(intent);
            }
            else if (id == R.id.nav_ticketView) {
                Intent intent = new Intent(this, ViewTicketsActivity.class);
                intent.putExtra("role", role); // Truyền role sang
                String phoneNumber = getIntent().getStringExtra("phoneNumber");
                intent.putExtra("phoneNumber", phoneNumber);// Truyền role sang
                startActivity(intent);
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}