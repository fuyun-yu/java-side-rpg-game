package Code;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Character {
    private ArrayList<Item> bag;//玩家背包

    public Player(int x, int y, String name,
                  int attackPower, int defense, int hp, int currentImage, String url,
                  GameWorld gw) throws UnsupportedAudioFileException, IOException {
        super(x, y, name, attackPower, defense, hp, currentImage, url, gw);
        bag = new ArrayList<>();
    }
    public ArrayList<Item> getBag(){
        return bag;
    }
    @Override
    public Object collisionReact(GameObject go) {
        /*if (go instanceof Item) {
            getItem((Item) go);
            return GameEnum.Function.GET;
        } else if (go instanceof NPC) {
            if (chatHappy((NPC) go)) {
                fight((NPC) go);
            }
            return GameEnum.Character.FIGHT;
        }*/
        return GameEnum.Collision.WITHOUT;
    }

    /*public boolean chatHappy(NPC n) {
        //boolean out=true;
        /*if(n instanceof NpcFighter f){
            String [] choice={"符号看象限","我不学数学"};
            JTextArea message=new JTextArea(f.getName()+"问：你知道奇变偶不变下一句是什么吗？");
            message.setLineWrap(true);
            message.setFont(new Font("黑体", Font.BOLD,20));
            JDialog dialog=new JDialog(gw.getCurrentGameMap(),"对话",false);
            JPanel panel=new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
            JPanel panel1=new JPanel();
            panel.add(message);
            panel.add(Box.createVerticalStrut(100));
            panel.add(panel1);
            dialog.add(panel);
            JButton [] buttons=new JButton[choice.length];
            for(int i=0;i<buttons.length;++i){
                buttons[i]=new JButton();
                buttons[i].setText(choice[i]);
                int finalI = i;
                buttons[i].addActionListener(e -> {
                    setClick(finalI);
                    dialog.setVisible(false);
                });
                panel1.add(buttons[i]);
            }
            dialog.setPreferredSize(new Dimension(gw.getCurrentGameMap().getWidth()/3,
                    gw.getCurrentGameMap().getHeight()/3));
            dialog.pack(); // 自动调整大小
            dialog.setLocationRelativeTo(gw.getCurrentGameMap().getGamePanel()); // 居中显示
            dialog.setVisible(true);
        }
        return click==0;
    }*/

    //private int click=0;
    /*private void setClick(int i){
        click=i;
    }*/
    public void getItem(Item item) {
        bag.add(item);
        for (int i = 0; i < gw.getCurrentGameMap().getItems().length; i++) {
            if(gw.getCurrentGameMap().getItems()[i]==item){
                gw.getCurrentGameMap().getItems()[i]=null;
                break;
            }
        }
    }

    /*public boolean fight(NPC n) {
        return false;
    }*/
}
