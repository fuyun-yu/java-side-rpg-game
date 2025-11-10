package Code;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class GameMap extends JFrame {
    private Timer saveTimer;
    private JPanel gamePanel;
    private GameWorld gw;
    //private JLabel mapJLabel;
    private String mapTitle;
    private NPC[] arr;
    private Item[] items;
    private Player player;
    private int width, height;
    private JLabel playerInfo;
    private JLabel playerInfo2;
    private JLabel playerInfo3;
    private ImageIcon mapImageIcon;
    public GameMap(GameWorld gw, String url, String mapTitle, int width, int height,NpcMessage[] npcMessages) throws UnsupportedAudioFileException, IOException {
        saveTimer=new Timer(1200,e -> {
            if(!gw.isSaving()){
                saveTimer.stop();
            }else{
                gw.setSaving(false);
            }
        });
        this.player = gw.getPlayer();
        this.gw = gw;
        this.mapTitle = mapTitle;
        this.width = width;
        this.height = height;
        this.mapImageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(url)));
        this.arr=new NPC[npcMessages.length];
        for(int i=0;i<arr.length;++i){
            if(npcMessages[i].getKind()==NpcMessage.FIGHTER) {
                arr[i]=new NpcFighter(370 + i * 80, 280 + i * 80,
                        npcMessages[i].getName(), 30,
                        10, 100, 0,
                        npcMessages[i].getImage(), gw);
            } else if (npcMessages[i].getKind()==NpcMessage.VENDER) {
                arr[i]=new NpcVender(370 + i * 80, 280 + i * 80,
                        npcMessages[i].getName(), 30,
                        10, 100, 0,
                        npcMessages[i].getImage(), gw);
            }
        }
        //this.mapJLabel = new JLabel(mapImageIcon);
        items=new Item[arr.length];
        init();
    }

    public void init() {
        setTitle(mapTitle);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //player.setCurrentComponent(this);
        /*for (int i = 0; i < arr.length; ++i) {
            arr[i] = new NPC(370 + i * 80, 280 + i * 80,
                    "NPC" + (i + 1) + "号", 10,
                    10, 100, 0,
                    "/Resources/character/狮子.png", gw);
            arr[i].setCurrentComponent(this);
        }*/
        // 创建游戏区域面板，使用绝对定位
        /*JPanel gamePanel = new JPanel(null); // 使用null布局，允许绝对定位
        gamePanel.setBackground(Color.white);

        // 添加地图背景，设置为面板大小
        mapImage.setBounds(0, 0, width, height);
        gamePanel.add(mapImage);

         创建一个专门的层来存放玩家角色
        JPanel playerLayer = new JPanel(null);
        playerLayer.setOpaque(false); // 透明背景
        playerLayer.setBounds(0, 0, width, height);
        gamePanel.add(playerLayer);

         //将玩家添加到playerLayer
         characterRenderer = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制玩家
                player.draw(g);

                // 绘制NPC
                for (NPC npc : arr) {
                    if (npc != null) {
                        npc.draw(g);
                    }
                }
            }
        };

        characterRenderer.setBounds(player.getX(),player.getY(),player.getWidth(),player.getHeight());
        playerLayer.add(characterRenderer);
        jl=new JLabel(new ImageIcon(Objects.requireNonNull(getClass().
                getResource("/Sources/GUI/Bg.png"))));
        jl.setBounds(player.getX(), player.getY(),
                player.getWidth(), player.getHeight());
        playerLayer.add(jl);
        playerLayer.revalidate();
        playerLayer.repaint();

        gamePanel.setComponentZOrder(playerLayer, 0); // 最上层
        gamePanel.setComponentZOrder(mapImage, gamePanel.getComponentCount() - 1); // 最底层

        // 将游戏区域添加到窗口中央
        add(gamePanel, BorderLayout.CENTER);*/
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制地图背景
                g.drawImage(mapImageIcon.getImage(), 0, 0, width, height, null);
                // 绘制玩家
                player.draw(g);
                // 绘制NPC
                for (NPC npc : arr) {
                    if (npc != null) {
                        npc.draw(g);
                    }
                }
                //绘制道具
                for(Item item:items){
                    if(item!=null){
                        item.draw(g);
                    }
                }
                //绘制存档成功提示
                if(gw.isSaving()){
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(gw.getWidth()/2,gw.getHeight()/2,100,60);
                    g.setColor(Color.black);
                    g.drawString("存档成功！",gw.getWidth()/2+20,gw.getHeight()/2+20);
                    g.setColor(null);
                    saveTimer.start();
                }
            }
        };

        // 设置游戏面板的首选大小
        gamePanel.setPreferredSize(new Dimension(width, height));

        // 将游戏区域添加到窗口中央
        add(gamePanel, BorderLayout.CENTER);
        // 创建状态栏面板
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(Color.LIGHT_GRAY);

        // 添加玩家信息到状态栏
        playerInfo = new JLabel("玩家: " + player.getName() + " | 生命值: " + player.getHp());
        playerInfo2 = new JLabel("攻击：" + player.getAttackPower() + " | 防御：" + player.getDefense());
        playerInfo3 = new JLabel("X:" + player.getX() + "Y:" + player.getY());
        statusPanel.add(playerInfo);
        statusPanel.add(playerInfo2);
        statusPanel.add(playerInfo3);
        // 将状态栏添加到窗口底部
        add(statusPanel, BorderLayout.SOUTH);

        //绑定事件监听
        gamePanel.setFocusable(true);

        gamePanel.addKeyListener(gw);


        gamePanel.addMouseListener(gw);
        pack();
    }

    public NPC[] getArr(){
        return arr;
    }
//    public void patrolAll(){
//        for (NPC n : arr) {
//            if(n!=null){
//                n.Patrol();
//            }
//        }
//    }
    public void start() {
        // 显示窗口
        setVisible(true);
        //patrolAll();
        /*Timer timer = new Timer(50, e -> {
            if (!isWorking) {
                ((Timer) e.getSource()).stop(); // 停止定时器
                return;
            }
            // 处理碰撞检测
            for (int i = 0; i < arr.length; ++i) {
                if (player.collision(arr[i])) {
                    player.collisionReact(arr[i]);
                }
            }
            // 刷新玩家信息和界面
            flashData(0, 0); // 触发重绘
        });
        timer.start(); // 启动定时器*/
    }

    public void end() {
        setVisible(false);
        //pauseMap();
    }
//    public void pauseMap(){
//        for(NPC n:arr){
//            if(n!=null){
//                n.pausePatrol();
//            }
//        }
//    }
    public void flashData() {
        //player.move(dx, dy);
        //characterRenderer.setBounds(player.getX(),player.getY(),player.getWidth(),player.getHeight());
        //jl.setBounds(player.getX(), player.getY(),
        // player.getWidth(), player.getHeight());
        playerInfo.setText("玩家: " + player.getName() + " | 生命值: " + player.getHp());
        playerInfo2.setText("攻击：" + player.getAttackPower() + " | 防御：" + player.getDefense());
        playerInfo3.setText("X:" + player.getX() + "Y:" + player.getY());
        gamePanel.repaint();
        //this.getContentPane().revalidate();
        //this.getContentPane().repaint();
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public void controlSound() {
        player.stopFootSound();
    }


    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
}
