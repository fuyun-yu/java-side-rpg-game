package Code;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;

public abstract class Character extends GameObject implements CollisionReact {
    protected GameWorld gw;
    //protected Component currentComponent;
    protected String name;
    protected int attackPower;
    protected int defense;
    protected int hp;
    protected int currentImage;
    protected Image image;
    protected int frameWidth, frameHeight;
    protected Clip footStepSound;
    protected int animationCounter = 0; // 添加动画计数器变量
    protected int frameControl = 0;

    public Character(int x, int y, String name,
                     int attackPower, int defense, int hp, int currentImage, String url, GameWorld gw) throws UnsupportedAudioFileException, IOException {
        super(x, y, 0, 0);
        changeImage(url);
        this.gw = gw;
        this.frameWidth = image.getWidth(null) / 13;
        this.frameHeight = image.getHeight(null) / 8;
        super.setWidth(frameWidth);
        super.setHeight(frameHeight);
        this.name = name;
        this.attackPower = attackPower;
        this.defense = defense;
        this.hp = hp;
        this.currentImage = currentImage;
        URL footStep = getClass().getResource("/Resources/帝国时代音效/走路声.wav");
        if (footStep != null) {
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(footStep)) {
                DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
                footStepSound = (Clip) AudioSystem.getLine(info);
                if (!footStepSound.isOpen()) {
                    footStepSound.open(ais);
                }
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(hp, 0);
    }

    public int getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(int currentImage) {
        this.currentImage = currentImage;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean attack(Character c) {
        c.setHp(c.getHp() - Math.max(0,attackPower - c.getDefense()));
        return c.getHp() == 0;
    }

    public boolean move(int dx, int dy) {
        boolean b1 = false;
        boolean b2 = false;
        if ((dx != 0 || dy != 0) && this instanceof Player) {
            footStepSound.loop(Clip.LOOP_CONTINUOUSLY);
        }
        // 计算方向
        if (dx > 0) {
            if (dy > 0) currentImage = 3;
            else if (dy < 0) currentImage = 1;
            else currentImage = 5;
        } else if (dx < 0) {
            if (dy > 0) currentImage = 0;
            else if (dy < 0) currentImage = 2;
            else currentImage = 4;
        } else {
            if (dy > 0) currentImage = 7;
            else if (dy < 0) currentImage = 6;
        }
        // 边界检查
        int newX = this.x + dx;
        int newY = this.y + dy;
        if (newX >= 0 && newX <= gw.getWidth() - this.width) {  // 地图宽度
            this.x = newX;
            b1 = true;
        }
        if (newY >= 0 && newY <= gw.getHeight() - this.height) {  // 地图高度
            this.y = newY;
            b2 = true;
        }

        // 更新动画帧 - 使用animationCounter控制帧率
        if (dx != 0 || dy != 0) {
            frameControl = (frameControl + 1) % 2;
            if (frameControl == 1) {
                animationCounter = (animationCounter + frameControl) % 13;
            }
        }
        return b1 && b2;
    }

    public void stopFootSound() {
        footStepSound.stop();
        footStepSound.setFramePosition(0);
    }

    public void draw(Graphics g) {
        //抠出小图像来渲染
        g.setColor(Color.white);
        g.fillRoundRect(this.getX(), this.getY() - 30, 80, 25, 10, 10);
        g.setColor(Color.BLACK);
        g.setFont(new Font("黑体", Font.BOLD, 14));
        g.drawString(name, this.getX() + 10, this.getY() - 10);
        g.drawImage(image,
                x, y, x + frameWidth, y + frameHeight,
                animationCounter * frameWidth, currentImage * frameHeight,
                (animationCounter + 1) * frameWidth, (currentImage + 1) * frameHeight,
                null);
    }

//    public void setCurrentComponent(Component c) {
//        currentComponent = c;
//    }

    public void changeImage(String url) {
        try {
            this.image = new ImageIcon(Objects.requireNonNull(getClass().getResource(url))).getImage();
        } catch (NullPointerException e) {
            System.err.println("Failed to load image: " + url);
            e.printStackTrace();
        }
    }
}