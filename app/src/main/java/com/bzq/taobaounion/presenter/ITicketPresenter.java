package com.bzq.taobaounion.presenter;

import com.bzq.taobaounion.base.IBasePresenter;
import com.bzq.taobaounion.view.ITicketPageCallback;

/**
 * @author Gerkey
 * Created on 2021/7/23
 */
public interface ITicketPresenter extends IBasePresenter<ITicketPageCallback> {
    /**
     * 获取优惠券，生成淘口令
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title,String url,String cover);
}
