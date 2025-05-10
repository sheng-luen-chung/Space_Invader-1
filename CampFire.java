import java.awt.*;

public class CampFire {
    private int x, y;
    private int width = 10;
    private int interactWidth = 50;
    private int height = 10;

    CampFire(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void draw(Graphics g) {
        g.setColor(Color.PINK);
        g.fillOval(x, y, width, height);
    }

    void drawText(Graphics g) {
        g.setColor(Color.PINK);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("按E休息", x - 10, y - 10);
    }
    
    int getCenterX() {
        return (int) (x + (double) width / 2);
    }

    int getCenterY() {
        return (int) (y + (double) height / 2);
    }

    int getWidth() {
        return interactWidth;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
