import java.awt.*;

public class PlayerUI {
    public static void draw(Graphics g, int maxHealth, int currentHealth, int maxEnergy, int currentEnergy, int exp, int estus) {
        double h = 10;
        g.setColor(Color.RED);
        g.drawRect(10, 10, maxHealth, (int) h);
        g.fillRect(10, 10, currentHealth, (int) h);

        g.setColor(Color.GREEN);
        g.drawRect(10, 30, maxEnergy, (int) h);
        g.fillRect(10, 30, currentEnergy, (int) h);

        // 繪製 Estus 數量（橘色矩形）
        g.setColor(Color.ORANGE);
        int estusX = 10;
        int estusY = 50;         // 在能量條下方
        int estusWidth = 10;
        int estusHeight = 20;
        int spacing = 5;

        for (int i = 0; i < estus; i++) {
            g.fillRect(estusX + i * (estusWidth + spacing), estusY, estusWidth, estusHeight);
        }

        // 顯示 EXP（右下角）
        String expText = "EXP: " + exp;
        g.setColor(Color.WHITE);
        Font expFont = new Font("SansSerif", Font.BOLD, 14);
        g.setFont(expFont);
        FontMetrics fm = g.getFontMetrics();

        Rectangle clip = g.getClipBounds();
        int x = clip.width - fm.stringWidth(expText) - 10;
        int y = clip.height - fm.getHeight() + fm.getAscent() - 10;

        g.drawString(expText, x, y);
    }
}
