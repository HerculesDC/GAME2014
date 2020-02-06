package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TitleScreen extends ScreenBeta {

    private SpriteBatch m_Batch;
    private Texture m_Tex;

    TitleScreen(){
        super();
        m_Batch = new SpriteBatch();
        m_Tex = new Texture("RandomRun.png");
        m_Buttons = new TextButton[3];
    }

    @Override
    public void initialize() {
        m_Buttons[0] = new TextButton("Play", FirstGame.getSkin());
        m_Buttons[1] = new TextButton("Settings", FirstGame.getSkin());
        m_Buttons[2] = new TextButton("Quit", FirstGame.getSkin());

        for (int i = 0; i < m_Buttons.length; ++i){ //TODO: Realign, add functionality
            m_Buttons[i].setPosition((Gdx.graphics.getWidth()/2)-(m_Buttons[i].getWidth()/2), (Gdx.graphics.getHeight()/2)-(1.15f*(i+1)*m_Buttons[i].getHeight()));
            m_Stage.addActor(m_Buttons[i]);
        }
        m_bInitialized = true;
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
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) { //TODO: Proper button presses (triggers when released)

        if (m_Buttons[0].isPressed()){FirstGame.setActiveScreen(new RunScreen());}

        if (m_Buttons[1].isPressed()){FirstGame.setActiveScreen(new MenuScreen());}

        if (m_Buttons[2].isPressed()){
            Gdx.app.exit();
            System.exit(0);
        }

        Gdx.gl.glClearColor(0.23f, 0.42f, 0.55f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        m_Batch.begin();
        m_Batch.draw(m_Tex, (Gdx.graphics.getWidth()/2)-(m_Tex.getWidth()/2), (Gdx.graphics.getHeight()/2)+(m_Tex.getHeight()/4));
        m_Batch.end();

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
    public void hide() {
        super.hide();
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
