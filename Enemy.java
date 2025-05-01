import java.awt.*;

public class Enemy{
    private double x, y;
    private int width, height;
    private Color color;
    private int speed = 2;
    private int maxHealth = 0;
    private int currentHealth = 0;

    Enemy() {
        this.x = 100;
        this.y = 100;
        this.width = 40;
        this.height = 40;
        this.color = Color.RED;
        this.maxHealth = 100;
        this.currentHealth = 100;
    }

    Enemy(int x, int y, int w, int h, Color c, int health) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.maxHealth = health;
        this.currentHealth = health;
    }

    void move(double playerX, double playerY) {
        double dx = playerX - getCenterX();
        double dy = playerY - getCenterY();

        double dxdy = Math.sqrt(dx * dx + dy * dy);
        if (dxdy != 0) {
            dx /= dxdy;
            dy /= dxdy;
            x += dx * speed;
            y += dy * speed;
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
        g.drawRect((int) x, (int) y, width, height);
        int centerX = (int) (x + (double) width / 2);
        int centerY = (int) (y + (double) height / 2);
        g.fillRect((int) (centerX - w / 2), (int) (centerY - h / 2), (int) w, (int) h);
    }

    double getCenterX() {
        return x + (double) width / 2; 
    }

    double getCenterY() {
        return y + (double) height / 2; 
    }

    double getHealth(){
        return currentHealth;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
