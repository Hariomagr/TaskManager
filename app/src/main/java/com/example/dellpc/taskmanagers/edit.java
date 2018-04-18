package com.example.dellpc.taskmanagers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class edit extends AppCompatActivity implements View.OnClickListener {
    String title,time,date,delete,status,mail;
    FloatingActionButton save;
    TextView txtDate, txtTime;
    EditText titlee;
    String secc;
    String finaltime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String dat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        titlee = (EditText) findViewById(R.id.input_ttitlee);
        save = (FloatingActionButton) findViewById(R.id.ssave);
        txtDate = (TextView) findViewById(R.id.iin_date);
        txtTime = (TextView) findViewById(R.id.iin_time);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        title = getIntent().getExtras().getString("title");
        time = getIntent().getExtras().getString("time");
        SimpleDateFormat _244HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _122HourSDF = new SimpleDateFormat("hh:mm a");
        try {
            Date _24HourDt = _244HourSDF.parse(time);
            txtTime.setText( _122HourSDF.format(_24HourDt).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date = getIntent().getExtras().getString("date");
        delete = getIntent().getExtras().getString("delete");
        status = getIntent().getExtras().getString("status");
        mail = getIntent().getExtras().getString("mail");
        Date c = Calendar.getInstance().getTime();
        Calendar rightNow = Calendar.getInstance();
        Integer t=rightNow.get(Calendar.DATE);
        Integer tt=rightNow.get(Calendar.MONTH)+1;
        Integer ttt= rightNow.get(Calendar.YEAR);
        final String datee=t+"/"+tt+"/"+ttt;
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentmin = rightNow.get(Calendar.MINUTE);
        final String mil=Integer.toString(currentHour*3600+currentmin*60);
        titlee.setText(title);
        txtDate.setText(date);
        final String[] timee = time.split(":");
        String hr = timee[0];
        String mi = timee[1];
        secc=Integer.toString(Integer.parseInt((hr.toString()))*3600+Integer.parseInt(mi.toString())*60);
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
                if (titlee.getText().toString().equals("") || txtDate.getText().toString().equals("") || txtTime.getText().toString().equals("")) {
                    StyleableToast.makeText(getApplicationContext(), "Fill all fields!", R.style.fillall).show();
                }
                else if (txtDate.getText().toString().equals(datee)) {
                    if (Integer.parseInt(mil.toString()) > Integer.parseInt(secc.toString())) {
                        StyleableToast.makeText(getApplicationContext(), "Time passed!", R.style.passed).show();
                    }
                    else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(edit.this);
                        builder1.setMessage("Edit this task");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(mail)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String statuss = snapshot.child("status").getValue(String.class);
                                                            String datee = snapshot.child("date").getValue(String.class);
                                                            String deletee = snapshot.child("delete").getValue(String.class);
                                                            String timee = snapshot.child("time").getValue(String.class);
                                                            String titleee = snapshot.child("title").getValue(String.class);
                                                            String key = snapshot.getRef().getKey();
                                                            DatabaseReference sub = FirebaseDatabase.getInstance().getReference("TODO");
                                                            if (statuss.equals(status) && datee.equals(date) && deletee.equals(delete) && timee.equals(time) && titleee.equals(title)) {
                                                                sub.child(key).child("title").setValue(titlee.getText().toString());
                                                                sub.child(key).child("date").setValue(txtDate.getText().toString());
                                                                sub.child(key).child("time").setValue(finaltime.toString());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent i = new Intent(edit.this, Mainclass.class);
                                                StyleableToast.makeText(getApplicationContext(), "Task Edited!", R.style.mytoast).show();
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
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(edit.this);
                    builder1.setMessage("Edit this task");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(mail)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        String statuss = snapshot.child("status").getValue(String.class);
                                                        String datee = snapshot.child("date").getValue(String.class);
                                                        String deletee = snapshot.child("delete").getValue(String.class);
                                                        String timee = snapshot.child("time").getValue(String.class);
                                                        String titleee = snapshot.child("title").getValue(String.class);
                                                        String key = snapshot.getRef().getKey();
                                                        DatabaseReference sub = FirebaseDatabase.getInstance().getReference("TODO");
                                                        if (statuss.equals(status) && datee.equals(date) && deletee.equals(delete) && timee.equals(time) && titleee.equals(title)) {
                                                            sub.child(key).child("title").setValue(titlee.getText().toString());
                                                            sub.child(key).child("date").setValue(txtDate.getText().toString());
                                                            sub.child(key).child("time").setValue(finaltime.toString());
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(edit.this, Mainclass.class);
                                            StyleableToast.makeText(getApplicationContext(), "Task Edited!", R.style.mytoast).show();
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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(edit.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(edit.this,Mainclass.class);
        startActivity(intent);
    }
}
