package Code;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Random;

public class NpcFighter extends NPC {
    private String[][] args = {
            {
                    this.getName() + "  \"问：你知道奇变偶不变下一句是什么吗？\"",
                    "符号看象限",
                    "我不学数学"
            },
            {
                    this.getName() + "  \"问：床前明月光的下一句是什么？\"",
                    "疑是地上霜",
                    "我不背古诗"
            },
            {
                    this.getName() + "  \"问：三角形内角和是多少度？\"",
                    "180度",
                    "我不懂几何"
            },
            {
                    this.getName() + "  \"问：少壮不努力的下一句是什么？\"",
                    "老大徒伤悲",
                    "我不想努力"
            },
            {
                    this.getName() + "  \"问：圆周率小数点后两位是多少？\"",
                    "1和4",
                    "我记不住数字"
            },
            {
                    this.getName() + "  \"问：欲穷千里目的下一句是什么？\"",
                    "更上一层楼",
                    "自己的作业自己做"
            },
            {
                    this.getName() + "  \"问：一年有多少个季节？\"",
                    "4个",
                    "我分不清季节"
            },
            {
                    this.getName() + "  \"问：两个黄鹂鸣翠柳的下一句是什么？\"",
                    "一行白鹭上青天",
                    "我不看鸟飞"
            },
            {
                    this.getName() + "  \"问：直角三角形中斜边的平方等于什么？\"",
                    "两直角边的平方和",
                    "我不研究三角"
            },
            {
                    this.getName() + "  \"问：春风又绿江南岸的下一句是什么？\"",
                    "明月何时照我还",
                    "轻舟已过万重山"
            }
    };
    public NpcFighter(int x, int y, String name, int attackPower, int defense, int hp, int currentImage,
                      String url, GameWorld gw) throws UnsupportedAudioFileException, IOException {
        super(x, y, name, attackPower, defense, hp, currentImage, url, gw);
    }

    @Override
    public Object collisionReact(GameObject go) {
        if (!waiting) {
            Random r=new Random();
            if (c == null) {
                c = new ChatDialog(gw, this,args[r.nextInt(args.length)]);
                c.setLocationRelativeTo(gw.getCurrentGameMap().getGamePanel());
                c.setChoiceCallback(choice -> {
                    if (choice == 0) {
                        //正确选择
                    } else if (choice == 1) {
                        BattleWindow b = new BattleWindow(gw, this);
                        //gw.setInFighting(true);
                    }
                    waitingTimer.start();
                    stop = false;
                });
            }else{
                c.setArgs(args[r.nextInt(args.length)]);
            }
            c.setVisible(true);
            gw.getCurrentGameMap().controlSound();
            waiting = true;
            stop = true;
        }
//        if(go instanceof Player&&!waiting) {
//            waiting=true;
//            if(!((Player) go).chatHappy(this)) {
//                String message = ((Player) go).fight(this)
//                        ? "赢了可不要骄傲自满啊"
//                        : "胜败乃兵家常事，还请重新来过";
//                JPanel panel=new JPanel();
//                panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
//                JDialog dialog=new JDialog(gw.getCurrentGameMap(),"提示",false);
//                JLabel label=new JLabel(message);
//                label.setFont(new Font("黑体",Font.BOLD,35));
//                panel.add(label);
//                JButton button=new JButton("确定");
//                panel.add(button);
//                dialog.setPreferredSize(new Dimension(gw.getCurrentGameMap().getWidth()/3,
//                        gw.getCurrentGameMap().getHeight()/3));
//                button.addActionListener(e->{
//                    waitingTimer.start();
//                });
//                dialog.add(panel);
//                dialog.pack();
//                dialog.setLocationRelativeTo(gw.getCurrentGameMap().getGamePanel());
//                dialog.setVisible(true);
//                /*Object[] options = {"确定"};
//
//                int result = JOptionPane.showOptionDialog(
//                        gw.getCurrentGameMap().getGamePanel(),
//                        message,
//                        "提示",
//                        JOptionPane.DEFAULT_OPTION,
//                        JOptionPane.INFORMATION_MESSAGE,
//                        null,
//                        options,
//                        options[0]
//                );
//
//                // 处理按钮点击事件
//                if(result == 0||result==JOptionPane.CLOSED_OPTION) { // 点击了按钮
//                    gw.setInteraction(false);
//                    waitingTimer.start();
//                }*/
//            }else {
//                gw.setInteraction(false);
//                waitingTimer.start();
//            }
//        }
        return null;
    }
}
