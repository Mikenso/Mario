package com.mike.mariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.screens.PlayScreen;
import com.mike.mariobros.sprites.Mario;

/**
 * Created by Mike on 30.10.2016.
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    protected Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        this.velocity = new Vector2(1, 0);
       b2body.setActive(false);

    }
    protected abstract  void defineEnemy();

    public abstract void hitOnHead(Mario mario);

    public void reverseVelocity(boolean x, boolean y) {
        if (x) velocity.x = -velocity.x;
        if (y) velocity.y = -velocity.y;
    }

    public abstract void update(float dt);
    public abstract void onEnemyHit(Enemy enemy);
}
