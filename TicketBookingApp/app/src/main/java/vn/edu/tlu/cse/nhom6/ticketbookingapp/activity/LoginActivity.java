package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextPhoneNumber, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        db = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = editTextPhoneNumber.getText().toString();
                String password = editTextPassword.getText().toString();
                if (db.checkUser(phone_number)) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    String role = db.checkRole(phone_number, password);
                    if(role.equals("Nhân viên")){
                        Intent intent = new Intent(LoginActivity.this, StaffMainActivity.class);
                        intent.putExtra("phone_number", phone_number);
                        startActivity(intent);
                        finish();
                    }else{
                        if (role.equals("Khách hàng")){
                            Intent intent = new Intent(LoginActivity.this, CustomerMainActivity.class);
                            intent.putExtra("phone_number",phone_number);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            if(role.equals("Admin")){
                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                intent.putExtra("phone_number", phone_number);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}