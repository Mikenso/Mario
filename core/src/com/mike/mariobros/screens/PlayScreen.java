package com.mike.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.scenes.Hud;


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


    public PlayScreen(MarioBros game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport( MarioBros.V_WIDTH, MarioBros.V_HEIGHT, gameCam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gameCam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight() / 2 , 0 );

    }

    public void update (float dt) {
        hadleInput(dt);
        gameCam.update();
        renderer.setView(gameCam);
    }

    private void hadleInput(float dt) {
        if (Gdx.input.isTouched()) {
            gameCam.position.x += 100 * dt;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0 ,1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
     gamePort.update(width, height);
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

    }
}
