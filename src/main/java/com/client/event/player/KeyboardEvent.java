package com.client.event.player;

import com.client.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyboardEvent extends Event {
	private long window;
	private int key;
	private int scancode;
	private int action;
	private int modifiers;
}
