package com.example.dllo.mirror_20.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.mirror_20.R;

import com.example.dllo.mirror_20.allcategories.AllCategoriesFragment;
import com.example.dllo.mirror_20.flatlight.FlatLightFragment;
import com.example.dllo.mirror_20.login.LoginActivity;
import com.example.dllo.mirror_20.projectshare.ProjectShareFragment;
import com.example.dllo.mirror_20.shoppingcart.ShoppingCartFragment;
import com.example.dllo.mirror_20.sunglasses.SunglassesFragment;
import com.example.dllo.mirror_20.view.VerticalViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AllCategoriesFragment.MenuOnClickListener, MenuFragment.MenuDetailOnClickListener, View.OnClickListener {
    private VerticalViewPager mainVerticalViewpager;
    private MainVerticalViewPagerAdapter mainVerticalViewPagerAdapter;
    private ArrayList<Fragment> fragments;
    private FrameLayout mainFrameLayout;
    private MenuFragment menuFragment;
    private ImageView mainMirrorImg;
    private TextView mainLoginTv;
    private  long mTime;//用来记录用户第一次点击返回键时的时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainVerticalViewpager = (VerticalViewPager) findViewById(R.id.main_vertical_viewpager);
        mainFrameLayout = (FrameLayout) findViewById(R.id.main_frame_layout);
        mainMirrorImg = (ImageView) findViewById(R.id.main_mirror_img);
        mainLoginTv = (TextView) findViewById(R.id.main_login_tv);

        mainMirrorImg.setOnClickListener(this);
        mainLoginTv.setOnClickListener(this);

        mainVerticalViewpager.internalCanScrollVertically(View.FOCUS_LEFT);
        mainVerticalViewPagerAdapter = new MainVerticalViewPagerAdapter(getSupportFragmentManager());
        fragments = new ArrayList<>();
        fragments.add(new AllCategoriesFragment());

        fragments.add(new FlatLightFragment());
        fragments.add(new SunglassesFragment());
        fragments.add(new ProjectShareFragment());
        fragments.add(new ShoppingCartFragment());


        mainVerticalViewPagerAdapter.setFragments(fragments);
        mainVerticalViewpager.setAdapter(mainVerticalViewPagerAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_mirror_img:
                //加动画
                showScaleAnim(mainMirrorImg);
                break;
            case R.id.main_login_tv:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


    //实现对应每个viewPager item的左上角点击监听
    @Override
    public void menuOnClick() {
        menuFragment = new MenuFragment();

        //替换为menufragment 并将viewpager的当前页position传入
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, menuFragment).commit();
        mainVerticalViewpager.setVisibility(View.GONE);
        int pos = mainVerticalViewpager.getCurrentItem();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        menuFragment.setArguments(bundle);

    }

    //设置mirror动画
    public void showScaleAnim(ImageView imageView){
        //前两个参数是x轴从多少道多少
        //3、4参数是y轴从多少道多少
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,1.1f,1f,1.1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setRepeatCount(1);//1为动两次
        scaleAnimation.setDuration(300);
        imageView.startAnimation(scaleAnimation);
    }


    //实现分类界面的按钮监听
    @Override
    public void menuDetailOnClick(int pos) {
        mainVerticalViewpager.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().hide(menuFragment).commit();
        mainVerticalViewpager.setCurrentItem(pos);

    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - mTime > 2000) {
            mTime = System.currentTimeMillis();//System.currentTimeMillis()是用来获取当前时间
            Toast.makeText(this, "小主，两秒内连点两下小的就挂啦", Toast.LENGTH_SHORT).show();
        } else {
            System.exit(0);
        }
    }
}
