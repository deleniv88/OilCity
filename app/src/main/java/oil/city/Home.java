package oil.city;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;
import com.jetradarmobile.snowfall.SnowfallView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.paperdb.Paper;
import me.anwarshahriar.calligrapher.Calligrapher;
import oil.city.Administrations.Buildings;
import oil.city.Bus.Bus;
import oil.city.Common.Common;
import oil.city.Events.EventActivity;
import oil.city.Flat.Flat;
import oil.city.Interface.ItemClickListener;
import oil.city.Model.News;
import oil.city.News.NewsActivity;
import oil.city.News.NewsDetail;
import oil.city.Relax.RelaxActivity;
import oil.city.Update.UpdateHelper;
import oil.city.ViewHolder.NewsViewHolder;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UpdateHelper.OnUpdateCheckListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    LinearLayout linearLayout;
    ImageView PhotoAdmin, PhotoNews, PhotoEvent, PhotoRelax, PhotoBus, PhotoFlat;
    HorizontalScrollView horizontalScrollView;

    TextView userName, userEmail, userPhone;//for profile view
    ImageView imgUserPhoto;//for profile view

    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImgUri;

    StorageReference reference;
    DatabaseReference mDatabase;

    FirebaseAuth mAuth; //facebook

    Dialog sendDialog;

    SnowfallView snowfallView;

    FirebaseDatabase database;
    DatabaseReference eventList;
    DatabaseReference foodList;

    String eventId = "";

//    FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;//for events
    FirebaseRecyclerAdapter<News, NewsViewHolder> adapter;

    //facebook share
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        Paper.init(Home.this);//for remember me

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "blogger.otf",true);

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

        database = FirebaseDatabase.getInstance();
        eventList = database.getReference("Event");
        foodList = database.getReference("Food");

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

//        horizontalScrollView = findViewById(R.id.horizontal);
        linearLayout = findViewById(R.id.linear);

        PhotoAdmin = findViewById(R.id.PhotoAdmin);
        PhotoEvent = findViewById(R.id.PhotoEvents);
        PhotoRelax = findViewById(R.id.PhotoRelax);
        PhotoNews = findViewById(R.id.PhotoNews);
        PhotoBus = findViewById(R.id.PhotoBus);
        PhotoFlat = findViewById(R.id.PhotoFlat);

        PhotoFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Flat.class);
                startActivity(intent);
            }
        });

        PhotoBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Bus.class);
                startActivity(intent);
            }
        });

        PhotoAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Buildings.class);
                startActivity(intent);
            }
        });

        PhotoEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        PhotoNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, EventActivity.class);
                startActivity(intent);
            }
        });

        PhotoRelax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, RelaxActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.list_home);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

//        snowfallView = findViewById(R.id.snowfall);

        mAuth = FirebaseAuth.getInstance(); //facebook

//        imgUserPhoto = findViewById(R.id.avatar);
        userEmail = findViewById(R.id.userEmail);//for profile view
        userName = findViewById(R.id.userName);//for profile view
        userPhone = findViewById(R.id.userPhone);//for profile view

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.getCurrentUser();

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

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
//        imgUserPhoto = header.findViewById(R.id.avatar);

//        final ImageView imgUserPhoto = header.findViewById(R.id.avatar);
//        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (pickedImgUri != null){
//                    updateUserPhoto();
//                }
//                else if (Build.VERSION.SDK_INT >= 22) {
//                    checkAndRequestPermision();
//                } else {
//                    openGallery();
//                }
//            }
//        });

//        if (user.getPhotoUrl() != null){
//            Glide.with(this).load(user.getPhotoUrl()).into(imgUserPhoto);
//        }else
//            Glide.with(this).load(R.drawable.ic_launcher_background).into(imgUserPhoto);

        if (Common.isConnectedToInternet(getBaseContext()))
            loadEvent();
        else {
            Toast.makeText(Home.this, "Немає з’єднання з інтернетом!", Toast.LENGTH_SHORT).show();
            return ;
        }

    }


//    private void updateUserPhoto() {
//        final FirebaseUser user = mAuth.getCurrentUser();
//        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
//        final StorageReference imgFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
//        imgFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
//                                .setPhotoUri(uri)
//                                .build();
//                        user.updateProfile(profileChangeRequest)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            updateUI();
//                                        }
//                                    }
//                                });
//                    }
//                });
//            }
//        });
//    }

    private void loadEvent() {

        FirebaseRecyclerOptions<News> foodOptions = new FirebaseRecyclerOptions.Builder<News>()
                .setQuery(foodList, News.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<News, NewsViewHolder>(foodOptions) {
            @NonNull
            @Override
            public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item, parent, false);
                return new NewsViewHolder(item);
            }

            @Override
            protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull News model) {
                holder.news_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(holder.news_image);

                final News local = model;
                holder.setListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent news = new Intent(Home.this, NewsDetail.class);
                        news.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(news);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

//        FirebaseRecyclerOptions<Event> eventOptions = new FirebaseRecyclerOptions.Builder<Event>()//for events
//                .setQuery(eventList , Event.class)
//                .build();
//
//        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(eventOptions) {
//            @NonNull
//            @Override
//            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.event_item_2, parent, false);
//                return new EventViewHolder(itemView);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull final EventViewHolder holder, final int position, @NonNull final Event model) {
//
//                holder.name.setText(model.getName());
//                holder.date.setText(model.getDate());
//                holder.time.setText(model.getTime());
//                holder.adress.setText(model.getAdress());
//                holder.price.setText(model.getPrice());
//                Picasso.with(getBaseContext()).load(model.getImage())
//                        .into(holder.event_image);
//
//                final Event local = model;
//
//                holder.setListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent event = new Intent(Home.this, EventDetail.class);
//                        event.putExtra("EventId", adapter.getRef(position).getKey());
//                        startActivity(event);
//                    }
//                });
//
//                holder.image_share.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Intent share = new Intent(Intent.ACTION_SEND);
////                        share.setType("text/plain");
////
////                        String shareBody = "EventId";
////                        String shareOb = "EventId";
////
////                        share.putExtra(Intent.EXTRA_SUBJECT, shareBody);
////                        share.putExtra(Intent.EXTRA_TEXT, shareOb);
////
////                        startActivity(Intent.createChooser(share, "Share Using"));
//
//                        Picasso.with(getApplicationContext())
//                                .load(model.getImage())
//                                .into(target);
//                    }
//                });
//            }
//        };
//        adapter.startListening();
//        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

//    private void openGallery() {
//
//        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
//        gallery.setType("image/*");
//        startActivityForResult(gallery,REQUESTCODE);
//
//    }

//    private void checkAndRequestPermision() {
//        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
//                Toast.makeText(this, "Please acept for required", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                ActivityCompat.requestPermissions(Home.this,
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
//            Picasso.with(this).load(pickedImgUri).into(imgUserPhoto);
//            imgUserPhoto.setImageURI(pickedImgUri);
//        }
//
    }
//    private void updateUI() {
//        Intent intent = new Intent(getApplicationContext(), Home.class);
//        startActivity(intent);
//        finish();
//    }

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

        if (id == R.id.nav_sign_in){
            if (mAuth.getCurrentUser() == null) {
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
            }else {
                Toast.makeText(this, "Ви вже увійшли", Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.nav_share){ //share button
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=oil.city";
            String shareOb = "Oil City";

            share.putExtra(Intent.EXTRA_TEXT, shareBody);
            share.putExtra(Intent.EXTRA_SUBJECT, shareOb);

            startActivity(Intent.createChooser(share, "Share Using"));

        }

        if (id == R.id.nav_send){
            sendDialog = new Dialog(this);
            showPopMenu();
        }

        if (id == R.id.nav_exit) {

            if (mAuth.getCurrentUser() != null) {
                Paper.book().destroy();

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//            com.facebook.login.LoginManager.getInstance().logOut();

                mAuth.signOut();
                sendToLogin();
            } else {
                Toast.makeText(this, "Ви не зареєстровані", Toast.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.nav_about){
            sendDialog = new Dialog(this);
            showAboutMenu();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAboutMenu() {

        LinearLayout instagramMe, facebookMe;
        sendDialog.setContentView(R.layout.layout_about);

        instagramMe = sendDialog.findViewById(R.id.instagramMe);
        facebookMe = sendDialog.findViewById(R.id.facebookMe);

        instagramMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insta = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/deleniv"));
                startActivity(insta);
            }
        });

        facebookMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent face = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/deleniv8888"));
                startActivity(face);
            }
        });
        sendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sendDialog.show();
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
                        Toast.makeText(Home.this, "Ви скасували оновлення! Оновіть застосунок для вірного відображення інформації!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
}

