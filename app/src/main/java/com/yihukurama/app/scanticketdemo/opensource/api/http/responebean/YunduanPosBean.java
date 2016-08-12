/**
 * @package 	com.iecheck.app.common.bean
 * @time 		2014年7月18日
 * @author 		DengShuai
 **/
package com.yihukurama.app.scanticketdemo.opensource.api.http.responebean;

import java.util.ArrayList;


/**
 * @author yawei
 *
 */
public class YunduanPosBean extends BaseResponseBean{
	/**
	 * 默认的构造方法必须不能省，不然不能解析
	 */
	public YunduanPosBean() {
		super();
	}


	private ArrayList<Outdata> outdata;

	public ArrayList<Outdata> getOutdata() {
		return outdata;
	}

	public void setOutdata(ArrayList<Outdata> outdata) {
		this.outdata = outdata;
	}

	public class Outdata {
		private String result;//接口成功或失败
		private String err_code;//接口失败原因描述"
		private String brand_name;//品牌名称",
		private String active_name;//活动名称",
		private String product_name;//产品名称",
		private String brand_logo;//品牌LOGO",
		private String card_title;//奖项名称",
		private String card_des;//奖项描述",
		private String card_expires;//截止时间",
		private String card_rule;//奖券规则",
		private String flag;//"0/1/2/3/4/5/6/7/8"


		public String getActive_name() {
			return active_name;
		}

		public void setActive_name(String active_name) {
			this.active_name = active_name;
		}

		public String getBrand_logo() {
			return brand_logo;
		}

		public void setBrand_logo(String brand_logo) {
			this.brand_logo = brand_logo;
		}

		public String getBrand_name() {
			return brand_name;
		}

		public void setBrand_name(String brand_name) {
			this.brand_name = brand_name;
		}

		public String getCard_des() {
			return card_des;
		}

		public void setCard_des(String card_des) {
			this.card_des = card_des;
		}

		public String getCard_expires() {
			return card_expires;
		}

		public void setCard_expires(String card_expires) {
			this.card_expires = card_expires;
		}

		public String getCard_rule() {
			return card_rule;
		}

		public void setCard_rule(String card_rule) {
			this.card_rule = card_rule;
		}

		public String getCard_title() {
			return card_title;
		}

		public void setCard_title(String card_title) {
			this.card_title = card_title;
		}

		public String getErr_code() {
			return err_code;
		}

		public void setErr_code(String err_code) {
			this.err_code = err_code;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
	}
}
