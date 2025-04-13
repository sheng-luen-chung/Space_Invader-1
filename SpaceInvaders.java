import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

// 主類別，繼承 JPanel 並實作定時器與鍵盤監聽
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {

    Timer timer;  // 遊戲主迴圈的計時器
    boolean leftPressed = false, rightPressed = false, spacePressed = false; // 控制鍵狀態
    int playerX = 250;  // 玩家飛船的 x 座標
    ArrayList<Bullet> bullets = new ArrayList<>();  // 玩家子彈列表
    ArrayList<Enemy> enemies = new ArrayList<>();   // 敵人列表

    // 建構子，初始化遊戲介面
    public SpaceInvaders() {
        setPreferredSize(new Dimension(600, 400)); // 設定畫面大小
        setBackground(Color.BLACK);                // 背景黑色
        setFocusable(true);
        addKeyListener(this);                      // 啟用鍵盤輸入

        timer = new Timer(15, this);               // 每 15 毫秒更新一次
        timer.start();

        spawnEnemies();                            // 生成敵人
    }

    // 建立敵人，依排排站的方式生成
    void spawnEnemies() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                enemies.add(new Enemy(60 + j * 50, 30 + i * 30));
            }
        }
    }

    // 畫面繪製
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫玩家飛船
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 350, 40, 10);

        // 畫子彈
        for (Bullet b : bullets)
            b.draw(g);

        // 畫敵人
        for (Enemy e : enemies)
            e.draw(g);
    }

    // 每次 Timer 觸發時執行（遊戲邏輯更新）
    public void actionPerformed(ActionEvent e) {
        // 玩家控制
        if (leftPressed && playerX > 0)
            playerX -= 5;
        if (rightPressed && playerX < 560)
            playerX += 5;

        // 發射子彈（按一下空白鍵就射一發）
        if (spacePressed) {
            bullets.add(new Bullet(playerX + 18, 340)); // 子彈從飛船上方出發
            spacePressed = false; // 避免長按發射多發
        }

        // 移動子彈並移除飛出邊界的子彈
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.move();
            if (b.y < 0)
                it.remove();
        }

        // 移動敵人
        for (Enemy enemy : enemies)
            enemy.move();

        // 檢查碰撞（子彈 vs 敵人）
        checkCollisions();

        repaint(); // 重繪畫面
    }

    // 子彈和敵人之間的碰撞檢查
    void checkCollisions() {
        Iterator<Bullet> bulletIt = bullets.iterator();
        while (bulletIt.hasNext()) {
            Bullet b = bulletIt.next();

            Iterator<Enemy> enemyIt = enemies.iterator();
            while (enemyIt.hasNext()) {
                Enemy e = enemyIt.next();

                if (b.getBounds().intersects(e.getBounds())) {
                    bulletIt.remove();  // 移除子彈
                    enemyIt.remove();   // 移除敵人
                    break;
                }
            }
        }
    }

    // 鍵盤按下時
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
        }
    }

    // 鍵盤放開時
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {} // 不使用此方法

    // 主程式進入點
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SpaceInvaders());
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // 子彈類別
    class Bullet {
        int x, y;

        Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void move() {
            y -= 8; // 子彈往上移動
        }

        void draw(Graphics g) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, 4, 10); // 畫出子彈
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, 4, 10); // 用來檢查碰撞的矩形區域
        }
    }

    // 敵人類別
    class Enemy {
        int x, y;
        int dx = 1; // 水平移動方向

        Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void move() {
            x += dx; // 左右移動
            if (x < 0 || x > 560) {
                dx = -dx; // 碰到邊界就反向
                y += 20;  // 同時向下移動
            }
        }

        void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, 30, 15); // 畫出敵人
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, 30, 15); // 用來檢查碰撞的區域
        }
    }
}
