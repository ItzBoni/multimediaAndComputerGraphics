package Homework02_3;

public class Coordinates {
    float r,theta, x,y;

    public Coordinates(float x, float y, float r, float theta){
        this.x = x;
        this.y = y;
        this.r = r;
        this.theta = theta;
    }

    public void toPolar(){
        this.r = (float) Math.sqrt(((this.x * this.x) + (this.y * this.y)));
        this.theta = (float) Math.toDegrees(Math.atan2(this.y, this.x));

        System.out.printf("Your polar coordinates are: (%f, %f)", this.r, this.theta);
    }

    public void toCartesian() {
        this.x = (float)(this.r * Math.cos(Math.toRadians(theta)));
        this.y = (float)(this.r * Math.sin(Math.toRadians(theta)));

        System.out.printf("Your cartesian coordinates are: (%f, %f)", this.x, this.y);
    }
}
