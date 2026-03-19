package gui;

public class TargetState {
    private volatile int positionX;
    private volatile int positionY;

    public TargetState(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }

    public void setPositionX(int positionX) { this.positionX = positionX; }
    public void setPositionY(int positionY) { this.positionY = positionY; }
}