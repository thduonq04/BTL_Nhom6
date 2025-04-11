package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private String role;

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
        if (role.equals("Admin")) {
            navigationView.getMenu().findItem(R.id.nav_staff).setVisible(true);
        }
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_staff).setVisible(true);




        if (savedInstanceState == null) {
            // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            //navigationView.setCheckedItem(R.id.nav_customer);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_staff) {
            Intent intent = new Intent(this, AdStaffActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        } else if (id == R.id.nav_customer) {
            Intent intent = new Intent(this, AdCustomerActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }
        else if (id == R.id.nav_qllt) {
            Intent intent = new Intent(this, AdScheduleActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }
        else if (id == R.id.nav_qlduong) {
            Intent intent = new Intent(this, AdRouteActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }
        else if(id==R.id.nav_car){
            Intent intent = new Intent(this, ManageCarActivity.class);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}