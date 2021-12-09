package RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.securedmessagingapp.R;

import java.util.ArrayList;

import ModelClass.Status;
import ModelClass.UserStatus;
import TabbedActivity.TabbedActivity;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusRecyclerview extends RecyclerView.Adapter<StatusRecyclerview.customholder> {

  private Context context;

    public StatusRecyclerview(Context context, FragmentManager fragmentManager, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.userStatuses = userStatuses;
    }

    private FragmentManager fragmentManager;

    private ArrayList<UserStatus> userStatuses;


    @NonNull
    @Override
    public customholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itemstatus, parent, false);
        return new customholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customholder holder, int position) {

        UserStatus userStatus= userStatuses.get(position);
        holder.statusname.setText(userStatus.getName());

        holder.circularStatusView.setPortionsCount(userStatus.getStatuses().size());
//     holder.statustime.setText(userStatus.getStatuses().get(0).getTime());



        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ArrayList<MyStory> myStories= new ArrayList<>();
                    myStories.clear();
                    for(Status status: userStatus.getStatuses()) {
                        myStories.add(new MyStory(status.getImageurl()));

                    }
                    new StoryView.Builder((fragmentManager))
                            .setStoriesList(myStories)
                            .setStoryDuration(5000)
                            .setTitleText(userStatus.getName())
                            .setStoryClickListeners(new StoryClickListeners() {
                                @Override
                                public void onDescriptionClickListener(int position) {

                                }

                                @Override
                                public void onTitleIconClickListener(int position) {

                                }
                            })
                            .build()
                            .show();




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class customholder extends RecyclerView.ViewHolder {

        CircularStatusView circularStatusView;
        TextView statusname,statustime;
        ConstraintLayout constraintLayout;
        public customholder(@NonNull View itemView) {
            super(itemView);
            circularStatusView=(CircularStatusView)itemView.findViewById(R.id.statusview);
            statusname=(TextView)itemView.findViewById(R.id.statusname);
            constraintLayout=(ConstraintLayout)itemView.findViewById(R.id.constraintLayout);
      
        }
    }
}
