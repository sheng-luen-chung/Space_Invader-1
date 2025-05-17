import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Beam extends Bullet {
    public int[] xPoints = new int[4];
    public int[] yPoints = new int[4];

    public String tag;
    private MusicPlayer musicPlayer;
    public List<Bullet> beamsToAdd;
    private boolean beamAdded = false;

    public Beam(int x, int y, int w, int h, Color c, double dx, double dy, int speed, int damage, String tag, MusicPlayer musicPlayer) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.color = c;
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        this.damage = damage;
        this.tag = tag;
        this.musicPlayer = musicPlayer;
        this.beamsToAdd = new ArrayList<>();
        rotate();
    }
    
    @Override
    public void move() {
        if (!beamAdded) {
            // Bullet b = new Beam((int) (x + dx * speed), 
            //                     (int) (y + dy * speed), 
            //                     20, 20, 
            //                     Color.YELLOW, 
            //                     dx, 
            //                     dy, 
            //                     7, 
            //                     10, 
            //                     "Enemy", 
            //                     musicPlayer);
            // beamsToAdd.add(b);
            // beamAdded = true;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 4);
        rotate();
    }

    void rotate() {
        double angle = Math.atan2(dy, dx);

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        xPoints[0] = (int) x;
        yPoints[0] = (int) y;

        xPoints[1] = (int) (x + cos * height);
        yPoints[1] = (int) (y + sin * height);

        xPoints[2] = (int) (x + cos * height - sin * width);
        yPoints[2] = (int) (y + sin * height + cos * width);

        xPoints[3] = (int) (x - sin * width);
        yPoints[3] = (int) (y + cos * width);
    }

    public Rectangle getBounds() {
        System.out.println("apple");
        return new Rectangle((int) x, (int) y, width, height);
    }
}
