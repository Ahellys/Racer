package com.codegym.games.racer.road;

import com.codegym.engine.cell.Game;
import com.codegym.games.racer.GameObject;
import com.codegym.games.racer.PlayerCar;
import com.codegym.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER= RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER=RacerGame.WIDTH-LEFT_BORDER;
    private static final int FIRST_LANE_POSITION=16;
    private static final int FOURTH_LANE_POSITION=44;
    private List<RoadObject> items = new ArrayList<>();
    private static final int PLAYER_CAR_DISTANCE=12;
    private int passedCarsCount=0;

    private RoadObject createRoadObject(RoadObjectType type, int x, int y){
        if (type==RoadObjectType.SPIKE) return new Spike(x,y);
        else if(type==RoadObjectType.DRUNK_CAR) return new MovingCar(x,y);
        return new Car(type,x,y);
    }

    public void draw(Game game){
        for (RoadObject r:items){
            r.draw(game);
        }
    }

    public void move(int boost){
        for (RoadObject r:items){
            r.move(boost+r.speed,items);
        }
        deletePassedItems();
    }

    public boolean checkCrash(PlayerCar car){
        for (RoadObject r:items){
            if (r.isCollision(car)) return true;
        }
        return false;
    }

    public void generateNewRoadObjects(Game game){
        generateSpike(game);
        generateMovingCar(game);
        generateRegularCar(game);
    }

    private void addRoadObject(RoadObjectType type, Game game){
        int x=game.getRandomNumber(FIRST_LANE_POSITION,FOURTH_LANE_POSITION);
        int y=-1*RoadObject.getHeight(type);
        RoadObject item=createRoadObject(type,x,y);
        if (isRoadSpaceFree(item))items.add(item);
    }

    private void generateSpike(Game game){
        if (game.getRandomNumber(100)<10 && !spikeExists()) addRoadObject(RoadObjectType.SPIKE,game);
    }

    private void generateMovingCar(Game game){
        if (game.getRandomNumber(100)<10 && !movingCarExists()) addRoadObject(RoadObjectType.DRUNK_CAR,game);
    }

    private void generateRegularCar(Game game){
        int carTypeNumber = game.getRandomNumber(4);
        if (game.getRandomNumber(100)<30){
         addRoadObject(RoadObjectType.values()[carTypeNumber],game);
        }
    }

    private boolean isRoadSpaceFree(RoadObject object){
        for (RoadObject x:items){
                if(x.isCollisionWithDistance(object,PLAYER_CAR_DISTANCE)) return false;

        }
        return true;
    }
    private void deletePassedItems(){
        List<RoadObject> temp=new ArrayList<>();
        temp.addAll(items);
        for (RoadObject r:temp){
            if (r.y>=RacerGame.HEIGHT){
                items.remove(r);
                if(r.type!=RoadObjectType.SPIKE) passedCarsCount++;
            }

        }
    }

    private boolean spikeExists(){
        for (RoadObject r:items){
            if (r.type==RoadObjectType.SPIKE) return true;
        }
        return false;
    }

    private boolean movingCarExists(){
        for (RoadObject r:items){
            if (r.type==RoadObjectType.DRUNK_CAR) return true;
        }
        return false;
    }

    public int getPassedCarsCount() {
        return passedCarsCount;
    }
}
