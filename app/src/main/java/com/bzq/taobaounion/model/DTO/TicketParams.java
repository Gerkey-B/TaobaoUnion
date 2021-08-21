package com.bzq.taobaounion.model.DTO;

/**
 * @author Gerkey
 * Created on 2021/7/23
 */
public class TicketParams {
    private String url;
    private String title;

    public TicketParams(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

