import java.awt.*;
import java.awt.event.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import javax.swing.*;

public class SpaceInvaderPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{
    Timer timer1, timer2, timer3, timer4;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    List<Enemy> enemiesToAdd = new ArrayList<>();
    ArrayList<Explode> explodes = new ArrayList<>();
    ArrayList<Coin> coins = new ArrayList<>();
    Player player;
    CampFire campFire;
    
    private boolean paused = false;
    private boolean playerInput = false;
    private boolean enemyMove = false;
    private boolean isGameOver = false;
    private boolean showBonfireText = false;
    private boolean inBonfire = false;

    private final int spawnEnemiesMaxDelay = 10000;
    private final int spawnEnemiesMinDelay = 9000;
    private int spawnEnemiesDelay = 10000;
    private int spawnEnemiesStep = 500;
    private final int bigTriangleDelay = 10000;
    private boolean stopSpawmEnemies = false;
    private boolean enemyFall = false;

    private boolean changeMusic = false;

    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private boolean ePressed = false;
    private boolean escPressed = false;
    private int rPressed = 0;
    private int spacePressed = 0;

    private int mouseLeftClicked = 0;
    private int mouseRightClicked = 0;

    private int leftClickX = 0;
    private int leftClickY = 0;

    private int mouseX = 0;
    private int mouseY = 0;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JLayeredPane layeredPane;
    private JPanel settingsPanel;
    private GameOverPanel gameOverPanel;
    private LevelUpPanel levelUpPanel;
    private MusicPlayer musicPlayer;

    public SpaceInvaderPanel(JLayeredPane lp) {
        setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.layeredPane = lp;
        setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));

        player = new Player(Constants.FRAMEWIDTH / 2, 
                            Constants.FRAMEHEIGHT - 2 * Constants.PLAYERHEIGHT, 
                            Constants.PLAYERWIDTH, 
                            Constants.PLAYERHEIGHT, 
                            Color.green, 
                            (int) Constants.playerActualHP, 
                            (int) Constants.playerActualEnergy);

        timer1 = new Timer(15, this);
        timer2 = new Timer(bigTriangleDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enemies.add(new BigTriangle(640, 40, 
                                            60, (int) (60 * 1.732 / 2), 
                                            Color.red, 
                                            (int) (20 * Math.pow(1.25, Constants.nRuns + 1)), 
                                            (int) (1000 * Math.pow((Constants.nRuns + 1), 1.2)), 
                                            Constants.BIGTRIANGLEDETECTZONE));
                musicPlayer.stopById("BigTriangle");
                musicPlayer.playSegment("BigTriangle", 0, 105, true);
                timer2.stop();
            }
        });
        timer3 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.adjustVolume(-1);
                if (musicPlayer.getCurrentVolume() < 1){
                    musicPlayer.stopById("BigTriangle");
                    musicPlayer.adjustVolume(10);
                    timer3.stop();
                }
            }
        });
        timer4 = new Timer(spawnEnemiesDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnEnemies();
                if (spawnEnemiesDelay > spawnEnemiesMinDelay) {
                    spawnEnemiesDelay -= spawnEnemiesStep;
                    System.out.println(spawnEnemiesDelay);
                }
                if (spawnEnemiesDelay == spawnEnemiesMinDelay) {
                    stopSpawmEnemies = true;
                    timer4.stop();
                    timer2.start();
                    System.out.println("Big Triangle");
                }
            }
        });
    }

    void spawnEnemies() {
        Random rand = new Random();
        int x = 0, y = 0;
        int times = 11 - spawnEnemiesDelay / 1000 + rand.nextInt(3);

        for (int a = 0; a < times; a++) {
            int side = rand.nextInt(4); // 0 = top, 1 = bottom, 2 = left, 3 = right

            switch (side) {
                case 0: // Top
                    x = rand.nextInt(Constants.FRAMEWIDTH);
                    y = -Constants.TRIANGLEHEIGHT;
                    break;
                case 1: // Bottom
                    x = rand.nextInt(Constants.FRAMEWIDTH);
                    y = Constants.FRAMEHEIGHT;
                    break;
                case 2: // Left
                    x = -Constants.TRIANGLEWIDTH;
                    y = rand.nextInt(Constants.FRAMEHEIGHT);
                    break;
                case 3: // Right
                    x = Constants.FRAMEWIDTH;
                    y = rand.nextInt(Constants.FRAMEHEIGHT);
                    break;
            }
            enemies.add(new Triangle(x, y, 
                                        Constants.TRIANGLEWIDTH, 
                                        Constants.TRIANGLEHEIGHT, 
                                        Color.red, 
                                        (int) (15 * Math.pow(1.25, Constants.nRuns + 1)), 
                                        (int) (50 * Math.pow(1.1, Constants.nRuns)), 
                                        Constants.TRIANGLEDETECTZONE));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        campFire.draw(g);
        if (showBonfireText) campFire.drawText(g);
        player.draw(g);
        PlayerUI.draw(g, 
                      (int) Constants.playerActualHP, 
                      player.getHealth(), 
                      (int) Constants.playerActualEnergy, 
                      player.getEnergy(), 
                      player.getExp(), 
                      player.getEstus());
        for(Enemy enemy : enemies) {
            enemy.draw(g);
        }
        for(Bullet bullet : bullets) {
            bullet.draw(g);
        }
        for(Explode explode : explodes) {
            explode.draw(g);
        }
        for(Coin coin : coins) {
            coin.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        requestFocusInWindow();
        if(!paused) {
            player.move(wPressed, aPressed, sPressed, dPressed);

            if (enemyMove) {
                for(Enemy enemy : enemies) {
                    enemy.move(player.getCenterX(), player.getCenterY());
                }
            }

            for (Enemy enemy : enemies) {
                if (enemy instanceof BigTriangle bt) {
                    enemiesToAdd.addAll(bt.enemiesToAdd);
                    if (bt.getPhase() == 1 && !changeMusic) {
                        musicPlayer.stopById("BigTriangle");
                        musicPlayer.playSegment("BigTriangle", 105, 245, true);
                        changeMusic = true;
                    }
                    bt.enemiesToAdd.clear();
                }
            }
            enemies.addAll(enemiesToAdd);
            enemiesToAdd.clear();

            for(Bullet bullet : bullets) {
                bullet.move();
            }
            
            deleteOutOfScreenBullets();

            if (mouseLeftClicked == 2 && player.getEnergy() > 0) {
                if (!player.isAttacking() && !player.isDodging() && !player.isKnockBacking() && !player.isHealing()){
                    double bulletDx = (double) (leftClickX - player.getCenterX());
                    double bulletDy = (double) (leftClickY - player.getCenterY());
                    double dxdy = Math.sqrt(bulletDx * bulletDx + bulletDy * bulletDy);
                    if (dxdy != 0) {
                        bulletDx /= dxdy;
                        bulletDy /= dxdy;
                    }
                    bullets.add(new Bullet((int) player.getCenterX() - 10, 
                                           (int) player.getCenterY() - 10, 
                                           20, 20, 
                                           Color.YELLOW, 
                                           bulletDx, bulletDy));
                    musicPlayer.playSegment("FireBall", 0.0f, 2.0f, false);
                    player.attack();
                }
                mouseLeftClicked = 0;
            }

            if (spacePressed == 2 && (wPressed || aPressed || sPressed || dPressed)) {
                player.dodge(wPressed, aPressed, sPressed, dPressed);
                spacePressed = 0;
            }

            if(rPressed == 2) {
                player.increaseHealth();
                if (player.getEstus() > 0) musicPlayer.playSegment("Estus", 0.0f, 2f, false);
                rPressed = 0;
            }

            if(escPressed) {
                pauseGame();
                escPressed = false;
                settingsPanel.setOpaque(false);
                for (Component comp : layeredPane.getComponents()) {
                    if (comp instanceof SettingsPanel) {
                        comp.setVisible(true);
                    }
                }
            }
                
            checkCollisions();
        }
        repaint();
    }

    void checkCollisions() {
        if (!inBonfire) {
            double dx = (double) (player.getCenterX() - campFire.getCenterX());
            double dy = (double) (player.getCenterY() - campFire.getCenterY());
            if (Math.sqrt(dx * dx + dy * dy) <= campFire.getWidth()) {
                showBonfireText = true;
                if (ePressed) {
                    pauseGame();
                    showBonfireText = false;
                    inBonfire = true;
                    changeMusic = false;
                    levelUpPanel.setOpaque(false);
                    levelUpPanel.setVisible(true);
                    musicPlayer.stopAll();
                    musicPlayer.playSegment("Bonfire", 0f, 4f, false);

                    player.restoreHealth();
                    player.restoreEnergy();
                    player.restoreEstus();

                    enemies.clear();
                    bullets.clear();
                    coins.clear();

                    if (spawnEnemiesDelay < spawnEnemiesMaxDelay) spawnEnemiesDelay += spawnEnemiesStep;

                    timer2.stop();
                    timer4.stop();
                }
            }
            else {
                showBonfireText = false;
            }
        }
        else {
            double dx = (double) (player.getCenterX() - campFire.getCenterX());
            double dy = (double) (player.getCenterY() - campFire.getCenterY());
            if (Math.sqrt(dx * dx + dy * dy) > (player.getWidth() + campFire.getWidth()) / 2) {
                inBonfire = false;
                levelUpPanel.setOpaque(true);
                levelUpPanel.setVisible(false);
                player.restoreHealth();
            }
        }

        Iterator<Bullet> bullet = bullets.iterator(); // bullet & enemy
        while (bullet.hasNext()) {
            Bullet b = bullet.next();

            Iterator<Enemy> enemy = enemies.iterator();
            while (enemy.hasNext()) {
                Enemy e = enemy.next();

                Rectangle bRect = b.getBounds();
                Rectangle eRect = e.getBounds();
                if (bRect.intersects(eRect)) {
                    Rectangle intersection = bRect.intersection(eRect);
                    int collisionX = intersection.x;
                    int collisionY = intersection.y;

                    explodes.add(new Explode(collisionX, collisionY, b.getWidth(), b.getHeight(), Color.RED, 100));

                    bullet.remove();
                    break;
                }
            }
        }
        Iterator<Explode> explode = explodes.iterator(); // explode & enemy
        while (explode.hasNext()) {
            Explode b = explode.next();
            b.getHurt(20);
            if (b.getHealth() <= 0) {
                explode.remove();
            }

            Iterator<Enemy> enemy = enemies.iterator();
            while (enemy.hasNext()) {
                Enemy e = enemy.next();

                Rectangle bRect = b.getBounds();
                Rectangle eRect = e.getBounds();
                if (bRect.intersects(eRect)) {
                    e.getHurt(b.getDamage());
                    e.knockBack(b.getCenterX(), b.getCenterY(), 10, 10);
                    if (e.getHealth() <= 0) {
                        for (int a = (int) (e.getMaxHealth()); a > 0; a-= 20) {
                            int offsetX = (int)(Math.random() * 100 - 50);
                            int offsetY = (int)(Math.random() * 100 - 50);
                            coins.add(new Coin(e.getCenterX() + offsetX, e.getCenterY() + offsetY, (int) (8 + Math.random() * 4)));
                        }
                        if (e instanceof BigTriangle) {
                            timer3.start();
                            enemyFall = true;
                            Constants.nRuns = Math.min(Constants.nRuns + 1, 10);
                        }
                        enemy.remove();
                        musicPlayer.playSegment("Kill", 0.0f, 1f, false);
                    }
                }
            }
        }
        Iterator<Enemy> enemy = enemies.iterator();
        while (enemy.hasNext()) {
            Enemy e = enemy.next();

            if (e.isAttacking()) {
                Rectangle pRect = player.getBounds();
                Rectangle eRect = e.getBounds();
                if (pRect.intersects(eRect)) {
                    if (!player.isKnockBacking() && !player.isInvincible()){
                        int damage = e.getDamage();
                        player.getHurt(damage);
                        player.knockBack(e.getCenterX(), e.getCenterY(), damage / 3, 30);
                        if (player.getHealth() <= 0 && !isGameOver) {
                            disablePlayerInput();
                            isGameOver = true;
                            gameOverPanel.triggerFadeIn();
                        }
                    }
                }
            }
        }
        Iterator<Coin> coin = coins.iterator();
        while (coin.hasNext()) {
            Coin c = coin.next();

            Rectangle pRect = player.getBounds();
            Rectangle cRect = c.getBounds();
            if (pRect.intersects(cRect)) {
                player.increaseExp(c.getExp());
                coin.remove();
            }
        }
    }
    
    void deleteOutOfScreenBullets() {
        Iterator<Bullet> bullet = bullets.iterator();
        while (bullet.hasNext()) {
            Bullet b = bullet.next();
            if (b.outOfScreen()) {
                bullet.remove();
            }
        }
    }

    public void pauseGame() {
        paused = true;
    }

    public void resumeGame() {
        paused = false;
        if (stopSpawmEnemies && !enemyFall) timer2.start();
        else if (!stopSpawmEnemies && enemyFall) timer4.start();
        else {
            stopSpawmEnemies = false;
            enemyFall = false;
            timer4.start();
        }
    }

    public void restartGame() {
        Constants.getActualHP();
        Constants.getActualSTR();
        Constants.getActualDEX();
        Constants.getActualEnergy();

        player = new Player(Constants.FRAMEWIDTH / 2, 
                            Constants.FRAMEHEIGHT - 2 * Constants.PLAYERHEIGHT, 
                            Constants.PLAYERWIDTH, 
                            Constants.PLAYERHEIGHT, 
                            Color.green, 
                            (int) Constants.playerActualHP, 
                            (int) Constants.playerActualEnergy);
        
        campFire = new CampFire(640, 600);
    
        enemies.clear();
        bullets.clear();
        coins.clear();
        
        paused = false;
        playerInput = true;
        enemyMove = true;
        isGameOver = false;
        showBonfireText = false;
        inBonfire = false;

        spawnEnemiesDelay = spawnEnemiesMaxDelay;
        stopSpawmEnemies = false;
        enemyFall = false;
        changeMusic = false;

        wPressed = false;
        aPressed = false;
        sPressed = false;
        dPressed = false;
        ePressed = false;
        escPressed = false;
        spacePressed = 0;
    
        mouseLeftClicked = 0;
        mouseRightClicked = 0;
    
        leftClickX = 0;
        leftClickY = 0;

        mouseX = 0;
        mouseY = 0;

        timer1.start();
        timer4.start();
    }

    public void disablePlayerInput() {
        playerInput = true;

        wPressed = false;
        aPressed = false;
        sPressed = false;
        dPressed = false;
        ePressed = false;
        escPressed = false;
        rPressed = 0;
        spacePressed = 0;
    
        mouseLeftClicked = 0;
        mouseRightClicked = 0;
    
        leftClickX = 0;
        leftClickY = 0;

        mouseX = 0;
        mouseY = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (playerInput) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> wPressed = true;
                case KeyEvent.VK_A -> aPressed = true;
                case KeyEvent.VK_S -> sPressed = true;
                case KeyEvent.VK_D -> dPressed = true;
                case KeyEvent.VK_E -> ePressed = true;
                case KeyEvent.VK_R -> rPressed = 1;
                case KeyEvent.VK_SPACE -> spacePressed = 1;
                case KeyEvent.VK_ESCAPE -> escPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (playerInput) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> wPressed = false;
                case KeyEvent.VK_A -> aPressed = false;
                case KeyEvent.VK_S -> sPressed = false;
                case KeyEvent.VK_D -> dPressed = false;
                case KeyEvent.VK_E -> ePressed = false;
                case KeyEvent.VK_R -> {
                    if(rPressed == 1) rPressed = 2;
                }
                case KeyEvent.VK_SPACE -> {
                    if(spacePressed == 1) spacePressed = 2;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (playerInput) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                mouseLeftClicked = 1;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                mouseRightClicked = 1;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (playerInput) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                leftClickX = e.getX();
                leftClickY = e.getY();
                mouseLeftClicked = 2;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                mouseRightClicked = 2;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {        
        if (playerInput) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    public void setSettingsPanel(SettingsPanel settingPanel) {
        this.settingsPanel = settingPanel;
    }

    public void setGameOverPanel(GameOverPanel gameOverPanel) {
        this.gameOverPanel = gameOverPanel;
    }

    public void setLevelUpPanel(LevelUpPanel levelUpPanel) {
        this.levelUpPanel = levelUpPanel;
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }
}
