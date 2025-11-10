package Code;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class GameTest {
    public static void main(String[] args) {
        System.out.println(System.getProperties());
        SwingUtilities.invokeLater(() -> {
            GameWorld gw;
            try {
                gw = new GameWorld(648 + 300 + 200, 480 + 200 + 150);
            } catch (UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }
            gw.start();
            /*GameStart gs=new GameStart();
            gs.start();*/
        });
    }
}