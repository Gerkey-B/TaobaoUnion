package com.bzq.taobaounion.model.DTO;

import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/8/8
 */
public class SearchRecommend {

    /**
     * success
     */
    private Boolean success;
    /**
     * code
     */
    private Integer code;
    /**
     * message
     */
    private String message;
    /**
     * data
     */
    private List<DataDTO> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        /**
         * id
         */
        private String id;
        /**
         * keyword
         */
        private String keyword;
        /**
         * createTime
         */
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}

