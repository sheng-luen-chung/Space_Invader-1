import java.awt.*;
import java.util.List;

public class BigSquare extends Square{
    private int phase = 0;
    private int moveType = 0;
    private int moveTypeTimer = 100;
    private int attackType = 0;
    private int attackTypeCounter = 0;
    private double rotationRadius = 0;
    private int rotateDirection = -1;

    private boolean isSummoned = false;

    private int phaseChangeTimer = 0;
    private boolean phaseChangeAttack = false;

    public List<Enemy> enemiesToAdd;

    private MusicPlayer musicPlayer;

    BigSquare(int x, int y, int w, int h, Color c, int attackDamage, int health, int detectZone, MusicPlayer musicPlayer) {
        super(x, y, w, h, c, attackDamage, health, detectZone, musicPlayer);
        // this.enemiesToAdd = new ArrayList<>();
        // this.bulletsToAdd = new ArrayList<>();
        this.musicPlayer = musicPlayer;
        musicPlayer.stopById("BigSquare");
        musicPlayer.playSegment("BigSquare", 0, 105, true);
    }

    @Override
    public void move(double playerX, double playerY) {
        color = oriColor;
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
        rotate();
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
        String name = "Big Square";
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
            if (distance <= Constants.BIGTRIANGLEATTACKZONE) {
                state = 3;
                double rand = Math.random();
                if (phase == 0) {
                    if (rand < 0.6) {
                        attackType = 0;
                    }
                    else {
                        attackType = 1;
                    }
                    attackTypeCounter = 1;
                }
                else if (phase == 1) {
                    if (rand < 0.4) {
                        attackType = 0;
                    }
                    else if (rand < 0.7) {
                        attackType = 1;
                    }
                    else {
                        attackType = 2;
                    }
                    attackTypeCounter = 1 + (int) (Math.random() * 3);
                }
                else if (phase == 2) {
                    if (rand < 0.3) {
                        attackType = 0;
                    }
                    else if (rand < 0.6) {
                        attackType = 1;
                    }
                    else if (rand < 0.8) {
                        attackType = 2;
                    }
                    else {
                        attackType = 3;
                    }
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
            rotationRadius = 200 + 50 * Math.random();
        }

        if (phase == 0 && currentHealth < (double) maxHealth / 3 * 2) {
            phase = 1;
            phaseChangeTimer = 100;
            musicPlayer.stopById("BigSquare");
            musicPlayer.playSegment("BigSquare", 117, 240, true);
            getHurtCounter = 0;
            phaseChangeAttack = true;
            state = 4;
        }
        else if (phase == 1 && currentHealth < (double) maxHealth / 3 * 1) {
            phase = 2;
            phaseChangeTimer = 100;
            musicPlayer.stopById("BigSquare");
            musicPlayer.playSegment("BigSquare", 243, 330, true);
            getHurtCounter = 0;
            phaseChangeAttack = true;
            state = 4;
        }

        faceDirection = 0;
    }

    @Override
    public void stateAttack(double playerX, double playerY) {
        if (!attacking) {
            switch (attackType) {
                case 0:
                    if (phase < 2) {
                        attackStartTime = Constants.BIGSQUAREATTACKSTARTTIME;
                        attackEndTime = Constants.BIGSQUAREATTACKENDTIME;
                        attackCoolDown = Constants.BIGSQUAREATTACKCOOLDOWN;
                    }
                    else {
                        attackStartTime = 2 * Constants.BIGSQUAREATTACKSTARTTIME;
                        attackEndTime = 1;
                        attackCoolDown = (int) (1.5 * Constants.BIGSQUAREATTACKCOOLDOWN);
                    }
                    break;
                case 1:
                    if (phase < 2) {
                        attackStartTime = (int) (2 * Constants.BIGSQUAREATTACKSTARTTIME);
                        attackEndTime = (int) (Constants.BIGSQUAREATTACKENDTIME);
                        attackCoolDown = (int) (1.5 * Constants.BIGSQUAREATTACKCOOLDOWN);
                    }
                    else {
                        attackStartTime = (int) (3 * Constants.BIGSQUAREATTACKSTARTTIME);
                        attackEndTime = (int) (2 * Constants.BIGSQUAREATTACKENDTIME);
                        attackCoolDown = (int) (2 * Constants.BIGSQUAREATTACKCOOLDOWN);
                    }
                    break;
                case 2:
                    if (phase < 2) {
                        attackStartTime = (int) (Constants.BIGSQUAREATTACKSTARTTIME);
                        attackEndTime = (int) ((3 + phase * 2) * Constants.BIGSQUAREATTACKENDTIME);
                        attackCoolDown = (int) (Constants.BIGSQUAREATTACKCOOLDOWN);
                    }
                    break;
                case 3:
                    attackStartTime = Constants.BIGSQUAREATTACKSTARTTIME;
                    attackEndTime = 1;
                    attackCoolDown = (int) (Constants.BIGSQUAREATTACKCOOLDOWN);
                    break;
            }
            attacking = true;
            isSummoned = false;
            faceDirection = 1;
        }
        else {
            switch (attackType) {
                case 0:
                    shotAttack(playerX, playerY);
                    break;
                case 1:
                    heavyAttack(playerX, playerY);
                    break;
                case 2:
                    sprintShotAttack(playerX, playerY);
                    break;
                case 3:
                    ringAttack(playerX, playerY);
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
                    rotateDirection *= -1;
                }
                attacking = false;
            }
        }
    }

    public void statePhaseChange() {
        if (phaseChangeTimer > 0) {
            if (phaseChangeTimer % 10 > 5) {
                color = Color.PINK;
            }
            else {
                color = oriColor;
            }
            phaseChangeTimer -= 1;
        }
        else {
            attackCoolDown = 0;
            attacking = false;
            attackType = 3;
            attackTypeCounter = phase * 2;
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

    @Override
    public void stateKnockBack() {
        if (knockBackTimer > 0){
            centerX += knockBackdx * knockBackSpeed;
            centerY += knockBackdy * knockBackSpeed;
            knockBackTimer -= 1;

            attackStartTime = 0;
            attackEndTime = 0;
            attackCoolDown = 0;
        }
        else {
            state = 1;
            knockBacking = false;
        }
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

    private void shotAttack(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        bulletDx = playerX - centerX;
        bulletDy = playerY - centerY;
        double dxdy = Math.sqrt(bulletDx * bulletDx + bulletDy * bulletDy);
        bulletDx /= dxdy;
        bulletDy /= dxdy;
        if (attackStartTime > 0) {
            if (phase < 2) {
                if (attackStartTime % 10 >= 5) {
                    color = Color.WHITE;
                }
                else {
                    color = oriColor;
                }
            }
            else {
                if (attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6){
                    color = Color.WHITE;
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 2) {
                    color = Color.BLACK;
                    if (attackStartTime == (int) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 1.5) {
                        double angle = Math.random() * 2 * Math.PI;
                        double distance = 150 + Math.random() * 100;

                        centerX = playerX + Math.cos(angle) * distance;
                        centerY = playerY + Math.sin(angle) * distance;
                    }
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 2 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 5) {
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
                if (phase != 2 && attackEndTime % 15 == 0) {
                    Bullet b1 = new Bullet((int) getCenterX() - 10, 
                                        (int) getCenterY() - 10, 
                                        20, 20, 
                                        Color.YELLOW, 
                                        bulletDx, 
                                        bulletDy, 
                                        7, 
                                        attackDamage, 
                                        100, 
                                        "Enemy", 
                                        musicPlayer);
                    bulletsToAdd.add(b1);
                    if (phase == 1) {
                        double angle = Math.atan2(bulletDy, bulletDx);
                        Bullet b2 = new Bullet((int) getCenterX() - 10, 
                                            (int) getCenterY() - 10, 
                                            20, 20, 
                                            Color.YELLOW, 
                                            Math.cos(angle + Math.PI / 10), 
                                            Math.sin(angle + Math.PI / 10), 
                                            7, 
                                            attackDamage, 
                                            100, 
                                            "Enemy", 
                                            musicPlayer);
                        bulletsToAdd.add(b2);
                        Bullet b3 = new Bullet((int) getCenterX() - 10, 
                                            (int) getCenterY() - 10, 
                                            20, 20, 
                                            Color.YELLOW, 
                                            Math.cos(angle - Math.PI / 10), 
                                            Math.sin(angle - Math.PI / 10), 
                                            7, 
                                            attackDamage, 
                                            100, 
                                            "Enemy", 
                                            musicPlayer);
                        bulletsToAdd.add(b3);
                    }
                }
                attackEndTime -= 1;
                if (attackEndTime == 0 && phase == 2) {
                    Bullet b1 = new Bullet((int) getCenterX() - 10, 
                                        (int) getCenterY() - 10, 
                                        20, 20, 
                                        Color.YELLOW, 
                                        bulletDx, 
                                        bulletDy, 
                                        10, 
                                        attackDamage, 
                                        100, 
                                        "Enemy", 
                                        musicPlayer);
                    bulletsToAdd.add(b1);
                    double angle = Math.atan2(bulletDy, bulletDx);
                    Bullet b2 = new Bullet((int) getCenterX() - 10, 
                                        (int) getCenterY() - 10, 
                                        20, 20, 
                                        Color.YELLOW, 
                                        Math.cos(angle + Math.PI / 10), 
                                        Math.sin(angle + Math.PI / 10), 
                                        10, 
                                        attackDamage, 
                                        100, 
                                        "Enemy", 
                                        musicPlayer);
                    bulletsToAdd.add(b2);
                    Bullet b3 = new Bullet((int) getCenterX() - 10, 
                                        (int) getCenterY() - 10, 
                                        20, 20, 
                                        Color.YELLOW, 
                                        Math.cos(angle - Math.PI / 10), 
                                        Math.sin(angle - Math.PI / 10), 
                                        10, 
                                        attackDamage, 
                                        100, 
                                        "Enemy", 
                                        musicPlayer);
                    bulletsToAdd.add(b3);
                }
            }
        }
    }

    private void heavyAttack(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        bulletDx = playerX - centerX;
        bulletDy = playerY - centerY;
        double dxdy = Math.sqrt(bulletDx * bulletDx + bulletDy * bulletDy);
        bulletDx /= dxdy;
        bulletDy /= dxdy;
        if (attackStartTime > 0) {
            if (phase < 2) {
                if (attackStartTime % 10 >= 5) {
                    color = Color.YELLOW;
                }
                else {
                    color = oriColor;
                }
            }
            else {
                if (attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6){
                    color = Color.YELLOW;
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 4) {
                    color = Color.BLACK;
                    if (attackStartTime == (int) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 3) {
                        double angle = Math.random() * 2 * Math.PI;
                        double distance = 300 + Math.random() * 100;

                        centerX = playerX + Math.cos(angle) * distance;
                        centerY = playerY + Math.sin(angle) * distance;
                    }
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 4 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 5) {
                    color = Color.YELLOW;
                }
                else {
                    color = oriColor;
                }
            }
            attackStartTime -= 1;
        }
        else {
            if (attackEndTime > 0) {
                if (attackEndTime % 10 == 0) {
                    Bullet b = new Bullet((int) getCenterX() - 20, 
                                        (int) getCenterY() - 20, 
                                        40, 40, 
                                        Color.YELLOW, 
                                        bulletDx, 
                                        bulletDy, 
                                        12, 
                                        attackDamage, 
                                        100, 
                                        "Enemy", 
                                        musicPlayer);
                    bulletsToAdd.add(b);
                }
                faceDirection = 0;
                attackEndTime -= 1;
            }
        }
    }

    private void sprintShotAttack(double playerX, double playerY) {
        centerX = getCenterX();
        centerY = getCenterY();
        bulletDx = playerX - centerX;
        bulletDy = playerY - centerY;
        double dxdy = Math.sqrt(bulletDx * bulletDx + bulletDy * bulletDy);
        bulletDx /= dxdy;
        bulletDy /= dxdy;
        centerX -= rotateDirection * bulletDy * speed * 3;
        centerY += rotateDirection * bulletDx * speed * 3;
        double radialDir = (dxdy > rotationRadius) ? -1 : 1;
        if (dxdy - rotationRadius > 10) {
            centerX -= bulletDx * speed * radialDir;
            centerY -= bulletDy * speed * radialDir;
        }
        else {
            centerX += bulletDx * speed * radialDir;
            centerY += bulletDy * speed * radialDir;
        }
        if (attackStartTime > 0) {
            if (phase < 2) {
                if (attackStartTime % 10 >= 5) {
                    color = Color.WHITE;
                }
                else {
                    color = oriColor;
                }
            }
            else {
                if (attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6){
                    color = Color.WHITE;
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 4) {
                    color = Color.BLACK;
                    if (attackStartTime == (int) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 3) {
                        double angle = Math.random() * 2 * Math.PI;
                        double distance = 150 + Math.random() * 100;

                        centerX = playerX + Math.cos(angle) * distance;
                        centerY = playerY + Math.sin(angle) * distance;
                    }
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 4 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 5) {
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
                if (attackEndTime % 7 == 0) {
                    Bullet b1 = new Bullet((int) getCenterX() - 10, 
                                        (int) getCenterY() - 10, 
                                        20, 20, 
                                        Color.YELLOW, 
                                        bulletDx, 
                                        bulletDy, 
                                        12, 
                                        (int) (attackDamage / 1.5), 
                                        500, 
                                        "Enemy", 
                                        musicPlayer);
                    bulletsToAdd.add(b1);
                }
                attackEndTime -= 1;
            }
        }
    }

    private void ringAttack(double playerX, double playerY) {
        if (phaseChangeAttack) attackStartTime = 0;
        if (attackStartTime > 0) {
            if (phase < 2) {
                if (attackStartTime % 10 >= 5) {
                    color = Color.PINK;
                }
                else {
                    color = oriColor;
                }
            }
            else {
                if (attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6){
                    color = Color.PINK;
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 4) {
                    color = Color.BLACK;
                    if (attackStartTime == (int) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 3) {
                        double angle = Math.random() * 2 * Math.PI;
                        double distance = 150 + Math.random() * 100;

                        centerX = playerX + Math.cos(angle) * distance;
                        centerY = playerY + Math.sin(angle) * distance;
                    }
                }
                else if (attackStartTime >= (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 4 && 
                    attackStartTime < (double) Constants.BIGSQUAREATTACKSTARTTIME / 6 * 5) {
                    color = Color.PINK;
                }
                else {
                    color = oriColor;
                }
            }
            attackStartTime -= 1;
        }
        else {
            if (attackEndTime > 0) {
                attackEndTime -= 1;
            }
            else {
                double angle = 0;
                for (int c = 0; c < phase; c++) {
                    for (int a = 0; a < 10; a++) {
                        Bullet b = new Bullet((int) getCenterX() - 10, 
                                            (int) getCenterY() - 10, 
                                            20, 20, 
                                            Color.YELLOW, 
                                            Math.cos(angle + (Math.PI / 5) * a), 
                                            Math.sin(angle + (Math.PI / 5) * a), 
                                            7, 
                                            attackDamage, 
                                            100, 
                                            "Enemy", 
                                            musicPlayer);
                        bulletsToAdd.add(b);
                    }
                    angle += Math.PI / 10;
                }
                if (phaseChangeAttack) {
                    phaseChangeAttack = false;
                    attackType = (phase == 2) ? 0 : 2;
                    attackTypeCounter = phase * 2 + 1;
                    state = 3;
                }
                else {
                    state = 1;
                }
            }
        }
    }
}
