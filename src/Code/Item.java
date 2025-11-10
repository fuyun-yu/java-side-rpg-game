package Code;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class Item extends GameObject implements CollisionReact {
    protected String introduction;
    protected Color color;
    protected String txt;
    protected boolean inCollision=false;
    protected String title;
    protected Image image;
    protected GameWorld gw;
    protected int frameWidth, frameHeight;
    public Item(int x, int y, String title,String introduction,String url,GameWorld gw) {
        super(x, y, 0,0);
        this.title = title;
        this.introduction=introduction;
        this.image=new ImageIcon(Objects.requireNonNull(getClass().getResource(url))).getImage();
        this.frameWidth =image.getWidth(null)/13;
        this.frameHeight =image.getHeight(null)/8;
        super.setHeight(frameHeight);
        super.setWidth(frameWidth);
        this.gw=gw;
        color=Color.white;
        txt=title;
    }

    public Image getImage(){
        return image;
    }
    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public abstract Object function(int index);
    public  Object dropped(int index){
        Player player=gw.getPlayer();
        antiFunction(index);
        gw.getBag().hasUsed.remove(index);
        player.getBag().remove(index);
        gw.getBag().refreshBag();
        return null;
    }

    public abstract Object antiFunction(int index);

    public void draw(Graphics g){
        g.setColor(color);
        g.fillRoundRect(getX(), getY() - 30, 80, 25, 10, 10);
        g.setColor(Color.BLACK);
        g.setFont(new Font("黑体", Font.BOLD, 14));
        g.drawString(txt, getX() + 10, getY() - 10);
        g.drawImage(image, x,y,x+ frameWidth ,y+ frameHeight,
                0,4*frameHeight,
                 frameWidth , 5*frameHeight,null);
    }
    public boolean isInCollision() {
        return inCollision;
    }

    public void setInCollision(boolean inCollision) {
        if(inCollision){
            color =Color.yellow ;//new Color(255, 215, 0, 200)
            txt = "按 P 拾取";
        }else{
            color=Color.white;
            txt=title;
        }
        this.inCollision = inCollision;
    }
    public ImageIcon getTargetImageIcon(){
        BufferedImage b=new BufferedImage(image.getWidth(null)
                ,image.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics2D g=b.createGraphics();
        g.drawImage(image,0,0,null);
        g.dispose();
        return new ImageIcon(b.getSubimage(0,4*frameHeight,
                frameWidth , frameHeight));
    }
}
