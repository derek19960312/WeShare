package weshare.groupfour.derek.myCourseOrders;

import java.util.Set;

public class NearbyState {
	private String type;
	// the user changing the state
	private String user;
	// total users
	private Set<MyLocationVO> myLocationVOS;

	public NearbyState(String type, String user, Set<MyLocationVO> myLocationVOS) {
		super();
		this.type = type;
		this.user = user;
		this.myLocationVOS = myLocationVOS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Set<MyLocationVO> getUsers() {
		return myLocationVOS;
	}

	public void setUsers(Set<MyLocationVO> myLocationVOS) {
		this.myLocationVOS = myLocationVOS;
	}

}
