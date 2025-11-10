package Code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class ChatDialog extends JWindow  {
    private JPanel chatPanel;
    private GameWorld gw;
    private int choice = 0;
    private ChoiceCallback callback;
    private Point initialClick;
    private JButton b1;
    private JButton b2;
    private JTextArea text;
    public interface ChoiceCallback {
        void onPress(int choice);
    }

    public void setArgs(String[] args){
        text.setText(args[0]);
        b1.setText(args[1]);
        b2.setText(args[2]);
    }
    public ChatDialog(GameWorld gw, NPC npc,String[] args) {
        super(gw.getCurrentGameMap());
        this.gw = gw;
        // 设置背景透明
        setBackground(Color.black);//new Color(0, 0, 0, 0)

        chatPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // 绘制矩形背景
                g2.setColor(new Color(128, 128, 128, 220));
                g2.fillRect(0, 0, getWidth(), getHeight());//, 20, 20
            }

            @Override
            public Dimension getPreferredSize() {
                // 计算内容所需的最佳大小
                Dimension size = super.getPreferredSize();
                return new Dimension(size.width + 40, size.height + 40);
            }
        };

        // 设置chatPanel为透明，只显示绘制的矩形
        chatPanel.setOpaque(false);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        text = new JTextArea(args[0]);
        text.setLineWrap(true);
        text.setFont(new Font("黑体", Font.BOLD, 20));
        text.setOpaque(false); // 设置文本区域透明
        text.setEditable(false);
        chatPanel.add(Box.createVerticalStrut(20));
        chatPanel.add(text);
        chatPanel.add(Box.createVerticalStrut(20));

        b1 = new JButton(args[1]);
        b1.setFont(new Font("黑体", Font.PLAIN, 18));
        b1.setFocusPainted(false);
        b1.addActionListener(e -> {
            if (callback != null) {
                callback.onPress(0);
            }
            setVisible(false);
        });

        b2 = new JButton(args[2]);
        b2.setFont(new Font("黑体", Font.PLAIN, 18));
        b2.setFocusPainted(false);
        b2.addActionListener(e -> {
            if (callback != null) {
                callback.onPress(1);
            }
            setVisible(false);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false); // 设置按钮面板透明
        buttonPanel.add(b1);
        buttonPanel.add(b2);

        chatPanel.add(buttonPanel);
        chatPanel.add(Box.createVerticalStrut(20));

        this.getContentPane().add(chatPanel);

        // 自适应内容大小
        pack();

        // 防止窗口大小小于内容所需大小
        setMinimumSize(getPreferredSize());

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
    }

    public int getChoice() {
        return choice;
    }

    public void setChoiceCallback(ChoiceCallback callback) {
        this.callback = callback;
    }
}