import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MapWindow extends JFrame implements KeyListener {
    private final int screenWidth = 1200;
    private final int screenHeight = 1000;
    private boolean run = true;

    private Player player;
    private Wall wall1;
    private Wall wall2;

    // square
    private Wall wall3;
    private Wall wall4;
    private Wall wall5;
    private Wall wall6;

    private int playerMiddleX;
    private int playerMiddleY;

    private ArrayList<Ray> rays;
    private ArrayList<String> debugTexts;
    private ArrayList<Wall> walls;
    // zmieniłem to na dystans a nie indeks
    private ArrayList<Integer> intersectingRaysIndices;
    private ArrayList<Double> intersectingRaysDistance;
    private ArrayList<String> typeOfIntersection;

    private Debug debug;
    private boolean debugMode;
    private int playerRadius;
    private int numberOfRays;
    private boolean threeDMode;

    public MapWindow() {
        setTitle("My Application");
        setSize(screenWidth, screenHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        debugMode = true;
        threeDMode = false;
        playerRadius = 1000;
//        musi byc wiecej niz 1 ray
        numberOfRays = 120;

        debugTexts = new ArrayList<>();
        debug = new Debug();

        player = new Player("gracz", 300, 300, 40, 40, Math.PI / 2, 0.0);
        playerMiddleX = player.x + (player.width / 2);
        playerMiddleY = player.y + (player.height / 2);

        rays = new ArrayList<>();
        intersectingRaysDistance = new ArrayList<>();
        intersectingRaysIndices = new ArrayList<>();
        typeOfIntersection = new ArrayList<>();

        generateRays();

        //GENERAL WALLS
        // RIGHT WALL
        wall1 = new Wall("wall1",800, 100, 800, 800, Color.BLUE);
        // TOP WALL
        wall2 = new Wall("wall2",800, 100, 200, 100, Color.BLUE);

        //LEFT WALL
        Wall wall6 = new Wall("wall6", 200, 100, 200, 800, Color.BLUE);

        //BOTTOM WALL
        Wall wall7 = new Wall("wall7", 200, 800, 800, 800, Color.BLUE);


        //INSIDE WALLS
        wall3 = new Wall("wall3", 700, 330, 750, 330, Color.yellow);
        wall4 = new Wall("wall4", 700, 330, 700, 360, Color.yellow);
        wall5 = new Wall("wall5", 700, 360, 750, 360, Color.yellow);
//        Wall wall6 = new Wall("wall6", )
//        wall6 = new Wall ("wall6", )

        walls = new ArrayList<>();
        walls.add(wall1);
        walls.add(wall2);
        walls.add(wall3);
        walls.add(wall4);
        walls.add(wall5);
        walls.add(wall6);
        walls.add(wall7);

        updateDebugInfo();

        // Add key listener
        addKeyListener(this);

        // Handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                run = false;
                System.exit(0);
            }
        });

        // Add the custom render panel
        add(new RenderPanel());
//        add(new RenderPanel());
    }
    // TODO COS TU JEST NIE TAK. JEST 150 PROMIENI I TYLKO 1 TRAFIA W BOK SCIANY?
    public void generateRays() {
        for (int i = 0; i < numberOfRays; i++) {
            double offset = (-player.FOV / 2) + (i * (player.FOV / (numberOfRays - 1))); // Evenly distribute rays
//            double offset = (player.FOV / 2) + (i * (player.FOV / (numberOfRays)));
            rays.add(new Ray(i, playerMiddleX, playerMiddleY, playerRadius, player.angle, offset));
        }
    }

    public void updateCenterPlayer() {
        playerMiddleX = player.x + (player.width / 2);
        playerMiddleY = player.y + (player.height / 2);
    }

    public void updateDebugInfo() {
        debugTexts.clear();
        debugTexts.add("Player x: " + playerMiddleX);
        debugTexts.add("Player y: " + playerMiddleY);
        debugTexts.add("Player angle: " + player.angle);

        for (Ray ray : rays) {
            debugTexts.add(ray.rayIndex + " end x: " + ray.xEnd);
            debugTexts.add(ray.rayIndex + " end y: " + ray.yEnd);
        }

        debugTexts.add("Wall1 point x3,y3: " + wall1.pointX + ", " + wall1.pointY);
        debugTexts.add("Wall1 point x4, y4: " + wall1.point2X + ", " + wall1.point2Y);
    }

    public void updateRays() {
        for (Ray ray : rays) {
            System.out.println(playerMiddleX);
            System.out.println(playerMiddleY);
            System.out.println(playerRadius);
            System.out.println(player.angle);

            ray.updateRayPosition(playerMiddleX, playerMiddleY, playerRadius, player.angle);
        }
    }

    private class RenderPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            double distanceFromPlayer = 0;

            super.paintComponent(g); // Clears the screen
            setBackground(Color.LIGHT_GRAY);


            if (!threeDMode) {
                player.draw(g);
            }


            int intersectingRayCount = 0;

            intersectingRaysDistance.clear();
            intersectingRaysIndices.clear();
            typeOfIntersection.clear();

            int rayCounter = 0;
            for (Ray ray : rays) {
                boolean rayIntersects = false;
                double closestDistance = 10000000;

                int intersectionX = 0;
                int intersectionY = 0;
                int xOfClosest = 0;
                int yOfClosest = 0;

                int wallWhichIntersectsX=0;
                int wallWhichIntersects2X=0;
                int wallWhichIntersectsY=0;
                int wallWhichIntersects2Y=0;

                boolean isIntersecting = false;
                int numeratorPx = 0;
                int numeratorPy = 0;
                int denominator = 0;


                if (!threeDMode) {
                    ray.draw(g, Color.green);
                }

                for (Wall wall : walls)
                {
                    denominator = ray.calculateDenominator(ray.xBeginning, ray.yBeginning, ray.xEnd, ray.yEnd, wall.pointX, wall.pointY, wall.point2X, wall.point2Y);
                    numeratorPx = ray.calculateNumeratorPx(ray.xBeginning, ray.yBeginning, ray.xEnd, ray.yEnd, wall.pointX, wall.pointY, wall.point2X, wall.point2Y);
                    numeratorPy = ray.calculateNumeratorPy(ray.xBeginning, ray.yBeginning, ray.xEnd, ray.yEnd, wall.pointX, wall.pointY, wall.point2X, wall.point2Y);
                    intersectionX = ray.calculatePx(numeratorPx,denominator);
                    intersectionY = ray.calculatePy(numeratorPy, denominator);

                    isIntersecting = ray.isIntersecting(numeratorPx, numeratorPy, denominator, ray.xBeginning, ray.yBeginning, ray.xEnd, ray.yEnd, wall.pointX, wall.pointY, wall.point2X, wall.point2Y);


                    if (isIntersecting && (denominator != 0))
                    {
                        // player nie moze widziec dwoch walli jednoczesnie
                        rayIntersects = true;
                        distanceFromPlayer = player.calculateDistanceFromIntersection(player.x, player.y, intersectionX, intersectionY);

                        if (closestDistance > distanceFromPlayer)
                        {
                            closestDistance = distanceFromPlayer;
                            xOfClosest = intersectionX;
                            yOfClosest = intersectionY;
                            wallWhichIntersectsX = wall.pointX;
                            wallWhichIntersects2X = wall.point2X;
                            wallWhichIntersectsY = wall.pointY;
                            wallWhichIntersects2Y = wall.point2Y;
                        }

                    }

                }
                if (rayIntersects)
                {
                    intersectingRayCount++;
                    intersectingRaysIndices.add(ray.rayIndex);
                    intersectingRaysDistance.add(closestDistance);
                    typeOfIntersection.add(ray.checkWhatTypeOfIntersection(wallWhichIntersectsX, wallWhichIntersects2X, wallWhichIntersectsY, wallWhichIntersects2Y ));

                }
                System.out.println(typeOfIntersection);
                if (threeDMode)
                {
                    player.draw3D(g, rays.size(), screenWidth, screenHeight, intersectingRaysDistance, intersectingRaysIndices, typeOfIntersection);
                }

//                dopiero renderuje intersection gdy sprawdzi wszystkie wall'e
                if (!threeDMode) {
                    ray.drawIntersection(g, xOfClosest, yOfClosest);
                }
                rayCounter++;

            }


//            System.out.println("Number of intersecting rays: " + intersectingRayCount);
//            System.out.println(intersectingRaysIndices);


            for (Wall wall : walls) {
                if (!threeDMode) {
                    wall.draw(g);
                }
            }



            if (debugMode) {
                if (!threeDMode) {
                    debug.displayDebugInfo(g, debugTexts, screenWidth - 150);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Move the player based on key presses
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.angleLeft();
                break;
            case KeyEvent.VK_D:
                player.angleRight();
                break;
            case KeyEvent.VK_UP:
                player.moveUp();
                break;
            case KeyEvent.VK_DOWN:
                player.moveDown();
                break;
            case KeyEvent.VK_LEFT:
                player.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                player.moveRight();
                break;
            case KeyEvent.VK_M:
                if (threeDMode == false) {
                    threeDMode = true;
                } else
                {
                    threeDMode = false;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Optional: Handle key release events if needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Optional: Handle typed events (characters like 'a', 'b', etc.)
    }

    public void mainloop() {
        long lastTime = System.nanoTime();
        final double nsPerFrame = 1000000000.0 / 60.0; // 60 FPS

        while (run) {
            long now = System.nanoTime();
            if (now - lastTime >= nsPerFrame) {
                updateCenterPlayer();
                updateRays();
                updateDebugInfo();
                SwingUtilities.invokeLater(this::repaint);
                lastTime = now;
            }
        }
    }

    public static void main(String[] args) {
        MapWindow app = new MapWindow();
        app.setVisible(true); // Make th// e frame visible

        app.mainloop();
    }
}
