import java.awt.*;

public class Player{
    private double x, y;
    private int width, height;
    private Color color;
    private int speed = 5;
    private int maxHealth = 0;
    private int currentHealth = 0;
    private int maxEnergy = 0;
    private double currentEnergy = 0;
    private boolean attacking = false;
    private int attackCoolDown = 0;
    private boolean invincible = false;
    private boolean dodging = false;
    private int dodgeTimer = 0;
    private int dodgeCoolDown = 0;
    public double dodgedx = 0;
    public double dodgedy = 0;
    public double dodgeSpeed = 0;

    Player(int x, int y, int w, int h, Color c, int health) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.maxHealth = health;
        this.currentHealth = 50;
        this.maxEnergy = 100;
        this.currentEnergy = 100;
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
            if (dodgeTimer > 0) {
                x += dodgedx * dodgeSpeed;
                y += dodgedy * dodgeSpeed;
                dodgeTimer -= 1;
                if (dodgeTimer == 0) {
                    dodging = false;
                    invincible = false;
                }
            }
            else {
                x += dx * speed;
                y += dy * speed;
            }

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
        if (attackCoolDown > 0) {
            attackCoolDown -= 1;
            if (attackCoolDown == 0) {
                attacking = false;
            }
        }
        if (dodgeCoolDown > 0) {
            dodgeCoolDown -= 1;
        }
        if (!attacking && !dodging && currentEnergy < maxEnergy) {
            currentEnergy += 0.5;
        }
    }

    void dodge(boolean wPressed, boolean aPressed, boolean sPressed, boolean dPressed) {
        if (!attacking && !dodging && currentEnergy > 0){
            dodging = true;
            dodgeTimer = 10;
            dodgeCoolDown = 20;
            currentEnergy -= 30;
            dodgeSpeed = 10;
        
            dodgedx = 0;
            dodgedy = 0;
        
            if (wPressed) dodgedy -= 1;
            if (aPressed) dodgedx -= 1;
            if (sPressed) dodgedy += 1;
            if (dPressed) dodgedx += 1;
        
            double dxdy = Math.sqrt(dodgedx * dodgedx + dodgedy * dodgedy);
            if (dxdy != 0) {
                dodgedx /= dxdy;
                dodgedy /= dxdy;
            }
        
            invincible = true;
        }
    }

    void getHurt(int damage) {
        if (!invincible) {
            currentHealth -= damage;
        }
    }

    void attack() {
        if (!attacking && !dodging && currentEnergy > 0) {
            attacking = true;
            attackCoolDown = 30;
            currentEnergy -= 20;
        }
    }

    boolean isAttacking() {
        return attacking;
    }

    void draw(Graphics g) {
        g.setColor(color);
        double centerX = getCenterX();
        double centerY = getCenterY();
        double healthPercant = (double) currentHealth / maxHealth;
        double w;
        double h;
        if (dodging) {
            w = width / 1.2;
            h = height / 1.2;
            g.drawOval((int) (centerX - w / 2 - dodgedx * 10), (int) (centerY - w / 2 - dodgedy * 10), (int) w, (int) h);
            w = width / 1.4;
            h = height / 1.4;
            g.drawOval((int) (centerX - w / 2 - dodgedx * 20), (int) (centerY - w / 2 - dodgedy * 20), (int) w, (int) h);
            w = width / 1.6;
            h = height / 1.6;
            g.drawOval((int) (centerX - w / 2 - dodgedx * 30), (int) (centerY - w / 2 - dodgedy * 30), (int) w, (int) h);
        }
        g.drawOval((int) x, (int) y, width, height);
        w = healthPercant * width;
        h = healthPercant * height;
        g.fillOval((int) (centerX - w / 2), (int) (centerY - h / 2), (int) w, (int) h);
    }

    int getMaxHealth() {
        return maxHealth;
    }

    int getHealth() {
        return currentHealth;
    }

    int getMaxEnergy() {
        return maxEnergy;
    }

    int getEnergy() {
        return (int) currentEnergy;
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
