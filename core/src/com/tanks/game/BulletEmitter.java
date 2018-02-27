package com.tanks.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BulletEmitter {
    private Texture bulletTexture;
    private Bullet[] bullets;

    public Bullet[] getBullets() {
        return bullets;
    }

    public BulletEmitter() {
        bulletTexture = new Texture("ammo.png");
        bullets = new Bullet[50];
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
        }
    }

    public boolean empty() {
        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i].isActive()) {
                return false;
            }
        }
        return true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i].isActive()) {
                batch.draw(bulletTexture, bullets[i].getPosition().x - 8, bullets[i].getPosition().y - 8);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i].isActive()) {
                bullets[i].update(dt);
            }
        }
    }

    public void setup(float x, float y, float vx, float vy) {
        for (int i = 0; i < bullets.length; i++) {
            if (!bullets[i].isActive()) {
                bullets[i].activate(x, y, vx, vy);
                break;
            }
        }
    }
}
