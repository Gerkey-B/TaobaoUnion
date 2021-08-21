package com.bzq.taobaounion.model.DTO;

import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/8/3
 */
public class OnSellContent {

    /**
     * success
     */
    private Boolean success;
    /**
     * code
     */
    private long code;
    /**
     * message
     */
    private String message;
    /**
     * data
     */
    private DataDTO data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        /**
         * tbk_dg_optimus_material_response
         */
        private TbkDgOptimusMaterialResponseDTO tbk_dg_optimus_material_response;

        public TbkDgOptimusMaterialResponseDTO getTbk_dg_optimus_material_response() {
            return tbk_dg_optimus_material_response;
        }

        public void setTbk_dg_optimus_material_response(TbkDgOptimusMaterialResponseDTO tbk_dg_optimus_material_response) {
            this.tbk_dg_optimus_material_response = tbk_dg_optimus_material_response;
        }

        public static class TbkDgOptimusMaterialResponseDTO {
            /**
             * is_default
             */
            private String is_default;
            /**
             * result_list
             */
            private ResultListDTO result_list;
            /**
             * request_id
             */
            private String request_id;

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }

            public ResultListDTO getResult_list() {
                return result_list;
            }

            public void setResult_list(ResultListDTO result_list) {
                this.result_list = result_list;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class ResultListDTO {
                /**
                 * map_data
                 */
                private List<MapDataDTO> map_data;

                public List<MapDataDTO> getMap_data() {
                    return map_data;
                }

                public void setMap_data(List<MapDataDTO> map_data) {
                    this.map_data = map_data;
                }

                public static class MapDataDTO {
                    /**
                     * category_id
                     */
                    private long category_id;
                    /**
                     * category_name
                     */
                    private Object category_name;
                    /**
                     * click_url
                     */
                    private String click_url;
                    /**
                     * commission_rate
                     */
                    private String commission_rate;
                    /**
                     * coupon_amount
                     */
                    private long coupon_amount;
                    /**
                     * coupon_click_url
                     */
                    private String coupon_click_url;
                    /**
                     * coupon_end_time
                     */
                    private String coupon_end_time;
                    /**
                     * coupon_info
                     */
                    private Object coupon_info;
                    /**
                     * coupon_remain_count
                     */
                    private long coupon_remain_count;
                    /**
                     * coupon_share_url
                     */
                    private String coupon_share_url;
                    /**
                     * coupon_start_fee
                     */
                    private String coupon_start_fee;
                    /**
                     * coupon_start_time
                     */
                    private String coupon_start_time;
                    /**
                     * coupon_total_count
                     */
                    private long coupon_total_count;
                    /**
                     * item_description
                     */
                    private String item_description;
                    /**
                     * item_id
                     */
                    private Long item_id;
                    /**
                     * level_one_category_id
                     */
                    private long level_one_category_id;
                    /**
                     * level_one_category_name
                     */
                    private String level_one_category_name;
                    /**
                     * nick
                     */
                    private String nick;
                    /**
                     * pict_url
                     */
                    private String pict_url;
                    /**
                     * seller_id
                     */
                    private long seller_id;
                    /**
                     * shop_title
                     */
                    private Object shop_title;
                    /**
                     * small_images
                     */
                    private SmallImagesDTO small_images;
                    /**
                     * title
                     */
                    private String title;
                    /**
                     * user_type
                     */
                    private long user_type;
                    /**
                     * volume
                     */
                    private long volume;
                    /**
                     * zk_final_price
                     */
                    private String zk_final_price;

                    public long getCategory_id() {
                        return category_id;
                    }

                    public void setCategory_id(long category_id) {
                        this.category_id = category_id;
                    }

                    public Object getCategory_name() {
                        return category_name;
                    }

                    public void setCategory_name(Object category_name) {
                        this.category_name = category_name;
                    }

                    public String getClick_url() {
                        return click_url;
                    }

                    public void setClick_url(String click_url) {
                        this.click_url = click_url;
                    }

                    public String getCommission_rate() {
                        return commission_rate;
                    }

                    public void setCommission_rate(String commission_rate) {
                        this.commission_rate = commission_rate;
                    }

                    public long getCoupon_amount() {
                        return coupon_amount;
                    }

                    public void setCoupon_amount(long coupon_amount) {
                        this.coupon_amount = coupon_amount;
                    }

                    public String getCoupon_click_url() {
                        return coupon_click_url;
                    }

                    public void setCoupon_click_url(String coupon_click_url) {
                        this.coupon_click_url = coupon_click_url;
                    }

                    public String getCoupon_end_time() {
                        return coupon_end_time;
                    }

                    public void setCoupon_end_time(String coupon_end_time) {
                        this.coupon_end_time = coupon_end_time;
                    }

                    public Object getCoupon_info() {
                        return coupon_info;
                    }

                    public void setCoupon_info(Object coupon_info) {
                        this.coupon_info = coupon_info;
                    }

                    public long getCoupon_remain_count() {
                        return coupon_remain_count;
                    }

                    public void setCoupon_remain_count(long coupon_remain_count) {
                        this.coupon_remain_count = coupon_remain_count;
                    }

                    public String getCoupon_share_url() {
                        return coupon_share_url;
                    }

                    public void setCoupon_share_url(String coupon_share_url) {
                        this.coupon_share_url = coupon_share_url;
                    }

                    public String getCoupon_start_fee() {
                        return coupon_start_fee;
                    }

                    public void setCoupon_start_fee(String coupon_start_fee) {
                        this.coupon_start_fee = coupon_start_fee;
                    }

                    public String getCoupon_start_time() {
                        return coupon_start_time;
                    }

                    public void setCoupon_start_time(String coupon_start_time) {
                        this.coupon_start_time = coupon_start_time;
                    }

                    public long getCoupon_total_count() {
                        return coupon_total_count;
                    }

                    public void setCoupon_total_count(long coupon_total_count) {
                        this.coupon_total_count = coupon_total_count;
                    }

                    public String getItem_description() {
                        return item_description;
                    }

                    public void setItem_description(String item_description) {
                        this.item_description = item_description;
                    }

                    public Long getItem_id() {
                        return item_id;
                    }

                    public void setItem_id(Long item_id) {
                        this.item_id = item_id;
                    }

                    public long getLevel_one_category_id() {
                        return level_one_category_id;
                    }

                    public void setLevel_one_category_id(long level_one_category_id) {
                        this.level_one_category_id = level_one_category_id;
                    }

                    public String getLevel_one_category_name() {
                        return level_one_category_name;
                    }

                    public void setLevel_one_category_name(String level_one_category_name) {
                        this.level_one_category_name = level_one_category_name;
                    }

                    public String getNick() {
                        return nick;
                    }

                    public void setNick(String nick) {
                        this.nick = nick;
                    }

                    public String getPict_url() {
                        return pict_url;
                    }

                    public void setPict_url(String pict_url) {
                        this.pict_url = pict_url;
                    }

                    public long getSeller_id() {
                        return seller_id;
                    }

                    public void setSeller_id(long seller_id) {
                        this.seller_id = seller_id;
                    }

                    public Object getShop_title() {
                        return shop_title;
                    }

                    public void setShop_title(Object shop_title) {
                        this.shop_title = shop_title;
                    }

                    public SmallImagesDTO getSmall_images() {
                        return small_images;
                    }

                    public void setSmall_images(SmallImagesDTO small_images) {
                        this.small_images = small_images;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public long getUser_type() {
                        return user_type;
                    }

                    public void setUser_type(long user_type) {
                        this.user_type = user_type;
                    }

                    public long getVolume() {
                        return volume;
                    }

                    public void setVolume(long volume) {
                        this.volume = volume;
                    }

                    public String getZk_final_price() {
                        return zk_final_price;
                    }

                    public void setZk_final_price(String zk_final_price) {
                        this.zk_final_price = zk_final_price;
                    }

                    public static class SmallImagesDTO {
                        /**
                         * string
                         */
                        private List<String> string;

                        public List<String> getString() {
                            return string;
                        }

                        public void setString(List<String> string) {
                            this.string = string;
                        }
                    }
                }
            }
        }
    }
}

