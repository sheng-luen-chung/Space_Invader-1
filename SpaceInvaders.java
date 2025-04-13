import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    Timer timer;
    boolean leftPressed = false, rightPressed = false, spacePressed = false;
    int playerX = 250;
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();

    public SpaceInvaders() {
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(15, this);
        timer.start();
        spawnEnemies();
    }

    void spawnEnemies() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 8; j++)
                enemies.add(new Enemy(60 + j * 50, 30 + i * 30));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 350, 40, 10);
        for (Bullet b : bullets) b.draw(g);
        for (Enemy e : enemies) e.draw(g);
    }

    public void actionPerformed(ActionEvent e) {
        if (leftPressed && playerX > 0) playerX -= 5;
        if (rightPressed && playerX < 560) playerX += 5;
        if (spacePressed) {
            bullets.add(new Bullet(playerX + 18, 340));
            spacePressed = false; // 避免按住連發
        }

        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.move();
            if (b.y < 0) it.remove();
        }

        for (Enemy enemy : enemies) enemy.move();
        checkCollisions();
        repaint();
    }

    void checkCollisions() {
        Iterator<Bullet> bulletIt = bullets.iterator();
        while (bulletIt.hasNext()) {
            Bullet b = bulletIt.next();
            Iterator<Enemy> enemyIt = enemies.iterator();
            while (enemyIt.hasNext()) {
                Enemy e = enemyIt.next();
                if (b.getBounds().intersects(e.getBounds())) {
                    bulletIt.remove();
                    enemyIt.remove();
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SpaceInvaders());
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class Bullet {
        int x, y;

        Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void move() {
            y -= 8;
        }

        void draw(Graphics g) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, 4, 10);
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, 4, 10);
        }
    }

    class Enemy {
        int x, y;
        int dx = 1;

        Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void move() {
            x += dx;
            if (x < 0 || x > 560) {
                dx = -dx;
                y += 20;
            }
        }

        void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, 30, 15);
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, 30, 15);
        }
    }
}
