package com.djandroid.jdroid.materialdesign;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;

import com.djandroid.jdroid.materialdesign.googleio.AddToScheduleFABFrameLayout;
import com.djandroid.jdroid.materialdesign.googleio.CheckableFrameLayout;
import com.djandroid.jdroid.materialdesign.googleio.ObservableScrollView;
import com.djandroid.jdroid.materialdesign.googleio.ProgressDrawable;

import java.util.ArrayList;

/**
 * Created by dhawal sodha parmar on 5/2/2015.
 */
public class SocialActivity extends AppCompatActivity {

    ArrayList<UserProfileModel> items = new ArrayList<UserProfileModel>();

    // the container for everything except for a toolbar
    protected ObservableScrollView scrollView;
    protected ImageView imageView;
    // title layout: can contain subtitles, etc.
    protected FrameLayout titleLayout;
    // title background color
    protected View titleBackground;
    // title text
    protected TextView titleView;
    // content layout: place the views you need into it
    protected LinearLayout bodyLayout;
    protected TextView bodyTextView;
    //protected ImageButton quasiFab;
    protected Toolbar toolbar;
    private boolean titleInUpperPosition = false;
    protected ProgressDrawable titleBackDrawable;
    private int titleBackColor;
    private int statusBarHeight;
    private int titleViewHeight;
    private int toolbarHeight;
    private int quasiFabCenterHeight;
    private int[] location = new int[2];
    CheckableFrameLayout mAddScheduleButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_social);

        final View root = getLayoutInflater().inflate(R.layout.activity_social, null);
        setContentView(root);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(false
                ? R.drawable.ic_ab_close : R.drawable.ic_up);
        toolbar.setTitle("" + getResources().getString(R.string.app_name));
    }
}
