package weshare.groupfour.derek.goods;

import java.io.Serializable;
import java.util.Objects;

import weshare.groupfour.derek.member.TeacherVO;

public class GoodsVO implements Serializable{
	private String goodId;
	private String teacherId;
	private String goodName;
	private Integer goodPrice;
	private String goodInfo;
	private byte[] goodImg;
	private Integer goodStatus;
	
	public GoodsVO() {
		super();
	}
	
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public Integer getGoodPrice() {
		return goodPrice;
	}
	public void setGoodPrice(Integer goodPrice) {
		this.goodPrice = goodPrice;
	}
	public String getGoodInfo() {
		return goodInfo;
	}
	public void setGoodInfo(String goodInfo) {
		this.goodInfo = goodInfo;
	}
	public byte[] getGoodImg() {
		return goodImg;
	}
	public void setGoodImg(byte[] goodImg) {
		this.goodImg = goodImg;
	}
	public Integer getGoodStatus() {
		return goodStatus;
	}
	public void setGoodStatus(Integer goodStatus) {
		this.goodStatus = goodStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GoodsVO goodsVO = (GoodsVO) o;
		return Objects.equals(goodId, goodsVO.goodId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(goodId);
	}
}
