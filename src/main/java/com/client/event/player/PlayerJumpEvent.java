package com.client.event.player;

import com.client.event.Event;
import com.client.event.EventStage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerJumpEvent extends Event {
	private final EventStage stage;
}
