package br.com.taticalzero.snakegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.bootstrap.DOMImplementationRegistry;

import java.util.Random;

public class GameScreen implements Screen, GestureDetector.GestureListener {
    private Game game;
    private Viewport viewport;
    private SpriteBatch batch;

    private Texture textCorpo;
    private Texture textFundo;
    private Texture textPonto;

    private Vector2 toque;

    private Array<Vector2>pontos;
    private float timeToNext;
    private Random rand;

    private int estado;

    private boolean[][] corpo;
    private Array<Vector2> partes;

    private int direcao; // 1 - cima 2-direita 3-baixo 4- esquerda

    private float timeToMove;


    public GameScreen(Game game){
        this.game = game;
    }
    @Override
    public void show() {

        batch = new SpriteBatch();
        viewport = new FitViewport(100 , 100);
        viewport.apply();

        gerarTextura();
        init();

        toque = new Vector2();
        rand = new Random();

        Gdx.input.setInputProcessor(new GestureDetector(this));

    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(0.29f,0.894f,0.373f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(textFundo, 0 ,0,100,100);
        for(Vector2 parte:partes){
            batch.draw(textCorpo,parte.x*5, parte.y*5 , 5,5);
        }
        for(Vector2 ponto:pontos){
            batch.draw(textPonto,ponto.x*5, ponto.y*5 , 5,5);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height , true);
    }
    private void update(float delta){
        if(estado == 0){
            timeToMove -= delta;
            if(timeToMove <= 0){
                timeToMove = 0.4f;

                Gdx.app.log("Log","mova-se");

                int x1,x2,y1,y2;

                x1 = (int)partes.get(0).x;
                y1 = (int)partes.get(0).y;
                corpo[x1][y1] = false;

                x2 = x1;
                y2 = y1;

                switch (direcao){
                    case 1:
                        y1++;
                        break;
                    case 2:
                        x1++;
                        break;
                    case 3:
                        y1--;
                        break;
                    case 4:
                        x1--;
                        break;
                }
                if (x1 < 0 || y1 <0 || x1 > 19 || y1 > 19 || corpo[x1][y1]){
                    // teste de colisão e perca !!
                    estado = 1;
                    return;
                }
                //coleta de pontos
                for(int j = 0 ; j < pontos.size; j++){
                    if(pontos.get(j).x == x1 && pontos.get(j).y == y1){
                        pontos.removeIndex(j);
                        partes.insert(0 , new Vector2(x1,y1));
                        corpo[x1][y1] = true;
                        corpo[x2][y2] = true;
                        return;
                    }
                }
                partes.get(0).set(x1,y1);
                corpo[x1][y1] = true;

                for(int i = 1; i<partes.size; i++){
                    x1 = (int)partes.get(i).x;
                    y1 = (int)partes.get(i).y;

                    partes.get(i).set(x2,y2);
                    corpo[x2][y2] = true;

                    x2 = y1;
                    y2 = y1;
                }

            }
            timeToNext -= delta;
            if (timeToNext <= 0){
                int x = rand.nextInt(20);
                int y = rand.nextInt(20);
                if(!corpo[x][y]){
                    pontos.add(new Vector2(x ,y));
                    timeToNext = 5f;
                }
            }
        }
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        viewport.unproject(toque.set(velocityX,velocityY));
        Gdx.app.log("Log", velocityX + " " + velocityY + " " + toque.x + " " + toque.y);
        if(estado == 0){
            if(Math.abs(toque.x) > Math.abs(toque.y)) toque.y = 0;
            else toque.x = 0;

            if(toque.x > 50 && direcao != 4 ){
                direcao = 2;
            }else if(toque.y > 50 && direcao != 3){
                direcao = 1;
            }else if(toque.x > -50 && direcao != 2){
                direcao = 4;
            }else if(toque.y > -50 && direcao !=1){
                direcao = 3;
            }
        }
        return true;
    }
    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(estado == 1) game.setScreen(new MainScreen(game));
        return false;
    }

    private void init(){
        corpo = new boolean[20][20];
        partes = new Array<Vector2>();

        partes.add(new Vector2(6,5));
        corpo[6][5] = true;

        partes.add(new Vector2(5,5));
        corpo[5][5] = true;

        direcao = 2;
        timeToMove = 0.4f;

        pontos = new Array<Vector2>();

        timeToNext = 3f;

        estado = 0;


    }
    private void gerarTextura(){
        Pixmap pixmap = new Pixmap(64,64,Pixmap.Format.RGBA8888);
        pixmap.setColor(1f,1f,1f,1f);
        pixmap.fillRectangle(0,0,64,64);
        textCorpo = new Texture(pixmap);
        pixmap.dispose();

        Pixmap pixmap2 = new Pixmap(64,64,Pixmap.Format.RGBA8888);
        pixmap2.setColor(0.29f,0.784f,0.373f,0.5f);
        pixmap2.fillRectangle(0,0,64,64);
        textFundo = new Texture(pixmap2);
        pixmap2.dispose();

        Pixmap pixmap3 = new Pixmap(64,64,Pixmap.Format.RGBA8888);
        pixmap3.setColor(1f,1f,1f,1f);
        pixmap3.fillCircle(32,32,32);
        textPonto = new Texture(pixmap3);
        pixmap3.dispose();
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
    public boolean longPress(float x, float y) {
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
