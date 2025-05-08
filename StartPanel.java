import java.awt.*;
import javax.swing.*;

public class StartPanel extends JPanel {
    private SettingsPanel settingsPanel; // 設定面板的參考
    private MusicPlayer musicPlayer;

    public StartPanel(SpaceInvaderPanel gamePanel, MusicPlayer musicPlayer) {
        setLayout(new BorderLayout()); // 設定佈局為 BorderLayout
        setOpaque(true); // 設定背景不透明

        // 設定背景顏色
        setBackground(new Color(0, 0, 0)); // 例如設定為黑色背景

        // 顯示 "SPACE INVADER" 標題
        JLabel titleLabel = new JLabel("SPACE INVADER", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 50)); // 設定字型和大小
        titleLabel.setForeground(Color.WHITE); // 設定文字顏色為白色

        // 給標題加上上邊距，讓它距離上邊緣更遠
        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50)); // 設置上下邊距
        titleWrapper.setOpaque(false); // 設定透明
        titleWrapper.add(titleLabel); // 加入標題文字

        // 將標題包裝器加入到面板的上方
        add(titleWrapper, BorderLayout.NORTH);

        // 建立兩個按鈕，分別是開始遊戲和設定
        JButton startButton = new JButton("開始遊戲");
        JButton settingsButton = new JButton("設定");

        // 設定按鈕為僅顯示文字樣式
        styleTextOnlyButton(startButton);
        styleTextOnlyButton(settingsButton);

        // 當點擊開始遊戲按鈕時，隱藏當前畫面，並開始遊戲
        startButton.addActionListener(e -> {
            this.setVisible(false);
            gamePanel.restartGame(); // 重新啟動遊戲
            gamePanel.resumeGame();  // 繼續遊戲
            settingsPanel.setBlackBackground(false);
            settingsPanel.showResumeButton(true);
            musicPlayer.stopMusic();
        });

        // 當點擊設定按鈕時，隱藏當前畫面並顯示設定面板
        settingsButton.addActionListener(e -> {
            if (settingsPanel != null) {
                this.setVisible(false);
                settingsPanel.setVisible(true); // 顯示設定面板
                settingsPanel.setOpaque(true);
                settingsPanel.setBlackBackground(true);
                settingsPanel.showResumeButton(false);
            } else {
                System.err.println("SettingsPanel 尚未初始化！");
            }
        });

        // 創建一個按鈕區塊，將按鈕垂直排列
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // 讓按鈕區域背景透明
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // 設置垂直排列

        // 將開始遊戲按鈕放在一個流佈局容器中，並將其置中
        JPanel startWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        startWrapper.setOpaque(false); // 設定容器透明
        startWrapper.add(startButton); // 添加開始遊戲按鈕

        // 將設定按鈕放在另一個流佈局容器中，並將其置中
        JPanel settingsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        settingsWrapper.setOpaque(false); // 設定容器透明
        settingsWrapper.add(settingsButton); // 添加設定按鈕

        // 將兩個按鈕容器加入按鈕區塊
        buttonPanel.add(startWrapper);
        buttonPanel.add(Box.createVerticalStrut(15)); // 在按鈕之間添加 15 像素的間距
        buttonPanel.add(settingsWrapper);

        // 創建一個包裝容器，並將按鈕區塊放到底部，設置與底部的間距
        JPanel bottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 置中對齊
        bottomWrapper.setOpaque(false); // 設定透明
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 150, 0)); // 設定底部留空 150 像素
        bottomWrapper.add(buttonPanel); // 將按鈕區塊加入到包裝容器

        // 最後將包裝容器加入到面板的底部
        add(bottomWrapper, BorderLayout.SOUTH);
    }

    // 設定按鈕樣式，僅顯示文字且沒有邊框和背景
    private void styleTextOnlyButton(JButton button) {
        button.setFocusPainted(false); // 不顯示焦點邊框
        button.setBorderPainted(false); // 不顯示邊框
        button.setContentAreaFilled(false); // 不顯示內容區域的背景
        button.setForeground(Color.WHITE); // 設定文字顏色為白色
        button.setFont(new Font("SansSerif", Font.BOLD, 20)); // 設定字型和大小

        // 設定滑鼠進入時顯示邊框，離開時隱藏邊框
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBorderPainted(true); // 滑鼠進入時顯示邊框
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBorderPainted(false); // 滑鼠離開時隱藏邊框
            }
        });
    }

    // 設定 SettingsPanel 的參考
    public void setSettingsPanel(SettingsPanel settingPanel) {
        this.settingsPanel = settingPanel;
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }
}
