import javax.swing.JFrame;

public class SpaceInvader {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SpaceInvaderPanel());
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        MusicPlayer player = new MusicPlayer();
        player.playMusic("D:/Github/Space_Invader/music/music1.wav");
    }
}
