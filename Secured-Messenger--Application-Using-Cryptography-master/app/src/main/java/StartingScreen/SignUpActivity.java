package StartingScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securedmessagingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {


    TextInputLayout fullname,email,password,confirmpassword,phone;
    MaterialButton signup;
    FirebaseAuth auth;
    TextView login;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        fullname=(TextInputLayout)findViewById(R.id.fullname);
        email=(TextInputLayout)findViewById(R.id.email);
        password=(TextInputLayout)findViewById(R.id.password);
        confirmpassword=(TextInputLayout)findViewById(R.id.confirmpassword);
        signup=(MaterialButton) findViewById(R.id.signup);
        phone=(TextInputLayout) findViewById(R.id.phone);
        login=(TextView)findViewById(R.id.login);

        auth=FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void validation(){
        if(fullname.getEditText().getText().length()==0){
            fullname.setError("Name can't be empty");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fullname.setError(null);
                }
            },2500);
        }

        else{

            fullname.setError(null);

        }

        if(email.getEditText().getText().length()==0){
            email.setError("Email Address can't be empty");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    email.setError(null);
                }
            },2500);
        }
        else{
          email.setError(null);

        }
        if(password.getEditText().getText().length()<6){
            password.setError("Password should be at least 6 characters");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    password.setError(null);
                }
            },2500);
        }
        else{
            password.setError(null);

        }
        if(confirmpassword.getEditText().getText().length()<6){
            confirmpassword.setError("Confirm Password doesn't match password");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    confirmpassword.setError(null);
                }
            },2500);
        }
        else{

            confirmpassword.setError(null);

        }
        if(phone.getEditText().getText().length()<=9){
            phone.setError("Phone Number must be a 10 Digit Number");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    phone.setError(null);
                }
            },2500);
        }
        else{

            phone.setError(null);

        }

        if(password.getEditText().getText().toString().equals(confirmpassword.getEditText().getText().toString())){

        }
        else{
            password.setError("Password and Confirm password must be same !");
            confirmpassword.setError("Password and Confirm password must be same !");
        }


        if(fullname.getEditText().getText().length()!=0 && email.getEditText().getText().length()!=0 &&password.getEditText().getText().length()>=6 &&confirmpassword.getEditText().getText().length()>=6 &&password.getEditText().getText().toString().equals(confirmpassword.getEditText().getText().toString()) && phone.getEditText().getText().length()>9){

            authentication();

        }
    }



    public void authentication(){

        AlertDialog.Builder alert= new AlertDialog.Builder(SignUpActivity.this);
        View view= getLayoutInflater().inflate(R.layout.loadingscreenalertdialog,null);
        alert.setView(view);

        AlertDialog alertDialog= alert.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        auth.createUserWithEmailAndPassword(email.getEditText().getText().toString(),password.getEditText().getText().toString()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast toast = Toast.makeText(SignUpActivity.this,"Registered Successfully",Toast.LENGTH_LONG);
                    toast.show();

                    userid=auth.getUid();

                    alertDialog.dismiss();

                   Intent intent= new Intent(SignUpActivity.this,LoginActivity.class);
                   startActivity(intent);
                   finish();

                    HashMap<String,Object> signupdata = new HashMap<>();
                    signupdata.put("Name", fullname.getEditText().getText().toString());
                    signupdata.put("Email", email.getEditText().getText().toString());
                    signupdata.put("Phone", phone.getEditText().getText().toString());
                    signupdata.put("Userid",userid);
                    String userid=auth.getUid();


                    FirebaseDatabase.getInstance().getReference().child("Profile").child(userid).setValue(signupdata);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.dismiss();
                Toast toast=Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
}