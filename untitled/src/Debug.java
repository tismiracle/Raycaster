import java.awt.*;
import java.util.ArrayList;

public class Debug {


    public void displayDebugInfo(Graphics g, ArrayList<String> debugTexts, int xPosition)
    {
        int x = xPosition;
        int y = 50;
        g.setColor(Color.BLACK);
        for (String debugText: debugTexts)
        {
            g.drawString(debugText, x, y);
            y += 15;
        }
        debugTexts.clear();

    }
}
