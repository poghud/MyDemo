package com.lle.mydemo.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lle.mydemo.MyApplication;
import com.lle.mydemo.R;
import com.lle.mydemo.base.BaseActivity;
import com.lle.mydemo.fragment.FragmentFactory;
import com.lle.mydemo.utils.AutoScrollTask;
import com.lle.mydemo.utils.ImageCache;
import com.lle.mydemo.utils.UiUtils;
import com.lle.mydemo.view.LazyViewPager;
import com.lle.mydemo.view.MyScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar;
    private LazyViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private MyScrollView mNestedScrollView;
    private NavigationView mNavigationView;
    private FloatingActionButton mFab;
    private FloatingActionButton mFab2;
    private RadioGroup mRadioGroup;

    //沉浸式效果
    //    private boolean hasImmersive;
    //NestedScrollView是否需要触摸事件
    //    private boolean hasTouchNested = true;

    //topviewpager的图片
    int[] imgs = {R.drawable.twitter_icon_1, R.drawable.twitter_icon_2, R.drawable.twitter_icon_3, R.drawable.twitter_icon_4};
    //CollapsingToolbarLayout
    private List<View> mList;
    private LinearLayout ll_container;
    private List<ImageView> mPoints;
    private TextView mTextView;
    private AutoScrollTask mTask;
    private AppBarLayout mAppBarLayout;
    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private CollapsingToolbarLayout mCollapsingToolbar;

    private int mColor = Color.TRANSPARENT;
    private boolean mIsBarExpanded = true;

    public boolean isBarExpanded() {
        return mIsBarExpanded;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public FloatingActionButton getFab2() {
        return mFab2;
    }

    public FloatingActionButton getFab() {
        return mFab;
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return mCollapsingToolbar;
    }

    public RadioGroup getRadioGroup() {
        return mRadioGroup;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        //透明状态栏
        setStatusBarTransparent();

        //设置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        assert mToolbar != null;
        //        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化AppBarLayout
        initAppBarLayout();

        //初始化DrawerLayout
        initDrawerLayout();

        //初始化CollapsingToolbarLayout
        initCollapsingToolbarLayout();

        //初始化FloatingActionButton
        initFloatingActionButton();

        //初始化ViewPager
        initViewPager();

        //初始化NavigationView
        initNavigationView();

        //初始化nestedScrollView
        initNestedScrollView();

    }

    private void setStatusBarTransparent() {
    /*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //清除系统提供的默认保护色
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置系统UI的显示方式
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //添加属性可以自定义设置系统工具栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initAppBarLayout() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.abl_main);
        mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //完全展开
                    mIsBarExpanded = true;
                    if (mTask != null)
                        mTask.start();
                    setAppBarColor(Color.TRANSPARENT);
                } else if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //完全收缩
                    mIsBarExpanded = false;
                    if (mTask != null)
                        mTask.stop();
                    setAppBarColor(mColor);
                }
            }
        };

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setAppBarColor(int color) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getWindow().getStatusBarColor() != color)
                getWindow().setStatusBarColor(color);
        }
    }

    private void initCollapsingToolbarLayout() {
        //设置工具栏标题
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.ctl_main);
        assert mCollapsingToolbar != null;
        mCollapsingToolbar.setTitle("Demo");
        mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        //折叠时标题颜色
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        //viewpager
        final ViewPager topViewPager = (ViewPager) findViewById(R.id.vp_main);
        mTextView = (TextView) findViewById(R.id.tv_title_main);
        ll_container = (LinearLayout) findViewById(R.id.ll_pointcontainer_main);

        mList = new ArrayList<>();
        mPoints = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            //初始化viewpager中的图片
            ImageView imageView = new ImageView(this);
            //            imageView.setImageResource(imgs[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageCache.getImageCache().setImage(imgs[i], imageView);
            mList.add(imageView);
            //初始化对应的点
            ImageView iv = new ImageView(this);
            iv.setImageResource(i == 0 ? R.drawable.point_pressed_guide : R.drawable.point_normal_guide);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.rightMargin = 8;
            iv.setLayoutParams(params);
            ll_container.addView(iv);
            mPoints.add(iv);
        }

        //设置viewpager的adapter
        assert topViewPager != null;
        topViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mList.get(position % mList.size()));
                return mList.get(position % mList.size());
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        //viewpager的滑动监听事件
        topViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //先移除所有的点
                ll_container.removeAllViews();
                for (int i = 0; i < mPoints.size(); i++) {
                    mPoints.get(i).setImageResource(i == position % mList.size() ? R.drawable.point_pressed_guide : R.drawable.point_normal_guide);
                    ll_container.addView(mPoints.get(i));
                }
                mTextView.setText("图片" + (position % mList.size() + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //无限轮播
        int firstItem = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mList.size());
        topViewPager.setCurrentItem(firstItem);

        //自动轮播
        if (mTask == null) {
            mTask = new AutoScrollTask(topViewPager);
        }
        mTask.start();
    }

    private float downY = 0;

    private void initNestedScrollView() {
        mNestedScrollView = (MyScrollView) findViewById(R.id.nestedScrollView);
        assert mNestedScrollView != null;
        mNestedScrollView.setOnTouchListener(new MyScrollView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (downY == 0) {
                            downY = event.getY();
                            return false;
                        }
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

                        break;
                    case MotionEvent.ACTION_UP:
                        downY = 0;
                        break;
                    default:
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
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Snackbar.make(mNavigationView, "pressed " + item.getTitle(), Snackbar.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawers();
                //详情页面
                if (item.getItemId() == R.id.nav_discussion) {
                    startActivity(new Intent(MainActivity.this, DetailActivity.class));
                    //主页面
                } else if (item.getItemId() == R.id.nav_home) {
                    mRadioGroup.check(R.id.rb_home);
                }

                return true;
            }
        });

        //menu headerView
        View headerView = mNavigationView.getHeaderView(0);
        final TextView textView = (TextView) headerView.findViewById(R.id.id_link);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转默认浏览器
                Intent intent = new Intent(Intent.ACTION_VIEW);    //为Intent设置Action属性
                intent.setData(Uri.parse(textView.getText().toString())); //为Intent设置DATA属性
                startActivity(intent);
            }
        });
    }

    private int[] id_radioButtons = {R.id.rb_home, R.id.rb_news, R.id.rb_service,
            R.id.rb_subject, R.id.rb_setting};

    private void initViewPager() {
        mViewPager = (LazyViewPager) findViewById(R.id.vp);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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
        mViewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mRadioGroup.check(id_radioButtons[position]);
                mNestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mNestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setOffscreenPageLimit(4);
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
                mViewPager.setCurrentItem(item);
                //                mViewPager.setCurrentItem(item, hasAnim);
                //                setHasTouchNested(item != 2);
            }
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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mFab.getLayoutParams();
            layoutParams.bottomMargin = UiUtils.dip2px(60);
            mFab.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) mFab2.getLayoutParams();
            layoutParams2.bottomMargin = UiUtils.dip2px(60);
            mFab2.setLayoutParams(layoutParams2);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mFab.getLayoutParams();
            layoutParams.bottomMargin = UiUtils.dip2px(76);
            mFab.setLayoutParams(layoutParams);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) mFab2.getLayoutParams();
            layoutParams2.bottomMargin = UiUtils.dip2px(76);
            mFab2.setLayoutParams(layoutParams2);
        }
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

    @SuppressWarnings("deprecation")
    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        //设置监听
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        //同步状态
        actionBarDrawerToggle.syncState();

    }

    public void setRadioGroupVisibility(int visibility) {
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

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);
        if (mTask != null)
            mTask.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(mOnOffsetChangedListener);
        if (mTask != null)
            mTask.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTask = null;
        MyApplication.setIsImmersive(false);
    }

}
