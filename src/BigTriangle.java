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
    private int rotateDirection = -1;

    private boolean isSummoned = false;

    private int phaseChangeTimer = 0;

    List<Enemy> enemiesToAdd;

    BigTriangle(int x, int y, int w, int h, Color c, int attackDamage, int health, int detectZone) {
        super(x, y, w, h, c, attackDamage, health, detectZone);
        this.enemiesToAdd = new ArrayList<>();
    }

    @Override
    public void move(double playerX, double playerY) {
        switch(state) {
            case 0 -> stateIdle(playerX, playerY);
            case 1 -> stateMove(playerX, playerY);
            case 2 -> stateKnockBack();
            case 3 -> stateAttack(playerX, playerY);
            case 4 -> statePhaseChange();
        }
        if (getHurtTimer > 0) {
            getHurtTimer -= 1;
        }
        else {
            getHurting = false;
        }
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

        // 2. 設定血條寬度與位置
        int barWidth = 640;
        int barHeight = 15;
        int barX = (Constants.FRAMEWIDTH - barWidth) / 2;
        int barY = Constants.FRAMEHEIGHT - 80;

        // 3. 計算當前血量比例
        double hpRatio = (double) currentHealth / maxHealth;
        int hpWidth = (int)(barWidth * hpRatio);

        // 4. 背景條
        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        // 5. 血量條
        g.setColor(Color.RED);
        g.fillRect(barX, barY, hpWidth, barHeight);

        // 6. 邊框
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);

        // 7. 名稱（置中顯示）
        String name = "Big Triangle";
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics();
        int nameX = Constants.FRAMEWIDTH / 2 - fm.stringWidth(name) / 2;
        int nameY = barY - 10;
        g.setColor(Color.WHITE);
        g.drawString(name, nameX, nameY);
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
                if (phase == 1) {
                    centerX += dx * speed;
                    centerY += dy * speed;
                }
            }
            if (distance <= 250) {
                state = 3;
                double rand = Math.random();
                if (phase == 0) {
                    if (rand < 0.7) {
                        attackType = 0;
                    }
                    else {
                        attackType = 1;
                    }
                    attackTypeCounter = 1;
                }
                else {
                    if (rand < 0.4) {
                        attackType = 0;
                    }
                    else if (rand < 0.6) {
                        attackType = 1;
                    }
                    else if (rand < 0.9) {
                        attackType = 2;
                    }
                    else {
                        attackType = 3;
                    }
                    attackTypeCounter = 1 + (int) (Math.random() * 3);
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

            double tangentX = -dy * rotateDirection;
            double tangentY = dx * rotateDirection;
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
            phaseChangeTimer = 100;
            state = 4;
        }

        rotate();
    }

    @Override
    public void stateAttack(double playerX, double playerY) {
        if (!attacking) {
            attackSpeed = 20;
            switch (attackType) {
                case 0:
                    if (phase == 0) {
                        attackStartTime = Constants.TRIANGLEATTACKSTARTTIME;
                        attackEndTime = Constants.TRIANGLEATTACKENDTIME;
                        attackCoolDown = Constants.TRIANGLEATTACKCOOLDOWN;
                    } else {
                        attackStartTime = (int) (1.5 * Constants.TRIANGLEATTACKSTARTTIME);
                        attackEndTime = Constants.TRIANGLEATTACKENDTIME;
                        attackCoolDown = (int) (1.25 * Constants.TRIANGLEATTACKCOOLDOWN);
                    }
                    break;
                case 1:
                    attackStartTime = (int) (2 * Constants.TRIANGLEATTACKSTARTTIME);
                    attackEndTime = (int) (2 * Constants.TRIANGLEATTACKENDTIME);
                    attackCoolDown = (int) (1.5 * Constants.TRIANGLEATTACKCOOLDOWN);
                    break;
                case 2:
                    attackStartTime = 2 * Constants.TRIANGLEATTACKSTARTTIME;
                    attackEndTime = Constants.TRIANGLEATTACKENDTIME;
                    attackCoolDown = (int) (1.25 * Constants.TRIANGLEATTACKCOOLDOWN);
                    break;
                case 3:
                    attackStartTime = Constants.TRIANGLEATTACKSTARTTIME;
                    attackEndTime = Constants.TRIANGLEATTACKENDTIME;
                    attackCoolDown = (int) (0.5 * Constants.TRIANGLEATTACKCOOLDOWN);
                    break;
            }
            attacking = true;
            isSummoned = false;
        }
        else {
            switch (attackType) {
                case 0:
                    sprintAttack(playerX, playerY);
                    break;
                case 1:
                    heavyAttack(playerX, playerY);
                    break;
                case 2:
                    sprintsprintAttack(playerX, playerY);
                    break;
                case 3:
                    if (!isSummoned) {
                        summonTriangleMinions(2 + (int) (Math.random() * 3));
                    }
                    break;
                default:
                    throw new AssertionError();
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
                rotateDirection = Math.random() < 0.5 ? -1 : 1;
            }
        }
        rotate();
    }

    public void statePhaseChange() {
        if (phaseChangeTimer > 0) {
            if (phaseChangeTimer % 10 > 6) {
                color = Color.WHITE;
            }
            else if (phaseChangeTimer % 10 <= 6 && phaseChangeTimer % 10 > 3) {
                color = Color.ORANGE;
            }
            else {
                color = oriColor;
            }
            phaseChangeTimer -= 1;
        }
        else {
            attackType = 2;
            attackTypeCounter = 4;
            state = 3;
        }
    }

    @Override
    public void getHurt(int damage) {
        if (!getHurting && !knockBacking){
            color = oriColor;
            currentHealth -= damage;
            getHurting = true;
            getHurtTimer = 5;
            getHurtCounter += 1;
        }
        moveType = 0;
        moveTypeTimer = 100;
    }
    
    @Override
    public int getDamage() {
        switch (attackType) {
            case 0:
                return (int) (attackDamage - 5 + Math.random() * 5);
            case 1:
                return (int) (attackDamage + 10 + Math.random() * 5);
            case 2:
                return (int) (attackDamage - 10 + Math.random() * 5);
            default:
                return (int) (attackDamage - 5 + Math.random() * 5);
        }
    }

    public int getPhase() {
        return phase;
    }

    @Override
    public void stateKnockBack() {
        if (knockBackTimer > 0){
            centerX += knockBackdx * knockBackSpeed;
            centerY += knockBackdy * knockBackSpeed;
            knockBackTimer -= 1;

            attackTimer = 0;
            attackStartTime = 0;
            attackEndTime = 0;
            attackCoolDown = 0;
        }
        else {
            state = 1;
            knockBacking = false;
        }
        rotate();
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
                if (phase == 1 && !isSummoned) summonTriangleMinions((int) (1 + Math.random() * 2));
            }
        }
    }

    private void heavyAttack(double playerX, double playerY) {
        if (attackStartTime > 0) {
            centerX = getCenterX();
            centerY = getCenterY();
            attackdx = playerX - centerX;
            attackdy = playerY - centerY;
            double dxdy = Math.sqrt(attackdx * attackdx + attackdy * attackdy);
            attackdx /= dxdy;
            attackdy /= dxdy;
            centerX -= attackdx * 2;
            centerY -= attackdy * 2;
            if (attackStartTime % 10 >= 5) {
                color = Color.YELLOW;
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
        }
    }

    private void sprintsprintAttack(double playerX, double playerY) {
        if (attackStartTime > 0) {
            centerX = getCenterX();
            centerY = getCenterY();
            attackdx = playerX - centerX;
            attackdy = playerY - centerY;
            double dxdy = Math.sqrt(attackdx * attackdx + attackdy * attackdy);
            attackdx /= dxdy;
            attackdy /= dxdy;
            centerX -= rotateDirection * attackdy * attackSpeed / 3;
            centerY += rotateDirection * attackdx * attackSpeed / 3;
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
                    attackStartTime < (double) Constants.TRIANGLEATTACKSTARTTIME / 6 * 3) {
                    color = Color.BLACK;
                }
                else if (attackStartTime >= (double) Constants.TRIANGLEATTACKSTARTTIME / 6 * 3 && 
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
                centerX += attackdx * attackSpeed * 1.5;
                centerY += attackdy * attackSpeed * 1.5;
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
                Triangle minion = new Triangle((int)centerX + offsetX, 
                                               (int)centerY + offsetY, 
                                               20, 
                                               (int) (20 * 1.732 / 2), 
                                               Color.ORANGE, 
                                               (int) (10 * Math.pow(1.25, Constants.nRuns + 1)), 
                                               1, 
                                               Constants.SMALLTRIANGLEDETECTZONE);
                enemiesToAdd.add(minion);
                isSummoned = true;
            }
        }
    }
}
