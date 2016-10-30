package com.mike.mariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mike.mariobros.MarioBros;
import com.mike.mariobros.screens.PlayScreen;



/**
 * Created by Mike on 27.10.2016.
 */
public class Mario extends Sprite {
    public World world;
    public Body b2body;



    public enum State {FALLING, JUMPING, STANDINING, RUNNING }
    public State currentState;
    public State previosState;

    private Animation marioRun;
    private Animation marioJump;
    private float stateTimer;
    private boolean runningRight;

    private TextureRegion marioStand;

    public Mario( PlayScreen screen) {
        super( screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        currentState = State.STANDINING;
        previosState = State.STANDINING;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4 ; i++)
            frames.add(new TextureRegion(getTexture(), i*16, 0 , 16, 16));
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6 ; i++)
            frames.add(new TextureRegion(getTexture(), i * 16,0 , 16, 16 ));
        marioJump = new Animation(0.1f, frames);
       // frames.clear();

        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);

       defineMario();
       //marioStand = new TextureRegion(getTexture(),0, 0, 16, 16);
        setBounds(0,0, 16 /MarioBros.PPM, 16 /MarioBros.PPM );
        setRegion(marioStand);
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioBros.PPM, 32/ MarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fixtureDef.filter.categoryBits = MarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioBros.GROUND_BIT
                | MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT ;

        fixtureDef.shape = shape;

        b2body.createFixture(fixtureDef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2( - 2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2( 2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        b2body.createFixture(fixtureDef).setUserData("head");
    }

    public  void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - getHeight() /2 );
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region ;
        switch (currentState) {
            case JUMPING: region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING: region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDINING:
            default:
                region = marioStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previosState ? stateTimer + dt : 0;
        previosState = currentState;
        return region;
    }

    public State getState() {
        if ((b2body.getLinearVelocity().y > 0) || (b2body.getLinearVelocity().y < 0 && previosState ==State.JUMPING) ) return State.JUMPING;
       else if (b2body.getLinearVelocity().y < 0) return State.FALLING;
       else if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
        else return State.STANDINING;
    }
}
