import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

public class SpaceInvaderPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{
    Timer timer;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    Player player;
    
    private boolean paused = false;

    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private boolean escPressed = false;
    private boolean spacePressed = false;

    private int mouseLeftClicked = 0;
    private int mouseRightClicked = 0;

    private int leftClickX = 0;
    private int leftClickY = 0;

    private int mouseX = 0;
    private int mouseY = 0;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public SpaceInvaderPanel(CardLayout cl, JPanel cp) {
        setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        // requestFocusInWindow();
        cardPanel = cp;
        cardLayout = cl;

        timer = new Timer(15, this);
        timer.start();

        spawnEnemies();
        player = new Player(Constants.FRAMEWIDTH / 2, Constants.FRAMEHEIGHT - 2 * Constants.PLAYERHEIGHT, 
                            Constants.PLAYERWIDTH, Constants.PLAYERHEIGHT, Color.green, Constants.PLAYERHEALTH);
    }

    void spawnEnemies() {
        for (int i = 0; i < Constants.FRAMEWIDTH; i+=120) {
            for (int j = 0; j < 2; j++){
                enemies.add(new Enemy(60 + i, 100 + j * 50, 30, 30, Color.red, 100));
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        player.draw(g);
        for(Enemy enemy : enemies) {
            enemy.draw(g);
        }
        for(Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    public void actionPerformed(ActionEvent e) {
        requestFocusInWindow();
        if(!paused) {
            player.move(wPressed, aPressed, sPressed, dPressed);
            for(Enemy enemy : enemies) {
                enemy.move(player.getCenterX(), player.getCenterY());
            }
            for(Bullet bullet : bullets) {
                bullet.move();
            }
            
            deleteOutOfScreenBullets();

            if (mouseLeftClicked == 2) {
                double bulletDx = (double) (leftClickX - player.getCenterX());
                double bulletDy = (double) (leftClickY - player.getCenterY());
                double dxdy = Math.sqrt(bulletDx * bulletDx + bulletDy * bulletDy);
                if (dxdy != 0) {
                    bulletDx /= dxdy;
                    bulletDy /= dxdy;
                }
                bullets.add(new Bullet((int) player.getCenterX() - 5, (int) player.getCenterY() - 5, 10, 10, Color.yellow, 
                            bulletDx, bulletDy));
                mouseLeftClicked = 0;
            }

            if(escPressed) {
                pauseGame();
                escPressed = false;
                cardLayout.show(cardPanel,"setting");
            }
                
            checkCollisions();
        }
        repaint();
    }

    void checkCollisions() {
        Iterator<Bullet> bullet = bullets.iterator();
        while (bullet.hasNext()) {
            Bullet b = bullet.next();

            Iterator<Enemy> enemy = enemies.iterator();
            while (enemy.hasNext()) {
                Enemy e = enemy.next();

                if (b.getBounds().intersects(e.getBounds())) {
                    bullet.remove();
                    e.getHurt(b.getDamage());
                    if (e.getHealth() <= 0) {
                        enemy.remove();
                    }
                    break;
                }
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
    }

    public void restartGame() {
        player = new Player(Constants.FRAMEWIDTH / 2, Constants.FRAMEHEIGHT - 2 * Constants.PLAYERHEIGHT, 
                            Constants.PLAYERWIDTH, Constants.PLAYERHEIGHT, Color.green, Constants.PLAYERHEALTH);
    
        enemies.clear();
        bullets.clear();
    
        spawnEnemies();

        wPressed = false;
        aPressed = false;
        sPressed = false;
        dPressed = false;
        escPressed = false;
        spacePressed = false;
    
        mouseLeftClicked = 0;
        mouseRightClicked = 0;
    
        leftClickX = 0;
        leftClickY = 0;

        mouseX = 0;
        mouseY = 0;

        paused = false;

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> wPressed = true;
            case KeyEvent.VK_A -> aPressed = true;
            case KeyEvent.VK_S -> sPressed = true;
            case KeyEvent.VK_D -> dPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
            case KeyEvent.VK_ESCAPE -> escPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> wPressed = false;
            case KeyEvent.VK_A -> aPressed = false;
            case KeyEvent.VK_S -> sPressed = false;
            case KeyEvent.VK_D -> dPressed = false;
            case KeyEvent.VK_SPACE -> spacePressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        // System.out.println("Mouse Pressed at: " + leftClickX + ", " + leftClickY);
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseLeftClicked = 1;
        }
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseRightClicked = 1;
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
        if (SwingUtilities.isLeftMouseButton(e)) {
            leftClickX = e.getX();
            leftClickY = e.getY();
            mouseLeftClicked = 2;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            mouseLeftClicked = 2;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
}
