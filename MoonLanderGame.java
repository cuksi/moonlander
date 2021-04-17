package com.codegym.games.moonlander;

import com.codegym.engine.cell.*;

public class MoonLanderGame extends Game {
    
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private GameObject platform;
    private boolean isGameStopped;
    private int score;
    
    
    @Override
    public void initialize() {
        
        setScreenSize(WIDTH,HEIGHT);
        
        createGame();
        
        showGrid(false);
        
    }
    
    @Override
    public void onTurn (int turn){
        
        rocket.move(isUpPressed,isLeftPressed,isRightPressed);
        if (score > 0) {
            score -=1;
        }
        check();
        setScore(score);
        drawScene();
    }
    
    @Override
    public void setCellColor(int x, int y, Color color) {
        
        if((x>0 && x<64) && (y>0 && y <64)) {
            super.setCellColor(x, y, color);
        }
        
    }
    
    @Override
    public void onKeyPress (Key key) {
        if(key == Key.UP) {
            isUpPressed = true;
        } else if (key == Key.LEFT) {
            isLeftPressed = true;
            isRightPressed = false;
        }else if (key == Key.RIGHT) {
            isRightPressed = true;
            isLeftPressed = false;
        } else if (key == Key.SPACE && isGameStopped) {
            createGame();
        }
        
        if (key == Key.SPACE && isGameStopped) {
                createGame();
            }
    }
    
    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.UP) {
            isUpPressed = false;
        }else if(key == Key.LEFT) {
            isLeftPressed = false;
        } else if (key == Key.RIGHT) {
            isRightPressed = false;
        }
    }
    
    private void createGame() {
        
        createGameObjects();

        drawScene();
        
        setTurnTimer(50);
        
        score = 1000;
        
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        
        isGameStopped = false;
    }
    
    private void drawScene() {
        
        for(int x =0; x < WIDTH; x++) {
            for(int y=0; y < HEIGHT; y++) {
                
                setCellColor(x,y,Color.BLACK);
                
            }
        }
        
        rocket.draw(this);
        
        landscape.draw(this);
        
    }
    
    private void createGameObjects() {
        
        rocket = new Rocket((WIDTH/2),0);
        
        landscape = new GameObject(0,25,ShapeMatrix.LANDSCAPE);
        
        platform = new GameObject (23, MoonLanderGame.HEIGHT-1, ShapeMatrix.PLATFORM);
        
    }
    
    private void check() {
        
        boolean landscapeCheck = rocket.isCollision(landscape);
        boolean platformCheck = rocket.isCollision(platform);
        
        boolean stoppedCheck = rocket.isStopped();
        
        if(landscapeCheck && !stoppedCheck) {
            gameOver();
        }else if (platformCheck && stoppedCheck) {
            win();
        }
        
    }
    
    private void win() {
        
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.BLUE, "EAGLE HAS LANDED, GOOD JOB! Score = "+ score,Color.GREEN, 30 );
        stopTurnTimer();
    }
    
    private void gameOver() {
        
        rocket.crash();
        isGameStopped = true;
        score = 0;
        showMessageDialog(Color.YELLOW, "Orao pao, orao pao! Score = " + score,Color.RED, 30);
        
        stopTurnTimer();
        
    }
    
    
    
    
    
    
}