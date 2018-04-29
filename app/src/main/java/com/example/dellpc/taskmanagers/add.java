package com.example.dellpc.taskmanagers;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.DatePickerDialog.*;
import static com.example.dellpc.taskmanagers.R.string.valida;

public class add extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton save;
    EditText txtDate, txtTime;
    EditText titlee;
    private TextInputLayout inputLayouttit;
    String secc;
    String finaltime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    FirebaseAuth mauth;
    String dat;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        titlee=(EditText)findViewById(R.id.input_titlee);
        inputLayouttit = findViewById(R.id.input_layout_titlee);
        titlee.addTextChangedListener(new MyTextWatcher(titlee));
        save=(FloatingActionButton) findViewById(R.id.save);
        txtDate=(EditText) findViewById(R.id.in_date);
        txtTime=(EditText) findViewById(R.id.in_time);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        Date c = Calendar.getInstance().getTime();
        Calendar rightNow = Calendar.getInstance();
        Integer t=rightNow.get(Calendar.DATE);
        Integer tt=rightNow.get(Calendar.MONTH)+1;
        Integer ttt= rightNow.get(Calendar.YEAR);
        final String datee=t+"/"+tt+"/"+ttt;
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentmin = rightNow.get(Calendar.MINUTE);
        final String mil=Integer.toString(currentHour*3600+currentmin*60);
        mauth = FirebaseAuth.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        databaseReference = FirebaseDatabase.getInstance().getReference("TODO");
        final FirebaseUser user = mauth.getCurrentUser();
        final String email = user.getEmail();
        titlee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatetit()) {
                    return;
                }
                if (titlee.getText().toString().equals("") || txtDate.getText().toString().equals("") || txtTime.getText().toString().equals("")) {
                   Toast.makeText(getApplicationContext(),"Fill all fields",Toast.LENGTH_SHORT).show();
                }
                else if(dat.equals(datee)){
                    if(Integer.parseInt(mil.toString())>Integer.parseInt(secc.toString())){
                        Toast.makeText(getApplicationContext(),"Time  Passed",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(add.this);
                        builder1.setMessage("Add this task");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String idd = databaseReference.push().getKey();
                                        Person peg = new Person();
                                        peg.setTitle(titlee.getText().toString());
                                        peg.setDate(txtDate.getText().toString());
                                        peg.setMail(email);
                                        peg.setStatus("0");
                                        peg.setDelete("0");
                                        peg.setTime(finaltime.toString());
                                        databaseReference.child(idd).setValue(peg);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent i = new Intent(add.this, Mainclass.class);
                                                Toast.makeText(getApplicationContext(),"Task Added",Toast.LENGTH_SHORT).show();
                                                startActivity(i);
                                            }
                                        }, 300);
                                    }
                                });

                        builder1.setNegativeButton(
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(add.this);
                    builder1.setMessage("Add this task");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String idd = databaseReference.push().getKey();
                                    Person peg = new Person();
                                    peg.setTitle(titlee.getText().toString());
                                    peg.setDate(txtDate.getText().toString());
                                    peg.setMail(email);
                                    peg.setStatus("0");
                                    peg.setDelete("0");
                                    peg.setTime(finaltime);
                                    databaseReference.child(idd).setValue(peg);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(add.this, Mainclass.class);
                                            Toast.makeText(getApplicationContext(),"Task Added",Toast.LENGTH_SHORT).show();
                                            startActivity(i);
                                        }
                                    }, 300);
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            dat=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }
        if (v == txtTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            secc=Integer.toString(hourOfDay*3600+minute*60);

                            String time24=hourOfDay+":"+minute;
                            finaltime=hourOfDay+":"+minute;
                            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                            try {
                                Date _24HourDt = _24HourSDF.parse(time24);
                               txtTime.setText( _12HourSDF.format(_24HourDt).toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
    private boolean validatetit() {
        String email = titlee.getText().toString().trim();

        if (email.isEmpty()) {
            inputLayouttit.setError(String.format(getString(R.string.valida)));
            requestFocus(titlee);
            return false;
        } else {
            inputLayouttit.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_titlee:
                    validatetit();
                    break;
            }
        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(add.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(add.this,Mainclass.class);
        startActivity(intent);
    }
}
