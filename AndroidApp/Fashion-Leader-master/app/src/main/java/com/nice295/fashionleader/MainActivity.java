package com.nice295.fashionleader;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = MainActivity.class.getName().trim();

    private ViewPager upPager;
    private ViewPager downPager;
    private UpPagerAdapter mUpPagerAdapter;
    private DownPagerAdapter mDownPagerAdapter;
    private Button btnConfirm;
    private ImageView ivFace;

    Integer upUrl[] = {R.drawable.up_clothe, R.drawable.up_clothe};
    Integer downUrl[] = {R.drawable.down_clothe, R.drawable.down_clothe};

    private ArrayList<ParseObject> parseObjectUp;
    private ArrayList<ParseObject> parseObjectDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parseObjectUp =  new ArrayList<ParseObject>();
        parseObjectDown =  new ArrayList<ParseObject>();

        upPager = (ViewPager) findViewById(R.id.upPager);
        mUpPagerAdapter = new UpPagerAdapter(this, parseObjectUp);
        upPager.setAdapter(mUpPagerAdapter);

        downPager = (ViewPager) findViewById(R.id.downPager);
        mDownPagerAdapter = new DownPagerAdapter(this, parseObjectDown);
        downPager.setAdapter(mDownPagerAdapter);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);


        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/BMHANNA_11yrs_ttf.ttf");
        btnConfirm.setTypeface(font);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setTypeface(font);

        upPager.setOnPageChangeListener(this);

        ivFace = (ImageView) findViewById(R.id.ivFace);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Coordination");
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Count of clothe" + list.size());

                    parseObjectUp.clear();
                    parseObjectDown.clear();

                    for (ParseObject cloth : list) {
                        Log.d(TAG, "ObjectId: " + cloth.getObjectId());
                        Log.d(TAG, "Type: " + cloth.getString("Type"));
                        Log.d(TAG, "Image URL: " + cloth.getParseFile("Image").getUrl());

                        if (cloth.getString("Type").equals("up")) {
                            parseObjectUp.add(cloth);
                        }
                        else if (cloth.getString("Type").equals("down")) {
                            parseObjectDown.add(cloth);
                        }
                    }

                    mUpPagerAdapter.update(parseObjectUp);
                    mDownPagerAdapter.update(parseObjectDown);

                    MediaPlayer music = MediaPlayer.create(getApplicationContext(), R.raw.voice1);
                    music.start();

                } else {
                        Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnConfirm) {
            ParseObject object = parseObjectUp.get(upPager.getCurrentItem());
            Log.e(TAG, "Selected item: " + object.getString("ID"));

            ParsePush push = new ParsePush();
            //push.setChannel("Hanger");
            push.setMessage(object.getString("ID"));
            push.sendInBackground();

            MediaPlayer music = MediaPlayer.create(getApplicationContext(), R.raw.voice2);
            music.start();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position % 2 == 0) {
            Log.d("onPageScrolled", "Even");
            ivFace.setImageResource(R.drawable.face1);
        }
        else {
            Log.d("onPageScrolled", "Even");
            ivFace.setImageResource(R.drawable.face0);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
