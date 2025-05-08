import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class MusicPlayer {
    private Clip clip;
    private FloatControl gainControl;   // 音量控制器
    private float currentVolume = 5.0f; // 目前音量 (範圍 0-10)

    /** 播放音樂，若已有正在播放的 clip，會先關閉再重新播放 */
    public void playMusic(String filepath, float volume) {
        stopMusic();               // 停止並釋放舊 clip（若有）
        try {
            AudioInputStream audioInput =
                AudioSystem.getAudioInputStream(new File(filepath));
            clip = AudioSystem.getClip();
            clip.open(audioInput);

            // 取得音量控制器
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // 設定音量 (從 0 到 10 映射到合法 dB 範圍)
            setVolume(volume);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException
                 | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** 停止音樂並釋放資源 */
    public void stopMusic() {
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.close();
            clip = null;
            gainControl = null;
        }
    }

    /** 以 0-10 範圍調整音量 (+/-) */
    public void adjustVolume(float delta) {
        if (gainControl == null) return;
        float newVolume = currentVolume + delta;
        setVolume(newVolume);
    }

    /** 直接設定音量（0-10 範圍） */
    public void setVolume(float percentage) {
        if (gainControl == null) return;

    // 確保音量在 0 - 10 範圍內
        currentVolume = Math.max(0.0f, Math.min(10.0f, percentage));

        // 計算 dB 值，並確保它在合法範圍內
        float dbValue;

        dbValue = (float) (gainControl.getMinimum() + (gainControl.getMaximum() - gainControl.getMinimum()) * Math.pow((currentVolume) / 5.0f, 0.5));

        // 保證 dB 值不超過最大或最小範圍
        dbValue = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dbValue));

        gainControl.setValue(dbValue);
        Constants.currentVolume = currentVolume;
    }

    /** 取得目前音量（0-10 範圍） */
    public float getCurrentVolume() {
        return currentVolume;
    }

    /** 取得音量的文字顯示（0-10 範圍） */
    public String getVolumeText() {
        return String.format("%.0f", currentVolume);
    }
}
