import java.awt.*;

public class PlayerUI {
    public static void draw(Graphics g, int maxHealth, int currentHealth, int maxEnergy, int currentEnergy) {
        g.setColor(Color.YELLOW);
        double energyPercent = (double) currentEnergy / maxEnergy;
        double w = energyPercent * 100;
        double h = 10;
        g.drawRect((int) (10), (int) (10), (int) 100, (int) 10);
        g.fillRect((int) (10), (int) (10), (int) w, (int) h);
    }
}
