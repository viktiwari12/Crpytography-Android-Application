package Fragments;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.securedmessagingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ModelClass.UserStatus;
import ModelClass.UsersData;
import RecyclerViews.StatusRecyclerview;
import StartingScreen.SignUpActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Status#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Status extends Fragment {

    RecyclerView recyclerView;
    ArrayList<UserStatus> userStatuses;
    ImageButton camerabutton;

    UsersData usersData;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Status() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Status.
     */
    // TODO: Rename and change types and number of parameters
    public static Status newInstance(String param1, String param2) {
        Status fragment = new Status();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        camerabutton=(ImageButton)view.findViewById(R.id.camerabutton);
        userStatuses= new ArrayList<>();

        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,69);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Profile").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersData= snapshot.getValue(UsersData.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userStatuses.clear();
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                       UserStatus userStatus= new UserStatus();
                       userStatus.setName(snapshot1.child("name").getValue().toString());
                        userStatus.setLastupdated(snapshot1.child("lastupdated").getValue(Long.class));


                        ArrayList<ModelClass.Status> statuses= new ArrayList<>();

                        for(DataSnapshot snapshot2:snapshot1.child("statuses").getChildren()){
                            ModelClass.Status status= snapshot2.getValue(ModelClass.Status.class);
                            statuses.add(status);
                        }
                        userStatus.setStatuses(statuses);

                        userStatuses.add(userStatus);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        StatusRecyclerview statusRecyclerview= new StatusRecyclerview(getActivity(),getFragmentManager(),userStatuses);
                        recyclerView.setAdapter(statusRecyclerview);

                        statusRecyclerview.notifyDataSetChanged();
                    }


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getData()!=null){
                AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
                View view= getLayoutInflater().inflate(R.layout.uploadingimage,null);
                alert.setView(view);

                AlertDialog alertDialog= alert.create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);

                Date date= new Date();
                FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
                StorageReference storageReference= firebaseStorage.getReference().child("status").child(date.getTime()+"");
             storageReference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                     if(task.isSuccessful()){
                         storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                             @Override
                             public void onComplete(@NonNull Task<Uri> task) {

                                 alertDialog.dismiss();

                                 UserStatus userStatus= new UserStatus();
                                 userStatus.setName(usersData.getName());
                                 userStatus.setProfilepic(userStatus.getProfilepic());
                                 userStatus.setLastupdated(date.getTime());
                                 Calendar currentdatetime = Calendar.getInstance();
                                 SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
                               String  currentTime = df.format(currentdatetime.getTime());


                                 String url= task.getResult().toString();
                                 System.out.println(url);

                                 ModelClass.Status status= new ModelClass.Status(url,userStatus.getLastupdated(),currentTime);


                                 HashMap<String,Object> objectHashMap= new HashMap<>();
                                 objectHashMap.put("name",userStatus.getName());
                                 objectHashMap.put("profilepic",userStatus.getProfilepic());
                                 objectHashMap.put("lastupdated",userStatus.getLastupdated());

                                 FirebaseDatabase.getInstance().getReference().child("status").child(FirebaseAuth.getInstance().getUid())
                                         .updateChildren(objectHashMap);

                                 FirebaseDatabase.getInstance().getReference().child("status").child(FirebaseAuth.getInstance().getUid()).child("statuses").push().setValue(status);



                             }
                         });
                     }

                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {

                     Toast toast=Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG);
                     toast.show();
                 }
             });
            }
        }
    }
}