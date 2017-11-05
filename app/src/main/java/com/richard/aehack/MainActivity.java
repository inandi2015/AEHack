package com.richard.aehack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import junit.framework.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            RecyclerView rv = findViewById(R.id.testimonialList);
            ConstraintLayout tl = findViewById(R.id.treatmentList);
            switch (item.getItemId()) {
                case R.id.testimonial:
                    rv.setVisibility(View.VISIBLE);
                    tl.setVisibility(View.GONE);
                    return true;
                case R.id.treatment:
                    rv.setVisibility(View.GONE);
                    tl.setVisibility(View.VISIBLE);
                    return true;
                case R.id.resource:
                    rv.setVisibility(View.GONE);
                    tl.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ConstraintLayout tl = findViewById(R.id.treatmentList);

        String myJson=inputStreamToString(getResources().openRawResource(R.raw.testimonial));

        Type listType = new TypeToken<List<Testimonial>>() {}.getType();
        List<Testimonial> testimonialList = new Gson().fromJson(myJson, listType);

        RecyclerView rv = findViewById(R.id.testimonialList);
        rv.setAdapter(new TestimonialAdapter(testimonialList));
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setVisibility(View.VISIBLE);
        tl.setVisibility(View.GONE);
    }

    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    class TestimonialAdapter  extends RecyclerView.Adapter<TestimonialHolder>{
        List<Testimonial> list;
        TestimonialAdapter(List<Testimonial> testimonials) {
            this.list = testimonials;
        }

        @Override
        public TestimonialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.testimoniallist_view, parent, false);
            TestimonialHolder vh = new TestimonialHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final TestimonialHolder holder, int position) {
            TextView tv = holder.v.findViewById(R.id.title);
            tv.setText(list.get(position).title);

            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ReadTestimonial.class);
                    intent.putExtra("TITLE", list.get(holder.getAdapterPosition()).title);
                    intent.putExtra("DESCRIPTION", list.get(holder.getAdapterPosition()).description);
                    startActivity(intent);
                }
            });

            ImageView imageView = holder.v.findViewById(R.id.imageView);

            int random = (position % 8) + 1;
            switch(random) {
                case 1:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic1));
                    break;
                case 2:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic2));
                    break;
                case 3:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic3));
                    break;
                case 4:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic4));
                    break;
                case 5:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic5));
                    break;
                case 6:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic6));
                    break;
                case 7:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic7));
                    break;
                default:
                    imageView.setImageDrawable(getDrawable(R.drawable.pic8));
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class TestimonialHolder extends RecyclerView.ViewHolder {
        View v;
        public TestimonialHolder(View itemView) {
            super(itemView);
            this.v = itemView;
        }
    }

    class Testimonial {
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
    }
}
