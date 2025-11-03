package com.client.event;

import lombok.Getter;

@Getter
public class Event {
	private boolean cancelled;
	
	public void cancel() {
		cancelled = true;
	}
}
