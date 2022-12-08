package com.example.wealer.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wealer.DashBoard.DashBoard;
import com.example.wealer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class loginscreen extends AppCompatActivity {

    RequestQueue queue;
    EditText edtLoginUsername, edtLoginPassword;
    Button btnLogin, btnGoToSignUp;
    Intent intent;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);

        queue = Volley.newRequestQueue(this);

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToSignUp = findViewById(R.id.btnGoToSignUp);

        // Wake up the back end
//        hello();

        // When the login button is clicked,
        btnLogin.setOnClickListener(view -> {
            // If the username textbox is empty, prompt the user to enter a username
            if (edtLoginUsername.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
            // Otherwise, if the password textbox is empty, prompt the user to enter a password
            else if (edtLoginPassword.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
            // Otherwise, call a method to attempt to login to the server
            else {
                login(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString());
            }
        });

        // When the sign up button is clicked,
        btnGoToSignUp.setOnClickListener(view -> {
            // Start the sign up activity
            intent = new Intent(this, signupscreen.class);
            startActivity(intent);
        });
    }

    /**
     * All this does is make a call to the /hello route of the back end to wake it up before the first
     * login attempt.
     */
//    public void hello() {
//        final String apiLink = "https://project3-ceparker.onrender.com/hello";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiLink, null, response-> {}, error-> {});
//        queue.add(request);
//    }

    public void fetchUsers() {
        final String apiLink = "https://project3-ceparker.onrender.com/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiLink, null,
                response->{
                    try {
                        Object result1 = response.get("users");
                        Log.d("MYAPP", result1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("MYAPP", "An error occurred in test: " + e.toString());
                    }

                },
                error-> {
                    Log.d("MYAPP", "An error occurred: " + error);
                });

        queue.add(request);
    }

    /**
     * Attempts to login to the server with a given username and password. If the login was successful,
     * the user will be redirected to the dashboard activity
     */
    public void login(String username, String password) {
        final String apiLink = "https://project3-ceparker.onrender.com/login";

        // Build the request body, using the given username and password
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("username", username);
        body.put("password", password);

        /*
         * Test User:
         *      username: Test 10
         *      password: 123456789
         */

        // Make a post request to the server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiLink, new JSONObject(body),
                response->{

                    try {
                        // If the response contains the attribute "message",
                        if (response.has("message")) {
                            // The login was not successful. Record the message and display it to the user
                            String message = response.getString("message");
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                        // Otherwise, if the response contains the attribute "username",
                        else if (response.has("user")) {
                            // The login was successful. Store the corresponding username in SharedPreferences
                            // and start the dashboard activity.

                            // Get the shared preferences and add the corresponding username
                            sharedPreferences = getSharedPreferences("com.example.wealer", MODE_PRIVATE);
                            sharedPreferences.edit().putString("activeUsername", username);

                            // Start the dashboard activity
                            intent = new Intent(this, DashBoard.class);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        // If an error occurred in the login process, notify the user
                        Log.d("MyAPP", e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                },
                error-> {
                    // If an error occurred in the login process, notify the user
                    Log.d("MyAPP", error.getMessage());
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);

    }
}