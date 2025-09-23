package exam2025;

import uk.ac.leedsbeckett.oop.LBUGraphics;

public class CustomTriangle extends ShapeDriver{
    private int[] sides;


    public CustomTriangle(LBUGraphics turtle, int[] sides) {
        super(turtle);
        this.sides = sides;
    }

    @Override
    public void draw() {
        for (int i = 0; i < 3; i++) {
            turtle.forward(sides[i]);
            turtle.right(120);
        }

    }
}