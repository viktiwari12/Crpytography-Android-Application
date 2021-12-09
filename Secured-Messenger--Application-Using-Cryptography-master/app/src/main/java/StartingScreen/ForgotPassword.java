package StartingScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.securedmessagingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText emailaddress;
    MaterialButton resetpassword;
    FirebaseAuth auth;
    ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        emailaddress = (EditText)findViewById(R.id.email);
        resetpassword=(MaterialButton)findViewById(R.id.resetpassword);
        auth=FirebaseAuth.getInstance();
        backbutton=(ImageButton)findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailaddress.getText().length()!=0) {
                    sendingemaillink();
                }
                else{
                    Toast toast=Toast.makeText(ForgotPassword.this,"Please enter email address",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    public void sendingemaillink(){

        auth.sendPasswordResetEmail(emailaddress.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast toast=Toast.makeText(ForgotPassword.this,"Reset Link sent to your Email",Toast.LENGTH_LONG);
                toast.show();

                emailaddress.getText().clear();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast toast=Toast.makeText(ForgotPassword.this,"This email is not verified",Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
}