import java.awt.*;

public class Bullet{
    private double x, y;
    private int width, height;
    private Color color;
    private int speed = 10;
    private int damage = 20;
    private double  dx = 0;
    private double dy = 0;

    Bullet(int x, int y, int w, int h, Color c, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.dx = dx;
        this.dy = dy;
    }

    void move() {
        x += dx * speed;
        y += dy * speed;
    }

    int getDamage() {
        return damage;
    }

    boolean outOfScreen() {
        return (x < 0 || x > Constants.FRAMEWIDTH || y < 0 || y > Constants.FRAMEHEIGHT);
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) x, (int) y, width, height);
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
