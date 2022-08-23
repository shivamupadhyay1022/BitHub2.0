package com.su.bithub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText name;
    private EditText sec;
    private EditText semester;
    private EditText admyear;
    private Button register;

    TextInputLayout lay_branch;
    AutoCompleteTextView act_branch;

    ArrayList<String> arraylist_branch;
    ArrayAdapter<String> arrayAdapter_branch;

    private FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lay_branch =(TextInputLayout)findViewById(R.id.layout_branch);
        act_branch =(AutoCompleteTextView) findViewById(R.id.select_branch);

        arraylist_branch = new ArrayList<>();
        arraylist_branch.add("ECE");
        arraylist_branch.add("CSE");
        arraylist_branch.add("IT");
        arraylist_branch.add("Mechanical");
        arraylist_branch.add("Chemistry");
        arraylist_branch.add("QEDS");

        arrayAdapter_branch = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdownlist,arraylist_branch);
        act_branch.setAdapter(arrayAdapter_branch);

        act_branch.setThreshold(1);
        //=== how many characters requires spinner suggestion===



        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register1);
        name = findViewById(R.id.login_name);
        sec = findViewById(R.id.login_section);
        admyear = findViewById(R.id.login_admyear);

        auth = FirebaseAuth.getInstance();

//        List<String> branches = Arrays.asList("CS", "IT", "ECE", "Mechanical", "Chemical", "Production", "QEDS");
//        final Spinner spinner1 = findViewById(R.id.login_branch);
//        ArrayAdapter adapter1 = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, branches);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(adapter1);
//
//        List<String> years = Arrays.asList("1st Year", "2nd Year", "3rd Year", "4th Year", "5th Year", "6th Year", "7th Year", "8th Year");
//        final Spinner spinner2 = findViewById(R.id.login_branch);
//        ArrayAdapter adapter2 = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, years);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(adapter2);

        db = FirebaseFirestore.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString().trim();
                String txt_password = password.getText().toString().trim();
//                String branch = spinner1.getSelectedItem().toString();
                String txt_name = name.getText().toString().trim();
                String txt_section = sec.getText().toString().trim();
                String txt_semester = semester.getText().toString();
//                String yearno  = spinner2.getSelectedItem().toString();


                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
//                    registerUser(txt_email, txt_password, branch, txt_name, txt_section, txt_semester,yearno);
                }
            }
        });
    }

    private void registerUser(String email, String password, String branch, String name, String section, String semester, String yearno) {

        String ADMYear = admyear.getText().toString();

        Map<String, Object> info = new HashMap<>();

        info.put("Email", email);
        info.put("Password", password);
        info.put("Name", name);
        info.put("Branch", branch);
        info.put("Section", section);
        info.put("Semester", semester);
        info.put("Admission Year", ADMYear);
        info.put("Id", auth.getCurrentUser().getUid());

        db.collection(yearno).document(name)
                .set(info)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            Toast.makeText(RegisterActivity.this, "Updated Successfully",Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(RegisterActivity.this, "User with same email alreday Exits",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivity.this, "Update error.",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(RegisterActivity.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}
