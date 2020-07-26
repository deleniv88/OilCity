package oil.city.Events;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rengwuxian.materialedittext.MaterialEditText;

import oil.city.Common.Common;
import oil.city.Model.Rating;
import oil.city.R;
import oil.city.ViewHolder.ShowCommentViewHolder;

public class ShowCommentEvent extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingBase;

    SwipeRefreshLayout swipeRefreshLayout;

    String event_detail_Id="";

    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;

    FirebaseAuth mAuth; //facebook

    MaterialEditText edtComment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);

        mAuth = FirebaseAuth.getInstance(); //facebook

        database = FirebaseDatabase.getInstance();
        ratingBase = database.getReference("Rating");

        recyclerView = findViewById(R.id.recyclerComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null);
                event_detail_Id = getIntent().getStringExtra(Common.INTENT_EVENT_ID);
                if (!event_detail_Id.isEmpty() && event_detail_Id != null)
                {

                    Query query = ratingBase.orderByChild("eventId").equalTo(event_detail_Id);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query, Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserName.setText(model.getUserName());
                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout, parent, false);
                            return new ShowCommentViewHolder(view);
                        }
                    };
                    loadComment(event_detail_Id);
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                if (getIntent() != null);
                event_detail_Id = getIntent().getStringExtra(Common.INTENT_EVENT_ID);
                if (!event_detail_Id.isEmpty() && event_detail_Id != null)
                {

                    Query query = ratingBase.orderByChild("eventId").equalTo(event_detail_Id);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query, Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserName.setText(model.getUserName());
                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.show_comment_layout, parent, false);
                            return new ShowCommentViewHolder(view);
                        }
                    };
                    loadComment(event_detail_Id);
                }
            }
        });

    }

    private void loadComment(String event_detail_id) {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateCommentDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals(Common.DELETE)){
            deleteComment(adapter.getRef(item.getOrder()).getKey());
        }
            return super.onContextItemSelected(item);
    }

    private void deleteComment(String key) {

    if (mAuth.getCurrentUser() != null) {
        ratingBase.child(key).removeValue();
        Toast.makeText(this, "Коментар видалено", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(this, "Для редагування потрібно увійти", Toast.LENGTH_SHORT).show();
    }
    }

    private void showUpdateCommentDialog(final String key, final Rating item) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(ShowCommentEvent.this);
            builder.setTitle("Оновіть коментар");

            final LayoutInflater inflater = this.getLayoutInflater();
            View add_new = inflater.inflate(R.layout.new_comment, null);

            edtComment = add_new.findViewById(R.id.edtName);

            edtComment.setText(item.getComment());

            builder.setView(add_new);

            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    updateComment(item);

                    item.setComment(edtComment.getText().toString());

                    ratingBase.child(key).setValue(item);
                    Toast.makeText(ShowCommentEvent.this, "Коментар оновлено", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Відміна", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

    }

    private void updateComment(Rating item) {
        if (getIntent() != null);
        event_detail_Id = getIntent().getStringExtra(Common.INTENT_EVENT_ID);
        if (!event_detail_Id.isEmpty() && event_detail_Id != null)
        {
            Query query = ratingBase.orderByChild("eventId").equalTo(event_detail_Id);

            FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                    .setQuery(query, Rating.class)
                    .build();

            adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {
                    holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                    holder.txtComment.setText(model.getComment());
                    holder.txtUserName.setText(model.getUserName());
                }

                @NonNull
                @Override
                public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.show_comment_layout, parent, false);
                    return new ShowCommentViewHolder(view);
                }
            };
            loadComment(event_detail_Id);
        }
    }
}


