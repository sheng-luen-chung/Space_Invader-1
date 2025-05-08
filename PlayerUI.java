import java.awt.*;

public class PlayerUI {
    public static void draw(Graphics g, int maxHealth, int currentHealth, int maxEnergy, int currentEnergy) {
        double energyPercent = (double) currentEnergy / maxEnergy;
        double healthPercent = (double) currentHealth / maxHealth;
        double w = healthPercent * 100;
        double h = 10;
        g.setColor(Color.RED);
        g.drawRect((int) (10), (int) (10), (int) maxHealth, (int) h);
        g.fillRect((int) (10), (int) (10), (int) w, (int) h);
        w = energyPercent * 100;
        g.setColor(Color.YELLOW);
        g.drawRect((int) (10), (int) (30), (int) maxEnergy, (int) h);
        g.fillRect((int) (10), (int) (30), (int) w, (int) h);
    }
}
