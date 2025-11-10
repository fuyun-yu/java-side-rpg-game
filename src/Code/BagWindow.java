package Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class BagWindow extends JWindow  {
    protected ArrayList<Boolean> hasUsed=new ArrayList<>();
    private JPanel mainPanel=new JPanel();
    private Point initialClick;
    private JPanel bagPanel=new JPanel();
    private int rows=3,cols=3;
    private JScrollPane scrollPane=new JScrollPane();
    private ArrayList<JButton> buttonItemList=new ArrayList<>();
    GameWorld gw;
    public BagWindow(GameWorld gw){
        this.gw=gw;
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        JLabel txt=new JLabel("角色背包,左键点击使用,再次点击取消使用,右键点击丢弃");
        txt.setFont(new Font("黑体",Font.BOLD,20));
        txt.setAlignmentX(BoxLayout.X_AXIS);
        mainPanel.add(txt);
        mainPanel.add(scrollPane);
        setPreferredSize(new Dimension(gw.getWidth()/2,gw.getHeight()/2));
        bagPanel.setLayout(new GridLayout(rows,cols,5,5));
        scrollPane.setViewportView(bagPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        for(int i=0;i<temp.size();++i){
//            Item item=temp.get(i);
//            JPanel t=new JPanel(){
//               @Override
//               protected void paintComponent(Graphics g) {
//                   super.paintComponent(g);
//                   g.drawImage(item.image,0,0,item.getWidth() ,item.frameHeight,
//                           5* item.frameHeight ,0,
//                           6* item.frameHeight , item.frameWidth,null);
//               }
//           };
//           t.setPreferredSize(new Dimension(item.getWidth(),item.getHeight()));
//           bagPanel.add(t);
//        }=;
        add(mainPanel);
        pack();
        setLocationRelativeTo(gw.getCurrentGameMap());
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
    public void showBag(){
        refreshBag();
        setVisible(true);
        gw.setWatchingBag(true);
    }
    public void hideBag(){
        setVisible(false);
        gw.setWatchingBag(false);
    }
    public ArrayList<Boolean> getHasUsed(){
        return hasUsed;
    }
    public void refreshBag(){
        bagPanel.removeAll();
        buttonItemList.clear();
        ArrayList<Item> temp=gw.getPlayer().getBag();
//        for(int i=0;i<temp.size();++i){
//            Item item=temp.get(i);
//            JPanel t=new JPanel(){
//               @Override
//               protected void paintComponent(Graphics g) {
//                   super.paintComponent(g);
//                   g.drawImage(item.image,0,0,item.getWidth() ,item.frameHeight,
//                           5* item.frameHeight ,0,
//                           6* item.frameHeight , item.frameWidth,null);
//               }
//           };
//           t.setPreferredSize(new Dimension(item.getWidth(),item.getHeight()));
//           bagPanel.add(t);
//        }
        for (int i=0;i<temp.size();++i) {
            if(hasUsed.size()==i){
                hasUsed.add(false);
            }
            Item item =temp.get(i);
            JButton t = new JButton("<html>" + item.getTitle() + "<br>" + item.introduction + "</html>",
                    item.getTargetImageIcon());
            if(hasUsed.get(i)){
                t.setBackground(Color.LIGHT_GRAY);
            }
            t.setPreferredSize(new Dimension(item.frameWidth, item.frameHeight));
            int index=i;
            t.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if(e.getButton()==MouseEvent.BUTTON1){
                        //左键点击
                        if(!hasUsed.get(index)){
                            item.function(index);
                            t.setBackground(Color.LIGHT_GRAY);
                        }else{
                            item.antiFunction(index);
                            t.setBackground(null);
                        }
                    }else if(e.getButton()==MouseEvent.BUTTON3){
                        //右键点击
                        item.dropped( index);//temp.indexOf(item)
                    }
                }
            });
            buttonItemList.add(t);
            bagPanel.add(t);
        }
        int index=buttonItemList.size()%(rows*cols);
        if(index!=0){
            for(int i=0;i<(rows*cols)-index;++i){
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
}
