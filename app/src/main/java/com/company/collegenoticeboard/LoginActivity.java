package com.company.collegenoticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private EditText studentUserNameField;
    private EditText studentPasswordField;
    private Button signInButton;
    private Button registerButton;
    private CheckBox savePasswordCheckBox;
    private SharedPreferences.Editor shPrefEditor;
    private VolleySingleton vs;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        checkSignIn();
        studentUserNameField = findViewById(R.id.studentUserName);
        studentPasswordField = findViewById(R.id.studentPassword);
        signInButton = findViewById(R.id.signInButton);
        registerButton = findViewById(R.id.registration_button);
        savePasswordCheckBox = findViewById(R.id.savePasswordCheckBox);
        SharedPreferences sharedPreferences = getSharedPreferences("studentObject", MODE_PRIVATE);
        shPrefEditor = sharedPreferences.edit();
        vs = VolleySingleton.getInstance(this.getApplicationContext());
        vs.getRequestQueue();
    }

    @Override
    protected void onStart() {
        super.onStart();
        signInButton.setOnClickListener(this::requestServer);
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerButton.setOnClickListener(v -> startActivity(registerIntent));
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkSignIn();
    }

    private void checkSignIn() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        SharedPreferences spCheck = getSharedPreferences("studentObject", MODE_PRIVATE);
        if (spCheck.getBoolean("isLoggedIn", false)) {
            startActivity(mainActivityIntent);
        }
    }

    private void signInUser(String username, String deptId) {

        boolean savePassword = savePasswordCheckBox.isChecked();

        Intent intent = new Intent(this, MainActivity.class);

        shPrefEditor.putString("studentUserId", username);
        shPrefEditor.putString("studentDeptId", deptId);
        shPrefEditor.putBoolean("isLoggedIn", true);
        if (savePassword) {
            shPrefEditor.putBoolean("rememberMe", true);
            shPrefEditor.apply();
            startActivity(intent);
        } else {
            shPrefEditor.apply();
            startActivity(intent);
        }
    }

    private void requestServer(View view) {
        String username = studentUserNameField.getText().toString();
        String password = studentPasswordField.getText().toString();

        String url = getString(R.string.requestUrl) +
                String.format("student/auth/%s/%s",
                        username, password
                );

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        String studentName = response.getString("student_email");
                        String studentPassword = response.getString("student_pwd");
                        String deptid = response.getString("student_dept_id");
                        if(username.equals(studentName) && password.equals(studentPassword)) {
                            signInUser(username, deptid);
                        } else {
                            showLoginError(view);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> showLoginError(view)
        );
        vs.getRequestQueue().start();
        vs.addToRequestQueue(req);
    }

    private void showLoginError(View view) {
        Snackbar snackbar = Snackbar.make(
                view,"Wrong password or email", Snackbar.LENGTH_LONG
        ).setAction("RETRY", v -> {
            studentUserNameField.setText("");
            studentPasswordField.setText("");
            studentUserNameField.requestFocus();
        });
        snackbar.show();
    }
}
