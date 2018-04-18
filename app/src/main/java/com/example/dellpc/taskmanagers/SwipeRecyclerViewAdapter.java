package com.example.dellpc.taskmanagers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.dellpc.taskmanagers.models.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;


public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {
    private Context mContext;
    private ArrayList<StudentModel> studentList;
    String sts;
    public SwipeRecyclerViewAdapter(Context context, ArrayList<StudentModel> objects) {
        this.mContext = context;
        this.studentList = objects;
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final StudentModel item = studentList.get(position);
        sts=item.getStatus();
        viewHolder.tvName.setText(item.getTitle());
        if(item.getStatus().equals("0")){
            viewHolder.btnLocation.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
        }
        else{
            viewHolder.btnLocation.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_24dp);
        }

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //drag from left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        //drag from right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wraper));


        //handling different event when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,edit.class);
                i.putExtra("title",item.getTitle());
                i.putExtra("date",item.getDate());
                i.putExtra("time",item.getTime());
                i.putExtra("delete",item.getDelete());
                i.putExtra("mail",item.getMail());
                i.putExtra("status",item.getStatus());
                mContext.startActivity(i);

            }
        });

        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(item.getMail())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    String status=snapshot.child("status").getValue(String.class);
                                    String date=snapshot.child("date").getValue(String.class);
                                    String delete=snapshot.child("delete").getValue(String.class);
                                    String time=snapshot.child("time").getValue(String.class);
                                    String title=snapshot.child("title").getValue(String.class);
                                    String key =snapshot.getRef().getKey();
                                    DatabaseReference sub = FirebaseDatabase.getInstance().getReference("TODO");
                                    if(status.equals(item.getStatus()) && date.equals(item.getDate()) && delete.equals(item.getDelete()) && time.equals(item.getTime()) && title.equals(item.getTitle())){
                                        if(status.equals("0")){
                                            item.setStatus("1");
                                            StyleableToast.makeText(mContext, "Task added in Completed!", R.style.complete).show();
                                            viewHolder.btnLocation.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_24dp);
                                            sub.child(key).child("status").setValue("1");

                                        } else if (status.equals("1")) {
                                            item.setStatus("0");
                                            StyleableToast.makeText(mContext, "Task removed from Completed!", R.style.pending).show();
                                            viewHolder.btnLocation.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
                                            sub.child(key).child("status").setValue("0");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });




        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Delete this task");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseDatabase.getInstance().getReference("TODO").orderByChild("mail").equalTo(item.getMail())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                    String status=snapshot.child("status").getValue(String.class);
                                                    String date=snapshot.child("date").getValue(String.class);
                                                    String delete=snapshot.child("delete").getValue(String.class);
                                                    String time=snapshot.child("time").getValue(String.class);
                                                    String title=snapshot.child("title").getValue(String.class);
                                                    String key =snapshot.getRef().getKey();
                                                    DatabaseReference sub = FirebaseDatabase.getInstance().getReference("TODO");
                                                    if(status.equals(item.getStatus()) && date.equals(item.getDate()) && delete.equals(item.getDelete()) && time.equals(item.getTime()) && title.equals(item.getTitle())){
                                                        StyleableToast.makeText(mContext, "Task Deleted!", R.style.delete).show();
                                                        sub.child(key).child("delete").setValue("1");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                                studentList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, studentList.size());
                                mItemManger.closeAllItems();
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
        });

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        public SwipeLayout swipeLayout;
        public TextView tvName;
        public ImageButton tvDelete;
        public TextView tvEdit;
        public TextView tvShare;
        public ImageButton btnLocation;
        public SimpleViewHolder(View itemView) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDelete = (ImageButton) itemView.findViewById(R.id.tvDelete);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
        }
    }
}
