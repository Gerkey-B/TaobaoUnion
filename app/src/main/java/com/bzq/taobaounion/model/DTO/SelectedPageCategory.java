package com.bzq.taobaounion.model.DTO;

import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/7/29
 */
public class SelectedPageCategory {

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

    @Override
    public String toString() {
        return "SelectedPageCategory{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataDTO {
        @Override
        public String toString() {
            return "DataDTO{" +
                    "type=" + type +
                    ", favorites_id=" + favorites_id +
                    ", favorites_title='" + favorites_title + '\'' +
                    '}';
        }

        /**
         * type
         */
        private Integer type;
        /**
         * favorites_id
         */
        private Integer favorites_id;
        /**
         * favorites_title
         */
        private String favorites_title;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getFavorites_id() {
            return favorites_id;
        }

        public void setFavorites_id(Integer favorites_id) {
            this.favorites_id = favorites_id;
        }

        public String getFavorites_title() {
            return favorites_title;
        }

        public void setFavorites_title(String favorites_title) {
            this.favorites_title = favorites_title;
        }
    }
}

