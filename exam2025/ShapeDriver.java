package exam2025;


import uk.ac.leedsbeckett.oop.LBUGraphics;

public abstract class ShapeDriver {

    protected LBUGraphics turtle;

    public ShapeDriver(LBUGraphics turtle) {
        this.turtle = turtle;
    }

    public abstract void draw();
}