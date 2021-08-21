package com.bzq.taobaounion.view;

import com.bzq.taobaounion.base.IBaseCallback;
import com.bzq.taobaounion.model.DTO.TicketResult;

/**
 * @author Gerkey
 * Created on 2021/7/23
 */
public interface ITicketPageCallback extends IBaseCallback {
    /**
     * 淘口令加载完成
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);
}

