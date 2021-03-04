package com.codegym.games.racer;

import com.codegym.engine.cell.*;
import com.codegym.games.racer.road.RoadManager;

public class RacerGame extends Game {
    public static final int HEIGHT=64;
    public static final int WIDTH=64;
    public static final int CENTER_X=WIDTH/2;
    public static final int ROADSIDE_WIDTH=14;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private FinishLine finishLine;
    private static final int RACE_GOAL_CARS_COUNT=40;
    private ProgressBar progressBar;
    private int score;


    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH,HEIGHT);
        createGame();
    }

    private void createGame(){
        roadMarking=new RoadMarking();
        player= new PlayerCar();
        roadManager= new RoadManager();
        finishLine= new FinishLine();
        progressBar= new ProgressBar(RACE_GOAL_CARS_COUNT);
        isGameStopped=false;
        score=3500;
        drawScene();
        setTurnTimer(40);
    }
    private void win(){
        isGameStopped=true;
        showMessageDialog(Color.GREENYELLOW,"Now that's a pilot!!",Color.BLACK,75);
        stopTurnTimer();
    }

    private void gameOver(){
        isGameStopped=true;
        showMessageDialog(Color.WHITE,"BOOM",Color.RED,75);
        stopTurnTimer();
        player.stop();
    }

    private void moveAll(){
        roadMarking.move(player.speed);
        player.move();
        roadManager.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
        finishLine.move(player.speed);
    }

    private void drawScene(){
        drawField();
        finishLine.draw(this);
        roadMarking.draw(this);
        player.draw(this);
        roadManager.draw(this);
        progressBar.draw(this);

    }

    private void drawField(){
        for (int y=0;y<HEIGHT;y++){
            for (int x=0;x<WIDTH;x++){
                if (x<ROADSIDE_WIDTH) setCellColor(x,y,Color.SANDYBROWN);
                else if (x<CENTER_X) setCellColor(x,y,Color.DARKGREY);
                else if (x==CENTER_X) setCellColor(x,y,Color.WHITE);
                else if (x<(WIDTH-ROADSIDE_WIDTH)) setCellColor(x,y,Color.DARKGREY);
                else setCellColor(x,y,Color.SANDYBROWN);
            }
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped && key==Key.SPACE) createGame();
        else if (key==Key.LEFT)player.setDirection(Direction.LEFT);
        else if (key==Key.RIGHT) player.setDirection(Direction.RIGHT);
        if (key==Key.UP) player.speed=2;
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key==Key.UP)player.speed=1;
        if (key==Key.LEFT && player.getDirection()==Direction.LEFT)player.setDirection(Direction.NONE);
        else if (key==Key.RIGHT && player.getDirection()==Direction.RIGHT ) player.setDirection(Direction.NONE);
    }

    @Override
    public void onTurn(int step) {
        if(roadManager.getPassedCarsCount()>=RACE_GOAL_CARS_COUNT) finishLine.show();
        if(roadManager.checkCrash(player)) gameOver();
        else if(finishLine.isCrossed(player)) win();
        else {
            moveAll();
            roadManager.generateNewRoadObjects(this);
        }
        score-=5;
        setScore(score);
        drawScene();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x>=0 && x<WIDTH && y>=0 && y<HEIGHT)
            super.setCellColor(x, y, color);
    }
}
