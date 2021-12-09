package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securedmessagingapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ModelClass.UsersData;
import RecyclerViews.UsersListRecyclerView;


public class UsersList extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> listname= new ArrayList<>();
    ArrayList<String> listimage = new ArrayList<>();
    ArrayList<String> listphone = new ArrayList<>();
    ArrayList<String> mainuser= new ArrayList<>();

    static String UserId;
    String CheckingUserId;
    String finaluserid;

    String name;
     String phone;
     String currentuser;

    String defaultvaluecurrentuser;

    FirebaseAuth auth;
    String userid;
ArrayList<String> listuserid= new ArrayList<>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chats.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersList newInstance(String param1, String param2) {
        UsersList fragment = new UsersList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_users_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mainmenu,menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);

        auth=FirebaseAuth.getInstance();
        userid=auth.getUid();
        CheckingUserId=userid;



        Query query = FirebaseDatabase.getInstance().getReference().child("Profile").orderByChild("Userid").equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    UsersData users = snapshot1.getValue(UsersData.class);
                    phone=users.getPhone();
                    currentuser=users.getName();
                    listphone.add(phone);
                    mainuser.add(currentuser);

                }
               if(phone!=null){
                   fetchingdata();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



    public void fetchingdata(){



        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Profile");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listname.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    UsersData data = snapshot1.getValue(UsersData.class);
                    name=data.getName();
                    listname.add(name);

                    UserId=snapshot1.getKey();

                    listuserid.add(UserId);

                }

                System.out.println("CURRENT USER"+currentuser);
                for(int i=0;i<listname.size();i++){
                   if(listname.get(i).equals(currentuser)){

                       listname.remove(i);
                   }
                }
                for(int i=0;i<listuserid.size();i++){
                    if(listuserid.get(i).equals(CheckingUserId)){
                        listuserid.remove(i);
                    }
                }


            sendingdata();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void sendingdata(){

        System.out.println(userid);
        System.out.println(listname);

        System.out.println(listname.size());
//
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setAdapter(new UsersListRecyclerView(getContext(),listname,listname,listphone,mainuser,listuserid));
    }


}