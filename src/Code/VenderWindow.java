package Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VenderWindow extends JWindow  {
    private NpcVender npc;
    private JPanel buttonPanel=new JPanel();
    private ArrayList<Item> items;
    private JPanel mainPanel = new JPanel();
    private Point initialClick;
    private JPanel bagPanel = new JPanel();
    private int rows = 3, cols = 3;
    private JScrollPane scrollPane = new JScrollPane();
    private ArrayList<JButton> buttonItemList = new ArrayList<>();
    GameWorld gw;

    public ArrayList<Item> getItems(){
        return items;
    }
    public VenderWindow(GameWorld gw, ArrayList<Item> items,NpcVender npc) {
        this.npc=npc;
        this.items = items;
        this.gw = gw;
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JLabel txt = new JLabel("道具界面");
        txt.setFont(new Font("黑体", Font.BOLD, 20));
        txt.setAlignmentX(BoxLayout.X_AXIS);
        mainPanel.add(txt);
        mainPanel.add(scrollPane);
        setPreferredSize(new Dimension(gw.getWidth() / 2, gw.getHeight() / 2));
        bagPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        scrollPane.setViewportView(bagPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(buttonPanel);
        JButton b1=new JButton("退出");
        b1.setAlignmentX(BoxLayout.X_AXIS);
        b1.addActionListener(e -> {
            end();
        });
        buttonPanel.add(b1);
        add(mainPanel);
        pack();
        setLocationRelativeTo(gw.getCurrentGameMap());
        refreshBag();
        // 添加鼠标拖动功能
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        mainPanel.addMouseMotionListener(new MouseAdapter() {
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
    }

    public void refreshBag() {
        bagPanel.removeAll();
        buttonItemList.clear();
        for (int i = 0; i < items.size(); ++i) {
            Item item = items.get(i);
            JButton t = new JButton("<html>" + item.getTitle() + "<br>" + item.introduction + "</html>",
                    item.getTargetImageIcon());
            t.setPreferredSize(new Dimension(item.frameWidth, item.frameHeight));
            t.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        //左键点击
                        gw.getPlayer().getItem(item);
                        items.remove(item);
                        refreshBag();
                    }
                }
            });
            buttonItemList.add(t);
            bagPanel.add(t);
        }
        int index = buttonItemList.size() % (rows * cols);
        if (index != 0) {
            for (int i = 0; i < (rows * cols) - index; ++i) {
                // 创建带边框的空白格子
                JLabel placeholder = new JLabel();
                placeholder.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                placeholder.setOpaque(true);
                placeholder.setBackground(new Color(240, 240, 240)); // 浅灰色背景
                bagPanel.add(placeholder);
            }
        }
        bagPanel.revalidate();
        bagPanel.repaint();
    }

    public void display() {
        setVisible(true);
        gw.setWatchingBag(true);
        npc.waiting=true;
        refreshBag();
    }

    public void end() {
        setVisible(false);
        gw.setWatchingBag(false);
        npc.waiting=true;
        npc.waitingTimer.start();
    }
}
