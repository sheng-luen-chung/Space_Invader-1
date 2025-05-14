import java.awt.*;

public class Triangle extends Enemy{
    public double centerX = 0;
    public double centerY = 0;
    public double dx = 0;
    public double dy = 0;
    public int[] xPoints = new int[3];
    public int[] yPoints = new int[3];

    public double attackdx = 0;
    public double attackdy = 0;
    public int attackSpeed = 0;
    public int attackTimer = 0;
    public int attackStartTime = 0;
    public int attackEndTime = 0;
    public int attackCoolDown = 0;

    Triangle(int x, int y, int w, int h, Color c, int attackDamage, int health, int detectZone) {
        super(x, y, w, h, c, attackDamage, health, detectZone);
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
        if (!attacking) {
            centerX = getCenterX();
            centerY = getCenterY();
            attackdx = playerX - centerX;
            attackdy = playerY - centerY;
            double dxdy = Math.sqrt(attackdx * attackdx + attackdy * attackdy);
            attackdx /= dxdy;
            attackdy /= dxdy;
            attackSpeed = 10;
            attackStartTime = Constants.TRIANGLEATTACKSTARTTIME;
            attackEndTime = Constants.TRIANGLEATTACKENDTIME;
            attackCoolDown = Constants.TRIANGLEATTACKCOOLDOWN;
            attacking = true;
        }
        else {
            if (attackStartTime > 0) {
                if (attackStartTime % 10 >= 5) {
                    color = Color.WHITE;
                }
                else {
                    color = oriColor;
                }
                attackStartTime -= 1;
            }
            else {
                if (attackEndTime > 0) {
                    color = oriColor;
                    centerX += attackdx * attackSpeed;
                    centerY += attackdy * attackSpeed;
                    attackEndTime -= 1;
                    damagePlayer = true;
                }
                else {
                    damagePlayer = false;
                }
            }
            if (attackCoolDown > 0) {
                attackCoolDown -= 1;
            }
            else {
                state = 1;
                attacking = false;
            }
        }
        rotate();
    }

    @Override
    public void rotate() {
        double angle;
        double radius = height * 2.0 / 3;

        if (knockBacking){
            // angle = Math.atan2(knockBackdy, knockBackdx);
            angle = Math.atan2(dy, dx);
        }
        else if (attacking) {
            angle = Math.atan2(attackdy, attackdx);
        }
        else {
            angle = Math.atan2(dy, dx);
        }

        xPoints[0] = (int) (centerX + Math.cos(angle) * radius);
        yPoints[0] = (int) (centerY + Math.sin(angle) * radius);
        
        xPoints[1] = (int) (centerX + Math.cos(angle + Math.PI / 3 * 2) * radius);
        yPoints[1] = (int) (centerY + Math.sin(angle + Math.PI / 3 * 2) * radius);
        
        xPoints[2] = (int) (centerX + Math.cos(angle - Math.PI / 3 * 2) * radius);
        yPoints[2] = (int) (centerY + Math.sin(angle - Math.PI / 3 * 2) * radius);
    }

    @Override
    public void draw(Graphics g) {
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

    @Override
    public double getCenterX() {
        int xTotal = 0;
        for (int a = 0; a < 3; a++) {
            xTotal += xPoints[a];
        }
        return xTotal / 3.0;
    }

    @Override
    public double getCenterY() {
        int yTotal = 0;
        for (int a = 0; a < 3; a++) {
            yTotal += yPoints[a];
        }
        return yTotal / 3.0;
    }

    @Override
    public double getHealth(){
        return currentHealth;
    }

    @Override
    public boolean isAttacking() {
        if (attackStartTime <= 0 && attackEndTime > 0) {
            return attacking;
        }
        else return false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (centerX - width / 2), (int) (centerY - height / 2), width, height);
    }

}
