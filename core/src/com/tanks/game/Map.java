package com.tanks.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
    private Texture textureGround;
    private byte[][] data;
    private float[][] color;
    private float time;

    private static final int CELL_SIZE = 2;
    private static final int WIDTH = 1280 / CELL_SIZE;
    private static final int HEIGHT = 720 / CELL_SIZE;

    public Map() {
        this.textureGround = new Texture("grass.png");
        this.data = new byte[WIDTH][HEIGHT];
        this.color = new float[WIDTH][HEIGHT];
        this.generate();
    }

    public void generate() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT / 2; j++) {
                data[i][j] = 1;
                color[i][j] = ((float)j / (HEIGHT / 2));
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (data[i][j] == 1) {
                    batch.setColor(0, color[i][j] / 2, 0, 1);
                    batch.draw(textureGround, i * CELL_SIZE, j * CELL_SIZE, 8, 0, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        batch.setColor(1, 1, 1, 1);
    }

    public void drop() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT - 1; j++) {
                if (data[i][j] == 0 && data[i][j + 1] == 1) {
                    data[i][j] = 1;
                    data[i][j + 1] = 0;
                    color[i][j] = color[i][j + 1];
                    color[i][j + 1] = 0;
                }
            }
        }
    }

    public boolean isGround(float x, float y) {
        int cellX = (int) (x / CELL_SIZE);
        int cellY = (int) (y / CELL_SIZE);
        if (cellX < 0 || cellX > WIDTH - 1 || cellY < 0 || cellY > HEIGHT - 1) {
            return false;
        }
        return data[cellX][cellY] == 1;
    }

    public void clearGround(float x, float y, int r) {
        int cellX = (int) (x / CELL_SIZE);
        int cellY = (int) (y / CELL_SIZE);

        int left = cellX - r;
        if (left < 0) {
            left = 0;
        }
        int right = cellX + r;
        if (right > WIDTH - 1) {
            right = WIDTH - 1;
        }
        int down = cellY - r;
        if (down < 0) {
            down = 0;
        }
        int up = cellY + r;
        if (up > HEIGHT - 1) {
            up = HEIGHT - 1;
        }

        for (int i = left; i <= right; i++) {
            for (int j = down; j <= up; j++) {
                if (Math.sqrt((i - cellX) * (i - cellX) + (j - cellY) * (j - cellY)) < r) {
                    data[i][j] = 0;
                }
            }
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > 0.5f) {
            drop();
        }
    }
}
