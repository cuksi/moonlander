package com.codegym.games.moonlander;

import com.codegym.engine.cell.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private double boost = 0.05;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        
        List<int[][]> listDown = new ArrayList<>();
        listDown.add(ShapeMatrix.FIRE_DOWN_1);
        listDown.add(ShapeMatrix.FIRE_DOWN_2);
        listDown.add(ShapeMatrix.FIRE_DOWN_3);
        
        this.downFire = new RocketFire(listDown);

        List<int[][]> listSide = new ArrayList<>();
        listSide.add(ShapeMatrix.FIRE_SIDE_1);
        listSide.add(ShapeMatrix.FIRE_SIDE_2);
        this.leftFire = new RocketFire(listSide);
        this.rightFire = new RocketFire(listSide);

    
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY -= boost;
        } else {
            speedY += boost;
        }
        y += speedY;

        if (isLeftPressed) {
            speedX -= boost;
            x += speedX;
        } else if (isRightPressed) {
            speedX += boost;
            x += speedX;
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders();
        
        switchFire(isUpPressed,isLeftPressed,isRightPressed);
    }

    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    public boolean isStopped() {
        return speedY < 10 * boost;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    public void land() {
        super.y -=1;
    }
    
    public void crash() {
        super.matrix = ShapeMatrix.ROCKET_CRASH;
    }
    
    
    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        
        if(isUpPressed) {
            
            downFire.x = super.x+(width/2);
            downFire.y = super.y + super.height;
            downFire.show();
        } else if (isLeftPressed) {
            leftFire.x = super.x + width;
            leftFire.y = super.y + height;
            leftFire.show();
        }else if (isRightPressed) {
            rightFire.x = super.x - ShapeMatrix.FIRE_SIDE_1[0].length;
            rightFire.y = super.y + height;
            rightFire.show();
        }
        else {
            downFire.hide();
            leftFire.hide();
            rightFire.hide();
        }
    }
    
    @Override
    public void draw(Game game) {
        
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }
    
    
    
}
