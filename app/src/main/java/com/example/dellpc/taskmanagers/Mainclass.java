package com.example.dellpc.taskmanagers;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.dellpc.taskmanagers.models.StudentModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Mainclass extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private FirebaseAuth mauth;
    private GoogleApiClient mGoogleApiClient;
    DatabaseReference databaseReference;
    private RecyclerView mRecyclerView;
    private String mail="";
    private ArrayList<StudentModel> mDataSet;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String s="0";
    final private static ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainclass);
        progressDialog = new ProgressDialog(Mainclass.this);
        progressDialog.setMessage("Its loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipee);
        swipeRefreshLayout.setOnRefreshListener(this);
        mauth = FirebaseAuth.getInstance();
        final FirebaseUser user = mauth.getCurrentUser();
        String nname="";
        if(user!=null){
            mail=user.getEmail();
            nname=user.getDisplayName();
        }
        final AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        for(int idx = 0 ; idx < intentArray.size() ; idx++){
            alarmManager .cancel(intentArray.get(idx));
        }
        final ArrayList<String> tim=new ArrayList<String>();
        final ArrayList<String> dat=new ArrayList<String>();
        final ArrayList<String> tit=new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(mail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String date=snapshot.child("date").getValue(String.class);
                            String status=snapshot.child("status").getValue(String.class);
                            String time=snapshot.child("time").getValue(String.class);
                            String title=snapshot.child("title").getValue(String.class);
                            String delete=snapshot.child("delete").getValue(String.class);
                            if(delete.equals("0")  && status.equals("0")){
                                tim.add(time);
                                dat.add(date);
                                tit.add(title);
                            }
                        }
                        for(int i = 0; i < tim.size(); ++i) {
                            long diff = 0;
                            String datt = dat.get(i);
                            String timm = tim.get(i);
                            String[] date = datt.split("-");
                            String mo = date[1];
                            String da = date[0];
                            String yr = date[2];
                            String[] time = timm.split(":");
                            String hr = time[0];
                            String mi = time[1];
                            if (hr.length() == 1) {
                                hr = "0" + hr;
                            }
                            if (mi.length() == 1) {
                                mi = "0" + mi;
                            }
                            if (da.length() == 1) {
                                da = "0" + da;
                            }
                            if (mo.length() == 1) {
                                mo = "0" + mo;
                            }
                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            String enddate = mo + "/" + da + "/" + yr + " " + hr + ":" + mi + ":" + "00";
                            Date datte = new Date();
                            String dateStart = format.format(datte);
                            Date d1 = null;
                            Date d2 = null;

                            try {
                                d1 = format.parse(dateStart);
                                d2 = format.parse(enddate);
                                diff = d2.getTime() - d1.getTime();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (diff > 0) {
                                Intent intent = new Intent(Mainclass.this, AlarmReciever.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(Mainclass.this, i, intent, 0);
                                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+diff, pendingIntent);
                                intentArray.add(pendingIntent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hey "+nname);
        setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference("TODO");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

        mDataSet = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(mail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDataSet.clear();
                        progressDialog.dismiss();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String date=snapshot.child("date").getValue(String.class);
                            String maiil=snapshot.child("mail").getValue(String.class);
                            String status=snapshot.child("status").getValue(String.class);
                            String time=snapshot.child("time").getValue(String.class);
                            String title=snapshot.child("title").getValue(String.class);
                            String delete=snapshot.child("delete").getValue(String.class);
                            if(delete.equals("0")){
                                mDataSet.add(new StudentModel(maiil,status,date,time,title,delete));
                            }
                        }
                        if(mDataSet.isEmpty()){
                            Toast.makeText(getApplicationContext(),"No record",Toast.LENGTH_SHORT).show();
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        SwipeRecyclerViewAdapter mAdapter = new SwipeRecyclerViewAdapter(Mainclass.this,mDataSet);
                        ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainclass, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            Intent i = new Intent(Mainclass.this,add.class);
            startActivity(i);
        }
        else if(id == R.id.filter){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Mainclass.this);
            builder1.setMessage("Select Type");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Completed",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            s="1";
                            progressDialog.show();
                            FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(mail)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mDataSet.clear();
                                            progressDialog.dismiss();
                                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                String date=snapshot.child("date").getValue(String.class);
                                                String maiil=snapshot.child("mail").getValue(String.class);
                                                String status=snapshot.child("status").getValue(String.class);
                                                String time=snapshot.child("time").getValue(String.class);
                                                String title=snapshot.child("title").getValue(String.class);
                                                String delete=snapshot.child("delete").getValue(String.class);
                                                if(delete.equals("0") && status.equals("1")){
                                                    mDataSet.add(new StudentModel(maiil,status,date,time,title,delete));
                                                }
                                            }
                                            if(mDataSet.isEmpty()){
                                                mRecyclerView.setVisibility(View.VISIBLE);
                                                Toast.makeText(getApplicationContext(),"No record",Toast.LENGTH_SHORT).show();
                                            }else{
                                                mRecyclerView.setVisibility(View.VISIBLE);
                                            }
                                            SwipeRecyclerViewAdapter mAdapter = new SwipeRecyclerViewAdapter(Mainclass.this,mDataSet);
                                            ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
                                            mRecyclerView.setAdapter(mAdapter);
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    });

            builder1.setNegativeButton(
                    "Pending",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            s="2";
                            progressDialog.show();
                            FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(mail)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mDataSet.clear();
                                            progressDialog.dismiss();
                                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                String date=snapshot.child("date").getValue(String.class);
                                                String maiil=snapshot.child("mail").getValue(String.class);
                                                String status=snapshot.child("status").getValue(String.class);
                                                String time=snapshot.child("time").getValue(String.class);
                                                String title=snapshot.child("title").getValue(String.class);
                                                String delete=snapshot.child("delete").getValue(String.class);
                                                if(delete.equals("0") && status.equals("0")){
                                                    mDataSet.add(new StudentModel(maiil,status,date,time,title,delete));
                                                }
                                            }
                                            if(mDataSet.isEmpty()){
                                                mRecyclerView.setVisibility(View.VISIBLE);
                                                Toast.makeText(getApplicationContext(),"No record",Toast.LENGTH_SHORT).show();
                                            }else{
                                                mRecyclerView.setVisibility(View.VISIBLE);
                                            }
                                            SwipeRecyclerViewAdapter mAdapter = new SwipeRecyclerViewAdapter(Mainclass.this,mDataSet);
                                            ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
                                            mRecyclerView.setAdapter(mAdapter);
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else if(id == R.id.logout){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure...");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "LogOut",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                    new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {
                                            Intent i = new Intent(Mainclass.this,MainActivity.class);
                                            startActivity(i);
                                        }
                                    }
                            );
                        }
                    });

            builder1.setNegativeButton(
                    "Stay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        swipeRefreshLayout.setRefreshing(true);
        FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(mail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDataSet.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String date=snapshot.child("date").getValue(String.class);
                            String maiil=snapshot.child("mail").getValue(String.class);
                            String status=snapshot.child("status").getValue(String.class);
                            String time=snapshot.child("time").getValue(String.class);
                            String title=snapshot.child("title").getValue(String.class);
                            String delete=snapshot.child("delete").getValue(String.class);
                            if(s.equals("0")){
                                if(delete.equals("0")) {
                                    mDataSet.add(new StudentModel(maiil, status, date, time, title, delete));
                                }
                            }
                            else if(s.equals("1")){
                                if(delete.equals("0") && status.equals("1")){
                                    mDataSet.add(new StudentModel(maiil,status,date,time,title,delete));
                                }
                            }
                            else if(s.equals("2")){
                                if(delete.equals("0") && status.equals("0")){
                                    mDataSet.add(new StudentModel(maiil,status,date,time,title,delete));
                                }
                            }
                        }
                        if(mDataSet.isEmpty()){
                            mRecyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"No record",Toast.LENGTH_SHORT).show();
                        }else{
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        SwipeRecyclerViewAdapter mAdapter = new SwipeRecyclerViewAdapter(Mainclass.this,mDataSet);
                        ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
                        mRecyclerView.setAdapter(mAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        swipeRefreshLayout.setRefreshing(false);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
    }
}
