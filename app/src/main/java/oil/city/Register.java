package oil.city;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Register extends AppCompatActivity {

    ImageView imgUserPhoto;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImgUri;

    MaterialEditText userName, userPass, userEmail, userConfPass;
    ProgressBar progressBar;
    Button register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.name);
        userEmail = findViewById(R.id.email);
        userPass = findViewById(R.id.pass);
        userConfPass = findViewById(R.id.confirm);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String pass = userPass.getText().toString();
                final String confPass = userConfPass.getText().toString();

                if (email.isEmpty() || name.isEmpty() || pass.isEmpty() || !pass.equals(confPass)) {
                    showMessage("Please verify all fields");
                    register.setVisibility(View.VISIBLE);
                } else {
                    CreateUserAccount(email, name, pass);
                }
            }
        });

        imgUserPhoto = findViewById(R.id.avatarPhoto);

//        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 22) {
//                    checkAndRequestPermision();
//                } else {
//                    openGallery();
//                }
//            }
//        });
    }

    private void CreateUserAccount(String email, final String name, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showMessage("Обліковий запис створено");
                            if (pickedImgUri != null){
                                updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());
                            }else
                                updateUserInfoWithOutPhoto(name, mAuth.getCurrentUser());

                        } else {
                            showMessage("Реєстрація невірна" + task.getException().getMessage());
                            register.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imgFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imgFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileChangeRequest)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showMessage("Register complite");
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUserInfoWithOutPhoto(final String name, final FirebaseUser currentUser) {

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showMessage("Register complite");
                            updateUI();
                        }
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

//    private void openGallery() {
//
//        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
//        gallery.setType("image/*");
//        startActivityForResult(gallery,REQUESTCODE);
//
//    }
//
//    private void checkAndRequestPermision() {
//        if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
//                Toast.makeText(this, "Please acept for required", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                ActivityCompat.requestPermissions(Register.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        PReqCode);
//            }
//        }
//        else {
//            openGallery();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
//            pickedImgUri = data.getData();
//            imgUserPhoto.setImageURI(pickedImgUri);
        }

}
