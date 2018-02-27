package com.tanks.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class Tank {
    protected Texture textureBase;
    protected Texture textureTurret;
    protected Texture textureTrack;
    protected Texture textureProgressBar;
    protected Vector2 position;
    protected Vector2 weaponPosition;
    protected TanksGame game;
    protected float turretAngle;
    protected int hp;
    protected int maxHp;
    protected Circle hitArea;
    protected float power;
    protected float maxPower;
    protected float speed;
    protected boolean makeTurn;
    protected float fuel;
    protected float time;

    public boolean isMakeTurn() {
        return makeTurn;
    }

    public void setMakeTurn(boolean makeTurn) {
        this.makeTurn = makeTurn;
    }

    public final static float TURRET_ROTATION_ANGULAR_SPEED = 90.0f; // скорость поворота турели
    public final static float MINIMAL_POWER = 100.0f; // минимальная сила выстрела
    public final static int MAX_MOVEMENT_DY = 10; // максимальная разница в высоте земли при движении

    public Circle getHitArea() {
        return hitArea;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeTurn() {
        makeTurn = false;
        fuel = 0.8f;
    }

    public Tank(TanksGame game, Vector2 position) {
        this.game = game;
        this.position = position;
        this.weaponPosition = new Vector2(position).add(0, 0);
        this.textureBase = new Texture("tankBody.png");
        this.textureTurret = new Texture("tankTurret.png");
        this.textureTrack = new Texture("tankTrack.png");
        this.textureProgressBar = new Texture("hbar.png");
        this.turretAngle = 0.0f;
        this.maxHp = 100;
        this.hp = this.maxHp;
        this.hitArea = new Circle(new Vector2(0, 0), textureBase.getWidth() * 0.4f);
        this.power = 0.0f;
        this.maxPower = 1200.0f;
        this.speed = 100.0f;
    }

    public void render(SpriteBatch batch) {
        if (game.isMyTurn(this)) {
            float tmp = 0.9f + 0.1f * (float) Math.sin(time * 4);
            batch.setColor(tmp, tmp, tmp, 1);
        } else {
            batch.setColor(1, 1, 1, 1);
        }
        batch.draw(textureTurret, weaponPosition.x, weaponPosition.y, textureTurret.getWidth() / 10, textureTurret.getHeight() / 2, textureTurret.getWidth(), textureTurret.getHeight(), 1, 1, turretAngle, 0, 0, textureTurret.getWidth(), textureTurret.getHeight(), false, false);
        batch.draw(textureTrack, position.x + 4, position.y);
        batch.draw(textureBase, position.x, position.y + textureTrack.getHeight() / 3);
        batch.setColor(1, 1, 1, 1);
    }

    public void renderHUD(SpriteBatch batch) {
        batch.setColor(0.5f, 0, 0, 0.8f);
        batch.draw(textureProgressBar, position.x + 2, position.y + 70, 0, 0, 80, 12);
        batch.setColor(0, 1, 0, 0.8f);
        batch.draw(textureProgressBar, position.x + 2, position.y + 70, 0, 0, (int) (80 * (float) hp / maxHp), 12);

        if (power > 100.0f) {
            batch.setColor(1, 0, 0, 0.8f);
            batch.draw(textureProgressBar, position.x + 2, position.y + 82, 0, 0, (int) (80 * power / maxPower), 12);
        }
        batch.setColor(1, 1, 1, 1);
    }

    public void rotateTurret(int n, float dt) {
        turretAngle += n * TURRET_ROTATION_ANGULAR_SPEED * dt;
    }

    public void move(int n, float dt) {
        if (fuel > 0.0f) {
            float dstX = position.x + speed * dt * n;
            for (int i = 1; i < MAX_MOVEMENT_DY; i++) {
                if (!checkOnGround(dstX, position.y + i)) {
                    position.x = dstX;
                    position.y += i - 1;
                    break;
                }
            }
            fuel -= dt;
        }
    }

    public void update(float dt) {
        if (!checkOnGround()) {
            position.y -= 100.0f * dt;
        }
        this.weaponPosition.set(position).add(34, 45);
        this.hitArea.x = position.x + textureBase.getWidth() / 2;
        this.hitArea.y = position.y + textureBase.getHeight() / 2;
        this.time += dt;
    }

    public boolean takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            return true;
        }
        return false;
    }
 // C# + XNA
    public boolean checkOnGround(float x, float y) {
        for (int i = 0; i < textureBase.getWidth(); i += 2) {
            if (game.getMap().isGround(x + i, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkOnGround() {
        return checkOnGround(position.x, position.y);
    }
}
