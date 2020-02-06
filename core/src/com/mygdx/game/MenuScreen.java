package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen extends ScreenBeta {

    //TODO: Sliders for music/SFX, texts

    private final int m_categories = 3;

    private Slider[] m_Sliders;
    private Label[] m_Labels;

    MenuScreen(){
        super();
        m_Sliders = new Slider[m_categories];
        m_Labels = new Label[m_categories];
        m_Buttons = new TextButton[2];
    }

    @Override
    public void initialize() {

        m_Buttons[0] = new TextButton("Start", FirstGame.getSkin());
        m_Buttons[0].setPosition(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
        m_Stage.addActor(m_Buttons[0]);

        m_Buttons[1] = new TextButton("Back", FirstGame.getSkin());
        m_Buttons[1].setPosition(Gdx.graphics.getWidth()*(8.0f/10), Gdx.graphics.getHeight()/10);
        m_Stage.addActor(m_Buttons[1]);


        m_Sliders[0] = new Slider(0.0f, 1.0f, 0.01f, false, FirstGame.getSkin());
        m_Sliders[0].setWidth(Gdx.graphics.getWidth()/3);
        m_Sliders[0].setPosition(Gdx.graphics.getWidth()/2, (Gdx.graphics.getHeight()/2.0f)+(2.0f*m_Sliders[0].getHeight()));
        m_Sliders[0].setValue(FirstGame.getMusicVolume());

        m_Labels[0] = new Label("Music Volume",FirstGame.getSkin());
        m_Labels[0].setPosition(Gdx.graphics.getWidth()/4, (Gdx.graphics.getHeight()/2.0f)+(2.0f*m_Sliders[0].getHeight()));


        m_Sliders[1] = new Slider(0.0f, 1.0f, 0.01f, false, FirstGame.getSkin());
        m_Sliders[1].setWidth(Gdx.graphics.getWidth()/3);
        m_Sliders[1].setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        m_Sliders[1].setValue(FirstGame.getSFXVolume());

        m_Labels[1] = new Label("SFX Volume",FirstGame.getSkin());
        m_Labels[1].setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/2);

        m_Sliders[2] = new Slider(0.0f, 1.0f, 0.01f, false, FirstGame.getSkin());
        m_Sliders[2].setWidth(Gdx.graphics.getWidth()/3);
        m_Sliders[2].setPosition(Gdx.graphics.getWidth()/2, (Gdx.graphics.getHeight()/2.0f)-(2.0f*m_Sliders[0].getHeight()));
        m_Sliders[2].setValue(FirstGame.getSensitivity());

        m_Labels[2] = new Label("Sensitivity",FirstGame.getSkin());
        m_Labels[2].setPosition(Gdx.graphics.getWidth()/4, (Gdx.graphics.getHeight()/2.0f)-(1.75f*m_Sliders[0].getHeight()));

        for (int i = 0; i < m_categories; ++i){
            m_Labels[i].setFontScale(2.5f);
            m_Stage.addActor(m_Sliders[i]);
            m_Stage.addActor(m_Labels[i]);
        }

        m_bInitialized = true;
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

    @Override
    public void render(float delta) {

        //This may not be where I need to put it...
        FirstGame.setMusicVolume(m_Sliders[0].getValue());
        FirstGame.setSFXVolume(m_Sliders[1].getValue());
        FirstGame.setSensitivity(m_Sliders[2].getValue());

        if (m_Buttons[0].isPressed()){FirstGame.setActiveScreen(new RunScreen());}

        if (m_Buttons[1].isPressed()){FirstGame.setActiveScreen(new TitleScreen());}

        Gdx.gl.glClearColor(0.23f, 0.42f, 0.55f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        m_Stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
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
