import java.awt.*;

public class Triangle extends Enemy{
    private double x, y;
    private double centerX = 0;
    private double centerY = 0;
    private int width, height;
    private Color color;
    private int speed = 2;
    private int maxHealth = 0;
    private int currentHealth = 0;
    private double dx = 0;
    private double dy = 0;
    private int[] xPoints = new int[3];
    private int[] yPoints = new int[3];

    Triangle(int x, int y, int w, int h, Color c, int health) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.maxHealth = health;
        this.currentHealth = health;
        this.xPoints = new int[] {
            (int) (x),
            (int) (x + (double) w / 2),
            (int) (x + w)
        };
        this.yPoints = new int[] {
            (int) (y + h),
            (int) (y),
            (int) (y + h)
        };
    }

    void move(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        dx = playerX - centerX;
        dy = playerY - centerY;

        double dxdy = Math.sqrt(dx * dx + dy * dy);
        if (dxdy != 0) {
            dx /= dxdy;
            dy /= dxdy;
            centerX += dx * speed;
            centerY += dy * speed;
        }
        rotateTri();
    }
    
    void rotateTri() {
        double angle = Math.atan2(dy, dx);

        double radius = height * 2.0 / 3;

        xPoints[0] = (int) (centerX + dx * radius);
        yPoints[0] = (int) (centerY + dy * radius);
        
        xPoints[1] = (int) (centerX + Math.cos(angle + Math.PI / 3 * 2) * radius);
        yPoints[1] = (int) (centerY + Math.sin(angle + Math.PI / 3 * 2) * radius);
        
        xPoints[2] = (int) (centerX + Math.cos(angle - Math.PI / 3 * 2) * radius);
        yPoints[2] = (int) (centerY + Math.sin(angle - Math.PI / 3 * 2) * radius);
    }

    void getHurt(int damage) {
        currentHealth -= damage;
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, 3);
        double healthPercant = (double) currentHealth / maxHealth;
        int[] _xPoints = {
            (int) (centerX + (xPoints[0] - centerX) * healthPercant),
            (int) (centerX + (xPoints[1] - centerX) * healthPercant),
            (int) (centerX + (xPoints[2] - centerX) * healthPercant)
        };
        int[] _yPoints = {
            (int) (centerY + (yPoints[0] - centerY) * healthPercant),
            (int) (centerY + (yPoints[1] - centerY) * healthPercant),
            (int) (centerY + (yPoints[2] - centerY) * healthPercant)
        };
        g.fillPolygon(_xPoints, _yPoints, 3);
    }

    double getCenterX() {
        int xTotal = 0;
        for (int a = 0; a < 3; a++) {
            xTotal += xPoints[a];
        }
        return xTotal / 3.0;
    }

    double getCenterY() {
        int yTotal = 0;
        for (int a = 0; a < 3; a++) {
            yTotal += yPoints[a];
        }
        return yTotal / 3.0;
    }

    double getHealth(){
        return currentHealth;
    }

    Rectangle getBounds() {
        return new Rectangle((int) centerX, (int) centerY, width, height);
    }
}
