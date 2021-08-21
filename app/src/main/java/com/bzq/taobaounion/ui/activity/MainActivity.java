package com.bzq.taobaounion.ui.activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.base.BaseActivity;
import com.bzq.taobaounion.base.BaseFragment;
import com.bzq.taobaounion.ui.fragment.HomeFragment;
import com.bzq.taobaounion.ui.fragment.OnSellFragment;
import com.bzq.taobaounion.ui.fragment.SearchFragment;
import com.bzq.taobaounion.ui.fragment.SelectedFragment;
import com.bzq.taobaounion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private SearchFragment mSearchFragment;
    private OnSellFragment mOnSellFragment;
    private SelectedFragment mSelectedFragment;
    private HomeFragment mHomeFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initEvent() {
        initListener();
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mSelectedFragment = new SelectedFragment();
        mOnSellFragment = new OnSellFragment();
        mSearchFragment = new SearchFragment();
        mFragmentManager = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                // 通过log，找到了对应 Item 的 id ，而我们后续的操作也将通过这个 ID 来进行。
//                Log.d(TAG,"title ------>  " + item.getItemId());

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    LogUtils.d(this, "切换到首页");
                    switchFragment(mHomeFragment);
                } else if (itemId == R.id.selected) {
                    LogUtils.d(this, "切换到精选");
                    switchFragment(mSelectedFragment);
                } else if (itemId == R.id.red_packet) {
                    LogUtils.d(this, "切换到特惠");
                    switchFragment(mOnSellFragment);
                } else if (itemId == R.id.search) {
                    LogUtils.d(this, "切换到搜索");
                    switchFragment(mSearchFragment);
                }
                return true;
            }
        });
    }

    /**
     * 用来保存上一次的fragmnet
     */
    private BaseFragment lastOneFragment;

    private void switchFragment(BaseFragment target) {
        // 因为之前是使用 replace 来切换 fragment，
        // 所以，在我们切换 fragment 后，并不能保存前一次的 fragment，
        // 所以使用 add，hide，和 show 来重构，
        // 当然，这样做的话，内存会占得更多，这样做是可能用户切换的次数比较频繁，为了切换时的流畅，做出这样的选择。
        // 所以，如果是有需求，用户只关注某一页，可能 replace 更好。

        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (!target.isAdded()) {
            mFragmentTransaction.add(R.id.main_page_container, target);
        } else {
            mFragmentTransaction.show(target);
        }
        if (lastOneFragment != null && lastOneFragment != target) {
            mFragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment = target;

        mFragmentTransaction.commit();
    }

    /**
     * 跳转到都搜索界面
     */
    @Override
    public void switch2Search() {
        switchFragment(mSearchFragment);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}