import java.awt.*;

public class Player{
    private double x, y;
    private final int width, height;
    private final Color color;

    private int speed = 5;
    private int currentHealth = 0;
    private double currentEnergy = 0;
    private int exp = 100;

    private boolean invincible = false;
    private boolean attacking = false;
    private int attackCoolDown = 0;

    private boolean dodging = false;
    private int dodgeTimer = 0;
    private int dodgeCoolDown = 0;
    public double dodgedx = 0;
    public double dodgedy = 0;
    public double dodgeSpeed = 0;
    
    private boolean knockBacking = false;
    public double knockBackdx = 0;
    public double knockBackdy = 0;
    public int knockBackSpeed = 0;
    public int knockBackTimer = 0;

    Player(int x, int y, int w, int h, Color c, int health, int energy) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.currentHealth = health;
        this.currentEnergy = energy;
    }

    void move(boolean wPressed, boolean aPressed, boolean sPressed, boolean dPressed) {
        if (!knockBacking) {
            if (!dodging) {
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
                    x += dx * Constants.playerActualSpeed;
                    y += dy * Constants.playerActualSpeed;
                }
            }
            else{
                if (dodgeTimer > 0) {
                    x += dodgedx * dodgeSpeed;
                    y += dodgedy * dodgeSpeed;
                    dodgeTimer -= 1;
                }
                else {
                    dodging = false;
                    invincible = false;
                }
            }
        }
        else {
            if (knockBackTimer > 0){
                x += knockBackdx * knockBackSpeed;
                y += knockBackdy * knockBackSpeed;
                knockBackTimer -= 1;
            }
            else {
                knockBacking = false;
            }
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

        if (attackCoolDown > 0) {
            attackCoolDown -= 1;
            if (attackCoolDown == 0) {
                attacking = false;
            }
        }
        if (dodgeCoolDown > 0) {
            dodgeCoolDown -= 1;
        }
        if (!attacking && !dodging && currentEnergy < Constants.playerActualEnergy) {
            currentEnergy += 0.5 * Constants.playerActualDEX;
        }
    }

    void dodge(boolean wPressed, boolean aPressed, boolean sPressed, boolean dPressed) {
        if (!attacking && !dodging && !knockBacking && currentEnergy > 0){
            dodging = true;
            dodgeTimer = 10;
            dodgeCoolDown = 50;
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

    public void knockBack(double x, double y, int s, int t) {
        knockBackdx = getCenterX() - x;
        knockBackdy = getCenterY() - y;
        double dxdy = Math.sqrt(knockBackdx * knockBackdx + knockBackdy * knockBackdy);
        knockBackdx /= dxdy;
        knockBackdy /= dxdy;
        knockBackSpeed = s;
        knockBackTimer = t;
        knockBacking = true;
    }

    void attack() {
        if (!attacking && !dodging && !knockBacking && currentEnergy > 0) {
            attacking = true;
            attackCoolDown = 30;
            currentEnergy -= 20;
        }
    }

    void getHurt(int damage) {
        if (!invincible) {
            currentHealth -= damage;
        }
    }

    boolean isAttacking() {
        return attacking;
    }

    boolean isKnockBacking() {
        return knockBacking;
    }

    boolean isInvincible() {
        return invincible;
    }

    void draw(Graphics g) {
        g.setColor(color);
        double centerX = getCenterX();
        double centerY = getCenterY();
        double healthPercant = (double) currentHealth / Constants.playerActualHP;
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

    int getHealth() {
        return currentHealth;
    }

    void restoreHealth() {
        currentHealth = (int) Constants.playerActualHP;
    }

    void restoreEnergy() {
        currentEnergy = (int) Constants.playerActualEnergy;
    }

    void increaseExp(int exp) {
        this.exp += exp;
    }

    void decreaseExp(int exp) {
        this.exp -= exp;
    }

    int getExp() {
        return exp;
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

    int getWidth() {
        return width;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
