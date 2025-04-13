import java.awt.*;

public class Player{
    private int x, y, width, height;
    private Color color;
    private int dx = 5;
    private int dy = 20;

    Player(int x, int y, int w, int h, Color c) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
    }

    void move(boolean leftPressed, boolean rightPressed) {
        if (leftPressed && x > 0)
            x -= dx;
        if (rightPressed && x < 560)
            x += dx;
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    int getPosition() {
        return x;
    }

    Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
