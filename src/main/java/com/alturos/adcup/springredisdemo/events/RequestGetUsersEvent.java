package com.alturos.adcup.springredisdemo.events;

public class RequestGetUsersEvent {
	
	private int start;
	
	private int count;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public RequestGetUsersEvent(int start, int count) {
		super();
		this.start = start;
		this.count = count;
	}

}
