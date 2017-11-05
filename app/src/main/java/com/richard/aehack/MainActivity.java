package com.richard.aehack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
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


        String myJson=inputStreamToString(getResources().openRawResource(R.raw.testimonial));
        Type testimonialType = new TypeToken<List<Testimonial>>() {}.getType();
        List<Testimonial> testimonialList = new Gson().fromJson(myJson, testimonialType);

        String myJson2=inputStreamToString(getResources().openRawResource(R.raw.treatment));
        Type resourceType = new TypeToken<List<Resource>>() {}.getType();
        List<Resource> resourceList = new Gson().fromJson(myJson2, resourceType);
        RecyclerView resourceView = findViewById(R.id.treatmentListView);
        resourceView.setAdapter(new ResourceAdapter(resourceList));
        resourceView.setHasFixedSize(true);
        resourceView.setLayoutManager(new LinearLayoutManager(this));
        resourceView.setItemAnimator(new DefaultItemAnimator());
        resourceView.setVisibility(View.VISIBLE);

        RecyclerView rv = findViewById(R.id.testimonialList);
        rv.setAdapter(new TestimonialAdapter(testimonialList));
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setVisibility(View.VISIBLE);

        ConstraintLayout tl = findViewById(R.id.treatmentList);
        tl.setVisibility(View.GONE);

        ((FloatingActionButton)findViewById(R.id.pledge)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToFacebook(MainActivity.this, "SUB", "TEX");
            }
        });
    }

    void shareToFacebook(Context context, String subject, String text) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList)
        {
            if ((app.activityInfo.name).startsWith("com.facebook.katana"))
            {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                context.startActivity(shareIntent);
                break;
            }
        }
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

    class TestimonialAdapter  extends RecyclerView.Adapter<Holder>{
        List<Testimonial> list;
        TestimonialAdapter(List<Testimonial> testimonials) {
            this.list = testimonials;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.testimoniallist_view, parent, false);
            Holder vh = new Holder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
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

    class ResourceAdapter  extends RecyclerView.Adapter<Holder>{
        List<Resource> list;
        ResourceAdapter(List<Resource> resources) {
            this.list = resources;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resources_view, parent, false);
            Holder vh = new Holder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            TextView tv = holder.v.findViewById(R.id.info_text);
            tv.setText(list.get(position).title);

            Button call = holder.v.findViewById(R.id.call);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + list.get(holder.getAdapterPosition()).phone));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        View v;
        public Holder(View itemView) {
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

    class Resource {
        @SerializedName("title")
        public String title;
        @SerializedName("phone")
        public String phone;
        @SerializedName("web")
        public String web;
    }
}
