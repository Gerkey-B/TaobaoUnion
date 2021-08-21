package com.bzq.taobaounion.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.base.BaseFragment;
import com.bzq.taobaounion.databinding.FragmentHomeBinding;
import com.bzq.taobaounion.model.DTO.Categories;
import com.bzq.taobaounion.presenter.IHomePresenter;
import com.bzq.taobaounion.ui.activity.IMainActivity;
import com.bzq.taobaounion.ui.activity.ScanQrCodeActivity;
import com.bzq.taobaounion.ui.adapter.HomePagerAdapter;
import com.bzq.taobaounion.utils.LogUtils;
import com.bzq.taobaounion.utils.PresenterManager;
import com.bzq.taobaounion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;
import com.vondear.rxfeature.activity.ActivityScanerCode;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

/**
 * @author Gerkey
 * Created on 2021/6/27
 */
public class HomeFragment extends BaseFragment implements IHomeCallback {

    private IHomePresenter mHomePresenter;
    private FragmentHomeBinding mBinding;

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager mHomePager;

    @BindView(R.id.home_search_input_box)
    public EditText mSearchInputBox;

    @BindView(R.id.scan_icon)
    public ImageView mScanBtn;

    private HomePagerAdapter mHomePagerAdapter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBinding != null) {
            mBinding = null;
        }
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(mHomePager);
        // 给 ViewPager 设置适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
//        使用 ViewBinding 解决不了，这里的 tabLayout 没有显示出来。
//        mBinding.homeIndicator.setupWithViewPager(mBinding.homePager);
//        // 给 ViewPager 设置适配器
//        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
//        mBinding.homePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected View loadRootView(@NotNull LayoutInflater inflater, @org.jetbrains.annotations.Nullable ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment, container, false);
    }

    @Override
    protected void initPresenter() {
        // 在这里创建 Presenter,创建完Presenter后,我们需要注册回调函数，
        // 因为我们的目的就是通过这个回调函数来通知Presenter层数据已经获取成功、Categories已经初始化
        LogUtils.d(this, "initPresenter....");
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
        mSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity instanceof IMainActivity) {
                    ((IMainActivity) activity).switch2Search();
                }
            }
        });
    }

    @Override
    protected void initListener() {
        mScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ScanQrCodeActivity.class));
            }
        });
    }

    @Override
    protected void loadData() {
        // 加载数据,即调用getCategory(),一般需要一个Presenter来调用,因此在此之前，我们还需要将Presenter准备好。
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState(State.SUCCESS);
        // 加载的数据就会从这里回来
        if (mHomePagerAdapter != null) {
            mHomePagerAdapter.setCategories(categories);
        }
    }

    @Override
    protected void release() {
        // 我们注册了Callback 就要将其销毁
        super.release();
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        // 对于 HomeFragment 来说，重新加载数据即可
        mHomePresenter.getCategories();
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }
}

