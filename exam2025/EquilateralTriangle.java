package exam2025;


import uk.ac.leedsbeckett.oop.LBUGraphics;

public class EquilateralTriangle extends ShapeDriver{
    private int size;

    public EquilateralTriangle(LBUGraphics turtle, int size) {
        super(turtle);
        this.size = size;
    }

    @Override
    public void draw() {
        /*
            turtle.left(150);
            turtle.forward(size);
            turtle.right(120);
            turtle.forward(size);
            turtle.left(150);
            turtle.left(90);
            turtle.forward(size);
*/

        for (int i = 0; i < 3; i++) {
            turtle.forward(size);
            turtle.right(120);
        }
    }
}