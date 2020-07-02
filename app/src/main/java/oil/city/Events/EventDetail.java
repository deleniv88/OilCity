package oil.city.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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

import java.net.URI;
import java.util.Arrays;

import io.paperdb.Paper;
import me.anwarshahriar.calligrapher.Calligrapher;
import oil.city.Administrations.Buildings;
import oil.city.Bus.Bus;
import oil.city.Common.Common;
import oil.city.Flat.Flat;
import oil.city.Home;
import oil.city.MainActivity;
import oil.city.Model.Event;
import oil.city.Model.Rating;
import oil.city.News.NewsActivity;
import oil.city.R;
import oil.city.Relax.RelaxActivity;
import oil.city.ShowCommentEvent;

public class EventDetail extends AppCompatActivity implements RatingDialogListener, NavigationView.OnNavigationItemSelectedListener {

    TextView event_detail_date, event_detail_time, event_detail_price, event_detail_adress, event_detail_description;
    ImageView img_event_detail;
    CollapsingToolbarLayout collapsing_event_detail;
    String event_detail_Id="";

    FirebaseDatabase database;
    DatabaseReference events;
    DatabaseReference ratingBase;

    Button btnShowComment;

    RatingBar ratingBar;
    FloatingActionButton btn_rating;

    FirebaseAuth mAuth; //facebook
    Dialog sendDialog;
    TextView userName, userEmail, userPhone;//for profile view
    CardView second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "blogger.otf",true);

        mAuth = FirebaseAuth.getInstance(); //facebook

        database = FirebaseDatabase.getInstance();
        events = database.getReference("Event");
        ratingBase = database.getReference("Rating");

        btnShowComment = findViewById(R.id.btnShowComment);
        event_detail_adress = findViewById(R.id.event_detail_adress);
        event_detail_date = findViewById(R.id.event_detail_date);
        event_detail_description = findViewById(R.id.event_detail_description);
        event_detail_price = findViewById(R.id.event_detail_price);
        event_detail_time = findViewById(R.id.event_detail_time);
        img_event_detail = findViewById(R.id.img_event_detail);
        second = findViewById(R.id.second);

        btn_rating = findViewById(R.id.btn_rating);
        ratingBar = findViewById(R.id.ratingBar);

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        collapsing_event_detail = findViewById(R.id.collapsing_event_detail);
        collapsing_event_detail.setExpandedTitleTextAppearance(R.style.ExpandeAppbar);
        collapsing_event_detail.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if (getIntent() != null)
            event_detail_Id = getIntent().getStringExtra("EventId");
        if (!event_detail_Id.isEmpty()){

            if (Common.isConnectedToInternet(getBaseContext())){
                //коли немає зєднання з інтернетом
                getEventDetail(event_detail_Id);
                getRatingEvent(event_detail_Id);
            }
            else  {
                Toast.makeText(EventDetail.this, "Немеє з’єднання з інтернетом!", Toast.LENGTH_SHORT).show();
                return ;
            }
        }

        btnShowComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetail.this, ShowCommentEvent.class);
                intent.putExtra(Common.INTENT_EVENT_ID, event_detail_Id);
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

    private void getRatingEvent(String event_detail_Id) {
        Query eventRating = ratingBase.orderByChild("eventId").equalTo(event_detail_Id);
        eventRating.addValueEventListener(new ValueEventListener() {
            int count=0, sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
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
                .create(EventDetail.this)
                .show();
    }

    private void getEventDetail(String event_detail_id) {

        events.child(event_detail_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Event event = dataSnapshot.getValue(Event.class);

                Picasso.with(getBaseContext()).load(event.getImage())
                        .into(img_event_detail);

//                collapsing_event_detail.setTitle(event.getName());

//                news_name.setText(news.getName());
                event_detail_date.setText(event.getDate());
                event_detail_adress.setText(event.getAdress());
                event_detail_description.setText(event.getDescription());
                event_detail_price.setText(event.getPrice());
                event_detail_time.setText(event.getTime());
                second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent eventLocation = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getLocation()));
                        startActivity(eventLocation);
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

        final Rating rating = new Rating(name,
                event_detail_Id,
                String.valueOf(value),
                comments);

        ratingBase.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EventDetail.this, "Thank you", Toast.LENGTH_SHORT).show();
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




