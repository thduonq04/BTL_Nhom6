package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
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

import com.google.android.material.navigation.NavigationView;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;

public class CustomerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private String role;
    private String phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflater = getLayoutInflater();

        drawerLayout = findViewById(R.id.Customermain);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        role = getIntent().getStringExtra("role");
        phone_number = getIntent().getStringExtra("phone_number");

        if(role.equals("Khách hàng")){
            navigationView.getMenu().findItem(R.id.nav_customer).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_qllt).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_qlduong).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_car).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_staff).setVisible(false);

        }
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_staff).setVisible(false);
        menu.findItem(R.id.nav_customer).setVisible(false);
        menu.findItem(R.id.nav_qllt).setVisible(false);
        menu.findItem(R.id.nav_qlduong).setVisible(false);
        menu.findItem(R.id.nav_car).setVisible(false);



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

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.nav_ticketView){
            Intent intent = new Intent(this, ViewTicketsActivity.class);
            intent.putExtra("phoneNumber", phone_number);
            intent.putExtra("role", role); // Truyền role sang
            startActivity(intent);
        }else{
            if (id == R.id.nav_ticket) {
                Intent intent = new Intent(this, BookTicketActivity.class);
                intent.putExtra("phoneNumber", phone_number);
                intent.putExtra("role", role); // Truyền role sang
                startActivity(intent);
            }
            else if (id == R.id.nav_review) {
                showReviewDialog();
            }
            else if (id == R.id.nav_logout) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("role", role); // Truyền role sang
                startActivity(intent);
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}