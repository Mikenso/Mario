package com.mike.mariobros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.OverlayLayout;

/**
 * Created by Mike on 04.11.2016.
 */
public class Controller {
    Viewport viewport;
    Stage stage;

    boolean upPressed, downPressed, rightPressed, leftPressed;
    OrthographicCamera cam;

    public Controller() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, MarioBros.batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();



        Image imageUp = new Image(new Texture("up.png"));
        imageUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image imageDown = new Image(new Texture("down.png"));


        imageDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
            }
        });


        Image imageRight = new Image(new Texture("right.png"));


        imageRight.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        Image imageLeft = new Image(new Texture("left.png"));


        imageLeft.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        table.add();
        table.add(imageUp).size(imageUp.getWidth(), imageUp.getHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(imageLeft).size(imageLeft.getWidth(), imageLeft.getHeight());
        table.add();
        table.add(imageRight).size(imageRight.getWidth(), imageRight.getHeight());

      /*  table.row().padBottom(5);
        table.add(imageDown).size(imageDown.getWidth(), imageDown.getHeight());
        table.add();*/

        stage.addActor(table);
    }

    public void draw() {
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
