package weshare.groupfour.derek.myGoodsOrders;

import java.io.Serializable;

import weshare.groupfour.derek.goods.GoodsVO;
import weshare.groupfour.derek.myGoodsOrders.GoodsOrderVO;


public class GoodsDetailsVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private GoodsOrderVO goodsOrderVO;
	private GoodsVO goodsVO;
	private Integer goodAmount;
	private Float goodScore;
	private String goodRate;



	public GoodsOrderVO getGoodsOrderVO() {
		return goodsOrderVO;
	}
	public void setGoodsOrderVO(GoodsOrderVO goodsOrderVO) {
		this.goodsOrderVO = goodsOrderVO;
	}

	public GoodsVO getGoodsVO() {
		return goodsVO;
	}
	public void setGoodsVO(GoodsVO goodsVO) {
		this.goodsVO = goodsVO;
	}

	public Integer getGoodAmount() {
		return goodAmount;
	}
	public void setGoodAmount(Integer goodAmount) {
		this.goodAmount = goodAmount;
	}
	public Float getGoodScore() {
		return goodScore;
	}
	public void setGoodScore(Float goodScore) {
		this.goodScore = goodScore;
	}
	public String getGoodRate() {
		return goodRate;
	}
	public void setGoodRate(String goodRate) {
		this.goodRate = goodRate;
	}
}
