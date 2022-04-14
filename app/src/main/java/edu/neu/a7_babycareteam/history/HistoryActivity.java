package edu.neu.a7_babycareteam.history;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.neu.a7_babycareteam.MyApplication;
import edu.neu.a7_babycareteam.R;
import edu.neu.a7_babycareteam.history.adapter.HistoryMsgListViewAdapter;
import edu.neu.a7_babycareteam.model.Message;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.getSimpleName();
    private DatabaseReference mDbConversationsRef;
    private ArrayList<Message> msgHistory = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryMsgListViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private String loginUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        loginUserName = MyApplication.getInstance().getLoginUserName();
        mDbConversationsRef = FirebaseDatabase.getInstance().getReference("conversations");
        mDbConversationsRef.child(loginUserName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Log.e(TAG, "onChildAdded: dataSnapshot = " + snapshot.getValue().toString());
                    Message msg = snapshot.getValue(Message.class);
                    msgHistory.add(msg);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String
                    previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new HistoryMsgListViewAdapter(msgHistory);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);

    }
}
