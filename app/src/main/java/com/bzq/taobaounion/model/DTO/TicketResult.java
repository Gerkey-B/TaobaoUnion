package com.bzq.taobaounion.model.DTO;

/**
 * @author Gerkey
 * Created on 2021/7/23
 */
public class TicketResult {

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
    private DataDTOX data;

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

    public DataDTOX getData() {
        return data;
    }

    public void setData(DataDTOX data) {
        this.data = data;
    }

    public static class DataDTOX {
        /**
         * tbk_tpwd_create_response
         */
        private TbkTpwdCreateResponseDTO tbk_tpwd_create_response;

        public TbkTpwdCreateResponseDTO getTbk_tpwd_create_response() {
            return tbk_tpwd_create_response;
        }

        public void setTbk_tpwd_create_response(TbkTpwdCreateResponseDTO tbk_tpwd_create_response) {
            this.tbk_tpwd_create_response = tbk_tpwd_create_response;
        }

        public static class TbkTpwdCreateResponseDTO {
            /**
             * data
             */
            private DataDTOX.TbkTpwdCreateResponseDTO.DataDTO data;
            /**
             * request_id
             */
            private String request_id;

            public DataDTO getData() {
                return data;
            }

            public void setData(DataDTO data) {
                this.data = data;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class DataDTO {
                /**
                 * model
                 */
                private String model;

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
                }
            }
        }
    }
}

