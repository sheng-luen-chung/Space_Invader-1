import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpaceInvader {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout); 

        SpaceInvaderPanel gamepanel = new SpaceInvaderPanel(cardLayout,cardPanel);
        StartPanel startpanel = new StartPanel(cardLayout,cardPanel,gamepanel);
        SettingsPanel settingpanel = new SettingsPanel(cardLayout,cardPanel,gamepanel);

        cardPanel.add(gamepanel, "game");
        cardPanel.add(startpanel, "start");
        cardPanel.add(settingpanel, "setting");

        frame.add(cardPanel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        
        cardLayout.show(cardPanel,"start");
        gamepanel.pauseGame();

        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("music/music1.wav", -10.0f);
    }
}
