package TabbedActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.securedmessagingapp.R;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ModelClass.UsersData;
import TabbedActivity.ui.main.SectionsPagerAdapter;


public class TabbedActivity extends AppCompatActivity {

    ImageButton imageButton;
    LinearLayout linearLayout;
    EditText searchview;

    ImageButton backbutton;

    String phoneno;
    String currentuser;
    String userid;
    FirebaseAuth auth;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);



        auth= FirebaseAuth.getInstance();

        userid=auth.getUid();

        Intent intent= getIntent();
         username = intent.getStringExtra("username");



            Query query = FirebaseDatabase.getInstance().getReference().child("Profile").orderByChild("Email").equalTo(username);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        UsersData users = snapshot1.getValue(UsersData.class);
                        phoneno=users.getPhone();
                        currentuser=users.getName();

                    }
                    if(phoneno!=null){
                    getCurrentuser();
                    getPhoneno();
                    getUserid();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }



    public String getPhoneno(){
        return phoneno;
    }
    public String getCurrentuser(){

        return currentuser;
    }

    public String getUserid(){
        return userid;
    }


}