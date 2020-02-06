package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

class SplashScreen extends ScreenBeta {

    private SpriteBatch m_Batch;
    private Texture [] m_Tex;

    private float m_fTimer;
    private float m_fMaxTime;
    private boolean m_bSplashed;
    private boolean m_bCredited;

    SplashScreen(){
        super();
        m_Tex = new Texture[2];
        m_fMaxTime = 2.0f;

        m_bSplashed = false;
        m_bCredited = false;
    }

    @Override
    public void initialize(){

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
        m_Batch = new SpriteBatch();
        m_Tex[0] = new Texture("Splash.png");
        m_Tex[1] = new Texture("Credits.png");
        m_fTimer = 0.0f;
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.10f, 0.22f, 0.35f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        m_fTimer += delta;
        if (m_fTimer > m_fMaxTime){
            m_fTimer = 0;

            if (!m_bSplashed) {
                m_bSplashed = true;
            }
            else{
                if(!m_bCredited) {
                    m_bCredited = true;
                }
            }
        }

        //TODO: Proper fades
        m_Batch.begin();
        if (!m_bSplashed){
            m_Batch.draw(m_Tex[0], (Gdx.graphics.getWidth()/2)-(m_Tex[0].getWidth()/2), (Gdx.graphics.getHeight()/2)-(m_Tex[0].getHeight()/2));
        }
        else{
            if (!m_bCredited){
                m_Batch.draw(m_Tex[1], (Gdx.graphics.getWidth()/2)-(m_Tex[1].getWidth()/2), (Gdx.graphics.getHeight()/2)-(m_Tex[1].getHeight()/2));
            }
        }
        m_Batch.end();

        if (m_bSplashed && m_bCredited){
            FirstGame.setActiveScreen(new TitleScreen());
        }
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
