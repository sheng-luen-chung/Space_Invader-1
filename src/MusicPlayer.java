import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;

public class MusicPlayer {
    private final Map<String, List<Clip>> clipPool = new HashMap<>();
    private float currentVolume = 10.0f; // 範圍 0~10

    public MusicPlayer() {
        load("StartMenu", "music/StartMenu.wav", 1);
        load("YouDied", "music/YouDied.wav", 1);
        load("Bonfire", "music/Bonfire.wav", 1);
        load("BigTriangle", "music/BigTriangle.wav", 1);
        load("BigSquare", "music/BigSquare.wav", 1);
        load("Estus", "music/Estus.wav", 1);
        load("FireBall", "music/FireBall.wav", 10);
        load("Kill", "music/Kill.wav", 5);
    }

    // 載入音效多份，供重複播放
    public void load(String id, String filepath, int count) {
        List<Clip> clips = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filepath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                applyVolume(clip);
                clips.add(clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Failed to load audio: " + filepath);
            }
        }
        clipPool.put(id, clips);
    }

    // 播放片段，支援重複播放（會找一個沒在播的 Clip）
    public void playSegment(String id, float startSec, float endSec, boolean loop) {
        List<Clip> clips = clipPool.get(id);
        if (clips == null || clips.isEmpty()) {
            System.err.println("Clip not loaded: " + id);
            return;
        }

        for (Clip clip : clips) {
            if (!clip.isRunning()) {
                AudioFormat format = clip.getFormat();
                float frameRate = format.getFrameRate();
                int startFrame = Math.max(0, (int) (startSec * frameRate));
                int endFrame = Math.min((int) (endSec * frameRate), clip.getFrameLength());

                if (startFrame >= endFrame) {
                    System.err.println("Invalid segment range.");
                    return;
                }

                clip.setLoopPoints(startFrame, endFrame - 1);
                clip.setFramePosition(startFrame);
                applyVolume(clip);

                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                    new Thread(() -> {
                        while (clip.getFramePosition() < endFrame && clip.isRunning()) {
                            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                        }
                        clip.stop();
                        clip.setFramePosition(0);
                    }).start();
                }
                return;
            }
        }

        System.err.println("All clips busy: " + id);
    }

    public void stopById(String id) {
        List<Clip> clips = clipPool.get(id);
        if (clips != null) {
            for (Clip clip : clips) {
                if (clip.isRunning()) clip.stop();
                // clip.close();
            }
            // clipPool.remove(id);
        }
    }

    public void stopAll() {
        for (List<Clip> clips : clipPool.values()) {
            for (Clip clip : clips) {
                if (clip.isRunning()) clip.stop();
                // clip.close();
            }
        }
        // clipPool.clear();
    }

    public void adjustVolume(float delta) {
        currentVolume = Math.max(0.0f, Math.min(10.0f, currentVolume + delta));
        for (List<Clip> clips : clipPool.values()) {
            for (Clip clip : clips) {
                applyVolume(clip);
            }
        }
    }

    public void setVolume(float v) {
        currentVolume = Math.max(0.0f, Math.min(10.0f, v));
        for (List<Clip> clips : clipPool.values()) {
            for (Clip clip : clips) {
                applyVolume(clip);
            }
        }
    }

    public float getCurrentVolume() {
        return currentVolume;
    }

    public String getVolumeText() {
        return String.format("%.0f", currentVolume);
    }

    private void applyVolume(Clip clip) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float normalized = currentVolume / 10.0f;
            float db = (float) (gainControl.getMinimum() +
                    (gainControl.getMaximum() - gainControl.getMinimum()) *
                    Math.pow(normalized, 0.5));
            gainControl.setValue(db);
        } catch (IllegalArgumentException ignored) {
            // 若不支援音量控制
        }
    }
}
