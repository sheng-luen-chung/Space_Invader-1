import java.awt.*;

public class Explode {
    private double x, y;
    private double centerX, centerY;
    private double width, height;
    private double w, h;
    private Color color;
    private int damage = 20;
    private int maxHealth = 0;
    private int currentHealth = 0;

    Explode(int x, int y, int w, int h, Color c, int health) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.centerX = x + width / 2;
        this.centerY = y + height / 2;
        this.color = c;
        this.maxHealth = health;
        this.currentHealth = health;
    }

    void getHurt(int damage) {
        currentHealth -= damage;
    }

    void draw(Graphics g) {
        g.setColor(Color.BLUE);
        double healthPercant = 1 - (double) currentHealth / maxHealth;
        w = width * Math.exp(healthPercant);
        h = height * Math.exp(healthPercant);
        g.fillOval((int) (centerX - w / 2), (int) (centerY - h / 2), (int) (w), (int) (h));
    }

    int getDamage() {
        return damage;
    }
    
    double getCenterX() {
        return centerX;
    }

    double getCenterY() {
        return centerY;
    }

    double getHealth(){
        return currentHealth;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) w, (int) h);
    }
}
