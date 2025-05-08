import java.awt.Dimension;
import javax.swing.*;

public class SpaceInvader {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // 播放背景音樂
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("music/StartMenu.wav", Constants.currentVolume);

        // 建立 layeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));
        frame.setContentPane(layeredPane);

        // 建立遊戲面板
        SpaceInvaderPanel gamePanel = new SpaceInvaderPanel(layeredPane);
        gamePanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);

        // 先建立 StartPanel
        StartPanel startPanel = new StartPanel(gamePanel, musicPlayer);
        startPanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);

        // 建立 SettingsPanel 並將 startPanel 傳入
        SettingsPanel settingsPanel = new SettingsPanel(gamePanel, startPanel, layeredPane, musicPlayer);
        settingsPanel.setBounds(0, 0, Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);

        // 設定 StartPanel 中的 startPanel 引用
        gamePanel.setSettingsPanel(settingsPanel);
        startPanel.setSettingsPanel(settingsPanel);  // 傳遞 settingsPanel 給 startPanel

        // 加入面板至 layeredPane
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(startPanel, JLayeredPane.MODAL_LAYER);      // 第2層
        layeredPane.add(settingsPanel, JLayeredPane.POPUP_LAYER);   // 第3層（最上層）

        // 預設顯示開始畫面，暫停遊戲
        gamePanel.pauseGame();
        startPanel.setVisible(true);
        settingsPanel.setVisible(false);

        frame.pack();
        frame.setLocationRelativeTo(null); // 視窗置中
        frame.setVisible(true);

        startPanel.setMusicPlayer(musicPlayer);
        settingsPanel.setMusicPlayer(musicPlayer);
    }
}
