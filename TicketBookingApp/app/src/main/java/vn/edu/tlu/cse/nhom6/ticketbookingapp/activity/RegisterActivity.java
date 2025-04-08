package vn.edu.tlu.cse.nhom6.ticketbookingapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tlu.cse.nhom6.ticketbookingapp.R;
import vn.edu.tlu.cse.nhom6.ticketbookingapp.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber, editTextEmail, editTextPassword, editTextFullName;
    private Button buttonRegister;

    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //anh xa
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFullName = findViewById(R.id.editTextFullName);
        buttonRegister = findViewById(R.id.buttonRegister);
        db = new DatabaseHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = editTextPhoneNumber.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String full_name = editTextFullName.getText().toString();
                if (phone_number.isEmpty() || email.isEmpty()||password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    //check định dạng email
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        editTextEmail.setError("Email không hợp lệ");
                        Toast.makeText(RegisterActivity.this, "Email sai định dạng. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "ok fine", Toast.LENGTH_SHORT).show();
                        if(db.checkUser(phone_number)){
                            Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            Boolean insert = db.insertCustomer(phone_number,email, password, full_name); // tạo tài khoản KH với quyền KH
                            if (insert){
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent =  new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                            }


//                            try {
//                                db.insertCustomer(phone_number,email, password, full_name); // tạo tài khoản KH với quyền KH
//                            }
//                            catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
//                            Intent intent =  new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    }
                }
            }
        });
    }
}