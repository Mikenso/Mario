package com.mike.mariobros.sprites;

import com.badlogic.gdx.audio.Sound;
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

    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;

    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;




    public enum State {FALLING, JUMPING, STANDINING, RUNNING, GROWING }
    public State currentState;
    public State previosState;

    private Animation marioRun;
    private TextureRegion marioJump;
    private float stateTimer;
    private boolean runningRight;

    private TextureRegion marioStand;

    public boolean isMarioIsBig() {
        return marioIsBig;
    }

    public Mario( PlayScreen screen) {

        this.world = screen.getWorld();
        currentState = State.STANDINING;
        previosState = State.STANDINING;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4 ; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0 , 16, 16));
        marioRun = new Animation(0.1f, frames);
        frames.clear();



        for (int i = 1; i < 4 ; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0 , 16, 32));
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));

        growMario = new Animation(0.2f, frames);
        frames.clear();


        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
       // frames.clear();

        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

       defineMario();

        setBounds(0,0, 16 /MarioBros.PPM, 16 /MarioBros.PPM );
        setRegion(marioStand);
    }

    public void grow() {
        runGrowAnimation =true;
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        MarioBros.assetManager.get("audio/sounds/powerup.wav", Sound.class).play(0.05f);

    }
    public  void defineBigMario() {
        Vector2 currentPosition =  b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT |
                MarioBros.COIN_BIT |
                MarioBros.BRICK_BIT |
                MarioBros.ENEMY_BIT |
                MarioBros.OBJECT_BIT |
                MarioBros.ENEMY_HEAD_BIT |
                MarioBros.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public  void update(float dt) {
        if (marioIsBig)
            setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - getHeight() /2 -6 /MarioBros.PPM );
       else
            setPosition(b2body.getPosition().x - getWidth() /2 , b2body.getPosition().y - getHeight() /2 );
        setRegion(getFrame(dt));

        if (timeToDefineBigMario) {
            defineBigMario();
        }
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region ;
        switch (currentState) {
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING: region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING: region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDINING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
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
        if (runGrowAnimation) return State.GROWING;
        else if ((b2body.getLinearVelocity().y > 0) || (b2body.getLinearVelocity().y < 0 && previosState ==State.JUMPING) ) return State.JUMPING;
       else if (b2body.getLinearVelocity().y < 0) return State.FALLING;
       else if (b2body.getLinearVelocity().x != 0) return State.RUNNING;
        else return State.STANDINING;
    }
}
