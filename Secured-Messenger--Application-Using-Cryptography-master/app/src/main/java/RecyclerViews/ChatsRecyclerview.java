package RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.securedmessagingapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ModelClass.MessageModel;

public class ChatsRecyclerview extends RecyclerView.Adapter {

  private   ArrayList<MessageModel> messageModels;
  private  Context context;

  int Senderviewtypr=1;
  int Receiverviewtype=2;

    public ChatsRecyclerview(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==Senderviewtypr){
            View view= LayoutInflater.from(context).inflate(R.layout.senderlayout,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiverlayout,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if(messageModels.get(position).getUserid().equals(FirebaseAuth.getInstance().getUid())){
            return Senderviewtypr;
        }
        else {
            return Receiverviewtype;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel=messageModels.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).sendermessage.setText(messageModel.getMessage());
            ((SenderViewHolder)holder).senderime.setText(messageModel.getTime());
        }
        else{
            ((ReceiverViewHolder)holder).receivermessage.setText(messageModel.getMessage());
            ((ReceiverViewHolder)holder).receiverime.setText(messageModel.getTime());
        }
    }


    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receivermessage,receiverime;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receivermessage=(TextView)itemView.findViewById(R.id.receivermessage);
            receiverime=(TextView)itemView.findViewById(R.id.receivertime);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView sendermessage,senderime;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendermessage=(TextView)itemView.findViewById(R.id.sendermessage);
            senderime=(TextView)itemView.findViewById(R.id.sendertime);


        }
    }
}
