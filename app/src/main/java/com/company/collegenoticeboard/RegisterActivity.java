package com.company.collegenoticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText stuName;
    private EditText stuEmail;
    private EditText pass;
    private EditText confirmPass;
    private Spinner department;
    private Button regButton;
    private VolleySingleton vs;
    private final String[] depts = {"CSE", "ISE", "EEE", "ECE", "ME", "CE"};
    private Map<String, String> deptMap;
    private String dept;
    private Intent loginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        stuName = findViewById(R.id.regName);
        stuEmail = findViewById(R.id.regEmail);
        pass = findViewById(R.id.regPass);
        confirmPass = findViewById(R.id.regConfirmPass);
        department = findViewById(R.id.deptSpinner);
        regButton = findViewById(R.id.registerBtn);
        deptMap = new HashMap<>();
        for(int i = 1; i < depts.length; i++) {
            deptMap.put(depts[i], String.valueOf(i));
        }

        ArrayAdapter adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.simple_spinner_item, depts
        );
        department.setAdapter(adapter);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        regButton = findViewById(R.id.registerBtn);
        vs = VolleySingleton.getInstance(this.getApplicationContext());
        vs.getRequestQueue();
    }

    @Override
    protected void onStart() {
        super.onStart();
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dept = depts[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        regButton.setOnClickListener(this::requestServer);
    }

    public boolean validateReg(String username, String password,
                               String confirmPassword, String email) {
        if(username.length() > 0)
            if(email.indexOf('@') > 1)
                if(password.length() > 5 && (password.equals(confirmPassword))) {
                    return true;
                }
        return false;
    }

    public void requestServer(View view) {
        String username = stuName.getText().toString();
        String passwd = pass.getText().toString();
        String conPass = confirmPass.getText().toString();
        String email = stuEmail.getText().toString();

        String url = getString(R.string.requestUrl) +
                String.format(
                        "student/register/%s/%s/%s/%s/%s",
                        username, email, passwd, "9916120167", deptMap.get(dept)
                );

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        String regitered = response.getString("regitered");
                        if (regitered.equals("true")) {
                            loginIntent = new Intent(this, LoginActivity.class);

                            if (validateReg(username, passwd, conPass, email)) {
                                Toast.makeText(
                                        getApplicationContext(), "Registered", Toast.LENGTH_LONG
                                ).show();
                                startActivity(loginIntent);
                            }
                        }else {
                            showErrorMessage(view);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorMessage(view);
                    }
                },
                error -> showErrorMessage(view)
        );


        vs.getRequestQueue().start();
        vs.addToRequestQueue(req);
    }

    private void showErrorMessage(View view) {
        Snackbar snackbar = Snackbar.make(view, "Wrng Credentials", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RETRY", v-> {
            stuName.setText("");
            stuEmail.setText("");
            pass.setText("");
            confirmPass.setText("");
            stuName.requestFocus();
        });
        snackbar.show();
    }
}
