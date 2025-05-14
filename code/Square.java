import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Square extends Enemy {
    public double centerX = 0;
    public double centerY = 0;
    public double dx = 0;
    public double dy = 0;
    public double bulletDx = 0;
    public double bulletDy = 0;
    public int[] xPoints = new int[4];
    public int[] yPoints = new int[4];

    public double attackdx = 0;
    public double attackdy = 0;
    public int attackSpeed = 0;
    public int attackTimer = 0;
    public int attackStartTime = 0;
    public int attackEndTime = 0;
    public int attackCoolDown = 0;

    List<Bullet> bulletsToAdd;

    Square(int x, int y, int w, int h, Color c, int attackDamage, int health, int detectZone) {
        super(x, y, w, h, c, attackDamage, health, detectZone);
        this.centerX = x + w / 2.0;
        this.centerY = y + h / 2.0;
        for (int i = 0; i < 4; i++) {
            double currentAngle = i * Math.PI / 2;
            xPoints[i] = (int) (centerX + Math.cos(currentAngle) * height / Math.sqrt(2));
            yPoints[i] = (int) (centerY + Math.sin(currentAngle) * height / Math.sqrt(2));
        }
        this.bulletsToAdd = new ArrayList<>();
    }

    @Override
    public void move(double playerX, double playerY) {
        switch(state) {
            case 0 -> stateIdle(playerX, playerY);
            case 1 -> stateMove(playerX, playerY);
            case 2 -> stateKnockBack();
            case 3 -> stateAttack(playerX, playerY);
        }
        if (getHurtTimer > 0) {
            getHurtTimer -= 1;
        }
        else {
            getHurting = false;
        }
    }

    @Override
    public void stateIdle(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        dx = playerX - centerX;
        dy = playerY - centerY;

        double dxdy = Math.sqrt(dx * dx + dy * dy);
        if (dxdy <= detectZone) {
            state = 1;
        }
    }

    @Override
    public void stateMove(double playerX, double playerY) {
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
        if (dxdy <= 100) {
            state = 3;
        }
        rotate();
    }

    @Override
    public void stateKnockBack() {
        color = oriColor;
        if (knockBackTimer > 0){
            centerX += knockBackdx * knockBackSpeed;
            centerY += knockBackdy * knockBackSpeed;
            knockBackTimer -= 1;
        }
        else {
            state = 1;
            knockBacking = false;
        }
        rotate();
    }

    @Override
    public void stateAttack(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        bulletDx = playerX - centerX;
        bulletDy = playerY - centerY;
        double dxdy = Math.sqrt(bulletDx * bulletDx + bulletDy * bulletDy);
        if (dxdy != 0) {
            bulletDx /= dxdy;
            bulletDy /= dxdy;
        }
        Bullet b = new Bullet((int) getCenterX(), 
                              (int) getCenterY(), 
                              20, 20, 
                              Color.YELLOW, 
                              bulletDx, 
                              bulletDy, 
                              10, 
                              "Enemy");
        bulletsToAdd.add(b);
        rotate();
    }

    @Override
    public void rotate() {
        double angle;
        double radius = height / Math.sqrt(2);
    
        if (knockBacking) {
            angle = Math.atan2(dy, dx);
        } else if (attacking) {
            angle = Math.atan2(attackdy, attackdx);
        } else {
            angle = Math.atan2(dy, dx);
        }
    
        for (int i = 0; i < 4; i++) {
            double currentAngle = angle + i * Math.PI / 2;
            xPoints[i] = (int) (centerX + Math.cos(currentAngle) * radius);
            yPoints[i] = (int) (centerY + Math.sin(currentAngle) * radius);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        double healthPercant = (double) currentHealth / maxHealth;
        g.drawPolygon(xPoints, yPoints, 4);
        int[] _xPoints = {
            (int) (centerX + (xPoints[0] - centerX) * healthPercant),
            (int) (centerX + (xPoints[1] - centerX) * healthPercant),
            (int) (centerX + (xPoints[2] - centerX) * healthPercant),
            (int) (centerX + (xPoints[3] - centerX) * healthPercant)
        };
        int[] _yPoints = {
            (int) (centerY + (yPoints[0] - centerY) * healthPercant),
            (int) (centerY + (yPoints[1] - centerY) * healthPercant),
            (int) (centerY + (yPoints[2] - centerY) * healthPercant),
            (int) (centerY + (yPoints[3] - centerY) * healthPercant)
        };
        g.fillPolygon(_xPoints, _yPoints, 4);
    }
    
    @Override
    public double getCenterX() {
        return centerX;
    }

    @Override
    public double getCenterY() {
        return centerY;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (centerX - width / 2), (int) (centerY - height / 2), width, height);
    }
}
