package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class RunScreen extends ScreenBeta {

    //Game State variables
    enum GameState {Play,Pause,Lost}
    private GameState m_State;
    private boolean m_bIsPaused;
    private boolean m_bIsLost;

    //I consider every other screen to be inherently an UI one.
    //This would be the only screen where a separate "Layer" would be necessary for now
    //Level proper variables
    private Stage m_LevelStage;
    private BackgroundElement [] m_bgElements;

    private ActorBeta m_floor;

    //Player Character variables
    private PlayerCharacter m_playerCharacter;

    //Obstacles variables
    private Array<ActorBeta> m_Obstacles;

    //UI layer-specific variables
    private Label m_PauseText;
    private Label m_TimerText;
    private Label m_LostText;
    private Label m_Instructions;
    private float m_TimeSinceStarted;

    private Random m_random;

    RunScreen(){

        m_Buttons = new TextButton[5];
        m_bIsPaused = false;
        m_bIsLost = false;
        m_State = GameState.Play;
        m_LevelStage = new Stage();

        m_PauseText = new Label("GAME PAUSED", FirstGame.getSkin());
        m_TimerText = new Label("Time:  ", FirstGame.getSkin());
        m_LostText = new Label("A dead adventurer\nis you!", FirstGame.getSkin());
        m_Instructions = new Label("Swipe up to jump over fires\n\nSwipe down to slide under clotheslines\n\nTap to attack posts", FirstGame.getSkin());

        m_bgElements = new BackgroundElement[10];

        m_floor = new ActorBeta(0, -Gdx.graphics.getHeight(), m_LevelStage);
        m_floor.setSize(Gdx.graphics.getWidth(), 1.1f*Gdx.graphics.getHeight());
        m_floor.setBoundaryRectangle();
        //m_floor.setDebug(true);

        m_playerCharacter = new PlayerCharacter();//WILL REQUIRE INITIALIZATION!!!
        float tempSize = 150*Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
        m_playerCharacter.setSize(tempSize, tempSize); //may require refactor
        //m_playerCharacter.setDebug(true);

        m_Obstacles = new Array<ActorBeta>(false, 3);

        m_random = new Random();

        //TODO: Music & Sounds!
        m_BGM = Gdx.audio.newMusic(Gdx.files.internal("Destiny.mp3"));
        m_BGM.setVolume(FirstGame.getMusicVolume());
        m_BGM.setLooping(true);

        m_sfx = new Sound[5];
        m_sfx[0] = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        m_sfx[1] = Gdx.audio.newSound(Gdx.files.internal("attack.mp3"));
        m_sfx[2] = Gdx.audio.newSound(Gdx.files.internal("skid.wav"));
        m_sfx[3] = Gdx.audio.newSound(Gdx.files.internal("break.wav"));
        m_sfx[4] = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
    }

    @Override
    public void show(){
        super.show();
    }

    @Override
    public void hide(){
        super.hide();
    }

    @Override
    public void initialize() {

        m_TimeSinceStarted = 0.0f;

        initializeLevel();
        initializeUI();

        m_BGM.play();

        m_bInitialized = true;
    }

    //Input Processor Overrides
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    //ScreenBeta Overrides
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.23f, 0.42f, 0.55f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(m_Buttons[0].isPressed()){
            m_bIsPaused = true;
            m_State = GameState.Pause;
        }

        if(m_Buttons[1].isPressed()){
            m_bIsPaused = false;
            m_State = GameState.Play;
        }

        if(m_Buttons[2].isPressed()){ FirstGame.setActiveScreen(new TitleScreen()); }

        if(m_Buttons[3].isPressed()){ FirstGame.setActiveScreen(new RunScreen()); }

        if(m_Buttons[4].isPressed()){
            Gdx.app.exit();
            System.exit(0);
        }

        switch(m_State){
            case Play:
                if (m_playerCharacter.getPlayerState() != PlayerCharacter.PlayerState.Sliding){
                    m_sfx[2].stop();
                }
                m_TimeSinceStarted += delta;
                m_TimerText.setText("Time:  "+(int)m_TimeSinceStarted);
                m_LevelStage.act(delta);
                m_Instructions.moveBy(-160f*delta, 0f);
                for(int i = 0; i < m_Obstacles.size; ++i){
                    m_Obstacles.get(i).moveBy(-180f*delta, 0);
                    processOverlap(i, m_Obstacles.get(i), m_playerCharacter);
                    if (m_Obstacles.get(i).getX() < - 2f*m_Obstacles.get(i).getWidth()){
                        //resets the thing's position to whatever off-screen position
                        processRespawn(i);
                        m_Obstacles.get(i).setX(Gdx.graphics.getWidth() +
                                      m_random.nextFloat()*Gdx.graphics.getWidth() +
                                m_Obstacles.get(i).getWidth());
                    }
                }
                if (!m_BGM.isPlaying()){
                    m_BGM.play();
                }
                //probs refactor later...
                renderPlay();
                break;
            case Pause:
                if (m_BGM.isPlaying()){
                    m_BGM.pause();
                }
                renderPause();
                break;
            case Lost:
                if(m_BGM.isPlaying()){
                    m_BGM.stop();
                }
                renderLose();
                break;
            default:
                renderPlay();
        }

        if (m_bIsLost) m_State = GameState.Lost;

        m_LevelStage.draw();
        m_Stage.draw();
    }

    private void processOverlap(int obstacleIndex, ActorBeta obstacle, PlayerCharacter player){
        if(obstacle.overlaps(m_playerCharacter)){
            if (obstacleIndex == 0 && player.getPlayerState() != PlayerCharacter.PlayerState.Jumping){
                m_playerCharacter.die();
            }
            else if (obstacleIndex == 1 && player.getPlayerState() != PlayerCharacter.PlayerState.Sliding){
                m_playerCharacter.die();
            }else if (obstacleIndex == 2 && player.getPlayerState() != PlayerCharacter.PlayerState.Attacking){
                m_playerCharacter.die();
            }else if(obstacleIndex == 2 && player.getPlayerState() == PlayerCharacter.PlayerState.Attacking){
                m_sfx[3].play(FirstGame.getSFXVolume());
                obstacle.setX(Gdx.graphics.getWidth() + m_random.nextFloat()*Gdx.graphics.getWidth());
            }
        }
        m_bIsLost = !m_playerCharacter.playerAlive();

        if (m_bIsLost){
            m_sfx[4].play(FirstGame.getSFXVolume());
        }
    }

    private void processRespawn(int index){
        if (index == 0) {
            while (m_Obstacles.get(index).overlaps(m_Obstacles.get(index+1))||
                   m_Obstacles.get(index).overlaps(m_Obstacles.get(index+2))){
                m_Obstacles.get(index).setX(Gdx.graphics.getWidth() + m_random.nextFloat()*Gdx.graphics.getWidth());
            }
        }
        if (index == 1) {
            while (m_Obstacles.get(index).overlaps(m_Obstacles.get(index-1))||
                   m_Obstacles.get(index).overlaps(m_Obstacles.get(index+1))){
                m_Obstacles.get(index).setX(Gdx.graphics.getWidth() + m_random.nextFloat()*Gdx.graphics.getWidth());
            }
        }if (index == 2) {
            while (m_Obstacles.get(index).overlaps(m_Obstacles.get(index-1))||
                   m_Obstacles.get(index).overlaps(m_Obstacles.get(index-2))){
                m_Obstacles.get(index).setX(Gdx.graphics.getWidth() + m_random.nextFloat()*Gdx.graphics.getWidth());
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        m_bIsPaused = true;
        m_State = GameState.Pause;
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        m_BGM.dispose();
        for(Sound s : m_sfx) s.dispose();
    }

    private void initializeUI(){

        m_Buttons[0] = new TextButton("Pause", FirstGame.getSkin());
        m_Buttons[0].setPosition(0.85f*Gdx.graphics.getWidth(), 0.1f*Gdx.graphics.getHeight());
        m_Buttons[0].setVisible(true);
        m_Buttons[1] = new TextButton("Resume", FirstGame.getSkin());
        m_Buttons[1].setPosition(Gdx.graphics.getWidth()/2-(m_Buttons[1].getWidth()/2), Gdx.graphics.getHeight()/2+(1.0f*m_Buttons[1].getHeight()));
        m_Buttons[1].setVisible(true);
        m_Buttons[2] = new TextButton("Main Menu", FirstGame.getSkin());
        m_Buttons[2].setPosition(Gdx.graphics.getWidth()/2-(m_Buttons[2].getWidth()/2), Gdx.graphics.getHeight()/2-(0.5f*m_Buttons[2].getHeight()));
        m_Buttons[2].setVisible(true);
        m_Buttons[3] = new TextButton("Retry", FirstGame.getSkin());
        m_Buttons[3].setPosition(Gdx.graphics.getWidth()/2-(m_Buttons[3].getWidth()/2), Gdx.graphics.getHeight()/2-(2.0f*m_Buttons[3].getHeight()));
        m_Buttons[3].setVisible(true);
        m_Buttons[4] = new TextButton("Quit", FirstGame.getSkin());
        m_Buttons[4].setPosition(Gdx.graphics.getWidth()/2-(m_Buttons[4].getWidth()/2), Gdx.graphics.getHeight()/2+(1.0f*m_Buttons[4].getHeight()));
        m_Buttons[4].setVisible(true);

        m_PauseText.setFontScale(4.0f);
        m_PauseText.setPosition(Gdx.graphics.getWidth()/2-(m_PauseText.getWidth()/2), Gdx.graphics.getHeight()-(2.5f*m_PauseText.getHeight()));
        m_PauseText.setAlignment(Align.center);
        m_PauseText.setVisible(true);
        m_Stage.addActor(m_PauseText);

        m_TimerText.setFontScale(2.0f);
        m_TimerText.setPosition(Gdx.graphics.getWidth()/20.0f, Gdx.graphics.getHeight()-(2*m_TimerText.getHeight()));
        m_TimerText.setVisible(true);
        m_Stage.addActor(m_TimerText);

        m_LostText.setFontScale(4.0f);
        m_LostText.setPosition(Gdx.graphics.getWidth()/2-(m_LostText.getWidth()/2), Gdx.graphics.getHeight()-(2.5f*m_LostText.getHeight()));
        m_LostText.setAlignment(Align.center);
        m_LostText.setVisible(true);
        m_Stage.addActor(m_LostText);

        m_Instructions.setFontScale(3.0f);
        m_Instructions.setPosition(Gdx.graphics.getWidth()+(m_Instructions.getWidth()), Gdx.graphics.getHeight()-(2.2f*m_Instructions.getHeight()));
        m_Instructions.setAlignment(Align.center);
        m_Instructions.setVisible(true);
        m_Stage.addActor(m_Instructions);

        for (int i = 0; i < m_Buttons.length; ++i){
            m_Stage.addActor(m_Buttons[i]);
        }
    }

    private void initializeLevel(){

        float widthAlias, heightAlias;
        widthAlias = Gdx.graphics.getWidth();
        heightAlias = Gdx.graphics.getHeight();
        /*("Layer_0009_2.png","Layer_0008_3.png","Layer_0007_Lights.png", "Layer_0006_4.png","Layer_0005_5.png",
           "Layer_0004_Lights.png", "Layer_0003_6.png","Layer_0002_7.png","Layer_0001_8.png","Layer_0000_9.png");*/

        m_bgElements[0] = new BackgroundElement("BG/Layer_0009_2.png", 20.0f, m_LevelStage);
        m_bgElements[1] = new BackgroundElement("BG/Layer_0008_3.png", 40.0f, m_LevelStage);
        m_bgElements[2] = new BackgroundElement("BG/Layer_0007_Lights.png", 60.0f, m_LevelStage);
        m_bgElements[3] = new BackgroundElement("BG/Layer_0006_4.png", 80.0f, m_LevelStage);
        m_bgElements[4] = new BackgroundElement("BG/Layer_0005_5.png", 100.0f, m_LevelStage);
        m_bgElements[5] = new BackgroundElement("BG/Layer_0004_Lights.png", 120.0f, m_LevelStage);
        m_bgElements[6] = new BackgroundElement("BG/Layer_0003_6.png", 140.0f, m_LevelStage);
        m_bgElements[7] = new BackgroundElement("BG/Layer_0002_7.png", 160.0f, m_LevelStage);
        m_bgElements[8] = new BackgroundElement("BG/Layer_0001_8.png", 180.0f, m_LevelStage);
        m_bgElements[9] = new BackgroundElement("BG/Layer_0000_9.png", 200.0f, m_LevelStage);

        m_playerCharacter.setPosition(0.075f*Gdx.graphics.getWidth(), 0.14f*Gdx.graphics.getHeight());

        //SIZES DON'T MATCH, SEEMS TO BE CENTERED ON ORIGIN...
        m_Obstacles.add(new ActorBeta((widthAlias+m_random.nextFloat()*widthAlias), 0.115f*heightAlias, m_LevelStage));
        m_Obstacles.get(0).setAnimation(m_Obstacles.get(0).loadAnimationFromSheet("Obstacles/bonfire.png", 2, 6, 0.16f, true));
        m_Obstacles.get(0).setScale(1.3f);
        m_Obstacles.get(0).setSize(130, 130);
        m_Obstacles.get(0).setBoundaryRectangle(30, 0, 70, 70);
        //m_Obstacles.get(0).setDebug(true);
        m_Obstacles.get(0).setVisible(true);

        m_Obstacles.add(new ActorBeta(widthAlias+m_random.nextFloat()*widthAlias, 0.19f*heightAlias, m_LevelStage));
        m_Obstacles.get(1).loadTexture("Obstacles/clothesLine.png");
        m_Obstacles.get(1).setScale(6f);
        m_Obstacles.get(1).setBoundaryRectangle();
        //m_Obstacles.get(1).setDebug(true);
        m_Obstacles.get(1).setVisible(true);

        m_Obstacles.add(new ActorBeta(widthAlias+m_random.nextFloat()*widthAlias, 0.225f*heightAlias, m_LevelStage));
        m_Obstacles.get(2).loadTexture("Obstacles/post.png");
        m_Obstacles.get(2).setScale(6f);
        m_Obstacles.get(2).setBoundaryRectangle();
        //m_Obstacles.get(2).setDebug(true);
        m_Obstacles.get(2).setVisible(true);

        for(int i = 0; i < m_Obstacles.size; ++i){
            processRespawn(i);
        }

        m_playerCharacter.initializePlayer(m_floor, m_LevelStage);
    }

    private void renderPlay(){

        if (!m_Buttons[0].isVisible()){ m_Buttons[0].setVisible(true); }
        if ( m_Buttons[1].isVisible()){ m_Buttons[1].setVisible(false);}
        if ( m_Buttons[2].isVisible()){ m_Buttons[2].setVisible(false);}
        if ( m_Buttons[3].isVisible()){ m_Buttons[3].setVisible(false);}
        if ( m_Buttons[4].isVisible()){ m_Buttons[4].setVisible(false);}
        if ( m_PauseText.isVisible() ){ m_PauseText.setVisible(false); }
        if ( m_LostText.isVisible()  ){ m_LostText.setVisible(false);  }

        if (m_Instructions.isVisible() &&
                m_Instructions.getX() < -2.0f*m_Instructions.getWidth()){
            m_Instructions.setVisible(false);
        }else if(!m_Instructions.isVisible() &&
                m_Instructions.getX() > -2.0f*m_Instructions.getWidth()){
            m_Instructions.setVisible(true);
        }
    }

    private void renderPause(){

        if ( m_Buttons[0].isVisible()){ m_Buttons[0].setVisible(false);  } //Pause
        if (!m_Buttons[1].isVisible()){ m_Buttons[1].setVisible(true);   } //Resume
        if (!m_Buttons[2].isVisible()){ m_Buttons[2].setVisible(true);   } //Main Menu
        if (!m_Buttons[3].isVisible()){ m_Buttons[3].setVisible(true);   } //Retry
        if ( m_Buttons[4].isVisible()){ m_Buttons[4].setVisible(false);  } //Quit
        if (!m_PauseText.isVisible() ){ m_PauseText.setVisible(true);    }
        if ( m_LostText.isVisible()  ){ m_LostText.setVisible(false);    }
        if (m_Instructions.isVisible()){m_Instructions.setVisible(false);}
    }

    private void renderLose(){
        if ( m_Buttons[0].isVisible()){ m_Buttons[0].setVisible(false);  } //Pause
        if ( m_Buttons[1].isVisible()){ m_Buttons[1].setVisible(false);  } //Resume
        if (!m_Buttons[2].isVisible()){ m_Buttons[2].setVisible(true);   } //Main Menu
        if (!m_Buttons[3].isVisible()){ m_Buttons[3].setVisible(true);  } //Retry
        if (!m_Buttons[4].isVisible()){ m_Buttons[4].setVisible(true);   } //Quit
        if ( m_PauseText.isVisible() ){ m_PauseText.setVisible(false);   }
        if (!m_LostText.isVisible()  ){ m_LostText.setVisible(true);     }
        if (m_Instructions.isVisible()){m_Instructions.setVisible(false);}
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if (!m_bIsPaused){
            m_playerCharacter.attack();
            m_sfx[1].play(FirstGame.getSFXVolume());
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if(!m_bIsPaused && m_playerCharacter.playerAlive()){
            if(Math.abs(velocityX)<Math.abs(velocityY)){
                //Velocities are reversed
                if(velocityY < 0){
                    m_playerCharacter.jump();
                    m_sfx[0].play(FirstGame.getSFXVolume());
                }else if (velocityY > 0){
                    if (m_playerCharacter.getPlayerState() != PlayerCharacter.PlayerState.Sliding){
                        m_sfx[2].loop(FirstGame.getSFXVolume());
                    }
                    m_playerCharacter.slide();
                }
            }
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}