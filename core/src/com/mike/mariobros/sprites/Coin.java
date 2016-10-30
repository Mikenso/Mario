package com.mike.mariobros.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.scenes.Hud;
import com.mike.mariobros.screens.PlayScreen;


/**
 * Created by Mike on 27.10.2016.
 */
public class Coin extends  InterActiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);

    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collusion");
        if(getCell().getTile().getId() == BLANK_COIN)
            MarioBros.assetManager.get("audio/sounds/bump.wav", Sound.class).play(0.05f);
        else  MarioBros.assetManager.get("audio/sounds/coin.wav", Sound.class).play(0.05f);

        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
