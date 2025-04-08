package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_main);
        Toast.makeText(this, "Xin tiền!", Toast.LENGTH_SHORT).show();
    }
}