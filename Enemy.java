import java.awt.*;

public class Enemy{
    int x, y, width, height;
    private Color color;
    private int dx = 1;
    private int dy = 10;

    Enemy(int x, int y, int w, int h, Color c) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
    }

    void move() {
        x += dx;
        if (x < 0 || x > 560) {
            dx = -dx;
            y += dy;
        }
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
