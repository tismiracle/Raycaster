import java.awt.*;

public class Wall {
    int pointX;
    int pointY;
    int point2X;
    int point2Y;
    String wallName;
    Color color;

    public Wall(String wallName, int pointX, int pointY, int point2X, int point2Y, Color color) {
        this.wallName = wallName;
        this.pointX = pointX;
        this.pointY = pointY;

        this.point2X = point2X;
        this.point2Y = point2Y;

        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        g.drawLine(this.pointX, this.pointY, this.point2X, this.point2Y);

    }
}
