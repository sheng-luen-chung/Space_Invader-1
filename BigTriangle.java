import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BigTriangle extends Triangle{
    private int phase = 0;
    private int moveType = 0;
    private int moveTypeTimer = 100;
    private int attackType = 0;
    private int attackTypeCounter = 0;
    private double rotationRadius = 0;

    private boolean isSummoned = false;

    ArrayList<Enemy> enemies;
    List<Enemy> enemiesToAdd;


    BigTriangle(int x, int y, int w, int h, Color c, int health, ArrayList<Enemy> enemies) {
        super(x, y, w, h, c, health);
        this.enemies = enemies;
        this.enemiesToAdd = new ArrayList<>();
    }

    @Override
    public void move(double playerX, double playerY) {
        switch(state) {
            case 0 -> stateIdle(playerX, playerY);
            case 1 -> stateMove(playerX, playerY);
            case 2 -> stateKnockBack();
            case 3 -> stateAttack(playerX, playerY);
        }
    }

    @Override
    public void stateIdle(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        dx = playerX - centerX;
        dy = playerY - centerY;

        double dxdy = Math.sqrt(dx * dx + dy * dy);
        if (dxdy <= 2000) {
            state = 1;
        }
    }

    @Override
    public void stateMove(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        dx = playerX - centerX;
        dy = playerY - centerY;
        
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (moveType == 0) {
            if (distance != 0) {
                dx /= distance;
                dy /= distance;
                centerX += dx * speed;
                centerY += dy * speed;
            }
            if (distance <= 200) {
                state = 3;
                if (phase == 0) {
                    attackType = 0;
                    attackTypeCounter = 1;
                }
                else {
                    attackType = (int) (Math.random() * 2);
                    attackTypeCounter = 2 + (int) (Math.random() * 2);
                }
            }
        }
        else if (moveType == 1) {
                if (distance < rotationRadius) rotationRadius = distance;
                if (Math.abs(distance - rotationRadius) > 10) {
                    dx /= distance;
                    dy /= distance;
                    double radialDir = (distance > rotationRadius) ? 1 : -1;
                    centerX += dx * speed * radialDir;
                    centerY += dy * speed * radialDir;
                }

                double tangentX = -dy;
                double tangentY = dx;
                double norm = Math.sqrt(tangentX * tangentX + tangentY * tangentY);
                if (norm != 0) {
                    tangentX /= norm;
                    tangentY /= norm;
                    centerX += tangentX * speed;
                    centerY += tangentY * speed;
                }
        }

        if (moveTypeTimer > 0) {
            moveTypeTimer -= 1;
        }
        else {
            moveType = (int) (2 * Math.random());
            moveTypeTimer = 100;
            rotationRadius = 100 + 100 * Math.random();
        }

        if (phase == 0 && currentHealth < (double) maxHealth / 2) {
            phase = 1;
        }

        rotateTri();

        if (getHurtTimer > 0) {
            getHurtTimer -= 1;
        }
        else {
            getHurting = false;
        }
    }

    @Override
    public void stateAttack(double playerX, double playerY) {
        if (!attacking) {
            attackSpeed = 20;
            if (phase == 1 && attackType == 0) {
                attackStartTime = 2 * Constants.TRIANGLEATTACKSTARTTIME;
                attackCoolDown = (int) (1.5 * Constants.TRIANGLEATTACKCOOLDOWN);
            }
            else {
                attackStartTime = Constants.TRIANGLEATTACKSTARTTIME;
                attackCoolDown = Constants.TRIANGLEATTACKCOOLDOWN;
            }
            attackEndTime = Constants.TRIANGLEATTACKENDTIME;
            attacking = true;
            isSummoned = false;
        }
        else {
            if (attackType == 0) {
                sprintAttack(playerX, playerY);
            }
            else if (attackType == 1) {
                if (!isSummoned){
                    summonTriangleMinions(1 + (int) (Math.random() * 3));
                }
            }

            if (attackCoolDown > 0) {
                attackCoolDown -= 1;
            }
            else {
                if (attackTypeCounter > 0) {
                    attackTypeCounter -= 1;
                }
                else {
                    state = 1;
                    moveType = 1;
                }
                attacking = false;
            }
        }
        rotateTri();
    }

    @Override
    public void getHurt(int damage) {
        if (!getHurting && !knockBacking){
            currentHealth -= damage + (int) (Constants.playerActualSTR * 10);
            getHurting = true;
            getHurtTimer = 5;
            getHurtCounter += 1;
        }
        moveType = 0;
        moveTypeTimer = 100;
    }
    
    @Override
    public void stateKnockBack() {
        if (knockBackTimer > 0){
            centerX += knockBackdx * knockBackSpeed;
            centerY += knockBackdy * knockBackSpeed;
            knockBackTimer -= 1;
        }
        else {
            state = 1;
            knockBacking = false;
        }
        rotateTri();
    }

    @Override
    public void knockBack(double x, double y, int s, int t) {
        if (getHurtCounter >= 5) {
            knockBackdx = getCenterX() - x;
            knockBackdy = getCenterY() - y;
            double dxdy = Math.sqrt(knockBackdx * knockBackdx + knockBackdy * knockBackdy);
            knockBackdx /= dxdy;
            knockBackdy /= dxdy;
            knockBackSpeed = s;
            knockBackTimer = t;
            state = 2;
            knockBacking = true;
            getHurtCounter = 0;
        }
    }

    
    private void sprintAttack(double playerX, double playerY) {
        if (attackStartTime > 0) {
            centerX = getCenterX();
            centerY = getCenterY();
            attackdx = playerX - centerX;
            attackdy = playerY - centerY;
            double dxdy = Math.sqrt(attackdx * attackdx + attackdy * attackdy);
            attackdx /= dxdy;
            attackdy /= dxdy;
            if (phase == 0) {
                if (attackStartTime % 10 >= 5) {
                    color = Color.WHITE;
                }
                else {
                    color = oriColor;
                }
            }
            else {
                if (attackStartTime < (double) Constants.TRIANGLEATTACKSTARTTIME / 6){
                    color = Color.WHITE;
                }
                else if (attackStartTime >= (double) Constants.TRIANGLEATTACKSTARTTIME / 6 && 
                    attackStartTime < (double) Constants.TRIANGLEATTACKSTARTTIME / 6 * 4) {
                    color = Color.BLACK;
                }
                else if (attackStartTime >= (double) Constants.TRIANGLEATTACKSTARTTIME / 6 * 4 && 
                    attackStartTime < (double) Constants.TRIANGLEATTACKSTARTTIME / 6 * 5) {
                    color = Color.WHITE;
                }
                else {
                    color = oriColor;
                }
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
    }

    private void summonTriangleMinions(int num) {
        if (attackStartTime > 0) {
            if (attackStartTime % 10 >= 5) {
                color = Color.ORANGE;
            }
            else {
                color = oriColor;
            }
            attackStartTime -= 1;
        }
        else {
            for (int i = 0; i < num; i++) {
                int offsetX = (int)(Math.random() * 100 - 50);
                int offsetY = (int)(Math.random() * 100 - 50);
                Triangle minion = new Triangle((int)centerX + offsetX, (int)centerY + offsetY, 20, (int) (20 * 1.732 / 2), Color.ORANGE, 1);
                enemiesToAdd.add(minion);
                isSummoned = true;
            }
        }
    }
}
