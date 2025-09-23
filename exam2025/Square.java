package exam2025;

import uk.ac.leedsbeckett.oop.LBUGraphics;

public class Square extends ShapeDriver {

    private int length;

    public Square(LBUGraphics turtle, int length) {
        super(turtle);
        this.length = length;
    }

    @Override
    public void draw() {
        for (int i = 0; i < 4; i++) {
            turtle.forward(length);
            turtle.left(90);
        }

    }
}