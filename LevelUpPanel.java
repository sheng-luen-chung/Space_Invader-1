import java.awt.*;
import javax.swing.*;

public class LevelUpPanel extends JPanel {
    private boolean confirmLevelUp = false;
    private SpaceInvaderPanel gamePanel;
    private MusicPlayer musicPlayer;

    private JLabel hpTextLabel, strTextLabel, dexTextLabel;
    private JLabel totalLevelLabel, nextLevelUpCostLabel, currentExpLabel;

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

        buttonPanel.add(Box.createVerticalStrut(100));
        buttonPanel.add(createLevelControlPanel());
        buttonPanel.add(createConfirmExitPanel());
        buttonPanel.add(Box.createVerticalStrut(200));

        add(Box.createVerticalGlue());
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }

    private JPanel createLevelInfoPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(300, 300));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalLevelLabel = new JLabel();
        nextLevelUpCostLabel = new JLabel();
        currentExpLabel = new JLabel();

        Font font = new Font("SansSerif", Font.BOLD, 20);
        Color color = Color.WHITE;

        JLabel[] labels = {totalLevelLabel, currentExpLabel, nextLevelUpCostLabel};
        panel.add(Box.createVerticalStrut(5));
        for (JLabel label : labels) {
            label.setFont(font);
            label.setForeground(color);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);
            panel.add(Box.createVerticalStrut(30));
        }

        updateLevelInfo();
        return panel;
    }

    private JPanel createLevelControlPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.anchor = GridBagConstraints.NORTHWEST;
    
        // Create the left-side level info panel
        JPanel levelInfoPanel = createLevelInfoPanel();
        mainPanel.add(levelInfoPanel, gbc);
    
        // Move to the next column for the right-side controls
        gbc.gridx = 1;
    
        // Create the right-side control panel
        JPanel levelControlPanel = new JPanel();
        levelControlPanel.setOpaque(false);
        levelControlPanel.setLayout(new BoxLayout(levelControlPanel, BoxLayout.Y_AXIS));
        levelControlPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        levelControlPanel.setMaximumSize(new Dimension(300, 300));
    
        // Create the panels for HP, STR, DEX UI
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
    
        // Add the panels to the right-side control panel
        levelControlPanel.add(hpPanel);
        levelControlPanel.add(Box.createVerticalStrut(20));
        levelControlPanel.add(strPanel);
        levelControlPanel.add(Box.createVerticalStrut(20));
        levelControlPanel.add(dexPanel);
    
        // Add the right-side control panel to the main panel
        mainPanel.add(levelControlPanel, gbc);
        return mainPanel;
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

    private void addHPUI(JPanel panel) {
        setupStatPanel(panel, "血量", () -> Constants.playerHPLevel, 
            () -> Constants.playerHPLevelTemp, v -> Constants.playerHPLevelTemp = v, 
            v -> hpTextLabel.setText(v), () -> hpTextLabel);
    }

    private void addSTRUI(JPanel panel) {
        setupStatPanel(panel, "力量", () -> Constants.playerSTRLevel, 
            () -> Constants.playerSTRLevelTemp, v -> Constants.playerSTRLevelTemp = v, 
            v -> strTextLabel.setText(v), () -> strTextLabel);
    }

    private void addDEXUI(JPanel panel) {
        setupStatPanel(panel, "敏捷", () -> Constants.playerDEXLevel, 
            () -> Constants.playerDEXLevelTemp, v -> Constants.playerDEXLevelTemp = v, 
            v -> dexTextLabel.setText(v), () -> dexTextLabel);
    }

    private void setupStatPanel(JPanel panel, String labelName, StatGetter baseLevelGetter, StatGetter tempLevelGetter, StatSetter tempLevelSetter, StatLabelSetter textSetter, StatLabelGetter labelGetter) {
        JButton decreaseButton = new JButton("-");
        JButton increaseButton = new JButton("+");
        JLabel label = new JLabel(labelName);
        JLabel valueLabel = new JLabel(String.format("%d", baseLevelGetter.get()));

        styleTextOnlyButton(decreaseButton);
        styleTextOnlyButton(increaseButton);

        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        tempLevelSetter.set(baseLevelGetter.get());
        updateLevelInfo();

        decreaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (tempLevelGetter.get() > baseLevelGetter.get()) tempLevelSetter.set(tempLevelGetter.get() - 1);
            valueLabel.setForeground(tempLevelGetter.get() != baseLevelGetter.get() ? Color.BLUE : Color.WHITE);
            textSetter.set(String.format("%d", tempLevelGetter.get()));
            updateLevelInfo();
        });

        increaseButton.addActionListener(e -> {
            confirmLevelUp = false;
            if (tempLevelGetter.get() < Constants.PLAYERMAXLEVEL &&
                gamePanel.player.getExp() - (int) Constants.levelUpCost() > (int) Constants.nextLevelUpCost())
                tempLevelSetter.set(tempLevelGetter.get() + 1);
            valueLabel.setForeground(tempLevelGetter.get() != baseLevelGetter.get() ? Color.BLUE : Color.WHITE);
            textSetter.set(String.format("%d", tempLevelGetter.get()));
            updateLevelInfo();
        });

        panel.add(Box.createHorizontalStrut(100));
        panel.add(decreaseButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(valueLabel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(increaseButton);

        if (labelName.equals("血量")) hpTextLabel = valueLabel;
        if (labelName.equals("力量")) strTextLabel = valueLabel;
        if (labelName.equals("敏捷")) dexTextLabel = valueLabel;
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

        updateLevelInfo();
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

        totalLevelLabel.setText(" | LEVEL: "+ totalLevel + " |" );
        nextLevelUpCostLabel.setText(" | Required EXP: "+ (int) Constants.nextLevelUpCost() + " |");
        currentExpLabel.setText(" | EXP: " + gamePanel.player.getExp() + " -> " + (gamePanel.player.getExp() - cost) + " |");
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
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    // Functional Interfaces for readability
    private interface StatGetter { int get(); }
    private interface StatSetter { void set(int value); }
    private interface StatLabelSetter { void set(String value); }
    private interface StatLabelGetter { JLabel get(); }
}
