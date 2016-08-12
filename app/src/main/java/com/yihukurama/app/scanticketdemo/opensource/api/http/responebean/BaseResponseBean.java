package com.yihukurama.app.scanticketdemo.opensource.api.http.responebean;

import java.io.Serializable;
import java.util.List;


/**
 * 服务端返回基类
 * 
 * @author Administrator
 * 
 */

public class BaseResponseBean implements Serializable{
	private static final long serialVersionUID = -7849808062675741128L;
	private String outdesc = "";
	private String outresult = "";
	private int pageno = 0;
	private int pagesize = 0;
	private String maxpage = "";

	private List<Object> uidataList;

	public List<Object> getUidataList() {
		return uidataList;
	}

	public void setUidataList(List<Object> uidataList) {
		this.uidataList = uidataList;
	}

	public String getOutdesc() {
		return outdesc;
	}

	public void setOutdesc(String outdesc) {
		this.outdesc = outdesc;
	}

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	

	public String getMaxpage() {
		return maxpage;
	}

	public void setMaxpage(String maxpage) {
		this.maxpage = maxpage;
	}

	public String getOutresult() {
		return outresult;
	}

	public void setOutresult(String outresult) {
		this.outresult = outresult;
	}

}
