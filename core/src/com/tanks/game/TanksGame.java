package com.tanks.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TanksGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture textureBackground;
    private Map map;
    private Tank player;
    private BulletEmitter bulletEmitter;

    // 1. Сила выстрела
    // 2. Попадание по танку пули
    // 3. Отображение здоровья танка

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public Map getMap() {
        return map;
    }

    public static final float GLOBAL_GRAVITY = 300.0f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        textureBackground = new Texture("background.png");
        map = new Map();
        player = new Tank(this, new Vector2(400, 380));
        bulletEmitter = new BulletEmitter();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(textureBackground, 0, 0);
        map.render(batch);
        player.render(batch);
        bulletEmitter.render(batch);
        batch.end();
    }

    public void update(float dt) {
        map.update(dt);
        player.update(dt);
        bulletEmitter.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        Bullet[] b = bulletEmitter.getBullets();
        for (int i = 0; i < b.length; i++) {
            if (b[i].isActive()) {
                if (map.isGround(b[i].getPosition().x, b[i].getPosition().y)) {
                    b[i].deactivate();
                    map.clearGround(b[i].getPosition().x, b[i].getPosition().y, 8);
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
