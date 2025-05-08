import java.awt.*;

public class Enemy{
    public double x, y;
    public int width, height;
    public Color color;
    
    public int maxHealth = 0;
    public int currentHealth = 0;
    public int speed = 2;

    public int state = 0;

    public boolean attacking = false;
    public boolean knockBacking = false;

    public double knockBackdx = 0;
    public double knockBackdy = 0;
    public int knockBackSpeed = 0;
    public int knockBackTimer = 0;

    public Enemy() {
        this.x = 100;
        this.y = 100;
        this.width = 40;
        this.height = 40;
        this.color = Color.RED;
        this.maxHealth = 100;
        this.currentHealth = 100;
    }

    public Enemy(int x, int y, int w, int h, Color c, int health) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.maxHealth = health;
        this.currentHealth = health;
    }

    public void move(double playerX, double playerY) {
        switch(state) {
            case 0 -> stateIdle(playerX, playerY);
            case 1 -> stateMove(playerX, playerY);
            case 2 -> stateKnockBack();
        }
    }

    public void stateIdle(double playerX, double playerY) {
        double dx = playerX - getCenterX();
        double dy = playerY - getCenterY();

        double dxdy = Math.sqrt(dx * dx + dy * dy);
        if (dxdy <= 100) {
            state = 1;
        }
    }

    public void stateMove(double playerX, double playerY) {
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

    public void stateKnockBack() {
        if (knockBackTimer > 0){
            x -= knockBackdx * knockBackSpeed;
            y -= knockBackdy * knockBackSpeed;
            knockBackTimer -= 1;
        }
        else {
            state = 1;
            knockBacking = false;
        }
    }

    public void getHurt(int damage) {
        currentHealth -= damage;
    }

    public void knockBack(double x, double y, int s, int t) {
        knockBackdx = getCenterX() - x;
        knockBackdy = getCenterY() - y;
        double dxdy = Math.sqrt(knockBackdx * knockBackdx + knockBackdy * knockBackdy);
        knockBackdx /= dxdy;
        knockBackdy /= dxdy;
        knockBackSpeed = s;
        knockBackTimer = t;
        state = 2;
        knockBacking = true;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        double healthPercant = (double) currentHealth / maxHealth;
        double w = healthPercant * width;
        double h = healthPercant * height;
        g.drawRect((int) x, (int) y, width, height);
        int centerX = (int) (x + (double) width / 2);
        int centerY = (int) (y + (double) height / 2);
        g.fillRect((int) (centerX - w / 2), (int) (centerY - h / 2), (int) w, (int) h);
    }

    public double getCenterX() {
        return x + (double) width / 2; 
    }

    public double getCenterY() {
        return y + (double) height / 2; 
    }

    public double getHealth(){
        return currentHealth;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
