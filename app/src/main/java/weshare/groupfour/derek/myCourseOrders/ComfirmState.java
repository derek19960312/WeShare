package weshare.groupfour.derek.myCourseOrders;

public class ComfirmState {
	private String type;
	private String receiver;
	private String check;

	public ComfirmState(String type, String receiver, String check) {
		this.type = type;
		this.receiver = receiver;
		this.check = check;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
