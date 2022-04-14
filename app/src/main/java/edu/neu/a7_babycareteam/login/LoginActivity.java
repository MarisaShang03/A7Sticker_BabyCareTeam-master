package edu.neu.a7_babycareteam.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.a7_babycareteam.MyApplication;
import edu.neu.a7_babycareteam.R;
import edu.neu.a7_babycareteam.main.MainActivity;
import edu.neu.a7_babycareteam.model.User;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private DatabaseReference mDatabase;

    private EditText userNameEt;
    private String loginUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        // Connect with firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void initView() {
        userNameEt = findViewById(R.id.userNameEt);
    }

    public void login(View view) {
        loginUserName = userNameEt.getText().toString();
        if (loginUserName == null || loginUserName.length() < 1) {
            Toast.makeText(getApplicationContext(),"Please input user name",Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(loginUserName);
        Task t = mDatabase.child("users").child(user.getUserName()).setValue(user);;
        t.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Unable to login, Please check the network connection",Toast.LENGTH_SHORT).show();
                } else {
                    MyApplication.getInstance().setLoginUserName(loginUserName);
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        });
    }
}