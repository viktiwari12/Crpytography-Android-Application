package TabbedActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securedmessagingapp.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import ModelClass.MessageModel;
import ModelClass.UsersData;
import RecyclerViews.ChatsRecyclerview;

public class UserChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView usertitle;
    RecyclerView recyclerView;
    EditText editText;
    FloatingActionButton send;
    String senderphone, receivername, sendername;
    static String receiverphone;

    ImageButton backbutton;
    ImageButton phone;
    String senderid;
    String receiverid;
    ArrayList<MessageModel> model = new ArrayList<>();

    FirebaseAuth auth;
    String senderroom;
    String receiverroom;

    //CRYPTOGRAPHY=

    byte[] keyasbytes;
    KeySpec keyspec;
    SecretKeyFactory secretKeyFactory;
    Cipher cipher;
    SecretKey key;
    String specifickey = "apeejay university";
    String unicodeformat = "UTF8";
    String encryptedstring;

    String currentTime;

    EditText alertkeydecrypt;


    // Decrypting firebase messgaes=
    MessageModel firebasemodel;
    String decryptedstring;

    String decuserid;
ScrollView scrollView;
     ArrayList<MessageModel> arrayListencryptedmessage;

  ArrayList<String> scroll = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchatactivity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        UserChatActivity.this.setTitle(null);
        usertitle = (TextView) findViewById(R.id.usertitle);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        editText = (EditText) findViewById(R.id.edittext);
        backbutton=(ImageButton)findViewById(R.id.backbutton);
        send = (FloatingActionButton) findViewById(R.id.send);
        phone=(ImageButton)findViewById(R.id.phone);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               calling();
            }
        });

        Intent intent = getIntent();
        receivername = intent.getStringExtra("chatname");
        senderphone = intent.getStringExtra("phoneno");
        sendername = intent.getStringExtra("mainuser");
     scrollView=(ScrollView)findViewById(R.id.scrollView);

        System.out.println(receivername);
        System.out.println(receiverphone);


        Query query= FirebaseDatabase.getInstance().getReference().child("Profile").orderByChild("Name").equalTo(receivername);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    UsersData users= snapshot1.getValue(UsersData.class);
                    receiverphone=users.getPhone();

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        receiverid = intent.getStringExtra("UserId");
        auth = FirebaseAuth.getInstance();

        senderid = auth.getUid();


        usertitle.setText(receivername);

        //Actual code=

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final String senderoom = senderid + receiverid;
        final String receiveroom = receiverid + senderid;


        //fetching=

        try {

            FirebaseDatabase.getInstance().getReference().child("chats").child(senderoom).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messageModels.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        MessageModel model = snapshot1.getValue(MessageModel.class);
                        messageModels.add(model);

                    }
                    final ChatsRecyclerview chatsrecyclerview = new ChatsRecyclerview(messageModels, UserChatActivity.this);
                    chatsrecyclerview.notifyDataSetChanged();
                    recyclerView.setAdapter(chatsrecyclerview);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserChatActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    if(messageModels.size()!=0) {
                        recyclerView.smoothScrollToPosition(messageModels.size() - 1);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText().toString()!=" " && editText.getText().toString()!=null && editText.getText().toString()!="" && editText.getText().length()!=0){
                    Calendar currentdatetime = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
                    currentTime = df.format(currentdatetime.getTime());
                    String message = editText.getText().toString();


                        keyasbytes = specifickey.getBytes();

                    try {
                        keyspec = new DESKeySpec(keyasbytes);
                    } catch (InvalidKeyException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        secretKeyFactory = SecretKeyFactory.getInstance("DES");
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        cipher = Cipher.getInstance("DES");
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        key = secretKeyFactory.generateSecret(keyspec);
                    } catch (InvalidKeySpecException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        cipher.init(Cipher.ENCRYPT_MODE, key);
                        byte[] plaintext = message.getBytes();
                        byte[] encryptedtext = cipher.doFinal(plaintext);

                        encryptedstring = Base64.getEncoder().encodeToString(encryptedtext);
                        System.out.println(encryptedstring);


                    } catch (InvalidKeyException |  IllegalBlockSizeException | BadPaddingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    final MessageModel model = new MessageModel(senderid, encryptedstring,currentTime);
                    model.setTimestamp(new Date().getTime());
                    editText.setText(null);

                    FirebaseDatabase.getInstance().getReference().child("chats").child(senderoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference().child("chats").child(receiveroom).push().setValue(model);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(e.getMessage());
                            Toast toast = Toast.makeText(UserChatActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
                }

        });


    }

    public void calling() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    69);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + receiverphone)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==69){
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                calling();
            } else {
                Toast toast= Toast.makeText(UserChatActivity.this,"Permission is denied",Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.item1) {

            AlertDialog.Builder alert = new AlertDialog.Builder(UserChatActivity.this);
            View view = getLayoutInflater().inflate(R.layout.keyalertdialog, null);

            alertkeydecrypt = (EditText) view.findViewById(R.id.decryptkey);
            TextView ok = (TextView) view.findViewById(R.id.ok);
            TextView cancel = (TextView) view.findViewById(R.id.cancel);
            alert.setView(view);

            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();

                    firebasefetching();

                }
            });

        }

        return super.onOptionsItemSelected(item);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void firebasefetching() {

        try {

            //Firebase fetching for normal text messages=
            final String senderoom = senderid + receiverid;
            final String receiveroom = receiverid + senderid;

           arrayListencryptedmessage = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("chats").child(senderoom).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayListencryptedmessage.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        firebasemodel = snapshot1.getValue(MessageModel.class);
                        decryptmessage(firebasemodel.getMessage());
                        firebasemodel.setMessage(decryptedstring);
                        arrayListencryptedmessage.add(firebasemodel);

                    }

               for(int i=0;i<arrayListencryptedmessage.size();i++){
                   System.out.println(arrayListencryptedmessage.get(i).getMessage());
                   System.out.println(arrayListencryptedmessage.get(i).getUserid());
               }

                    final ChatsRecyclerview chatsrecyclerview = new ChatsRecyclerview(arrayListencryptedmessage, UserChatActivity.this);
                    chatsrecyclerview.notifyDataSetChanged();
                    recyclerView.setAdapter(chatsrecyclerview);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserChatActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setNestedScrollingEnabled(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void decryptmessage(String encryptedstring){


            keyasbytes=alertkeydecrypt.getText().toString().getBytes();

        try {
            keyspec=new DESKeySpec(keyasbytes);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            secretKeyFactory= SecretKeyFactory.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            cipher=Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            key=secretKeyFactory.generateSecret(keyspec);
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try{
            String encryptedString=encryptedstring;


            cipher.init(Cipher.DECRYPT_MODE,key);

            Base64.Decoder decoder= Base64.getDecoder();
            byte[] encryptedtext=decoder.decode(encryptedString);
            byte[] plaintext=cipher.doFinal(encryptedtext);
            String s = new String(plaintext);

           decryptedstring=s;




    } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
    }


}
}