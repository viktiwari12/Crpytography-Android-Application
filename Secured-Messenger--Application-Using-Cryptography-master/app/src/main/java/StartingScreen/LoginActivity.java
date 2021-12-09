package StartingScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securedmessagingapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import TabbedActivity.TabbedActivity;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout username;
    TextInputLayout password;
    TextView signup, forgotpassword;
    MaterialButton login;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (TextInputLayout) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.password);
        login=(MaterialButton)findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.signup);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        mAuth=FirebaseAuth.getInstance();
        login();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                username.setError(null);
                password.setError(null);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);



            }
        });

    }

    public void login() {
        try{
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (username.getEditText().getText().toString().length() == 0) {
                        username.setError("Username can't be empty");
                    }
                    else {
                        username.setError(null);
                    }
                    if (password.getEditText().getText().toString().length() == 0) {
                        password.setError("Password can't be empty");
                    }
                    else{
                        password.setError(null);
                    }

                    if(username.getEditText().getText().toString().length()!= 0 && password.getEditText().getText().toString().length() !=0 ){

                        mAuth.signInWithEmailAndPassword(username.getEditText().getText().toString(),password.getEditText().getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                Toast toast = Toast.makeText(LoginActivity.this,"Logged in Successfully",Toast.LENGTH_LONG);
                                toast.show();

                                Intent intent= new Intent(LoginActivity.this, TabbedActivity.class);
                                intent.putExtra("username",username.getEditText().getText().toString());

                                startActivity(intent);



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast toast = Toast.makeText(LoginActivity.this,"Wrong Email id or Password",Toast.LENGTH_LONG);
                                toast.show();

                            }
                        });
                                         
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}