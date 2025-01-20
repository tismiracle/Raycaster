import java.awt.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Ray {
    int rayIndex;
    int xBeginning;
    int yBeginning;
    int xEnd;
    int yEnd;
    double radius;
    double angle;
    double offset;
    int pX;
    int pY;
    double rayAngle;

    public Ray (int rayIndex, int xBeginning, int yBeginning, double radius, double angle, double offset)
    {
        this.rayIndex = rayIndex;
        this.xBeginning = xBeginning;
        this.yBeginning = yBeginning;
        this.radius = radius;
        this.angle = angle;
        this.offset = offset;
    }

    // TODO ROZWALA KOD W PEWNYCH MOMENTACH. TRZEBA ZROBIC Z TEGO DOUBLE, A NIE INT. MOZE WTEDY SIE TO NAPRAWI. JAK NIE TO TRZEBA POMYSLEC O CZYMS INNYM
    public int calculateDenominator(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
    {
        int denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
        return denominator;
    }

    public int calculateNumeratorPx(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
    {
        int numeratorPx = ((x1 * y2 - y1 * x2) * (x3 - x4)) - ((x1 - x2) * (x3 * y4 - y3 * x4));
        return numeratorPx;
    }

    public int calculateNumeratorPy(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
    {
        int numeratorPy = ((x1 * y2 - y1 * x2) * (y3 - y4)) - ((y1 - y2) * (x3 * y4 - y3 * x4));
        return numeratorPy;
    }


    //TO NIE JEST OPTYMALNE, ALE NIE MAM POMYSŁU JAK TO ZROBIĆ
    public String checkWhatTypeOfIntersection(int pointX, int point2X, int pointY, int  point2Y)
    {

        if (pointY == point2Y) {
            return "horizontal";
        } else if (pointX == point2X) {
            return "vertical";
        } else {
            return "diagonal"; // Optional, if your walls could be diagonal
        }

    }

    public int calculatePx(int  numeratorPx, int denominator)
    {
//        System.out.println(numeratorPx/denominator);
        int Px;
        if (denominator != 0) {
            Px = numeratorPx / denominator;
        } else {
            System.out.println("denominator jest 0 w ray: " + this.rayIndex);
            Px = 0;
        }
//        int Px = numeratorPx/denominator;
        return Px;
    }

    public int calculatePy(int numeratorPy, int denominator)
    {
//        System.out.println(numeratorPy/denominator);
        int Py;
        if (denominator != 0) {
            Py = numeratorPy / denominator;
        } else {
            System.out.println("denominator jest 0 w ray: " + this.rayIndex);
            Py = 0;
        }
        return Py;
    }

    public boolean isIntersecting(int numeratorPx, int numeratorPy, int denominator, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int px = numeratorPx / denominator;
        int py = numeratorPy / denominator;

//        boolean onSegment1 = isPointOnSegment(pX, pY, x1, y1, x2, y2);
//        boolean onSegment2 = isPointOnSegment(pX, pY, x3, y3, x4, y4);
        boolean isIntersectingOnSegment1 = px >= Math.min(x1, x2) && px <= Math.max(x1, x2) && py >= Math.min(y1, y2) && py <= Math.max(y1, y2);
        boolean isIntersectingOnSegment2 = px >= Math.min(x3, x4) && px <= Math.max(x3, x4) && py >= Math.min(y3, y4) && py <= Math.max(y3, y4);

        if (isIntersectingOnSegment1 && isIntersectingOnSegment2) {
//            System.out.println("Intersection at: (" + pX + ", " + pY + ")");

            // DRAW INTERSECTION TRZEBA UŻYWAĆ W MAINLOOP. ABY TO ZROBIĆ TRZEBA ZWRACAĆ ODDZIELNIE WARTOŚCI PX I PY W MAINLOOP I WSTAWIĆ DO DRAWINTERSECTION
//            drawIntersection(g, pX, pY);
            return true; // Valid intersection

        }
        else {
            return false;
        }

    }
    public void drawIntersection(Graphics g, int pX, int pY){
        g.setColor(Color.red);
//        g.drawOval(pX - ((pX + 15)/2) ,pY - ((pY + 15)/2), 15, 15)
        g.drawOval(pX - (15/2) ,pY - (15/2), 15, 15);
    }

    public void draw(Graphics g, Color color) {
        g.setColor(color);
        g.drawLine(xBeginning, yBeginning, xEnd, yEnd);
        //g.fillRect(this.x, this.y, this.width, this.height);
    }

    public void updateRayPosition(int playerX, int playerY, double radius, double angle)
    {
        this.xBeginning = playerX;
        this.yBeginning = playerY;

        rayAngle = (angle + this.offset) % (2 * Math.PI);
        if (rayAngle < 0 ) rayAngle += (2 * Math.PI);
        this.xEnd = (int) (this.xBeginning + radius * Math.cos(rayAngle));
        this.yEnd = (int) (this.yBeginning + radius * Math.sin(rayAngle));

    }

}
