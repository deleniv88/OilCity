package oil.city;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rey.material.widget.CheckBox;

import java.util.Arrays;
import java.util.HashMap;

import io.paperdb.Paper;
import oil.city.Common.Common;
import oil.city.Model.User;
import oil.city.Update.UpdateHelper;

public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener{

    private static final int RC_SING_IN = 100;
    GoogleSignInClient mGoogleSingInClient;
    EditText edtEmail, edtPassword;
    Button btnSingIn;
    TextView singup;

    CheckBox ckbRemember;

//    Button loginButton, btnGoogle; //facebook
    LinearLayout btnGoogle;
    CallbackManager callbackManager; //facebook

    String TAG = "FACELOG"; //facebook

    FirebaseAuth mAuth, mGoogle;//facebook and google
//    FirebaseAuth pAuth;//phone
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//for notifications
            NotificationChannel channel =
                    new NotificationChannel( "MyNotifications", "MyNotifications",
                            NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")//cloud messaging
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Вітаємо"; //повідомлення при вході у меню
//                        if (!task.isSuccessful()){
//                            msg = "Failed";
//                        }
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();

        mAuth = FirebaseAuth.getInstance(); //facebook and google
        mGoogle = FirebaseAuth.getInstance();

//        pAuth = FirebaseAuth.getInstance(); // phone


        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create(); //facebook
//        loginButton = findViewById(R.id.login_button); //facebook

//        loginButton.setOnClickListener(new View.OnClickListener() { //facebook
//            @Override
//            public void onClick(View v) {
//
//                if (Common.isConnectedToInternet(getBaseContext())) {
//
//                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
//                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
////                            Log.d(TAG, " " + loginResult);
//                            handleFacebookAccessToken(loginResult.getAccessToken());
//                        }
//
//                        @Override
//                        public void onCancel() {
////                            Log.d(TAG, " ");
//                            // ...
//                        }
//
//                        @Override
//                        public void onError(FacebookException error) {
////                            Log.d(TAG, " ", error);
//                            // ...
//                        }
//                    });
//                }else {
//                    Toast.makeText(MainActivity.this, "Немає з’єднання з інтернетом!", Toast.LENGTH_SHORT).show();
//                    return ;
//                }
//            }
//        });

        btnGoogle = findViewById(R.id.btnGoogle);//for google sing in
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singInGoogle = mGoogleSingInClient.getSignInIntent();
                startActivityForResult(singInGoogle, RC_SING_IN);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)//for google sing in
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSingInClient = GoogleSignIn.getClient(this, gso);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        ckbRemember = findViewById(R.id.ckbRemember);

        btnSingIn = findViewById(R.id.btnSingIn);
        singup = findViewById(R.id.singin);

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtEmail.getText().toString();
                final String pass = edtPassword.getText().toString();

                if (email.isEmpty() || pass.isEmpty()){
                    showMessage("Введіть емейл і пароль");
                }else {
                    singIn(email,pass);
                }
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");


        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(MainActivity.this, Register.class);
                startActivity(reg);

                Paper.init(MainActivity.this);//for remember me

                if (Common.isConnectedToInternet(getBaseContext())){
                    if (ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY, edtEmail.getText().toString());
                        Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
                    }
                }

                String user_key = Paper.book().read(Common.USER_KEY);
                String user_pwd = Paper.book().read(Common.PWD_KEY);
                if (user_key != null && user_pwd != null){
                    if (!user_key.isEmpty() && !user_pwd.isEmpty())
                        login(user_key,user_pwd);
                }
//                if (Common.isConnectedToInternet(getBaseContext())) {
////                    sendVerificationCode();
////                    Toast.makeText(MainActivity.this, "Код надіслано", Toast.LENGTH_SHORT).show();
////                }else {
////                    Toast.makeText(MainActivity.this, "Немає з’єднання з інтернетом!", Toast.LENGTH_SHORT).show();
////                    return ;
////                }
            }
        });

    }

    private void login(final String user_key, final String user_pwd) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())){

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Будь ласка, зачекайте");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(user_key).exists()){
                        mDialog.dismiss();
                        User user = dataSnapshot.child(user_key).getValue(User.class);
                        user.setName(user_key);
                        if (user.getPassword().equals(user_pwd)){
                            Intent home = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(home);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void singIn(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    btnSingIn.setVisibility(View.VISIBLE);
                    updateUIEmail();
                }
                else {
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    private void updateUIEmail() {
        Intent home = new Intent(MainActivity.this, Home.class);
        startActivity(home);
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void verifySingInCode() {
        String code = edtPassword.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.getCurrentUser();

                            String phone = auth.getCurrentUser().getPhoneNumber();
                            String uid = auth.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put into in hashmap
                            hashMap.put("email", "");
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");
                            hashMap.put("phone", phone);
                            hashMap.put("image", "");//1
                            FirebaseDatabase database = FirebaseDatabase.getInstance();//1
                            DatabaseReference reference = database.getReference("User");
                            reference.child(uid).setValue(hashMap);//1

                            Toast.makeText(MainActivity.this, "Вхід", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                Toast.makeText(MainActivity.this, "Невірний код", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    private void sendVerificationCode() {
//        String phone = edtPhone.getText().toString();
//
//        if (phone.isEmpty()){
//            edtPhone.setError("Phone number is required");
//            edtPhone.requestFocus();
//            return;
//        }
//        if (phone.length()<10){
//            edtPhone.setError("Будь ласка, введіть вірний номер");
//            edtPhone.requestFocus();
//            return;
//        }
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phone,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks
//    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };

    @Override
    protected void onStart() {//facebook
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            updateUI(currentUser);
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            updateUIEmail();
        }

//        FirebaseUser g = mGoogle.getCurrentUser();
//        if (g != null){
//            updateUIGoogle();
//        }

    }

//    private void updateUIGoogle() {
//        Toast.makeText(this, "Ви увійшли за допогомою Google", Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(MainActivity.this, Home.class);
//        startActivity(intent);
//    }


    private void updateUI(FirebaseUser currentUser) { //facebook
//        Toast.makeText(this, "Ви увійшли за допогомою Facebook", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, Home.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //facebook
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {//for google sing in

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mGoogle.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mGoogle.getCurrentUser();
                            String email = user.getEmail();//1
                            String uid = user.getUid();
                            String name = user.getDisplayName();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put into in hashmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", "");
                            hashMap.put("image", "");//1
                            FirebaseDatabase database = FirebaseDatabase.getInstance();//1
                            DatabaseReference reference = database.getReference("User");
                            reference.child(uid).setValue(hashMap);//1

                            startActivity(new Intent(MainActivity.this, Home.class));
                            finish();
                            Toast.makeText(MainActivity.this, "Ви ввійшли за допомогою Google", Toast.LENGTH_SHORT).show();

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Вхід невиконано", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleFacebookAccessToken(AccessToken token) { //facebook
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:Вдало");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String phone = user.getPhoneNumber();
                            String email = user.getEmail();//1 for saving profile
                            String name = user.getDisplayName();
                            String uid = user.getUid();

                            HashMap<Object,String> hashMap = new HashMap<>();
                            //put into in hashmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", phone);
                            hashMap.put("image", "");//1
                            FirebaseDatabase database = FirebaseDatabase.getInstance();//1
                            DatabaseReference reference = database.getReference("User");
                            reference.child(uid).setValue(hashMap);//1
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Авторизацію відхилено",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Доступна нова версія")
                .setMessage("Будь ласка, оновіть для вірного відображення інформації")
                .setPositiveButton("Оновити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, ""+urlApp, Toast.LENGTH_SHORT).show();
                        Intent newVersion = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=oil.city"));
                        startActivity(newVersion);
                    }
                }).setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(MainActivity.this, "Ви скасували оновлення! Оновіть застосунок для вірного відображення інформації!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
}


