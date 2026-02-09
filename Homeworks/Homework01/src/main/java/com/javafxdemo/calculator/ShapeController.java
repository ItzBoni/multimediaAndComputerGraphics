package com.javafxdemo.calculator;

public class ShapeController {
    private double area, perimeter;

    protected void calculateSquare(double side) {
        this.area = side * side;
        this.perimeter = side * 4;
    }

    protected void calculateRect(double side1, double side2) {
        this.area = side1 * side2;
        this.perimeter = (side1 * 2) + (side2 * 2);
    }

    protected void calculateTriangle(double base, double height){
        this.area = base * height / 2;
        this.perimeter = base * 3; //I'm assuming an equilateral triangle
    }

    protected void calculateCircle(double r) {
        double PI = 3.14159;
        this.area = PI * r * r;
        this.perimeter = PI * r * 2;
    }

    protected void calculatePentagon(double apo, double side) {
        this.perimeter = side * 5;
        this.area = this.perimeter * apo / 2;
    }

    protected void calculatePentagram(double side) {
        double PENTAGRAM_CONSTANT = 2.633049952; //sqrt(2.5 * (5 - sqrt(5))) pre calculated to make code more efficient.
        this.area = PENTAGRAM_CONSTANT * side * side;
        this.perimeter = side *  10;
    }

    protected void calculateSemiCircle(double r){
        double PI = 3.14159;
        this.area = PI * r * r;
        this.perimeter = (PI * r) + (r * 2);
    }

    public double getArea(){
        return this.area;
    }

    public double getPerimeter(){
        return this.perimeter;
    }
}
