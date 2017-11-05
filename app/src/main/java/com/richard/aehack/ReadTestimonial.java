package com.richard.aehack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReadTestimonial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_testimonial);

        ((TextView)findViewById(R.id.title)).setText(getIntent().getStringExtra("TITLE"));
        ((TextView)findViewById(R.id.description)).setText(getIntent().getStringExtra("DESCRIPTION"));
    }
}
