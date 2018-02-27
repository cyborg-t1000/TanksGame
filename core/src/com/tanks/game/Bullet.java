package com.tanks.game;

import com.badlogic.gdx.math.Vector2;

import java.io.BufferedOutputStream;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float angle;
    private float time;

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public float getAngle() {
        return angle;
    }

    public boolean isArmed() {
        return time > 0.2f;
    }

    public Bullet() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        active = false;
        angle = 0.0f;
        time = 0.0f;
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        time = 0.0f;
    }

    public void update(float dt) {
        time += dt;
        velocity.y -= TanksGame.GLOBAL_GRAVITY * dt;
        angle = velocity.angle();
        position.mulAdd(velocity, dt);
    }


}
