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
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.DatePickerDialog.*;

public class add extends AppCompatActivity implements View.OnClickListener {
    Button btnDatePicker, btnTimePicker,save;
    TextView txtDate, txtTime;
    EditText titlee;
    String secc;
    private int mYear, mMonth, mDay, mHour, mMinute;
    FirebaseAuth mauth;
    String dat;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        titlee=(EditText)findViewById(R.id.titlee);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        save=(Button)findViewById(R.id.save);
        txtDate=(TextView) findViewById(R.id.in_date);
        txtTime=(TextView) findViewById(R.id.in_time);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        Date c = Calendar.getInstance().getTime();
        Calendar rightNow = Calendar.getInstance();
        Integer t=rightNow.get(Calendar.DATE);
        Integer tt=rightNow.get(Calendar.MONTH)+1;
        Integer ttt= rightNow.get(Calendar.YEAR);
        final String datee=t+"-"+tt+"-"+ttt;
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titlee.getText().toString().equals("") || txtDate.getText().toString().equals("") || txtTime.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }
                else if(dat.equals(datee)){
                    if(Integer.parseInt(mil.toString())>Integer.parseInt(secc.toString())){
                        Toast.makeText(getApplicationContext(), "Time already passed", Toast.LENGTH_SHORT).show();
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
                                        peg.setTime(txtTime.getText().toString());
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
                                    peg.setTime(txtTime.getText().toString());
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

        if (v == btnDatePicker) {

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
                            dat=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

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
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(add.this,Mainclass.class);
        startActivity(intent);
    }
}
