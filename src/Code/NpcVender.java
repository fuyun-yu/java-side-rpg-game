package Code;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class NpcVender extends NPC{
    private ArrayList<Item> items;
    private VenderWindow v;
    private String [] args={
            getName()+"说：你需要装备吗，我可以送你一些",
            "谢谢，我不需要",
            "多谢，帮大忙了"
    };
    public NpcVender(int x, int y, String name, int attackPower, int defense, int hp, int currentImage, String url, GameWorld gw) throws UnsupportedAudioFileException, IOException {
        super(x, y, name, attackPower, defense, hp, currentImage, url, gw);
        items=new ArrayList<>();
        items.add(new Sword(0,0,"村好剑","攻击力+10","/Resources/character/飞剑.png",gw));
        for(int i=0;i<5;++i){
            items.add( new BloodPill(0,0,"回血丹","血量+50","/Resources/character/鸡蛋.jpg",gw));
        }
        v=new VenderWindow(gw,items,this);
    }
    public VenderWindow getVenderWindow(){
        return v;
    }
    @Override
    public Object collisionReact(GameObject go) {
        if (!waiting) {
            if (c == null) {
                c = new ChatDialog(gw, this,args);
                c.setLocationRelativeTo(gw.getCurrentGameMap().getGamePanel());
                c.setChoiceCallback(choice -> {
                    if (choice == 0) {
                        //拒绝
                    } else if (choice == 1) {
                        //接受
                        v.display();
                    }
                    waitingTimer.start();
                    stop = false;
                });
            }
            c.setVisible(true);
            gw.getCurrentGameMap().controlSound();
            waiting = true;
            stop = true;
        }
        return null;
    }
}
