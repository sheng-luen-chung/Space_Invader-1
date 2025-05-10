import java.awt.*;

public class Coin {
    private double x, y;
    private final int width = 10;
    private final int height = 10;
    private final int exp;
    private final Color color = Color.YELLOW;
    private int[] xPoints = new int[4];
    private int[] yPoints = new int[4];
    private double dx, dy;
    
    Coin(double x, double y, int exp) {
        this.x = x;
        this.y = y;
        this.exp = exp;
        this.dx = Math.random() / 3;
        this.dy = Math.random() / 3;
    }

    void move() {
        x += dx;
        y += dy;
        xPoints[0] = (int) (x - width / 2);
        xPoints[1] = (int) (x);
        xPoints[2] = (int) (x + width / 2);
        xPoints[3] = (int) (x);
        yPoints[0] = (int) (y);
        yPoints[1] = (int) (y - width / 2);
        yPoints[2] = (int) (y);
        yPoints[3] = (int) (y + width / 2);
    }

    void draw(Graphics g) {
        move();
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 4);
    }

    int getExp() {
        return exp;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
