import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

public class SpaceInvaderPanel extends JPanel implements ActionListener, KeyListener{
    Timer timer;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    Player player;
    
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    public SpaceInvaderPanel() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(15, this);
        timer.start();

        spawnEnemies();
        player = new Player(250, 350, 40, 10, Color.green);
    }

    void spawnEnemies() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                enemies.add(new Enemy(60 + j * 50, 30 + i * 30, 30, 15, Color.red));
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
        player.move(leftPressed, rightPressed);
        for(Enemy enemy : enemies) {
            enemy.move();
        }
        for(Bullet bullet : bullets) {
            bullet.move();
        }
        
        if (spacePressed) {
            bullets.add(new Bullet(player.getPosition() + 18, 340, 4, 10, Color.yellow));
            spacePressed = false;
        }
        
        checkCollisions();

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
                    enemy.remove();
                    break;
                }
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {}
}
