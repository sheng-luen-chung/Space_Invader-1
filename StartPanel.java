import java.awt.*;
import javax.swing.*;

public class StartPanel extends JPanel {

    public StartPanel(CardLayout cardLayout, JPanel cardPanel, SpaceInvaderPanel gamPanel) {
        setLayout(new GridBagLayout());

        JButton startButton = new JButton("開始");
        JButton settingsButton = new JButton("設定");

        startButton.addActionListener(e -> {cardLayout.show(cardPanel,"game");gamPanel.resumeGame();});
        settingsButton.addActionListener(e -> cardLayout.show(cardPanel, "setting"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(settingsButton);

        add(buttonPanel);
    }
}
