import java.awt.*;
import javax.swing.*;

public class SettingsPanel extends JPanel {
    private StartPanel startPanel;
    private JLayeredPane layeredPane;
    private boolean isBlackBackground = false;
    private boolean showResumeButton = true;
    private MusicPlayer musicPlayer;

    private JButton resumeButton;

    public SettingsPanel(SpaceInvaderPanel gamePanel, StartPanel startPanel, JLayeredPane layeredPane, MusicPlayer musicPlayer) {
        this.startPanel = startPanel;
        this.layeredPane = layeredPane;
        this.musicPlayer = musicPlayer;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resumeButton = new JButton("繼續遊戲");
        JButton backButton = new JButton("返回主畫面");

        styleTextOnlyButton(resumeButton);
        styleTextOnlyButton(backButton);
        customizeButton(resumeButton);
        customizeButton(backButton);

        resumeButton.addActionListener(e -> {
            this.setVisible(false);
            gamePanel.resumeGame();
        });

        backButton.addActionListener(e -> {
            gamePanel.restartGame();
            gamePanel.pauseGame();
            this.setVisible(false);
            startPanel.setVisible(true);
            startPanel.revalidate();
            startPanel.repaint();
            layeredPane.moveToFront(startPanel);
            musicPlayer.playSegment("StartMenu", 0f, 186f, true);
        });

        buttonPanel.add(resumeButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createVolumeControlPanel()); // 加入音量調整元件
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);

        add(Box.createVerticalGlue());
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }

    private JPanel createVolumeControlPanel() {
        JPanel volumePanel = new JPanel();
        volumePanel.setOpaque(false);
        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.X_AXIS));
        volumePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton decreaseButton = new JButton("-");
        JLabel volumeLabel = new JLabel("音量大小");
        JLabel volumeTextLabel = new JLabel(musicPlayer.getVolumeText()); // 顯示當前音量
        JButton increaseButton = new JButton("+");

        // 統一樣式
        styleTextOnlyButton(decreaseButton);
        styleTextOnlyButton(increaseButton);
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        volumeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        volumeTextLabel.setForeground(Color.WHITE);
        volumeTextLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        decreaseButton.addActionListener(e -> {
            System.out.println("Decreasing volume..."); // 用於確認事件是否觸發
            musicPlayer.adjustVolume(-1.0f);
            volumeTextLabel.setText(musicPlayer.getVolumeText());  // 更新音量顯示
        });
        increaseButton.addActionListener(e -> {
            System.out.println("Increasing volume..."); // 用於確認事件是否觸發
            musicPlayer.adjustVolume(1.0f);
            volumeTextLabel.setText(musicPlayer.getVolumeText());  // 更新音量顯示
        });

        volumePanel.add(decreaseButton);
        volumePanel.add(volumeLabel);
        volumePanel.add(volumeTextLabel);
        volumePanel.add(increaseButton);

        return volumePanel;
    }

    private void customizeButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
    }

    private void styleTextOnlyButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBorderPainted(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBorderPainted(false);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(isBlackBackground ? Color.BLACK : new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    public void setBlackBackground(boolean isBlackBackground) {
        this.isBlackBackground = isBlackBackground;
    }

    public void setStartPanel(StartPanel startPanel) {
        this.startPanel = startPanel;
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void showResumeButton(boolean show) {
        this.showResumeButton = show;
        if (resumeButton != null) {
            resumeButton.setVisible(show);
        }
    }
}
