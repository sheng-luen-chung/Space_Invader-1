import java.awt.*;
import javax.swing.*;

public class LevelUpPanel extends JPanel {
    private boolean isBlackBackground = false;
    private SpaceInvaderPanel gamePanel;
    private MusicPlayer musicPlayer;
    private boolean confirmLevelUp = false;
    private JLabel hpTextLabel;
    private JLabel strTextLabel;
    private JLabel dexTextLabel;

    private JLabel totalLevelLabel;
    private JLabel levelUpCostLabel;
    private JLabel nextLevelUpCostLabel;
    private JLabel currentExpLabel;

    public LevelUpPanel(SpaceInvaderPanel gamePanel, MusicPlayer musicPlayer) {
        this.gamePanel = gamePanel;
        this.musicPlayer = musicPlayer;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(createLevelInfoPanel());
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createLevelControlPanel());
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(createConfirmExitPanel());

        add(Box.createVerticalGlue());
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }

    private JPanel createLevelInfoPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(1, 3, 20, 0));
        panel.setMaximumSize(new Dimension(800, 30));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        totalLevelLabel = new JLabel();
        levelUpCostLabel = new JLabel();
        nextLevelUpCostLabel = new JLabel();
        currentExpLabel = new JLabel();
    
        Font font = new Font("SansSerif", Font.BOLD, 18);
        Color color = Color.WHITE;
    
        totalLevelLabel.setFont(font);
        totalLevelLabel.setForeground(color);
        levelUpCostLabel.setFont(font);
        levelUpCostLabel.setForeground(color);
        nextLevelUpCostLabel.setFont(font);
        nextLevelUpCostLabel.setForeground(color);
        currentExpLabel.setFont(font);
        currentExpLabel.setForeground(color);
    
        panel.add(totalLevelLabel);
        panel.add(levelUpCostLabel);
        panel.add(nextLevelUpCostLabel);
        panel.add(currentExpLabel);
    
        updateLevelInfo(); // 初始顯示
    
        return panel;
    }

    private JPanel createLevelControlPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setOpaque(false);
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel hpPanel = new JPanel();
        hpPanel.setOpaque(false);
        hpPanel.setLayout(new BoxLayout(hpPanel, BoxLayout.X_AXIS));
        addHPUI(hpPanel);

        JPanel strPanel = new JPanel();
        strPanel.setOpaque(false);
        strPanel.setLayout(new BoxLayout(strPanel, BoxLayout.X_AXIS));
        addSTRUI(strPanel);

        JPanel dexPanel = new JPanel();
        dexPanel.setOpaque(false);
        dexPanel.setLayout(new BoxLayout(dexPanel, BoxLayout.X_AXIS));
        addDEXUI(dexPanel);

        outerPanel.add(hpPanel);
        outerPanel.add(Box.createVerticalStrut(10)); // 間隔
        outerPanel.add(strPanel);
        outerPanel.add(Box.createVerticalStrut(10)); // 間隔
        outerPanel.add(dexPanel);

        return outerPanel;
    }

    private JPanel createConfirmExitPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JButton confirmButton = new JButton("確認");
        JButton exitButton = new JButton("離開");
    
        styleTextOnlyButton(confirmButton);
        styleTextOnlyButton(exitButton);
    
        confirmButton.addActionListener(e -> confirmLevelUp());
        exitButton.addActionListener(e -> exitLevelUp());
    
        panel.add(Box.createHorizontalGlue());
        panel.add(confirmButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(exitButton);
        panel.add(Box.createHorizontalGlue());
    
        return panel;
    }    

    private void addHPUI(JPanel levelPanel) {
        JButton decreaseButton = new JButton("-");
        JLabel label = new JLabel("血量");
        hpTextLabel = new JLabel(String.format("%d", Constants.playerHPLevel));
        JButton increaseButton = new JButton("+");

        styleTextOnlyButton(decreaseButton);
        styleTextOnlyButton(increaseButton);

        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        hpTextLabel.setForeground(Color.WHITE);
        hpTextLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        Constants.playerHPLevelTemp = Constants.playerHPLevel;
        decreaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (Constants.playerHPLevelTemp > Constants.playerHPLevel) Constants.playerHPLevelTemp -= 1;
            if (!confirmLevelUp && Constants.playerHPLevelTemp != Constants.playerHPLevel) {
                hpTextLabel.setForeground(Color.BLUE);
                hpTextLabel.setText(String.format("%d", Constants.playerHPLevelTemp));
            }
            else {
                hpTextLabel.setForeground(Color.WHITE);
                hpTextLabel.setText(String.format("%d", Constants.playerHPLevel));
            }
            updateLevelInfo();
        });
        increaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (Constants.playerHPLevelTemp < Constants.PLAYERMAXLEVEL && 
                gamePanel.player.getExp() - (int) Constants.levelUpCost() > (int) Constants.nextLevelUpCost()) 
                Constants.playerHPLevelTemp += 1;
            if (!confirmLevelUp && Constants.playerHPLevelTemp != Constants.playerHPLevel) {
                hpTextLabel.setForeground(Color.BLUE);
                hpTextLabel.setText(String.format("%d", Constants.playerHPLevelTemp));
            }
            else {
                hpTextLabel.setForeground(Color.WHITE);
                hpTextLabel.setText(String.format("%d", Constants.playerHPLevel));
            }
            updateLevelInfo();
        });

        levelPanel.add(decreaseButton);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(label);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(hpTextLabel);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(increaseButton);
    }

    private void addSTRUI(JPanel levelPanel) {
        JButton decreaseButton = new JButton("-");
        JLabel label = new JLabel("力量");
        strTextLabel = new JLabel(String.format("%d", Constants.playerSTRLevel));
        JButton increaseButton = new JButton("+");
    
        styleTextOnlyButton(decreaseButton);
        styleTextOnlyButton(increaseButton);
    
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    
        strTextLabel.setForeground(Color.WHITE);
        strTextLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    
        Constants.playerSTRLevelTemp = Constants.playerSTRLevel;
        decreaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (Constants.playerSTRLevelTemp > Constants.playerSTRLevel) Constants.playerSTRLevelTemp -= 1;
            if (!confirmLevelUp && Constants.playerSTRLevelTemp != Constants.playerSTRLevel) {
                strTextLabel.setForeground(Color.BLUE);
                strTextLabel.setText(String.format("%d", Constants.playerSTRLevelTemp));
            } else {
                strTextLabel.setForeground(Color.WHITE);
                strTextLabel.setText(String.format("%d", Constants.playerSTRLevel));
            }
            updateLevelInfo();
        });
        increaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (Constants.playerSTRLevelTemp < Constants.PLAYERMAXLEVEL &&
                gamePanel.player.getExp() - (int) Constants.levelUpCost() > (int) Constants.nextLevelUpCost())
                Constants.playerSTRLevelTemp += 1;
            if (!confirmLevelUp && Constants.playerSTRLevelTemp != Constants.playerSTRLevel) {
                strTextLabel.setForeground(Color.BLUE);
                strTextLabel.setText(String.format("%d", Constants.playerSTRLevelTemp));
            } else {
                strTextLabel.setForeground(Color.WHITE);
                strTextLabel.setText(String.format("%d", Constants.playerSTRLevel));
            }
            updateLevelInfo();
        });
    
        levelPanel.add(decreaseButton);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(label);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(strTextLabel);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(increaseButton);
    }
    
    private void addDEXUI(JPanel levelPanel) {
        JButton decreaseButton = new JButton("-");
        JLabel label = new JLabel("敏捷");
        dexTextLabel = new JLabel(String.format("%d", Constants.playerDEXLevel));
        JButton increaseButton = new JButton("+");
    
        styleTextOnlyButton(decreaseButton);
        styleTextOnlyButton(increaseButton);
    
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    
        dexTextLabel.setForeground(Color.WHITE);
        dexTextLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    
        Constants.playerDEXLevelTemp = Constants.playerDEXLevel;
        decreaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (Constants.playerDEXLevelTemp > Constants.playerDEXLevel) Constants.playerDEXLevelTemp -= 1;
            if (!confirmLevelUp && Constants.playerDEXLevelTemp != Constants.playerDEXLevel) {
                dexTextLabel.setForeground(Color.BLUE);
                dexTextLabel.setText(String.format("%d", Constants.playerDEXLevelTemp));
            } else {
                dexTextLabel.setForeground(Color.WHITE);
                dexTextLabel.setText(String.format("%d", Constants.playerDEXLevel));
            }
            updateLevelInfo();
        });
        increaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (Constants.playerDEXLevelTemp < Constants.PLAYERMAXLEVEL &&
                gamePanel.player.getExp() - (int) Constants.levelUpCost() > (int) Constants.nextLevelUpCost())
                Constants.playerDEXLevelTemp += 1;
            if (!confirmLevelUp && Constants.playerDEXLevelTemp != Constants.playerDEXLevel) {
                dexTextLabel.setForeground(Color.BLUE);
                dexTextLabel.setText(String.format("%d", Constants.playerDEXLevelTemp));
            } else {
                dexTextLabel.setForeground(Color.WHITE);
                dexTextLabel.setText(String.format("%d", Constants.playerDEXLevel));
            }
            updateLevelInfo();
        });
    
        levelPanel.add(decreaseButton);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(label);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(dexTextLabel);
        levelPanel.add(Box.createHorizontalStrut(10));
        levelPanel.add(increaseButton);
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

    private void confirmLevelUp() {
        gamePanel.player.decreaseExp((int) Constants.levelUpCost());

        confirmLevelUp = true;
        Constants.playerHPLevel = Constants.playerHPLevelTemp;
        Constants.playerSTRLevel = Constants.playerSTRLevelTemp;
        Constants.playerDEXLevel = Constants.playerDEXLevelTemp;
        
        hpTextLabel.setForeground(Color.WHITE);
        hpTextLabel.setText(String.format("%d", Constants.playerHPLevel));
        
        strTextLabel.setForeground(Color.WHITE);
        strTextLabel.setText(String.format("%d", Constants.playerSTRLevel));
        
        dexTextLabel.setForeground(Color.WHITE);
        dexTextLabel.setText(String.format("%d", Constants.playerDEXLevel));

        Constants.getActualHP();
        Constants.getActualSTR();
        Constants.getActualDEX();
        Constants.getActualEnergy();
    }
    
    private void exitLevelUp() {
        this.setVisible(false);
        gamePanel.resumeGame();
        gamePanel.player.restoreHealth();
        gamePanel.player.restoreEnergy();
    }
    
    private void updateLevelInfo() {
        int totalLevel = Constants.playerHPLevelTemp + Constants.playerSTRLevelTemp + Constants.playerDEXLevelTemp;
        int cost = (int) Constants.levelUpCost();
    
        totalLevelLabel.setText("目前等級: " + totalLevel);
        levelUpCostLabel.setText("升級花費: " + (int) Constants.levelUpCost());
        nextLevelUpCostLabel.setText("下次升級花費: " + (int) Constants.nextLevelUpCost());
        currentExpLabel.setText("目前經驗: " + (gamePanel.player.getExp() - cost));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }
}
