package com.example.wealer.LoginSignUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

import java.util.HashMap;

public class signupscreen extends AppCompatActivity {

    RequestQueue queue;
    EditText edtSignUpUsername, edtSignUpEmail, edtSignUpPassword, edtSignUpConfirm;
    Button btnSignUp, btnGoToLogin;
    TextView txtPasswordMatch;
    Intent intent;
    SharedPreferences sharedPreferences;
    boolean passwordsMatch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupscreen_activity);

        queue = Volley.newRequestQueue(this);

        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        edtSignUpConfirm = findViewById(R.id.edtSignUpConfirm);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);
        txtPasswordMatch = findViewById(R.id.txtPasswordMatch);

        // Listens for changes in the confirm password textbox
        edtSignUpConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password1 = edtSignUpPassword.getText().toString();
                String password2 = edtSignUpConfirm.getText().toString();

                // If the passwords do not match,
                if (!password2.equals(password1)) {
                    // Indicate as such to the user
                    txtPasswordMatch.setText("Passwords Do Not Match");
                    txtPasswordMatch.setTextColor(0xFFFF0000);  // Sets the textview color to red
                    passwordsMatch = false;
                }
                // Otherwise,
                else {
                    // Indicate that the passwords match to the user
                    txtPasswordMatch.setText("Passwords Match");
                    txtPasswordMatch.setTextColor(0xFF37FF00);  // Sets the textview color to green
                    passwordsMatch = true;
                }

                // If the confirm password textbox is not empty, set it to visible
                if (password2.trim().length() != 0) {
                    txtPasswordMatch.setVisibility(View.VISIBLE);
                }
                // Otherwise, set it to invisible
                else {
                    txtPasswordMatch.setVisibility(View.INVISIBLE);
                    passwordsMatch = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // When the sign up button is clicked,
        btnSignUp.setOnClickListener(view -> {
            // If the username textbox is empty, prompt the user to enter a username
            if (edtSignUpUsername.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            }
            // Otherwise, if the email textbox is empty, prompt the user to enter an email address
            else if (edtSignUpEmail.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            }
            // Otherwise, if the password textbox is empty, prompt the user to enter a password
            else if (edtSignUpPassword.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
            // Otherwise, if the password and password confirmation do not match, notify the user
            else if (!passwordsMatch) {
                Toast.makeText(this, "Please enter matching passwords", Toast.LENGTH_SHORT).show();
            }
            // Otherwise, call a method to attempt to register a new user
            else {
                signUp(edtSignUpUsername.getText().toString(), edtSignUpEmail.getText().toString(), edtSignUpPassword.getText().toString());
            }
        });

        // When the back to login button is clicked,
        btnGoToLogin.setOnClickListener(view -> {
            // Finish this activity
            finish();
        });
    }

    /**
     * Attempts to register a new user with the server. If the registration was successful,
     * the user will be redirected to the dashboard activity
     */
    public void signUp(String username, String email, String password) {
        final String apiLink = "https://project3-ceparker.onrender.com/register";

        // Build the request body, using the given username, email, and password
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("username", username);
        body.put("email", email);
        body.put("password", password);

        // Make a post request to the server
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiLink, new JSONObject(body),
                response->{
                    try {
                        // Regardless of success or failure, the server will return an object with the
                        // attribute "message". If the message is "Successful Registration", username
                        // in SharedPreference and start the dashboard activity

                        if (response.getString("message").equals("Successful Registration")) {
                            // Get the shared preferences and add the corresponding username
                            sharedPreferences = getSharedPreferences("com.example.wealer", MODE_PRIVATE);
                            sharedPreferences.edit().putString("activeUsername", username);

                            // Start the dashboard activity
                            intent = new Intent(this, DashBoard.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        // If an error occurred in the registration process, notify the user
                        e.printStackTrace();
                        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                },
                error-> {
                    // If an error occurred in the registration process, notify the user
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);

    }
}