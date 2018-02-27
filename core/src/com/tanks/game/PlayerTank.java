package com.tanks.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class PlayerTank extends Tank {
    public PlayerTank(TanksGame game, Vector2 position) {
        super(game, position);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (game.isMyTurn(this) && !makeTurn) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                rotateTurret(1, dt);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                rotateTurret(-1, dt);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                move(-1, dt);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                move(1, dt);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (power < MINIMAL_POWER) {
                    power = MINIMAL_POWER + 1.0f;
                } else {
                    power += 400.0f * dt;
                    if (power > maxPower) {
                        power = maxPower;
                    }
                }
            } else {
                if (power > MINIMAL_POWER) {
                    float ammoPosX = weaponPosition.x + 12 + 28 * (float) Math.cos(Math.toRadians(turretAngle));
                    float ammoPosY = weaponPosition.y + 16 + 28 * (float) Math.sin(Math.toRadians(turretAngle));

                    float ammoVelX = power * (float) Math.cos(Math.toRadians(turretAngle));
                    float ammoVelY = power * (float) Math.sin(Math.toRadians(turretAngle));

                    game.getBulletEmitter().setup(ammoPosX, ammoPosY, ammoVelX, ammoVelY);

                    power = 0.0f;

                    makeTurn = true;
                }
            }
        }
    }
}
