import java.awt.*;

public class Player{
    private double x, y;
    private int width, height;
    private Color color;
    private int speed = 5;
    private int maxHealth = 0;
    private int currentHealth = 0;

    Player(int x, int y, int w, int h, Color c, int health) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.maxHealth = health;
        this.currentHealth = health;
    }

    void move(boolean wPressed, boolean aPressed, boolean sPressed, boolean dPressed) {
        double dx = 0;
        double dy = 0;

        if (wPressed) dy -= 1;
        if (aPressed) dx -= 1;
        if (sPressed) dy += 1;
        if (dPressed) dx += 1;

        double dxdy = Math.sqrt(dx * dx + dy * dy);
        if (dxdy != 0) {
            dx /= dxdy;
            dy /= dxdy;
            x += (int)(dx * speed);
            y += (int)(dy * speed);
            if (x > Constants.FRAMEWIDTH - width) {
                x = Constants.FRAMEWIDTH - width;
            }
            else if (x < 0) {
                x = 0;
            }
            if (y > Constants.FRAMEHEIGHT - height) {
                y = Constants.FRAMEHEIGHT - height;
            }
            else if (y < 0) {
                y = 0;
            }
        }
    }

    void getHurt(int damage) {
        currentHealth -= damage;
    }

    void draw(Graphics g) {
        g.setColor(color);
        double healthPercant = (double) currentHealth / maxHealth;
        double w = healthPercant * width;
        double h = healthPercant * height;
        g.drawOval((int) x, (int) y, width, height);
        double centerX = getCenterX();
        double centerY = getCenterY();
        g.fillOval((int) (centerX - w / 2), (int) (centerY - h / 2), (int) w, (int) h);
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }
    
    double getCenterX() {
        return (int) (x + (double) width / 2);
    }

    double getCenterY() {
        return y + (double) height / 2;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
