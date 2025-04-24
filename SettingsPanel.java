import java.awt.*;
import javax.swing.*;

public class SettingsPanel extends JPanel {

    public SettingsPanel(CardLayout cardLayout, JPanel cardPanel, SpaceInvaderPanel gamPanel) {
        // 垂直排列元件
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // 加點邊距
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 音效選項
        JCheckBox soundCheck = new JCheckBox("開啟音效", true);
        JCheckBox musicCheck = new JCheckBox("背景音樂", true);

        // 返回按鈕
        JButton backButton = new JButton("返回");
        JButton resume = new JButton("繼續");
        // backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 水平置中
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel,"start");
            gamPanel.restartGame();
        });
        resume.addActionListener(e -> {
            cardLayout.show(cardPanel,"game");
            gamPanel.resumeGame();
        });

        // 加入元件與間距
        add(soundCheck);
        add(Box.createVerticalStrut(10)); // 垂直空格
        add(musicCheck);
        add(Box.createVerticalStrut(20));
        add(backButton);
        add(Box.createVerticalStrut(10));
        add(resume);
    }
}
