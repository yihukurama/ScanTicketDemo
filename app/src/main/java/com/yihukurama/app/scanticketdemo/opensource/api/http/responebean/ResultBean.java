package com.yihukurama.app.scanticketdemo.opensource.api.http.responebean;

/**
 * Created by dengshuai on 16/7/16.
 */
public class ResultBean extends BaseResponseBean {
    /**
     * 默认的构造方法必须不能省，不然不能解析
     */
    public ResultBean() {
        super();
    }
    private String result = "";
    private String resultdesc = "";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultdesc() {
        return resultdesc;
    }

    public void setResultdesc(String resultdesc) {
        this.resultdesc = resultdesc;
    }
}
