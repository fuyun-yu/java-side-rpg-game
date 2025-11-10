package Code;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public class GameStart extends JFrame  {
    private JTextField nameInput=new JTextField(15);
    private JTextField attackInput=new JTextField(15);
    private JTextField defenseInput=new JTextField(15);
    private JTextField hpInput=new JTextField(15);
    private JDialog PropertySettingDialog;
    private JDialog changeCharacterDialog;
    private int buttonWidth;
    private int buttonHeight;
    private double resizeFactorWidth = 1, resizeFactorHeight = 1;
    private boolean needMenu = false;
    private JPanel mapMenu;
    private JPanel otherMenu;
    private JPanel choiceMenu;
    private JPanel buttonPanel;
    private int count;
    private GameWorld gw;
    private String[] arr;
    private Timer startTimer;
    private int width, height;
    private JPanel gamePanel;
    private int index = 0;
    private Clip backgroundMusic;

    public GameStart(GameWorld gameWorld) {
        count = 0;
        gw = gameWorld;
        this.width = gw.getWidth();
        this.height = gw.getHeight();
        this.resizeFactorHeight = height / 480.0;
        this.resizeFactorWidth = width / 648.0;
        buttonWidth = getTargetWidth(120);
        buttonHeight = getTargetHeight(74);
        arr = new String[7];
        for (int i = 1; i <= 3; ++i) {
            arr[i - 1] = "/Resources/45度角地图-背景图+遮挡图/nvwa_" + i + ".jpg";
        }

        arr[3] = "/Resources/45度角地图-背景图+遮挡图/shushan_1.jpg";
        arr[4] = "/Resources/45度角地图-背景图+遮挡图/shushan_2.png";
        arr[5] = "/Resources/45度角地图-背景图+遮挡图/shushan_3.jpg";
        arr[6] = "/Resources/45度角地图-背景图+遮挡图/shushan_4.png";

        String m = "/Resources/帝国时代音效/27070age12.mid";
        URL u = getClass().getResource(m);
        if (u != null) {
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(u)) {
                DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
                backgroundMusic = (Clip) AudioSystem.getLine(info);
                if (!backgroundMusic.isOpen()) {
                    backgroundMusic.open(ais);
                }
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        init();
    }

    public double getResizeFactorWidth() {
        return resizeFactorWidth;
    }

    public void setResizeFactorWidth(double resizeFactorWidth) {
        this.resizeFactorWidth = resizeFactorWidth;
    }

    public double getResizeFactorHeight() {
        return resizeFactorHeight;
    }

    public void setResizeFactorHeight(double resizeFactorHeight) {
        this.resizeFactorHeight = resizeFactorHeight;
    }

    private Font getScaledFont(int buttonHeight) {
        // 字体大小设为按钮高度的1/3（可根据视觉效果调整比例）
        int fontSize = Math.max(12, buttonHeight / 3); // 最小12号字，避免太小
        return new Font("宋体", Font.PLAIN, fontSize);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTargetWidth(int num) {
        return (int) (resizeFactorWidth * num);
    }

    public int getTargetHeight(int num) {
        return (int) (resizeFactorHeight * num);
    }

    public void init() {
        setTitle("欢迎游玩本游戏 作者：789 tztfotzttztt");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(arr[index]))).getImage(),
                        0, 0, width, height, null);
                index = (index + 1) % arr.length;
            }
        };
        // 设置游戏面板的首选大小
        gamePanel.setPreferredSize(new Dimension(width, height));
        // 设置布局为 BorderLayout
        gamePanel.setLayout(new BorderLayout());
        // 将游戏区域添加到窗口中央
        add(gamePanel, BorderLayout.CENTER);
        //创建一个面板用于放置按钮，使用FlowLayout居中对齐
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        //创建按钮
        JButton startButton = new JButton("开始游戏");
        //startButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        startButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        startButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        startButton.setFont(getScaledFont(buttonHeight));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                needMenu = true;
            }
        });
        JButton endButton = new JButton("结束游戏");
        //endButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        endButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        endButton.setMinimumSize(new Dimension(buttonWidth, buttonHeight));
        endButton.setFont(getScaledFont(buttonHeight));
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowEvent windowClosing = new WindowEvent(
                        GameStart.this,  // 当前窗口实例
                        WindowEvent.WINDOW_CLOSING  // 模拟关闭事件
                );
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowClosing);
            }
        });
        //Menu
        choiceMenu = new JPanel();
        otherMenu = new JPanel();
        otherMenu.setOpaque(false);
        mapMenu = new JPanel();
        mapMenu.setOpaque(false);
        choiceMenu.setLayout(new BoxLayout(choiceMenu, BoxLayout.X_AXIS));
        choiceMenu.setOpaque(false);
        mapMenu.setLayout(new BoxLayout(mapMenu, BoxLayout.Y_AXIS));
        otherMenu.setLayout(new BoxLayout(otherMenu, BoxLayout.Y_AXIS));
        choiceMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        choiceMenu.setAlignmentY(Component.CENTER_ALIGNMENT);
        //

        JButton[] buttons = new JButton[3];
        String [] tempS={"新的游戏","读取存档","属性设置"};
        mapMenu.add(Box.createVerticalStrut(20));
        for (int i = 0; i < buttons.length; ++i) {
            buttons[i] = new JButton();
            buttons[i].setText(tempS[i]);
            buttons[i].setMaximumSize(new Dimension(buttonWidth, buttonHeight));
            buttons[i].setMinimumSize(new Dimension(buttonWidth, buttonHeight));
            buttons[i].setFont(getScaledFont(buttonHeight));
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttons[i].setAlignmentY(Component.CENTER_ALIGNMENT);
            mapMenu.add(buttons[i]);
            mapMenu.add(Box.createVerticalStrut(20));
        }
        buttons[0].addActionListener(e -> {
            gw.setIndex(0);
            try {
                gw.enterGame();
            } catch (UnsupportedAudioFileException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttons[1].addActionListener(e -> {
            File f=new File("saveData.txt");
            if(!f.exists()){
                JOptionPane.showMessageDialog(this,
                        "当前无存档信息可用！"
                ,"提示",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f))){
                SaveData s=(SaveData) ois.readObject();
                //GameWorld信息
                gw.setIndex(s.getCurrentMapIndex());
                gw.setInFighting(s.isInFighting());
                gw.setWatchingBag(s.isWatchingBag());
                try {
                    gw.enterGame();
                } catch (UnsupportedAudioFileException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                //player信息读取
                Player p=gw.getPlayer();
                p.setName(s.getName());
                p.setX(s.getPlayerX());
                p.setY(s.getPlayerY());
                p.setAttackPower(s.getPlayerAttack());
                p.setDefense(s.getPlayerDefense());
                p.setHp(s.getPlayerHp());
                gw.setCharacterNumber(s.getImageNum());
                gw.getBag().getHasUsed().addAll(s.getHasUsed());
                for(String str:s.getBagItems()){
                    if(str.equals("回血丹")){
                        p.getBag().add(new BloodPill(0,0,"回血丹",
                                "血量+50","/Resources/character/鸡蛋.jpg",gw));
                    }else{
                        p.getBag().add(new Sword(0,0,"村好剑",
                                "攻击力+10","/Resources/character/飞剑.png",gw));
                    }
                }
                //map (1到3)
                for(int i=0;i<s.getMapNPC().length;++i){
                    for(int j=0;j<s.getMapNPC()[i].length;++j){
                        if(s.getMapNPC()[i][j].equals("null")){
                            gw.getList()[i+1].getArr()[j]=null;
                        }else{
                            if(s.getNpcKind()[i][j]==NpcMessage.FIGHTER){
                                gw.getList()[i+1].getArr()[j]=new NpcFighter(0,0,
                                        gw.getNpcMessages()[i+1][j].getName(), 30,
                                        10, 0, 0,
                                        gw.getNpcMessages()[i+1][j].getImage(), gw);
                            }else if(s.getNpcKind()[i][j]==NpcMessage.VENDER){
                                gw.getList()[i+1].getArr()[j]=new NpcVender(0,0,
                                        gw.getNpcMessages()[i+1][j].getName(), 30,
                                        10, 0, 0,
                                        gw.getNpcMessages()[i+1][j].getImage(), gw);
                            }
                            String[] arr=s.getMapNPC()[i][j].split(",");
                            gw.getList()[i+1].getArr()[j].setX(Integer.parseInt(arr[0]));
                            gw.getList()[i+1].getArr()[j].setY(Integer.parseInt(arr[1]));
                            gw.getList()[i+1].getArr()[j].setHp(Integer.parseInt(arr[2]));
                        }
                    }
                }
                for(int i=0;i<s.getMapItem().length;++i){
                    for(int j=0;j<s.getMapItem()[i].length;++j){
                        if(s.getMapItem()[i][j].equals("null")){
                            gw.getList()[i+1].getItems()[j]=null;
                        }else{
                            String [] arr=s.getMapItem()[i][j].split(",");
                            if(arr[2].equals("村好剑")){
                                gw.getList()[i+1].getItems()[j]=new Sword(
                                        Integer.parseInt(arr[0]), Integer.parseInt(arr[1]),
                                        "村好剑", "攻击力+10","/Resources/character/飞剑.png",gw);
                            }else{
                                gw.getList()[i+1].getItems()[j]=new BloodPill(
                                        Integer.parseInt(arr[0]),Integer.parseInt(arr[1]),
                                        "回血丹","血量+50","/Resources/character/鸡蛋.jpg",gw);
                            }
                        }
                    }
                }
                //map_0
                ((NpcVender)(gw.getList()[0].getArr()[0])).getVenderWindow().getItems().clear();
                for(String str:s.getVenderItems()){
                    if(str.equals("村好剑")){
                      ((NpcVender)(gw.getList()[0].getArr()[0])).getVenderWindow().getItems().add(
                              new Sword(0,0,"村好剑",
                                      "攻击力+10","/Resources/character/飞剑.png",gw)
                      );
                    }else{
                        ((NpcVender)(gw.getList()[0].getArr()[0])).getVenderWindow().getItems().add(
                                new BloodPill(0,0,"回血丹",
                                        "血量+50","/Resources/character/鸡蛋.jpg",gw)
                        );
                    }
                }
                String [] venderStr = s.getVender().split(",");
                gw.getList()[0].getArr()[0].setX(Integer.parseInt(venderStr[0]));
                gw.getList()[0].getArr()[0].setY(Integer.parseInt(venderStr[1]));
            } catch (IOException | ClassNotFoundException | UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttons[2].addActionListener(e -> {
            PropertySettingDialog=new JDialog(this,"属性设置",false);
            JLabel l=new JLabel("姓名最长四个字，超出会被自动截断！");
            PropertySettingDialog.setPreferredSize(new Dimension(gw.getWidth()/2,gw.getHeight()/2));
            JPanel mainPanel=new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
            mainPanel.add(l);
            JPanel name=getInputPanel("角色名",nameInput);
            JPanel attack=getInputPanel("攻击力",attackInput);
            JPanel defense=getInputPanel("防御力",defenseInput);
            JPanel hp=getInputPanel("血量",hpInput);
            JPanel buttonPanel=new JPanel();
            JButton yes=new JButton("确定");
            JButton cancel=new JButton("取消");
            buttonPanel.add(yes);
            buttonPanel.add(cancel);
            mainPanel.add(name);
            mainPanel.add(attack);
            mainPanel.add(defense);
            mainPanel.add(hp);
            mainPanel.add(buttonPanel);
            PropertySettingDialog.add(mainPanel);
            PropertySettingDialog.pack();
            PropertySettingDialog.setLocationRelativeTo(this);
            PropertySettingDialog.setVisible(true);
            yes.addActionListener(e1 -> {
                String[] t={
                  nameInput.getText(),attackInput.getText(),defenseInput.getText(),hpInput.getText()
                };
                if(!"".equals(t[0])){
                    if(t[0].length()>4){
                        gw.getPlayer().setName(t[0].substring(0,4));
                    }else{
                        gw.getPlayer().setName(t[0]);
                    }
                }
                boolean mark=false;
                if(!"".equals(t[1])){
                    try {
                        int a = Integer.parseInt(t[1]);
                        gw.getPlayer().setAttackPower(a);
                    } catch (Exception e2) {
                        mark = true;
                    }
                }
                if(!"".equals(t[2])){
                    try {
                        int a = Integer.parseInt(t[2]);
                        gw.getPlayer().setDefense(a);
                    } catch (Exception e2) {
                        mark = true;
                    }
                }
                if(!"".equals(t[3])){
                    try {
                        int a = Integer.parseInt(t[3]);
                        gw.getPlayer().setHp(a);
                    } catch (Exception e2) {
                        mark = true;
                    }
                }
                PropertySettingDialog.setVisible(false);
                String tx;
                if(!mark){
                    tx="设置成功";
                }else{
                    tx="攻击,血量和防御都应为正整数！";
                }
                JOptionPane.showMessageDialog(this,tx,
                        "提示",JOptionPane.INFORMATION_MESSAGE);
            });
            cancel.addActionListener(e1 -> {
                PropertySettingDialog.setVisible(false);
            });
        });

        //
        JButton[] others = new JButton[3];
        otherMenu.add(Box.createVerticalStrut(20));
        for (int i = 0; i < others.length; ++i) {
            others[i] = new JButton();
            others[i].setMaximumSize(new Dimension(buttonWidth, buttonHeight));
            others[i].setMinimumSize(new Dimension(buttonWidth, buttonHeight));
            others[i].setFont(getScaledFont(buttonHeight));
            others[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            others[i].setAlignmentY(Component.CENTER_ALIGNMENT);
            otherMenu.add(others[i]);
            otherMenu.add(Box.createVerticalStrut(20));
        }
        others[0].setText("游戏指导");
        others[0].addActionListener(e -> {
            JTextArea message=new JTextArea(GameLongText.getGameGuidance());
            message.setEditable(false);
            message.setLineWrap(true);
            message.setFont(new Font("黑体",Font.BOLD,20));
            JScrollPane scrollPane=new JScrollPane(message);
            scrollPane.setPreferredSize(new Dimension(getWidth()/2,getHeight()/2));
            JOptionPane.showMessageDialog(
                    gamePanel,
                    scrollPane,
                    "游戏指导",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        others[1].setText("角色切换");
        others[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] arr = {"将军", "女王", "国王", "牛仔"};
                int originalNumber = gw.getCharacterNumber();
                changeCharacterDialog = new JDialog((JFrame) null, "角色切换", true);
                changeCharacterDialog.setPreferredSize(new Dimension(gw.getWidth(), gw.getHeight()));
                JPanel changeCharacter = new JPanel(new BorderLayout());
                JLabel nameLabel = new JLabel();
                nameLabel.setText(arr[gw.getCharacterNumber()]);
                nameLabel.setFont(getScaledFont(buttonHeight));
                nameLabel.setAlignmentX(BoxLayout.X_AXIS);
                changeCharacter.add(nameLabel, BorderLayout.NORTH);
                JPanel imagePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(new ImageIcon(Objects.requireNonNull(getClass().
                                        getResource(gw.getCharacterURLString(gw.getCharacterNumber())))).getImage(),
                                0, 0, getWidth(), getHeight(),
                                0, 7 * gw.getPlayer().getHeight(),
                                gw.getPlayer().getWidth(), 8 * gw.getPlayer().getHeight(), null);
                    }
                };
                changeCharacter.add(imagePanel, BorderLayout.CENTER);
                JButton last = new JButton();
                last.setText("上一个");
                last.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changeCharacter(-1);
                        nameLabel.setText(arr[gw.getCharacterNumber()]);
                        imagePanel.repaint();
                    }
                });
                JButton next = new JButton();
                next.setText("下一个");
                next.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changeCharacter(1);
                        nameLabel.setText(arr[gw.getCharacterNumber()]);
                        imagePanel.repaint();
                    }
                });
                JButton yes = new JButton();
                yes.setText("确定");
                yes.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gw.getPlayer().changeImage(gw.getCharacterURLString(gw.getCharacterNumber()));
                        changeCharacterDialog.dispose();
                    }
                });
                JButton cancel = new JButton();
                cancel.setText("取消");
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gw.setCharacterNumber(originalNumber);
                        changeCharacterDialog.dispose();
                    }
                });
                changeCharacter.add(last, BorderLayout.WEST);
                changeCharacter.add(next, BorderLayout.EAST);
                JPanel yesAndCancelPanel = new JPanel();
                yesAndCancelPanel.add(yes);
                yesAndCancelPanel.add(cancel);
                changeCharacter.add(yesAndCancelPanel, BorderLayout.SOUTH);
                changeCharacter.setVisible(true);
                changeCharacterDialog.add(changeCharacter);
                changeCharacterDialog.setContentPane(changeCharacter);
                changeCharacterDialog.setPreferredSize(new Dimension(gw.getWidth(), gw.getHeight()));
                changeCharacterDialog.pack(); // 调整对话框大小以适应组件
                changeCharacterDialog.setLocationRelativeTo(null); // 居中显示
                changeCharacterDialog.setVisible(true); // 显示对话框
            }
        });
        others[2].setText("作者介绍");
        others[2].addActionListener(e -> {
            JTextArea message=new JTextArea(GameLongText.getAuthorIntroduction());
            message.setEditable(false);
            message.setLineWrap(true);
            message.setFont(new Font("黑体",Font.BOLD,20));
            JScrollPane scrollPane=new JScrollPane(message);
            scrollPane.setPreferredSize(new Dimension(getWidth()/2,getHeight()/2));
            JOptionPane.showMessageDialog(
                    gamePanel,
                    scrollPane,
                    "作者介绍",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        choiceMenu.add(mapMenu);
        choiceMenu.add(Box.createHorizontalStrut(20));
        choiceMenu.add(otherMenu);
        // 将按钮添加到按钮面板
        buttonPanel.add(startButton);
        buttonPanel.add(endButton);

        //绑定事件
        this.setFocusable(true);
        this.addMouseListener(gw);
        pack();
    }

    public void changeCharacter(int i) {
        if (i > 0) {
            gw.setCharacterNumber((gw.getCharacterNumber() + i) % gw.getCharacterURLLength());
        } else {
            gw.setCharacterNumber((gw.getCharacterNumber() + i + gw.getCharacterURLLength()) % gw.getCharacterURLLength());
        }
    }

    public void start() {
        setVisible(true);
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        startTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                if (count < 7) {
                    setCount(count + 1);
                }
                if (count == 7 && !gamePanel.isAncestorOf(buttonPanel)) {
                    // 确保只添加一次
                    gamePanel.add(buttonPanel, BorderLayout.NORTH);
                    gamePanel.revalidate(); // 刷新布局
                    gamePanel.repaint(); // 重绘组件
                } else if (!gamePanel.isAncestorOf(choiceMenu) && needMenu) {
                    gamePanel.add(choiceMenu);
                    gamePanel.revalidate(); // 刷新布局
                    gamePanel.repaint(); // 重绘组件
                }
            }
        });
        startTimer.start();
    }

    public void nextImage() {
        if (startTimer != null && startTimer.isRunning()) {
            startTimer.stop();               // 先停止计时器（如果已启动）
            startTimer.setInitialDelay(0);   // 设置首次执行延迟为0毫秒
            startTimer.start();              // 启动计时器，会立即触发一次任务
            startTimer.setInitialDelay(1000);//
        }
    }

    public void end() {
        backgroundMusic.stop();
        startTimer.stop();
        setVisible(false);
        dispose();
    }

    public int getButtonWidth() {
        return buttonWidth;
    }

    public int getButtonHeight() {
        return buttonHeight;
    }
    private  JPanel getInputPanel(String word,JTextField field){
        JLabel l=new JLabel(word);
        l.setFont(new Font("黑体",Font.BOLD,18));
        JPanel p=new JPanel();
        p.add(l);
        p.add(field);
        return p;
    }
}
