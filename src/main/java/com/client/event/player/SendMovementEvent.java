package com.client.event.player;

import com.client.event.Event;
import com.client.event.EventStage;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMovementEvent extends Event {
	private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;
	private final EventStage stage;

    public SendMovementEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, EventStage stage) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.stage = stage;
    }

    public SendMovementEvent(EventStage stage) {
        this.stage = stage;
    }
}
