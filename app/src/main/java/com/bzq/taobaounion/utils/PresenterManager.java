package com.bzq.taobaounion.utils;

import com.bzq.taobaounion.presenter.ICategoryPagerPresenter;
import com.bzq.taobaounion.presenter.IHomePresenter;
import com.bzq.taobaounion.presenter.IOnSellPagePresenter;
import com.bzq.taobaounion.presenter.ISearchPresenter;
import com.bzq.taobaounion.presenter.ISelectedPagePresenter;
import com.bzq.taobaounion.presenter.ITicketPresenter;
import com.bzq.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.bzq.taobaounion.presenter.impl.HomePresenterImpl;
import com.bzq.taobaounion.presenter.impl.OnSellPagePresenterImpl;
import com.bzq.taobaounion.presenter.impl.SearchPresenterImpl;
import com.bzq.taobaounion.presenter.impl.SelectedPagePresenterImpl;
import com.bzq.taobaounion.presenter.impl.TicketPresenterImpl;

/**
 * @author Gerkey
 * Created on 2021/7/23
 */
public class PresenterManager {

    private static final PresenterManager OUR_INSTANCE = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagerPresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final ISelectedPagePresenter mSelectedPagePresenter;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;


    public static PresenterManager getInstance() {
        return OUR_INSTANCE;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public ISelectedPagePresenter getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }

    private PresenterManager() {
        mCategoryPagerPresenter = new CategoryPagerPresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
        mSearchPresenter = new SearchPresenterImpl();
    }
}
