package com.company.collegenoticeboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.beans.Notice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String deptId;
    private ArrayList<Notice> notices;
    private Intent loginIntent;
    private SharedPreferences sharedPreferences;
    private VolleySingleton vs;
    private NotificationsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        this.notices = new ArrayList<>();
        TextView helloUser = findViewById(R.id.helloUser);
        Button logoutButton = findViewById(R.id.logoutButton);
        RecyclerView recyclerView = findViewById(R.id.notificationsRecycler);
        loginIntent = new Intent(this, LoginActivity.class);
        sharedPreferences = getSharedPreferences("studentObject", MODE_PRIVATE);
        vs = VolleySingleton.getInstance(this.getApplicationContext());
        checkLogin();
        String userName = sharedPreferences.getString("studentUserId", null);
        deptId = sharedPreferences.getString("studentDeptId", null);
        if (userName != null) {
            String user = userName.substring(0, userName.indexOf('@'));
            helloUser.setText(String.format("Hello %s", user));
        }
        createNotices();
        logoutButton.setOnClickListener(view -> logoutButtonFunc());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationsListAdapter(this.notices);
        recyclerView.setAdapter(adapter);
        rememberMeCheck();
    }

    // This function can be put into Notices class so as to make it a ORM class
    private void createNotices() {
        String url = getString(R.string.requestUrl) + "notice/" + this.deptId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                this.notices.add(new Notice(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("message"),
                                        jsonObject.getString("notice_dept_id")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                },
                error -> {
                        this.notices.add(
                                new Notice("-1", "X", "ERROR", "-1")
                        );
                        adapter.notifyDataSetChanged();
                }
        );
        vs.getRequestQueue().start();
        vs.addToRequestQueue(jsonArrayRequest);
    }

    private void checkLogin() {
        if(!sharedPreferences.getBoolean("isLoggedIn", false) ) {
            startActivity(loginIntent);
        }
    }

    void rememberMeCheck() {
        if(!sharedPreferences.getBoolean("rememberMe", false)) {
            clearData();
        }
    }

    private void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void logoutButtonFunc() {
        clearData();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
