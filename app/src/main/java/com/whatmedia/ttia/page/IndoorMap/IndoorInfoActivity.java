package com.whatmedia.ttia.page.IndoorMap;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.point_consulting.pc_indoormapoverlaylib.Manager;
import com.whatmedia.ttia.R;
import com.whatmedia.ttia.component.MyMarquee;
import com.whatmedia.ttia.component.MyToolbar;
import com.whatmedia.ttia.page.IndoorBaseActivity;
import com.whatmedia.ttia.utility.MyAppUtils;
import com.whatmedia.ttia.utility.MyApplication;
import com.whatmedia.ttia.utility.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndoorInfoActivity extends IndoorBaseActivity {

    @BindView(R.id.myToolbar)
    MyToolbar mMyToolbar;
    @BindView(R.id.myMarquee)
    MyMarquee mMyMarquee;
    @BindView(R.id.loadingView)
    FrameLayout mLoadingView;
    @BindView(R.id.imageView_home)
    ImageView mImageViewHome;

    private String mMarqueeMessage;

    private Manager.Location m_location;
    private static final int s_requestCodeDirections = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        initAppbar();
        initMarquee();

        ImageView logo = (ImageView)findViewById(R.id.info_logo);
        final int logoRes = getIntent().getIntExtra(MyAppUtils.s_extra_logo, 0);
        if (logoRes != 0) {
            logo.setImageResource(logoRes);
        }
        else
        {
            logo.setVisibility(View.GONE);
        }

        Button button = (Button) findViewById(R.id.info_directions);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final Intent intent = getIntent();
        // ktodo subst with location
        m_location = intent.getParcelableExtra(MyAppUtils.s_extra_location);
        final List<MyAppUtils.PropDesc> props = (List<MyAppUtils.PropDesc>)intent.getSerializableExtra(MyAppUtils.s_extra_propsMap);

        final View header = findViewById(R.id.info_header);
        header.setBackgroundColor(intent.getIntExtra(MyAppUtils.s_extra_color, 0xffffffff));

        final MyApplication app = (MyApplication)getApplication();
        final Manager manager = app.m_manager;
        final String[] titles = new String[2];
        if (manager != null)
        {
            manager.getTitleForLocation(m_location, titles);
        }

        if (titles[0] != null)
        {
            TextView tv = (TextView) findViewById(R.id.info_title);
            tv.setText(titles[0]);
        }
        if (titles[1] != null) {
            TextView tv = (TextView) findViewById(R.id.info_subtitle);
            tv.setText(titles[1]);
        }
        {
            TextView tv = (TextView) findViewById(R.id.info_level);
            tv.setText(String.format(getString(R.string.level_string), m_location.m_coord3D.m_ordinal));
        }

        final LayoutInflater inflater = getLayoutInflater();
        LinearLayout propsGroup = (LinearLayout)findViewById(R.id.info_props);
        for (MyAppUtils.PropDesc entry : props) {
            final LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.prop, propsGroup, false);
            final ImageView iv = (ImageView)ll.findViewById(R.id.prop_image);
            iv.setImageResource(entry.m_id);
            final TextView tv2 = (TextView)ll.findViewById(R.id.prop_text);
            tv2.setText(entry.m_string);
            propsGroup.addView(ll);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == s_requestCodeDirections && RESULT_OK == resultCode)
        {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    public void onDirections(View view)
    {
        Intent intent = new Intent(this, IndoorSearchActivity.class);
        intent.putExtra(MyAppUtils.s_extra_end, m_location);
        startActivityForResult(intent, s_requestCodeDirections);
    }

    /**
     * init App bar
     */
    private void initAppbar() {
        mMyToolbar.clearState()
                .setBackground(ContextCompat.getColor(getApplicationContext(), R.color.colorMarquee))
                .setBackIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back))
                .setTitleText(getString(R.string.home_indoor_map_title))
                .setOnBackClickListener(new MyToolbar.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    /**
     * init marquee
     */
    private void initMarquee() {
        mMarqueeMessage = getString(R.string.marquee_parking_info, Util.getMarqueeSubMessage(getApplicationContext()));
        mMyMarquee.clearState()
                .setMessage(mMarqueeMessage)
                .setIconVisibility(View.GONE);
    }
}
