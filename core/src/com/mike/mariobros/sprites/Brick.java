package com.mike.mariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.scenes.Hud;
import com.mike.mariobros.screens.PlayScreen;

/**
 * Created by Mike on 27.10.2016.
 */
public class Brick extends  InterActiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);

        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
     Gdx.app.log("Brick", "Collusion");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        MarioBros.assetManager.get("audio/sounds/breakblock.wav", Sound.class).play(0.05f);
    }
}
