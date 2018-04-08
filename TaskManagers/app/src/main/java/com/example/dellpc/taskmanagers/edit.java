package com.example.dellpc.taskmanagers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class edit extends AppCompatActivity implements View.OnClickListener {
    String title,time,date,delete,status,mail;
    Button btnDatePicker, btnTimePicker,save;
    TextView txtDate, txtTime;
    EditText titlee;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        titlee=(EditText)findViewById(R.id.ttitlee);
        btnDatePicker=(Button)findViewById(R.id.bbtn_date);
        btnTimePicker=(Button)findViewById(R.id.bbtn_time);
        save=(Button)findViewById(R.id.ssave);
        txtDate=(TextView) findViewById(R.id.iin_date);
        txtTime=(TextView) findViewById(R.id.iin_time);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        title=getIntent().getExtras().getString("title");
        time=getIntent().getExtras().getString("time");
        date=getIntent().getExtras().getString("date");
        delete=getIntent().getExtras().getString("delete");
        status=getIntent().getExtras().getString("status");
        mail=getIntent().getExtras().getString("mail");
        titlee.setText(title);
        txtDate.setText(date);
        txtTime.setText(time);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titlee.getText().toString().equals("") || txtDate.getText().toString().equals("") || txtTime.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
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
                                                            sub.child(key).child("time").setValue(txtTime.getText().toString());
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            Intent i = new Intent(edit.this, Mainclass.class);
                                            Toast.makeText(getApplicationContext(),"Task edited",Toast.LENGTH_SHORT).show();
                                            startActivity(i);

                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent i = new Intent(edit.this, Mainclass.class);
                                   startActivity(i);
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

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(edit.this,Mainclass.class);
        startActivity(intent);
    }
}
