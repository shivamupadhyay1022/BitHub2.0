package com.su.bithub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText name;
    private EditText sec;
    private EditText semester;
    private EditText year;
    private Button register;

    private FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register1);
        name     = findViewById(R.id.login_name);
        sec      = findViewById(R.id.login_section);
        semester = findViewById(R.id.login_semester);
        year     = findViewById(R.id.login_year);

        auth = FirebaseAuth.getInstance();

        List<String> states= Arrays.asList("CS","IT","ECE","Mechanical","Chemical","Production","QEDS");
        final Spinner spinner=findViewById(R.id.login_branch);
        ArrayAdapter adapter=new ArrayAdapter(RegisterActivity.this,android.R.layout.simple_spinner_item,states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email    = email.getText().toString().trim();
                String txt_password = password.getText().toString().trim();
                String branch       = spinner.getSelectedItem().toString();
                String txt_name     = name.getText().toString();
                String txt_section  = sec.getText().toString();
                String txt_semester = semester.getText().toString();






                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password, branch, txt_name,txt_section,txt_semester);
                }
            }
        });
    }

    private void registerUser(String email, String password, String branch, String name, String section, String semester) {

        String Year = year.getText().toString();

        Map<String,Object> info= new HashMap<>();

        info.put("Email",email);
        info.put("Password",password);
        info.put("Name",name);
        info.put("Branch",branch);
        info.put("Section",section);
        info.put("Semester",semester);

        db.collection("1 Year").document(name).set(info);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(RegisterActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}
