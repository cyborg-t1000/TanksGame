package com.tanks.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class TanksGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture textureBackground;
    private Map map;
    private BulletEmitter bulletEmitter;
    private ShapeRenderer shapeRenderer;
    private List<Tank> players;
    private int currentPlayerIndex;

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public Map getMap() {
        return map;
    }

    public static final float GLOBAL_GRAVITY = 300.0f;

    // Разобраться с кодом
    // * Реализовать мозги боту

    @Override
    public void create() {
        batch = new SpriteBatch();
        textureBackground = new Texture("background.png");
        map = new Map();

        players = new ArrayList<Tank>();
        players.add(new PlayerTank(this, new Vector2(400, 380)));
        players.add(new AiTank(this, new Vector2(800, 380)));
        for (int i = 0; i < 10; i++) {
            players.add(new AiTank(this, new Vector2(MathUtils.random(0, 1000), 380)));

        }

        currentPlayerIndex = 0;
        players.get(currentPlayerIndex).takeTurn();

        bulletEmitter = new BulletEmitter();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    public boolean isMyTurn(Tank tank) {
        return tank == players.get(currentPlayerIndex);
    }

    public void checkNextTurn() {
        if (!players.get(currentPlayerIndex).makeTurn) {
            return;
        }
        if (!bulletEmitter.empty()) {
            return;
        }
        do {
            currentPlayerIndex++;
            if (currentPlayerIndex >= players.size()) {
                currentPlayerIndex = 0;
            }
        } while (!players.get(currentPlayerIndex).isAlive());
        players.get(currentPlayerIndex).takeTurn();
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
        for (int i = 0; i < players.size(); i++) {
            players.get(i).render(batch);
        }
        bulletEmitter.render(batch);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).renderHUD(batch);
        }
        batch.end();
        shapeRenderer.begin();
        for (int i = 0; i < players.size(); i++) {
            shapeRenderer.circle(players.get(i).getHitArea().x, players.get(i).getHitArea().y, players.get(i).getHitArea().radius);
        }
        shapeRenderer.end();
    }


    public void update(float dt) {
        map.update(dt);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).update(dt);
        }
        bulletEmitter.update(dt);
        checkCollisions();
        checkNextTurn();
    }

    public void checkCollisions() {
        Bullet[] b = bulletEmitter.getBullets();
        for (int i = 0; i < b.length; i++) {
            if (b[i].isActive()) {
                for (int j = 0; j < players.size(); j++) {
                    if (b[i].isArmed() && players.get(j).getHitArea().contains(b[i].getPosition())) {
                        b[i].deactivate();
                        players.get(j).takeDamage(5);
                        map.clearGround(b[i].getPosition().x, b[i].getPosition().y, 8);
                        continue;
                    }
                }
                if (map.isGround(b[i].getPosition().x, b[i].getPosition().y)) {
                    b[i].deactivate();
                    map.clearGround(b[i].getPosition().x, b[i].getPosition().y, 8);
                    continue;
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
