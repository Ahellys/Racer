package com.codegym.games.racer;


import com.codegym.games.racer.road.RoadManager;

public class PlayerCar extends GameObject {
    private static int playerCarHeight=ShapeMatrix.PLAYER.length;
    public int speed=1;
    private Direction direction;

    public PlayerCar() {
        super(RacerGame.WIDTH/2+2,RacerGame.HEIGHT-playerCarHeight-1,ShapeMatrix.PLAYER);
    }

    public void move(){

      if (this.direction==Direction.LEFT){
          this.x--;
      }
      else if(this.direction==Direction.RIGHT){
          this.x++;

      }
        if(this.x< RoadManager.LEFT_BORDER) this.x=RoadManager.LEFT_BORDER;
        else if (this.x>RoadManager.RIGHT_BORDER) this.x=RoadManager.RIGHT_BORDER-this.width;
    }

    public void stop(){
        this.matrix=ShapeMatrix.PLAYER_DEAD;
    }

    public void setDirection(Direction direction){
        this.direction=direction;
    }

    public Direction getDirection(){
        return this.direction;
    }

}
