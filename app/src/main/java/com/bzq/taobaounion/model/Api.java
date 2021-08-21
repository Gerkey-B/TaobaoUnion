package com.bzq.taobaounion.model;

import com.bzq.taobaounion.model.DTO.Categories;
import com.bzq.taobaounion.model.DTO.HomePagerContent;
import com.bzq.taobaounion.model.DTO.OnSellContent;
import com.bzq.taobaounion.model.DTO.SearchRecommend;
import com.bzq.taobaounion.model.DTO.SearchResult;
import com.bzq.taobaounion.model.DTO.SelectedContent;
import com.bzq.taobaounion.model.DTO.SelectedPageCategory;
import com.bzq.taobaounion.model.DTO.TicketParams;
import com.bzq.taobaounion.model.DTO.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author Gerkey
 * Created on 2021/7/2
 */
public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET
    Call<SelectedContent> getSelectedPageContent(@Url String url);

    @GET
    Call<OnSellContent> getOnSellPageContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> getSearchResult(@Query("page") int page, @Query("keyword") String keyword);
}
