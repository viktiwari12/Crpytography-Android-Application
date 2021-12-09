package RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securedmessagingapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ModelClass.MessageModel;
import ModelClass.UsersData;

import TabbedActivity.UserChatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListRecyclerView extends RecyclerView.Adapter<UsersListRecyclerView.customholder> {

   private Context context;
   private ArrayList<String> name;
    private ArrayList<String> image;
    private ArrayList<String> phone;
    private ArrayList<String> mainuser;
    private ArrayList<String> userid;
    String chatnamephone;
    String lastmessagesender,lastmessagetimesender,lastmessagetimereceiver,lastmessagereceiver;
    String nodekey;

    ArrayList<String> chatphonelist= new ArrayList<>();


    public UsersListRecyclerView(Context context, ArrayList<String> name, ArrayList<String> image , ArrayList<String> phone, ArrayList<String> mainuser, ArrayList<String> userid) {
        this.context = context;
        this.name = name;
        this.image = image;
        this.phone =phone;
        this.mainuser=mainuser;
        this.userid=userid;
    }



    @NonNull
    @Override
    public customholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.userslistrecyclerview,parent,false);
        customholder ch = new customholder(view);
        return ch;
    }

    @Override
    public void onBindViewHolder(@NonNull customholder holder, int position) {

        try {
            String chatname= name.get(position);
            String phoneno = phone.get(0);
            String MainUser=mainuser.get(0);

            String UserId=userid.get(position);

            holder.layoutname.setText(chatname);

            System.out.println(FirebaseAuth.getInstance().getUid()+UserId);

            holder.lastmessgaetime.setVisibility(View.GONE);

            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child(FirebaseAuth.getInstance().getUid()+UserId)
                    .orderByChild("timestamp")
                    .limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChildren()){
                                for(DataSnapshot snapshot1:snapshot.getChildren()){
                                    lastmessagesender=snapshot1.child("message").getValue().toString();
                                   lastmessagetimesender=snapshot1.child("time").getValue().toString();
                                   nodekey= FirebaseAuth.getInstance().getUid()+UserId;

                                }

                                if(lastmessagesender!=null){
                                    holder.lastmessage.setText(lastmessagesender);
                                    holder.lastmessagenumber.setVisibility(View.GONE);
                                    holder.lastmessgaetime.setVisibility(View.VISIBLE);
                                    holder.lastmessgaetime.setText(lastmessagetimesender);
                                }
                                else {
                                    holder.lastmessage.setText("Say hi to your new friend");
                                    holder.lastmessgaetime.setVisibility(View.GONE);
                                    holder.lastmessagenumber.setVisibility(View.VISIBLE);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    });




            Query query= FirebaseDatabase.getInstance().getReference().child("Profile").orderByChild("Name").equalTo(chatname);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        UsersData users= snapshot1.getValue(UsersData.class);
                        chatnamephone=users.getPhone();
                    }

                    if(chatnamephone!=null){
                        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, UserChatActivity.class);
                                intent.putExtra("phoneno",phoneno);
                                intent.putExtra("chatname",chatname);
                                intent.putExtra("chatnamephone",chatnamephone);
                                intent.putExtra("mainuser",MainUser);
                                intent.putExtra("UserId",UserId);
                                context.startActivity(intent);
                                holder.lastmessagenumber.setVisibility(View.GONE);

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class customholder extends RecyclerView.ViewHolder {

        TextView layoutname;
        CircleImageView layoutimage;
        LinearLayout linearLayout;
        TextView lastmessage,lastmessgaetime;
        CircleImageView lastmessagenumber;

        public customholder(@NonNull View itemView) {
            super(itemView);
            layoutname=(TextView)itemView.findViewById(R.id.chattext);
            layoutimage=(CircleImageView)itemView.findViewById(R.id.chatimage);
            linearLayout=itemView.findViewById(R.id.linearlayout);
            lastmessage=(TextView)itemView.findViewById(R.id.lastmessage);
            lastmessagenumber=(CircleImageView) itemView.findViewById(R.id.lastmessagenumber);
            lastmessgaetime=(TextView)itemView.findViewById(R.id.lastmessagetime);
        }

    }

}
