package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public final class PlayerCharacter extends ActorBeta {

    //***STATES, ANIMATIONS, AND KEYFRAME ARRAYS MUST HAVE THE SAME NUMBER OF ELEMENTS!!!
    enum PlayerState {Running, Jumping, Falling, Attacking, Sliding, Dying}

    private PlayerState m_playerState;
    //For optimization purposes, this may be converted into a set later.
    //I realized the problem wasn't the wrong array notation, but rather, its initialization.
    //I may consider returning this into a simple Java array in the future.
    private Array<Animation<TextureRegion>> m_animation;
    private int [] m_iKeyFrames;
    private float m_fSlideTime, m_fMaxSlideTime; //used for slide
    private boolean m_bIsGrounded;

    float m_fElapsedTime;
    private boolean m_bAnimationPaused;
    private boolean m_isAlive;

    //not sure whether this is the best method... looks like a memory leak...
    private ActorBeta m_floorReference;

    PlayerCharacter(){
        super();
        m_playerState = PlayerState.Running;
        m_animation = new Array<Animation<TextureRegion>>(true,6);
        m_iKeyFrames = new int[6];
        m_isAlive = true;
        m_bAnimationPaused = false;
        m_fElapsedTime = 0;
        m_fSlideTime = 0;
        m_fMaxSlideTime = 1f;
    }

    PlayerCharacter(float x, float y, Stage s){
        super(x, y, s);
        m_playerState = PlayerState.Running;
        m_animation = new Array<Animation<TextureRegion>>(true,6);
        m_iKeyFrames = new int[6];
        m_isAlive = true;
        m_bAnimationPaused = false;
        m_fElapsedTime = 0;
        m_fSlideTime = 0;
        m_fMaxSlideTime = 1f;
    }

    public void initializePlayer(ActorBeta floor, Stage s){

        float tempSize = 150*Gdx.graphics.getWidth()/Gdx.graphics.getHeight();

        float frameDuration = 0.20f;

        //Default (run) animation
        String[] runFiles = new String[6];
        for(int i = 0; i < runFiles.length; ++i){runFiles[i] = "AdvAnim/adventurer-run-0"+i+".png";}
        this.setAnimation(this.loadAnimationFromFiles(runFiles, frameDuration, true));

        //Jump animation:
        String[] jumpFiles = new String[4];
        for(int i = 0; i < jumpFiles.length; ++i){jumpFiles[i] = "AdvAnim/adventurer-jump-0"+i+".png";}
        this.setAnimation(this.loadAnimationFromFiles(jumpFiles, frameDuration, true));

        //Fall animation:
        String[] fallFiles = new String[2];
        for(int i = 0; i < fallFiles.length; ++i){fallFiles[i] = "AdvAnim/adventurer-fall-0"+i+".png";}
        this.setAnimation(this.loadAnimationFromFiles(fallFiles, frameDuration, true));

        //Attack animation:
        String[] attackFiles = new String[6];
        for(int i = 0; i < attackFiles.length; ++i){attackFiles[i] = "AdvAnim/adventurer-attack3-0"+i+".png";}
        this.setAnimation(this.loadAnimationFromFiles(attackFiles, frameDuration, true));

        //Slide animation:
        String[] slideFiles = new String[2];
        for(int i = 0; i < slideFiles.length; ++i){slideFiles[i] = "AdvAnim/adventurer-slide-0"+i+".png";}
        this.setAnimation(this.loadAnimationFromFiles(slideFiles, frameDuration, true));

        //Dying animation
        String [] deathFiles = new String[7];
        for(int i = 0; i < deathFiles.length; ++i){deathFiles[i] = "AdvAnim/adventurer-die-0"+i+".png";}
        this.setAnimation(this.loadAnimationFromFiles(deathFiles, frameDuration, false));

        m_floorReference = floor;

        this.setVisible(true);
        this.setSize(tempSize, tempSize);
        this.setBoundaryRectangle();
        accelerationVec = new Vector2(0, -500);
        s.addActor(this);
    }

    @Override
    public void setBoundaryRectangle()
    {
        float w = getWidth()/3;
        float h = getHeight()*0.75f;

        float [] vertices = {w, 0, 2*w, 0, 2*w, h, w, h};
        boundaryPolygon = new Polygon(vertices);
    }

    @Override
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop) {
        //Number of images to read
        int fileCount = fileNames.length;

        //Create empty TextureRegion array
        Array<TextureRegion> textureArray = new Array<TextureRegion>();

        //For the number of images, add them back into the array
        for (int n = 0; n < fileCount; n++) {
            String fileName = fileNames[n];
            //Create new texture with fileName at n
            Texture texture = new Texture(Gdx.files.internal(fileName));
            //Set Linear filter
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            //Add this textures to the Array
            textureArray.add(new TextureRegion(texture));
        }

        //Instantiate animation object while passing in array and duration of each frame
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

        //if loop is true, set LOOP ON
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);

            //else, no looping
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);

        return anim;
    }

    @Override
    public void setAnimation(Animation<TextureRegion> anim){
        //TODO: Add code
        m_animation.add(anim);
        m_iKeyFrames[m_animation.indexOf(anim,true)]= (int)(anim.getAnimationDuration()/anim.getFrameDuration());
        TextureRegion tr = m_animation.peek().getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();

        setSize(w, h);
        setOrigin(w / 2, h / 2);
    }

    public PlayerCharacter.PlayerState getPlayerState(){return m_playerState;}
    public void setPlayerState(PlayerCharacter.PlayerState newState){  m_playerState = newState; }

    @Override
    public void act(float dT){
        super.act(dT);

        if(!m_bAnimationPaused){
            m_fElapsedTime+=dT;
            applyPhysics(dT);
        }

        m_bIsGrounded = overlaps(m_floorReference);

        if(!m_bIsGrounded){
            if(velocityVec.y > 0 && m_playerState != PlayerState.Jumping){
                setPlayerState(PlayerState.Jumping);
            }
            else if (velocityVec.y < 0 && m_playerState != PlayerState.Falling){
                setPlayerState(PlayerState.Falling);
            }
        }else{
            velocityVec.y = 0;
            if(m_playerState == PlayerState.Jumping || m_playerState == PlayerState.Falling){
                setPlayerState(PlayerState.Running);
            }
            else if(m_playerState == PlayerState.Sliding){
                m_fSlideTime+=dT;
                if(m_fSlideTime > m_fMaxSlideTime){
                    setPlayerState(PlayerState.Running);
                }
            }else if(m_playerState == PlayerState.Attacking && isAnimationFinished()){
                setPlayerState(PlayerState.Running);
            }
            preventOverlap(m_floorReference);
        }
    }

    @Override
    public boolean isAnimationFinished() {
        int targetAnimation;
        switch(m_playerState){
            case Running:
                targetAnimation = 0;
                break;
            case Jumping:
                targetAnimation = 1;
                break;
            case Falling:
                targetAnimation = 2;
                break;
            case Attacking:
                targetAnimation = 3;
                break;
            case Sliding:
                targetAnimation = 4;
                break;
            case Dying:
                targetAnimation = 5;
                break;
            default:
                //PING ERROR
                return false;
        }

        return m_animation.get(targetAnimation).getKeyFrameIndex(m_fElapsedTime) > (m_iKeyFrames[targetAnimation]-2);
    }

    @Override
    public void setAnimationPaused(boolean pause) {
        m_bAnimationPaused = pause;
    }

    //TODO: Will require refactoring from ActorBeta
    @Override
    public void draw(Batch batch, float parentAlpha) {

        Color c = getColor(); // used to apply tint color effect

        batch.setColor(c.r, c.g, c.b, c.a);

        if (m_animation != null && isVisible())
            switch (m_playerState){
                case Running:
                    batch.draw(m_animation.get(0).getKeyFrame(m_fElapsedTime),
                            getX(), getY(), getOriginX(), getOriginY(),
                            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                    break;
                case Jumping:
                    batch.draw(m_animation.get(1).getKeyFrame(m_fElapsedTime),
                            getX(), getY(), getOriginX(), getOriginY(),
                            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                    break;
                case Falling:
                    batch.draw(m_animation.get(2).getKeyFrame(m_fElapsedTime),
                            getX(), getY(), getOriginX(), getOriginY(),
                            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                    break;
                case Attacking:
                    batch.draw(m_animation.get(3).getKeyFrame(m_fElapsedTime),
                            getX(), getY(), getOriginX(), getOriginY(),
                            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                    break;
                case Sliding:
                    batch.draw(m_animation.get(4).getKeyFrame(m_fElapsedTime),
                            getX(), getY(), getOriginX(), getOriginY(),
                            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
                    break;
                default:
                    batch.draw(m_animation.get(5).getKeyFrame(m_fElapsedTime),
                            getX(), getY(), getOriginX(), getOriginY(),
                            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }

        super.draw(batch, parentAlpha);
    }

    @Override
    public void applyPhysics(float dt)
    {
        //apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);

        float speed = getSpeed();

        //decrease soeed (decelerate) when not accelerating
        if(accelerationVec.len() == 0)
            speed -= deceleration * dt;

        //keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);

        //update velocity
        setSpeed(speed);

        //update position according to value stored in velocity vector
        moveBy(velocityVec.x * dt, velocityVec.y * dt);
    }

    //Player-specific methods
    public void jump(){
        if (m_playerState==PlayerState.Running) {
            velocityVec = new Vector2(0, 500);
            setPlayerState(PlayerState.Jumping);
        }
    }

    public void die(){
        m_isAlive = false;
        m_playerState = PlayerState.Dying;
    }

    public void slide(){
        //I've made it possible for the player to keep sliding
        if(m_playerState == PlayerState.Running || m_playerState == PlayerState.Sliding) {
            setPlayerState(PlayerCharacter.PlayerState.Sliding);
            m_fSlideTime = 0;
        }
    }

    public void attack(){
        if(m_playerState == PlayerState.Running) {
            setPlayerState(PlayerCharacter.PlayerState.Attacking);
        }
    }

    public boolean playerAlive(){ return m_isAlive; }
}
