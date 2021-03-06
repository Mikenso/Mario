package com.mike.mariobros.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.mike.mariobros.Controller;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.scenes.Hud;
import com.mike.mariobros.sprites.enemies.Enemy;
import com.mike.mariobros.sprites.Mario;
import com.mike.mariobros.sprites.items.Item;
import com.mike.mariobros.sprites.items.ItemDef;
import com.mike.mariobros.sprites.items.Mushroom;
import com.mike.mariobros.tools.B2WorldCreator;
import com.mike.mariobros.tools.WorldContactListener;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by Mike on 25.10.2016.
 */
public class PlayScreen implements Screen {

    private MarioBros game;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Viewport gamePort;
    private OrthographicCamera gameCam;
    private Mario player;
    private B2WorldCreator creator;
    private TextureAtlas atlas;
    private Music music;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private Controller controller;



    private World world;
    private Box2DDebugRenderer b2dr;


    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport( MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT /  MarioBros.PPM, gameCam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight() / 2 , 0 );

        world = new World(new Vector2(0, -10 ), true);
        b2dr = new Box2DDebugRenderer();
        player = new Mario(this);


       creator = new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

         music = MarioBros.assetManager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.05f);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        controller = new Controller();

    }

    public boolean gameOver() {
        if (player.currentState.equals(Mario.State.DEAD) && player.getStateTimer() > 3) return true;
        else  return false;
    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef itemDef = itemsToSpawn.poll();
            if (itemDef.type == Mushroom.class) {
                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public void update (float dt) {
        handleInput(dt);
        handleSpawningItems();
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        for (Enemy enemy: creator.getEnemies()   ) {
              enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
                enemy.b2body.setActive(true);
            }
        }
        for (Item item : items ) {
            item.update(dt);
        }

        hud.update(dt);

        if (!player.currentState.equals(Mario.State.DEAD)) { gameCam.position.x = player.b2body.getPosition().x; }
        gameCam.update();
        renderer.setView(gameCam);
    }

    private void handleInput(float dt) {
        if (!player.currentState.equals(Mario.State.DEAD)) {
            if (player.getCurrentState() != Mario.State.FALLING && player.getCurrentState() != Mario.State.JUMPING  ) { if  (Gdx.input.isKeyJustPressed(Input.Keys.UP) || controller.isUpPressed()) {
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ||  controller.isLeftPressed()&& player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

      //  b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);

        for (Enemy enemy: creator.getEnemies()   ) {
            enemy.draw(game.batch);
        }
        for (Item item : items ) {
            item.draw(game.batch);
        }
        game.batch.end();


        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }


        if (Gdx.app.getType() == Application.ApplicationType.Android)
        controller.draw();

    }

    @Override
    public void resize(int width, int height) {
     gamePort.update(width, height);
     controller.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
