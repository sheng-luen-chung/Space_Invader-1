import java.awt.*;

public class Bullet{
    public double x, y;
    public int width, height;
    public int health;
    public Color color;
    public int speed = 7;
    public int damage = 20;
    public double  dx = 0;
    public double dy = 0;
    public String tag;
    public MusicPlayer musicPlayer;

    public Bullet() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.health = 0;
        this.color = null;
        this.dx = 0;
        this.dy = 0;
        this.speed = 0;
        this.damage = 0;
        this.tag = null;
        this.musicPlayer = null;
    }

    public Bullet(int x, int y, int w, int h, Color c, double dx, double dy, int speed, int damage, int health, String tag, MusicPlayer musicPlayer) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.health = health;
        this.color = c;
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        this.damage = damage;
        this.tag = tag;
        this.musicPlayer = musicPlayer;
        musicPlayer.playSegment("FireBall", 0.0f, 2.0f, false);
    }

    public void move() {
        x += dx * speed;
        y += dy * speed;
    }

    public void getHurt(int damage) {
        health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getCenterX() {
        return x + width / 2;
    }

    public double getCenterY() {
        return y + height / 2;
    }

    public String getTag() {
        return tag;
    }

    public void draw(Graphics g) {
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        double _w = (double) (width / 2.4);
        double _h = (double) (height / 2.4);
        double _x = centerX - 17 * dx - _w / 2;
        double _y = centerY - 17 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 2.2);
        _h = (double) (height / 2.2);
        _x = centerX - 15 * dx - _w / 2;
        _y = centerY - 15 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 2);
        _h = (double) (height / 2);
        _x = centerX - 14 * dx - _w / 2;
        _y = centerY - 14 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 1.9);
        _h = (double) (height / 1.9);
        _x = centerX - 13 * dx - _w / 2;
        _y = centerY - 13 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 1.7);
        _h = (double) (height / 1.7);
        _x = centerX - 12 * dx - _w / 2;
        _y = centerY - 12 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 1.5);
        _h = (double) (height / 1.5);
        _x = centerX - 11 * dx - _w / 2;
        _y = centerY - 11 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 1.3);
        _h = (double) (height / 1.3);
        _x = centerX - 9 * dx - _w / 2;
        _y = centerY - 9 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 1.3);
        _h = (double) (height / 1.3);
        _x = centerX - 7 * dx - _w / 2;
        _y = centerY - 7 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 1.1);
        _h = (double) (height / 1.1);
        _x = centerX - 5 * dx - _w / 2;
        _y = centerY - 5 * dy - _h / 2;
        g.setColor(Color.RED);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        g.setColor(Color.RED);
        g.fillOval((int) x, (int) y, width, height);
        _w = (double) (width / 1.2);
        _h = (double) (height / 1.2);
        _x = centerX + 1 * dx - _w / 2;
        _y = centerY + 1 * dy - _h / 2;
        g.setColor(Color.ORANGE);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
        _w = (double) (width / 1.7);
        _h = (double) (height / 1.7);
        _x = centerX + 3 * dx - _w / 2;
        _y = centerY + 3 * dy - _h / 2;
        g.setColor(Color.YELLOW);
        g.fillOval((int) (_x), (int) (_y), (int) (_w), (int) (_h));
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
