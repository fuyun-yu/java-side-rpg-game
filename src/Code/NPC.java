package Code;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;
import java.util.Random;

public abstract class NPC extends Character {
    private final int[] px = {1, -1, 0};
    private final int[] py = {1, -1, 0};
    private boolean hasReachBounds = false;
    //private Timer patrolTimer;
    private int index;
    private Random random;
    private Random r;
    private int x = 0;
    private int y = 0;
    protected boolean waiting = false;//用于标记NPC是否可以碰撞，waiting期间时不可以碰撞的
    protected Timer waitingTimer;
    protected ChatDialog c;
    protected boolean stop=false;//这是一个用于标记NPC是否可以移动的变量
    public boolean getWaiting() {
        return waiting;
    }

    public boolean getStop() {
        return stop;
    }

    public NPC(int x, int y, String name, int attackPower, int defense, int hp,
               int currentImage, String url, GameWorld gw) throws UnsupportedAudioFileException, IOException {
        super(x, y, name, attackPower, defense, hp, currentImage, url, gw);
        random = new Random();//33590781
        r = new Random(random.nextInt(33550337)+33590781);
        index = 1;
        waitingTimer = new Timer(3000, e -> {
            if (waitingTimer != null && waitingTimer.isRunning()) {
                if (!waiting) {
                    waitingTimer.stop();
                }
            }
            waiting = false;
        });
    }

    public void setIndex(int i) {
        index = i;
    }

    public void setHasReachBounds(boolean b) {
        hasReachBounds = b;
    }

    @Override
    public abstract Object collisionReact(GameObject go) ;

    public void Patrol() {
        int v = Math.max(1, gw.getV() / 2);
        if (hasReachBounds || index % 15 == 0) {
            r.setSeed(random.nextInt());
            x = r.nextInt(3);
            y = r.nextInt(3);
        }
        setHasReachBounds(!move(v * px[x], v * py[y]));
        index = (index + 1) % 15;
    }
        /*if (patrolTimer != null && patrolTimer.isRunning()) {
            patrolTimer.stop();
        }
        patrolTimer = new Timer(25, e -> {
                if (hasReachBounds || index % 15 == 0) {
                    r.setSeed(random.nextInt());
                    x = r.nextInt(3);
                    y = r.nextInt(3);
                }
                setHasReachBounds(!move(v * px[x], v * py[y]));
                //gw.getCurrentGameMap().getGamePanel().repaint();
             index = (index + 1) % 15;

        });
        patrolTimer.start();
    }*/
    /*public void pausePatrol(){
        if(patrolTimer!=null&&patrolTimer.isRunning()){
            patrolTimer.stop();
        }
    }*/
}