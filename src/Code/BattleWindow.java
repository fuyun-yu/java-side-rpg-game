package Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Random;

public class BattleWindow extends JWindow {
    private JPanel imagePanel = new JPanel();
    private JPanel textPanel = new JPanel();
    private JPanel mainPanel = new JPanel();
    private JPanel buttonPanel=new JPanel();
    private JScrollPane battleMessagePanel;
    private JTextArea battleMessage;
    private Image playerImage;
    private Image npcImage;
    private GameWorld gw;
    private Point initialClick;
    private Timer fightTimer;
    private Player player;
    private boolean stillFight=true;
    public BattleWindow(GameWorld gw, NpcFighter npc) {
        super(gw.getCurrentGameMap());
        buttonPanel.setLayout(new GridLayout(1,2));
        JButton startButton=new JButton("开战");
        startButton.addActionListener(e -> {
            if(stillFight){
                fightTimer.setInitialDelay(0);
                fightTimer.start();
                fightTimer.setInitialDelay(1000);
            }else{
                setVisible(false);
                gw.setInFighting(false);
                if(player.getHp()>0){
                    for(int i=0;i<gw.getCurrentGameMap().getItems().length;++i){
                        if(gw.getCurrentGameMap().getItems()[i]==null){
                            Random r=new Random();
                            if(r.nextInt(2)==0){
                                gw.getCurrentGameMap().getItems()[i]=new Sword(npc.getX(),npc.getY(),
                                        "村好剑","攻击力+10","/Resources/character/飞剑.png",
                                        gw);
                            }else{
                                gw.getCurrentGameMap().getItems()[i]=new BloodPill(npc.getX(),npc.getY(),
                                        "回血丹","血量+50","/Resources/character/鸡蛋.jpg",
                                        gw);
                            }
                            break;
                        }
                    }
                    for(int i=0;i<gw.getCurrentGameMap().getArr().length;++i){
                        if(gw.getCurrentGameMap().getArr()[i]==npc){
                            gw.getCurrentGameMap().getArr()[i]=null;
                        }
                    }
                }else{
                    gw.getPlayer().setX(100);
                    gw.getPlayer().setY(100);
                    gw.getPlayer().setHp(100);
                    gw.getCurrentGameMap().end();
                    gw.getGameStart().start();
                    gw.setGameState(gw.START_STATE);
                }
            }
        });
        JButton escapeButton=new JButton("逃跑");
        escapeButton.addActionListener(e -> {
            setVisible(false);
            gw.setInFighting(false);
            if(npc.getHp()==0){
                for(int i=0;i<gw.getCurrentGameMap().getItems().length;++i){
                    if(gw.getCurrentGameMap().getItems()[i]==null){
                        gw.getCurrentGameMap().getItems()[i]=new Sword(npc.getX(),npc.getY(),
                                "村好剑","攻击力+10","/Resources/character/飞剑.png",
                                gw);
                        break;
                    }
                }
                for(int i=0;i<gw.getCurrentGameMap().getArr().length;++i){
                    if(gw.getCurrentGameMap().getArr()[i]==npc){
                        gw.getCurrentGameMap().getArr()[i]=null;
                    }
                }
            }else {
                npc.waiting = true;
                npc.waitingTimer.start();
                setVisible(false);
                gw.setInFighting(false);
            }
        });
        buttonPanel.add(startButton);
        buttonPanel.add(escapeButton);
        this.gw = gw;
        player = gw.getPlayer();
        setPreferredSize(new Dimension(this.gw.getWidth() / 4 * 3, this.gw.getHeight() / 4 * 3));
        this.battleMessage = new JTextArea();
        this.battleMessagePanel = new JScrollPane(battleMessage);
        battleMessagePanel.setPreferredSize(new Dimension(gw.getWidth() / 3, gw.getWidth() / 3)); // 设置滚动面板大小
        battleMessage.setLineWrap(true);
        battleMessage.setEditable(false);
        playerImage = gw.getPlayer().getImage();
        npcImage = npc.getImage();
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(playerImage, 0, 0, gw.getPlayer().getWidth() * 3, gw.getPlayer().getHeight() * 3,
                        0, 7 * gw.getPlayer().getHeight(),
                        gw.getPlayer().getWidth(), 8 * gw.getPlayer().getHeight(), null);
            }
        };
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(npcImage, 0, 0, npc.getWidth() * 3, npc.getHeight() * 3,
                        0, 7 * npc.getHeight(),
                        npc.getWidth(), 8 * npc.getHeight(), null);
            }
        };
        leftPanel.setPreferredSize(new Dimension(gw.getPlayer().getWidth() * 4, gw.getPlayer().getHeight() * 4));
        rightPanel.setPreferredSize(new Dimension(npc.getWidth() * 4, npc.getHeight() * 4));
        imagePanel.add(leftPanel);
        imagePanel.add(rightPanel);
        imagePanel.setLayout(new GridLayout(1, 2)); // 左右并排
        textPanel.setLayout(new BorderLayout());     // 滚动面板占满
        textPanel.add(battleMessagePanel, BorderLayout.CENTER);
        textPanel.add(battleMessagePanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(imagePanel);
        mainPanel.add(textPanel);
        mainPanel.add(buttonPanel);
        this.add(mainPanel);
        setLocationRelativeTo(gw.getCurrentGameMap());
        // 添加调试信息
//        System.out.println("BattleWindow location: " + getLocation());
//        System.out.println("BattleWindow size: " + getSize());

        // 添加鼠标拖动功能
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                // 获取当前窗口位置
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // 计算移动距离
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // 移动窗口
                int maxX = Toolkit.getDefaultToolkit().getScreenSize().width - getWidth();
                int maxY = Toolkit.getDefaultToolkit().getScreenSize().height - getHeight();
                setLocation(Math.max(0, Math.min(thisX + xMoved, maxX)),
                        Math.max(0, Math.min(thisY + yMoved, maxY)));
            }
        });
        fightTimer = new Timer(1000, e -> {
            if (npc.getHp() != 0) {
                npc.attack(player);
                String s1 = npc.getName() + " 给 " + gw.getPlayer().getName() + " 造成了 " + (Math.max(npc.getAttackPower() -
                        player.getDefense(), 0)) + " 点伤害\n" + player.getName() + " 剩余 " + player.getHp() + " 点血\n" +
                        "---------------------------------\n";
                battleMessage.append(s1);
            }
            if (player.getHp() != 0) {
                player.attack(npc);
                String s2 = player.getName() + " 给 " + npc.getName() + " 造成了 " + (Math.max(player.getAttackPower() -
                        npc.getDefense(), 0)) + " 点伤害\n" + npc.getName() + " 剩余 " + npc.getHp() + " 点血\n" +
                        "---------------------------------\n";
                battleMessage.append(s2);
            }
            JScrollBar bar = battleMessagePanel.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
            if (gw.getPlayer().getHp() == 0) {
                battleMessage.append(player.getName() + " 血量归零，" + npc.getName() + "赢了\n");
                startButton.setText("退出");
                stillFight=false;
                fightTimer.stop();
            }
            if (npc.getHp() == 0) {
                battleMessage.append(npc.getName() + " 血量归零，" + player.getName() + "赢了\n");
                startButton.setText("退出");
                stillFight=false;
                fightTimer.stop();
            }
        });
        pack();
        setLocationRelativeTo(this.gw.getCurrentGameMap());
        setVisible(true);
        gw.setInFighting(true);//留待其他方法复位
    }
}
