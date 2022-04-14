package edu.neu.a7_babycareteam.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.a7_babycareteam.MyApplication;
import edu.neu.a7_babycareteam.R;
import edu.neu.a7_babycareteam.history.HistoryActivity;
import edu.neu.a7_babycareteam.main.adapter.FriendsListViewAdapter;
import edu.neu.a7_babycareteam.main.adapter.GridViewAdapter;
import edu.neu.a7_babycareteam.model.FriendItem;
import edu.neu.a7_babycareteam.model.Message;
import edu.neu.a7_babycareteam.model.Sticker;
import edu.neu.a7_babycareteam.model.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String STICKER_SEL_POS = "STICKER_SEL_POS";
    private static final String FRIEND_SEL_POS = "FRIEND_SEL_POS";

    private String loginUserName;
    private Button sendBtn;
    private Button historyBtn;

    // GridView
    private ArrayList<Sticker> stickers = null;
    private GridViewAdapter mGridViewAdapter = null;
    int stickerSelectorPosition = 0;

    // RecyclerView
    private ArrayList<FriendItem> friendItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private FriendsListViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    int friendSelectorPosition = 0;

    //firebaseDatabase
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbUsersRef;
    private DatabaseReference mDbConversationsRef;

    private boolean isOnPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        initialItemData(savedInstanceState);
        initMainTitle();
        initStickerView();
        initFriendListView();
        initFirebaseDb();
        initListener();
        createNotificationChannel();
    }

    private void initVariables() {
        loginUserName = MyApplication.getInstance().getLoginUserName();
        stickers = new ArrayList<Sticker>();
        stickers.add(new Sticker(R.drawable.chill_out, "chill_out"));
        stickers.add(new Sticker(R.drawable.cry, "cry"));
        stickers.add(new Sticker(R.drawable.enough, "enough"));
        stickers.add(new Sticker(R.drawable.happy, "happy"));
        stickers.add(new Sticker(R.drawable.love, "love"));
        stickers.add(new Sticker(R.drawable.no, "no"));
        stickers.add(new Sticker(R.drawable.question, "question"));
        stickers.add(new Sticker(R.drawable.sigh, "sigh"));
        stickers.add(new Sticker(R.drawable.sleep, "sleep"));
        stickers.add(new Sticker(R.drawable.yes, "yes"));
    }

    private void initialItemData(Bundle savedInstanceState) {
        // Not the first time to open this Activity
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STICKER_SEL_POS)) {
                stickerSelectorPosition = savedInstanceState.getInt(STICKER_SEL_POS);
            }
            if (savedInstanceState.containsKey(FRIEND_SEL_POS)) {
                friendSelectorPosition = savedInstanceState.getInt(FRIEND_SEL_POS);
            }
            if (savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    int sendTimes = savedInstanceState.getInt(KEY_OF_INSTANCE + i);
                    stickers.get(i).setSentTimes(sendTimes);
                }
            }
        }
    }

    private void initMainTitle() {
        TextView tv_title = findViewById(R.id.main_title);
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setText("Hello, " + loginUserName);
    }

    private void initStickerView() {
        GridView stickerView = (GridView) findViewById(R.id.gridView);
        mGridViewAdapter = new GridViewAdapter<Sticker>(this, stickers);
        stickerView.setAdapter(mGridViewAdapter);
        stickerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGridViewAdapter.changeState(position);
                stickerSelectorPosition = position;
            }
        });
        mGridViewAdapter.changeState(stickerSelectorPosition);
    }

    private void initFriendListView() {
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new FriendsListViewAdapter(friendItems, friendSelectorPosition, recyclerView);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(Context context, String friendName, int position) {
                friendSelectorPosition = position;
                Log.d(TAG, "onItemClick: " + position + " , " + friendName);
            }
        };
        recyclerViewAdapter.setOnItemClickListener(itemClickListener);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private void initFirebaseDb() {
        mFirebaseDb = FirebaseDatabase.getInstance();
        mDbUsersRef = mFirebaseDb.getReference("users");
        mDbUsersRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                } else {
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String userName = postSnapshot.getValue(User.class).userName;
                        if (!userName.equals(loginUserName)) {
                            friendItems.add(new FriendItem(userName));
                        }
                    }
                    if (friendItems.size() > 0) {
                        friendItems.get(friendSelectorPosition).setSelected(true);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        mDbConversationsRef = mFirebaseDb.getReference("conversations");
        mDbConversationsRef = FirebaseDatabase.getInstance().getReference("conversations");
        mDbConversationsRef.child(loginUserName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!isOnPause) {
                    Log.d(TAG, "onChildAdded1: " + snapshot.getValue().toString());
                } else {
                    Log.d(TAG, "onChildAdded2: " + snapshot.getValue().toString());
                    Message msg = snapshot.getValue(Message.class);
                    sendNotification(msg);
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

    }

    public void sendNotification(Message msg) {

        // Prepare intent which is triggered if the notification is selected
        Intent intent = new Intent(this, HistoryActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Need to define a channel ID after Android Oreo
        String channelId = getString(R.string.channel_id);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), msg.getStickerID());
        NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                //"Notification icons must be entirely white."
                .setSmallIcon(R.drawable.foo)
                .setLargeIcon(largeIcon)
                .setContentTitle("New message received ")
                .setContentText(msg.getSenderName() + " send you a sticker " + msg.getStickerName() + " at " + msg.getSendTime())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // hide the notification after its selected
                .setAutoCancel(true)
                .setContentIntent(pIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, notifyBuild.build());

    }

    private void initListener() {
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friendItems.size() == 0) {
                    Toast.makeText(getApplicationContext(),"Please select a friend",Toast.LENGTH_SHORT).show();
                    return;
                }
                int sentTimes = stickers.get(stickerSelectorPosition).getSentTimes();
                stickers.get(stickerSelectorPosition).setSentTimes(sentTimes + 1);
                mGridViewAdapter.notifyDataSetChanged();

                int stickId = stickers.get(stickerSelectorPosition).getStickerId();
                String iconName = stickers.get(stickerSelectorPosition).getStickerName();
                String friendName = friendItems.get(friendSelectorPosition).getFriendName();
                Message msg = new Message(stickId, iconName, loginUserName);
                mDbConversationsRef.child(friendName).child(msg.getMsgId() + "").setValue(msg);
            }
        });

        historyBtn = findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent();
                 intent.setClass(MainActivity.this, HistoryActivity.class);
                 startActivity(intent);
            }
        });
    }

    private void createNotificationChannel() {
        // This must be called early because it must be called before a notification is sent.
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Handling Orientation Changes on Android
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = stickers == null ? 0 : stickers.size();
        outState.putInt(NUMBER_OF_ITEMS, size);
        for (int i = 0; i < size; i++) {
            outState.putInt(KEY_OF_INSTANCE + i, stickers.get(i).getSentTimes());
        }
        outState.putInt(STICKER_SEL_POS, stickerSelectorPosition);
        outState.putInt(FRIEND_SEL_POS, friendSelectorPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        isOnPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        isOnPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}