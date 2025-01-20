import java.awt.*;
import java.util.ArrayList;

public class Player {
    String playerName;
    int x;
    int y;
    int width;
    int height;
    double FOV;
    double angle;

    public Player (String playerName, int x, int y, int width, int height, double FOV, double angle)
    {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.FOV = FOV;
        this.angle = angle;

    }

    public void moveUp() {
        this.y -= 10; // Move up
    }

    public void moveDown() {
        this.y += 10; // Move down
    }

    public void moveLeft() {
        this.x -= 10; // Move left
    }

    public void moveRight() {
        this.x += 10; // Move right
    }

    public void angleRight()
    {
        this.angle = (this.angle + 0.05) % (2 * Math.PI);
//        this.angle = (this.angle + 0.05);

    }

    public void angleLeft()
    {
//        this.angle = (this.angle - 0.05 + 2 * Math.PI) % (2 * Math.PI);
        this.angle = (this.angle - 0.05) % (2 * Math.PI);
//        this.angle = (this.angle - 0.05);
    }

    public void draw(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    public double calculateDistanceFromIntersection(int playerX, int playerY, int intersectionX, int intersectionY)
    {
        int playerMiddleX = playerX + (this.width / 2);
        int playerMiddleY = playerY + (this.height / 2);
        double calculateX = Math.pow((intersectionX - playerMiddleX), 2);
        double calculateY = Math.pow((intersectionY - playerMiddleY), 2);
        return Math.sqrt(calculateX + calculateY);
    }

    //TODO WORK IN PROGRESS
    public void draw3D(Graphics g, int numberOfRays, int screenWidth, int screenHeight, ArrayList<Double> intersectingRaysDistance, ArrayList<Integer> intersectingRayIndices, ArrayList<String> typeOfIntersection)
    {
        ArrayList<Color> colorToDisplay;
        ArrayList<Double> whereToDisplay;

        int dividedScreen = screenWidth/numberOfRays;
        whereToDisplay = new ArrayList<Double>();
        colorToDisplay = new ArrayList<Color>();

        for (int i = 0; i < numberOfRays; i++)
        {
            whereToDisplay.add(i, 0.0);
            colorToDisplay.add(i, Color.darkGray);
        }

        int ctr = 0;
        Color testColor;
        for (Integer intersectingRayIndex  : intersectingRayIndices) {
            whereToDisplay.set(intersectingRayIndex, intersectingRaysDistance.get(ctr));

            // SPRAWDZA CZY PRZECIECIE LINII JEST HORYZONTALNE CZY WERTYKALNE. NA TEJ ZASADZIE ZMIENIA KOLOR LINII.
            if (typeOfIntersection.get(ctr).equals("horizontal")) {
                colorToDisplay.set(intersectingRayIndex, Color.gray);

                //TODO DOBRZE ZROBIONE TYLKO TRZEBA DOSTOSOWAC KOLORY BY BYLY DOBRZE ZROBIONE.
//                switch ((int) (intersectingRaysDistance.get(ctr)/50))
//                {
//                    case 1:
//                         testColor = new Color(Color.HSBtoRGB(235, 0, 40));
//                        colorToDisplay.set(intersectingRayIndex, testColor);
//                        break;
//                    case 2:
//                        testColor = new Color(Color.HSBtoRGB(235, 0, 60));
//                        colorToDisplay.set(intersectingRayIndex, testColor);
//                        break;
//                    case 3:
//                        testColor = new Color(Color.HSBtoRGB(235, 0, 80));
//                        colorToDisplay.set(intersectingRayIndex, testColor);
//                        break;
//                    case 4:
//                        testColor = new Color(Color.HSBtoRGB(235, 0, 100));
//                        colorToDisplay.set(intersectingRayIndex, testColor);
//                        break;
//                    default:
//                        testColor = Color.GRAY;
//                        break;
//                }
            }
            ctr++;
        }

        //calculate the rendering distance based on distance from intersection
        System.out.println(whereToDisplay);

        int displayPointCounter = 0;
        for (Double displayPoint : whereToDisplay)
        {
            g.setColor(colorToDisplay.get(displayPointCounter));
            if (displayPoint != 0) {
                double calculatedDistance = 15*((screenHeight)/displayPoint);

                g.fillRect((int) Math.ceil(displayPointCounter * (screenWidth / numberOfRays)),screenHeight/4, dividedScreen, (int) (calculatedDistance*2));

            }
            displayPointCounter++;
        }

        System.out.println("Display point counter: " + displayPointCounter);
    }
}

