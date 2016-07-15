package com.lle.mydemo.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lle.mydemo.R;
import com.lle.mydemo.base.BaseActivity;
import com.lle.mydemo.fragment.FragmentFactory;
import com.lle.mydemo.view.NoScrollViewPager;

public class MainActivity extends BaseActivity {
    //页面切换动画效果
    private boolean hasAnim;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;
    private NestedScrollView mNestedScrollView;
    private NavigationView mNavigationView;
    private FloatingActionButton mFab2;
    //NestedScrollView是否需要触摸事件
    private boolean hasTouchNested = true;

    public void setHasTouchNested(boolean hasTouchNested) {
        this.hasTouchNested = hasTouchNested;
    }

    public FloatingActionButton getFab2() {
        return mFab2;
    }

    public FloatingActionButton getFab() {
        return mFab;
    }

    private RadioGroup mRadioGroup;

    public void setHasAnim(boolean hasAnim) {
        this.hasAnim = hasAnim;
    }

    public boolean isHasAnim() {
        return hasAnim;
    }

    private Toolbar mToolbar;
    private NoScrollViewPager mViewPager;

    float downY = 0;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        //设置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        assert mToolbar != null;
        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化DrawerLayout
        initDrawerLayout();

        //初始化FloatingActionButton
        initFloatingActionButton();

        //初始化ViewPager
        initViewPager();

        //初始化NavigationView
        initNavigationView();

        //初始化nestedScrollView
        initNestedScrollView();
    }

    private void initNestedScrollView() {
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        assert mNestedScrollView != null;
        mNestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!hasTouchNested)return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveY = event.getY();
                        float y = moveY - downY;
                        if (y < -10) {
                            if (mFab2.getVisibility() == View.VISIBLE) {
                                fabOutsideAnimator(mFab2, 50);
                                mFab2.setVisibility(View.GONE);
                            }
                            mFab.hide();
                        } else if (y > 10) {
                            mFab.show();
                        }

//                        mRadioGroup.setVisibility(View.GONE);
                        break;
                    default:
//                        mRadioGroup.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });
    }

    private void initNavigationView() {
        mNavigationView = (NavigationView) findViewById(R.id.navigationview);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Snackbar.make(mNavigationView, "pressed " + item.getTitle(), Snackbar.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawers();
                //详情页面
                if (item.getItemId() == R.id.nav_discussion) {
                    startActivity(new Intent(MainActivity.this, DetailActivity.class));
                //主页面
                }else if(item.getItemId() == R.id.nav_home){
                    mRadioGroup.check(R.id.rb_home);
                }

                return true;
            }
        });
    }

    private void initViewPager() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.vp);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FragmentFactory.getFragment(position);
            }

            @Override
            public int getCount() {
                return 5;
            }
        });
        //设置viewpager页面切换监听事件
        mViewPager.setOnPageChangeListener(new NoScrollViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                mNestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mNestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
                    }
                });
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initFloatingActionButton() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab2 = (FloatingActionButton) findViewById(R.id.fab2);
        assert mFab != null;
        assert mFab2 != null;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                fab2.setVisibility(fab2.getVisibility() == View.GONE? View.VISIBLE: View.GONE);
                if (mFab2.getVisibility() == View.GONE) {
                    mFab2.setVisibility(View.VISIBLE);
                    //悬浮按钮移动动画--显示
                    fabInsideAnimator(mFab2);
                } else {
                    //悬浮按钮移动动画--消失
                    fabOutsideAnimator(mFab2);
                }
            }
        });
        mFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //悬浮按钮移动动画--消失
                fabOutsideAnimator(mFab2);
            }
        });
    }

    /**
     * 悬浮按钮消失动画
     * @param fab2 悬浮按钮
     */
    public void fabOutsideAnimator(final FloatingActionButton fab2, long duration) {
        float height = fab2.getMeasuredHeight();
        height = height + getResources().getDimension(R.dimen.fab_margin) * 2;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(fab2, "translationY", -height, 0);
        translationY.setDuration(duration);
        translationY.setTarget(fab2);
        translationY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fab2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        translationY.start();
    }

    private void fabOutsideAnimator(final FloatingActionButton fab2) {
        fabOutsideAnimator(fab2, 500);
    }

    /**
     * 悬浮按钮显示动画
     * @param fab2 悬浮按钮
     */
    private void fabInsideAnimator(FloatingActionButton fab2) {
        fab2.measure(0, 0);
        float height = fab2.getMeasuredHeight();
        height = height + getResources().getDimension(R.dimen.fab_margin) * 2;
        //        Snackbar.make(fab2, "height"+height, Snackbar.LENGTH_SHORT).show();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(fab2, "translationY", 0, -height);
        translationY.setDuration(500);
        translationY.setTarget(fab2);
        translationY.start();
    }

    @Override
    protected void setListener() {
        //设置RadioGroup监听事件
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        assert mRadioGroup != null;
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int item = 0;
                switch (checkedId) {
                    case R.id.rb_home:
                        item = 0;
                        mToolbar.setTitle("页面1");
                        break;
                    case R.id.rb_news:
                        item = 1;
                        mToolbar.setTitle("页面2");
                        break;
                    case R.id.rb_service:
                        item = 2;
                        mToolbar.setTitle("页面3");
                        break;
                    case R.id.rb_subject:
                        item = 3;
                        mToolbar.setTitle("页面4");
                        break;
                    case R.id.rb_setting:
                        item = 4;
                        mToolbar.setTitle("页面5");
                        break;
                }
                mViewPager.setCurrentItem(item, hasAnim);
                setHasTouchNested(item==2?false:true);
            }
        });
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        //设置监听
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        //同步状态
        actionBarDrawerToggle.syncState();
    }

    public void setRadioGroupVisibility(int visibility){
        mRadioGroup.setVisibility(visibility);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(MainActivity.this, "item1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                Toast.makeText(MainActivity.this, "item2", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
