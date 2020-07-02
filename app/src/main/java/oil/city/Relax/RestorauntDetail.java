package oil.city.Relax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import io.paperdb.Paper;
import oil.city.Administrations.Buildings;
import oil.city.Bus.Bus;
import oil.city.Common.Common;
import oil.city.Events.EventActivity;
import oil.city.Flat.Flat;
import oil.city.Home;
import oil.city.MainActivity;
import oil.city.Model.Rating;
import oil.city.Model.RatingRest;
import oil.city.Model.Restoraunt;
import oil.city.News.NewsActivity;
import oil.city.R;
import oil.city.ShowCommentEvent;
import oil.city.ShowCommentRestoraunt;

public class RestorauntDetail extends AppCompatActivity implements RatingDialogListener, NavigationView.OnNavigationItemSelectedListener {

    TextView admin_detail_name, admin_detail_menu, admin_detail_phone, admin_detail_adress,admin_detail_time;
    ImageView img_admin_detail,img,img1;
    String restoraunt_detail_Id="";
    RelativeLayout rest_location;

    FirebaseDatabase database;
    DatabaseReference admins;

    FirebaseAuth mAuth; //facebook
    Dialog sendDialog;
    TextView userName, userEmail, userPhone;//for profile view

    DatabaseReference ratingBase;

    CardView comments, nameAndLocation;

    RatingBar ratingBar;
    FloatingActionButton btn_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoraunt_detail);

        mAuth = FirebaseAuth.getInstance(); //facebook

        database = FirebaseDatabase.getInstance();
        admins = database.getReference("Restoraunt");
        ratingBase = database.getReference("RatingRestoraunt");

        comments = findViewById(R.id.comments);
        nameAndLocation = findViewById(R.id.nameAndLocation);
        admin_detail_time = findViewById(R.id.admin_detail_time);
        admin_detail_adress = findViewById(R.id.admin_detail_adress);
        admin_detail_name = findViewById(R.id.admin_detail_name);
        admin_detail_phone = findViewById(R.id.admin_detail_phone);
        admin_detail_menu = findViewById(R.id.admin_detail_menu);
        img_admin_detail = findViewById(R.id.img_admin_detail);
        img = findViewById(R.id.img);
        img1 = findViewById(R.id.img1);

        rest_location = findViewById(R.id.rest_location);

        btn_rating = findViewById(R.id.btn_rating);
        ratingBar = findViewById(R.id.ratingBar);

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        if (getIntent() != null)
            restoraunt_detail_Id = getIntent().getStringExtra("RestorauntId");
        if (!restoraunt_detail_Id.isEmpty()){

            if (Common.isConnectedToInternet(getBaseContext())){
                //коли немає зєднання з інтернетом
                getAdminDetail(restoraunt_detail_Id);
                getRatingEvent(restoraunt_detail_Id);
            }
            else  {
                Toast.makeText(RestorauntDetail.this, "Немеє з’єднання з інтернетом!", Toast.LENGTH_SHORT).show();
                return ;
            }
        }

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestorauntDetail.this, ShowCommentRestoraunt.class);
                intent.putExtra(Common.INTENT_RESTORAUNT_ID, restoraunt_detail_Id);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String phone = user.getPhoneNumber();


        View header = navigationView.getHeaderView(0);
        userPhone = header.findViewById(R.id.userPhone);
//        userPhone.setText(phone1);
        userEmail = header.findViewById(R.id.userEmail);
        userEmail.setText(email);
        userName = header.findViewById(R.id.userName);
        userName.setText(name);

    }

    private void getRatingEvent(String restoraunt_detail_Id) {
        Query eventRating = ratingBase.orderByChild("restorauntId").equalTo(restoraunt_detail_Id);
        eventRating.addValueEventListener(new ValueEventListener() {
            int count=0, sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    RatingRest item = postSnapshot.getValue(RatingRest.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float avarage = sum / count;
                    ratingBar.setRating(avarage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Підтвердити")
                .setNegativeButtonText("Відмінити")
                .setNoteDescriptions(Arrays.asList("Жахливо", "Погано", "Ок", "Добре", "Чудово"))
                .setDefaultRating(1)
                .setTitle("Дайте оцінку події")
                .setDescription("Оберіть рейтинг")
                .setHintTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Напишіть свій відгук")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(RestorauntDetail.this)
                .show();
    }

    private void getAdminDetail(String admin_detail_id) {

        admins.child(admin_detail_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Restoraunt admin = dataSnapshot.getValue(Restoraunt.class);

                Picasso.with(getBaseContext()).load(admin.getImage())
                        .into(img_admin_detail);
                Picasso.with(getBaseContext()).load(admin.getImg())
                        .into(img);
                Picasso.with(getBaseContext()).load(admin.getImg1())
                        .into(img1);

                admin_detail_adress.setText(admin.getAdress());
                admin_detail_time.setText(admin.getTime());
                admin_detail_name.setText(admin.getName());

                rest_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(admin.getLocation()));
                        startActivity(intent);
                    }
                });
                admin_detail_phone.setText(admin.getPhoneOfRest());
                admin_detail_phone.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                admin_detail_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + admin.getPhoneOfRest()));
                        if (ActivityCompat.checkSelfPermission(RestorauntDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {

        final FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();

        final RatingRest rating = new RatingRest(name,
                restoraunt_detail_Id,
                String.valueOf(value),
                comments);

        ratingBase.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RestorauntDetail.this, "Thank you", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home){
            Intent menu = new Intent(this, Home.class);
            startActivity(menu);
        }

        if (id == R.id.nav_admin){
            Intent admin = new Intent(this, Buildings.class);
            startActivity(admin);
        }

        if (id == R.id.nav_news){
            Intent news = new Intent(this, NewsActivity.class);
            startActivity(news);
        }

        if (id == R.id.nav_events){
            Intent events= new Intent(this, EventActivity.class);
            startActivity(events);
        }

        if (id == R.id.nav_relax){
            Intent relax = new Intent(this, RelaxActivity.class);
            startActivity(relax);
        }

        if (id == R.id.nav_bus){
            Intent bus = new Intent(this, Bus.class);
            startActivity(bus);
        }

        if (id == R.id.nav_flat){
            Intent flat = new Intent(this, Flat.class);
            startActivity(flat);
        }


        if (id == R.id.nav_share){ //share button
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=oil.city";
            String shareOb = "Oil City Boryslav";

            share.putExtra(Intent.EXTRA_TEXT, shareBody);
            share.putExtra(Intent.EXTRA_SUBJECT, shareOb);

            startActivity(Intent.createChooser(share, "Share Using"));

        }

        if (id == R.id.nav_send){
            sendDialog = new Dialog(this);
            showPopMenu();
        }

        if (id == R.id.nav_exit) {

            Paper.book().destroy();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

//            com.facebook.login.LoginManager.getInstance().logOut();

            mAuth.signOut();
            sendToLogin();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showPopMenu() {
        final EditText etTo, etSubject, etMessage;
        Button btnSend;
        sendDialog.setContentView(R.layout.activity_email);
        etMessage = sendDialog.findViewById(R.id.et_message);
        etSubject = sendDialog.findViewById(R.id.et_subject);
        etTo = sendDialog.findViewById(R.id.et_to);
        btnSend = sendDialog.findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:" + etTo.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                startActivity(intent);
            }
        });
        sendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sendDialog.show();
    }

    private void sendToLogin() {//facebook
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}







