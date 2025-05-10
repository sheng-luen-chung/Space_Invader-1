import java.awt.*;
import javax.swing.*;

public class GameOverPanel extends JPanel {
    private float alpha = 0f; // 透明度從 0 到 1
    private Timer fadeTimer;
    private Timer restartTimer;
    private SpaceInvaderPanel gamePanel;
    private MusicPlayer musicPlayer;

    public GameOverPanel(SpaceInvaderPanel gamePanel, MusicPlayer musicPlayer) {
        this.gamePanel = gamePanel;
        this.musicPlayer = musicPlayer;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        add(Box.createVerticalGlue());

        JLabel gameOverLabel = new JLabel("YOU DIED");
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setOpaque(false);
        add(gameOverLabel);

        add(Box.createVerticalGlue());

        // 建立淡入動畫 Timer
        fadeTimer = new Timer(40, e -> {
            alpha += 0.05f;
            if (alpha >= 1f) {
                alpha = 1f;
                fadeTimer.stop();
            }
            repaint();
        });
    }

    public void triggerFadeIn() {
        alpha = 0f;
        setVisible(true);
        fadeTimer.start();
        musicPlayer.playSegment("YouDied", 0f, 10f, false);
        restartTimer = new Timer(8000, e -> {
            gamePanel.restartGame();
            GameOverPanel.this.setVisible(false);
            restartTimer.stop();
            musicPlayer.stopAll();
        });
        restartTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (alpha > 0f) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.SrcOver.derive(alpha));

            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
        }
    }
}
