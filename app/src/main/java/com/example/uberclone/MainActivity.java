package com.example.uberclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (editTextDOP.getText().toString().equals("Driver") || editTextDOP.getText().toString().equals("Passenger")) {
            if (ParseUser.getCurrentUser() == null) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null)
                        {
                            Toast.makeText(MainActivity.this, "Anonymous User", Toast.LENGTH_SHORT).show();
                            user.put("as", editTextDOP.getText().toString());
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        transitionToPassengerActivity();
                                        transitionToDriverRequestListActivity();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    enum State {
        SIGNUP, LOGIN
    }
    private State state;
    private Button buttonSignUp, buttonDOP;
    private RadioButton radioButtonPassenger, radioButtonDriver;
    EditText editTextUserName, editTextPassword, editTextDOP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        if (ParseUser.getCurrentUser() != null) {
            //ParseUser.logOut();
            transitionToPassengerActivity();
            transitionToDriverRequestListActivity();
        }

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonDOP = findViewById(R.id.buttonDOP);
        buttonDOP.setOnClickListener(this);

        state = State.SIGNUP;

        radioButtonDriver = findViewById(R.id.radioButtonDriver);
        radioButtonPassenger = findViewById(R.id.radioButtonPassenger);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextDOP = findViewById(R.id.editTextDOP);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == State.SIGNUP) {
                    if (radioButtonDriver.isChecked() == false && radioButtonPassenger.isChecked() == false) {
                        Toast.makeText(MainActivity.this, "Are you a driver or passenger?", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ParseUser appUser = new ParseUser();
                    appUser.setUsername(editTextUserName.getText().toString());
                    appUser.setPassword(editTextPassword.getText().toString());
                    if (radioButtonDriver.isChecked()) {
                        appUser.put("as", "Driver");
                    } else if (radioButtonPassenger.isChecked()) {
                        appUser.put("as", "Passenger");
                    }
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "Signed Up!", Toast.LENGTH_SHORT).show();
                                transitionToPassengerActivity();
                                transitionToDriverRequestListActivity();
                            }
                        }
                    });
                } else if (state == State.LOGIN) {
                    ParseUser.logInInBackground(editTextUserName.getText().toString(),
                            editTextPassword.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {
                                        Toast.makeText(MainActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();
                                        transitionToPassengerActivity();
                                        transitionToDriverRequestListActivity();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.loginItem:
                if (state == State.SIGNUP) {
                    state = State.LOGIN;
                    item.setTitle("Sign Up");
                    buttonSignUp.setText("Login");
                } else if (state == State.LOGIN) {
                    state = State.SIGNUP;
                    item.setTitle("Login");
                    buttonSignUp.setText("Sign Up");
            }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void transitionToPassengerActivity() {
        if (ParseUser.getCurrentUser() != null) {
            if (ParseUser.getCurrentUser().get("as").equals("Passenger")) {
                Intent intent = new Intent(MainActivity.this, PassengerActivity.class);
                startActivity(intent);
            }
        }
    }
    private void transitionToDriverRequestListActivity() {
        if (ParseUser.getCurrentUser() != null) {
            if (ParseUser.getCurrentUser().get("as").equals("Driver")) {
                startActivity(new Intent(MainActivity.this, DriverRequestListActivity.class));
            }
        }
    }
}