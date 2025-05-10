import java.awt.*;

public class PlayerUI {
    public static void draw(Graphics g, int maxHealth, int currentHealth, int maxEnergy, int currentEnergy, int exp) {
        double h = 10;
        g.setColor(Color.RED);
        g.drawRect((int) (10), (int) (10), (int) maxHealth, (int) h);
        g.fillRect((int) (10), (int) (10), (int) currentHealth, (int) h);
        g.setColor(Color.YELLOW);
        g.drawRect((int) (10), (int) (30), (int) maxEnergy, (int) h);
        g.fillRect((int) (10), (int) (30), (int) currentEnergy, (int) h);

        String expText = "EXP: " + exp;
        g.setColor(Color.WHITE);
        Font font = new Font("SansSerif", Font.BOLD, 14);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        Rectangle clip = g.getClipBounds();
        int x = clip.width - fm.stringWidth(expText) - 10;
        int y = clip.height - fm.getHeight() + fm.getAscent() - 10;

        g.drawString(expText, x, y);
    }
}
