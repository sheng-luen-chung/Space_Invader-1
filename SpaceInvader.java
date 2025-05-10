import java.awt.Dimension;
import javax.swing.*;

public class SpaceInvader {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // 播放背景音樂
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playSegment("StartMenu", 0f, 186f, true);

        // 建立 layeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));
        frame.setContentPane(layeredPane);

        // 建立遊戲面板
        SpaceInvaderPanel gamePanel = new SpaceInvaderPanel(layeredPane);
        gamePanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);

        // 建立 StartPanel
        StartPanel startPanel = new StartPanel(gamePanel, musicPlayer);
        startPanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);

        // 建立 SettingsPanel
        SettingsPanel settingsPanel = new SettingsPanel(gamePanel, startPanel, layeredPane, musicPlayer);
        settingsPanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);

        // 建立 GameOverPanel
        GameOverPanel gameOverPanel = new GameOverPanel(gamePanel, musicPlayer);
        gameOverPanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);
        gameOverPanel.setVisible(false);

        LevelUpPanel levelUpPanel = new LevelUpPanel(gamePanel, musicPlayer);
        levelUpPanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);
        levelUpPanel.setVisible(false);

        // 傳遞面板引用給其他面板
        gamePanel.setSettingsPanel(settingsPanel);
        gamePanel.setGameOverPanel(gameOverPanel);
        gamePanel.setLevelUpPanel(levelUpPanel);
        gamePanel.setMusicPlayer(musicPlayer);
        startPanel.setSettingsPanel(settingsPanel);

        // 加入面板至 layeredPane
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(startPanel, JLayeredPane.MODAL_LAYER);        // 第2層
        layeredPane.add(settingsPanel, JLayeredPane.POPUP_LAYER);     // 第3層
        layeredPane.add(gameOverPanel, Integer.valueOf(4));           // 最上層
        layeredPane.add(levelUpPanel, Integer.valueOf(5));           // 最上層

        // 顯示開始畫面、暫停遊戲
        gamePanel.pauseGame();
        startPanel.setVisible(true);
        settingsPanel.setVisible(false);

        // 視窗顯示設定
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // 設定 musicPlayer 至需要的面板
        startPanel.setMusicPlayer(musicPlayer);
        settingsPanel.setMusicPlayer(musicPlayer);
    }
}
