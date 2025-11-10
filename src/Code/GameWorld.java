package Code;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class GameWorld implements MouseListener, KeyListener {
    private boolean watchingBag = false;
    private BagWindow bag;
//    private boolean o = false;
//    private boolean c = false;
//    private boolean b = false;
    private boolean isSaving=false;
    private boolean inFighting = false;
    private int keyMovedX, keyMovedY;
    private boolean clickMove = false;
    private final String[] characterURL = {
            "/Resources/character/小男孩-将军.png",
            "/Resources/character/女仆.png",
            "/Resources/character/女王.png",
            "/Resources/character/小男孩-牛仔帽.png"
    };
    private NpcMessage[][] npcMessages = {
            {
                    new NpcMessage("/Resources/character/白马.png", "卖货白马", NpcMessage.VENDER)
            },
            {
                    new NpcMessage("/Resources/character/狮子.png",
                            "大肥飞龙", NpcMessage.FIGHTER),
                    new NpcMessage("/Resources/character/蜘蛛.png",
                            "小瘦蜘蛛", NpcMessage.FIGHTER)
            },
            {
                    new NpcMessage("/Resources/character/狮子.png",
                            "大肥飞龙", NpcMessage.FIGHTER),
                    new NpcMessage("/Resources/character/蜘蛛.png",
                            "小瘦蜘蛛", NpcMessage.FIGHTER)
            },
            {
                    new NpcMessage("/Resources/character/狮子.png",
                            "大肥飞龙", NpcMessage.FIGHTER),
                    new NpcMessage("/Resources/character/蜘蛛.png",
                            "小瘦蜘蛛", NpcMessage.FIGHTER)
            }
    };
    public final int START_STATE = 0, MAP_STATE = 1;
    private final int v = 4;
    private int clickTargetX, clickTargetY;
    private int characterNumber = 0;
    private int gameState;
    private GameStart gameStart;
    //private Timer moveTimer;
    private int index = 0;
    private boolean up = false, down = false, left = false, right = false;
    private boolean hasNext = false;
    private Player player = new Player(100, 130, "player", 100, 10,
            100, 0, characterURL[characterNumber], this);
    private GameMap[] list = new GameMap[4];
    private GameMap currentGameMap;
    private int width, height;
    private Timer worldTimer;

    public boolean isSaving() {
        return isSaving;
    }

    public void setSaving(boolean saving) {
        isSaving = saving;
    }

    public boolean getInFighting() {
        return inFighting;
    }

    public void setInFighting(boolean inFighting) {
        this.inFighting = inFighting;
    }

    public boolean isWatchingBag() {
        return watchingBag;
    }

    public void setWatchingBag(boolean watchingBag) {
        this.watchingBag = watchingBag;
    }

    public BagWindow getBag(){
        return bag;
    }
    public void setGameState(int i){
        gameState=i;
    }
    public GameWorld(int width, int height) throws UnsupportedAudioFileException, IOException {
        /*moveTimer = new Timer(25, actionEvent -> {
            if(moveTimer!=null){
                moveTimer.stop();
            }
            int dx = x - player.getX();
            int dy = y - player.getY();
            if (dx == 0 && dy == 0) {
                ((Timer) actionEvent.getSource()).stop(); // 停止当前定时器
                currentGameMap.flashData(0, 0);
                currentGameMap.controlSound();
                return;
            }
            int px = 0, py = 0;
            if (dx != 0) {
                if (Math.abs(dx) < v) {
                    player.setX(x);
                } else {
                    px = dx / Math.abs(dx) * v;
                }
            }
            if (dy != 0) {
                if (Math.abs(dy) < v) {
                    player.setY(y);
                } else {
                    py = dy / Math.abs(dy) * v;
                }
            }
            currentGameMap.flashData(px, py);
        });*/
        this.gameState = START_STATE;
        this.width = width;
        this.height = height;
        gameStart = new GameStart(this);
        currentGameMap = list[index];
        bag = new BagWindow(this);
        //游戏总时间循环
        worldTimer = new Timer(15, e -> {
            //NPC碰撞检测和NPC巡逻的进行
            if (!watchingBag) {
                for (NPC n : currentGameMap.getArr()) {
                    if (n != null) {
                        if (n.collision(player) && !inFighting) {
                            if (!n.getWaiting()) {
                                left = right = up = down = false;
                                clickMove = false;
                            }
                            n.collisionReact(player);
                        }
                        if (!n.getStop() && !inFighting) {
                            n.Patrol();
                        }
                    }
                }
            }
            //道具碰撞检测
            for (Item item : currentGameMap.getItems()) {
                if (item != null) {
                    if (item.collision(player)) {
                        item.collisionReact(player);
                    } else {
                        item.setInCollision(false);
                    }
                }
            }
            //行动相关事务
            if ((left || right || up || down) && !inFighting) {
                player.move(keyMovedX, keyMovedY);
            } else if (clickMove && !inFighting) {
                int dx = clickTargetX - player.getX();
                int dy = clickTargetY - player.getY();
                if (dx == 0 && dy == 0) {
                    clickMove = false;
                    currentGameMap.controlSound();
                } else {
                    int px = 0, py = 0;
                    if (dx != 0) {
                        if (Math.abs(dx) < v) {
                            player.setX(clickTargetX);
                        } else {
                            px = dx / Math.abs(dx) * v;
                        }
                    }
                    if (dy != 0) {
                        if (Math.abs(dy) < v) {
                            player.setY(clickTargetY);
                        } else {
                            py = dy / Math.abs(dy) * v;
                        }
                    }
                    player.move(px, py);
                }

            }
            currentGameMap.flashData();
        });
    }
    public GameMap[] getList(){
        return list;
    }
    public GameStart getGameStart() {
        return this.gameStart;
    }

    public String getCharacterURLString(int i) {
        return characterURL[i];
    }

    public int getCharacterURLLength() {
        return characterURL.length;
    }

    public int getCharacterNumber() {
        return characterNumber;
    }

    public void setCharacterNumber(int characterNumber) {
        this.characterNumber = characterNumber;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public GameMap getCurrentGameMap() {
        return currentGameMap;
    }

    public int getV() {
        return v;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Player getPlayer() {
        return player;
    }

    public void start() {
        gameStart.start();
    }

    public void enterGame() throws UnsupportedAudioFileException, IOException {
        list[0] = new GameMap(this, "/Resources/45度角地图-背景图+遮挡图/nanzhao.jpg", "休息区", width, height,
                npcMessages[0]);
        for (int i = 1; i < list.length; ++i) {
            list[i] = new GameMap(this, "/Resources/45度角地图-背景图+遮挡图/house_" + (i + 1) + ".jpg"
                    , "房子" + i, width, height, npcMessages[i]);
        }
        gameStart.end();
        currentGameMap = list[index];
        currentGameMap.start();
        gameState = MAP_STATE;
        worldTimer.start();
    }
    public NpcMessage[][] getNpcMessages(){
        return npcMessages;
    }
    private SaveData getLoadObject(){
        return new SaveData(this);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'p') {
            for (Item item : currentGameMap.getItems()) {
                if (item != null) {
                    if (item.isInCollision()) {
                        player.getItem(item);
                    }
                }
            }
        }else if(c=='b'){
            bag.showBag();
        }else if(c=='c'){
            bag.hideBag();
        }else if(c=='o'){
            File f=new File("saveData.txt");
            try(ObjectOutputStream ops=new ObjectOutputStream(new FileOutputStream(f))) {
                //序列化
                ops.writeObject(getLoadObject());
                setSaving(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                down = true;
                break;
        }
        if (left) {
            keyMovedX = -v;
        }
        if (right) {
            keyMovedX = v;
        }
        if (up) {
            keyMovedY = -v;
        }
        if (down) {
            keyMovedY = v;
        }

        //player.move(keyTargetX, keyTargetY);

        if ((code == KeyEvent.VK_V || code == KeyEvent.VK_N) && !hasNext) {
            GameMap last = currentGameMap;
            if (code == KeyEvent.VK_V) {
                currentGameMap = list[++index % list.length];
            } else {
                index = (index - 1 + list.length) % list.length;
                currentGameMap = list[index];
            }
            player.setX(100);
            player.setY(100);
            currentGameMap.start();
            last.end();
            hasNext = true;
            if (watchingBag) {
                bag.hideBag();
            }
        }
        clickMove = false;
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        currentGameMap.controlSound();
        if (code == KeyEvent.VK_V || code == KeyEvent.VK_N) {
            hasNext = false;
        }
        switch (code) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                down = false;
                break;
        }
        if (!left && !right && !up && !down) {
            keyMovedY = 0;
            keyMovedX = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameState == MAP_STATE) {
            if(e.getButton()==MouseEvent.BUTTON1){
                //左键
                clickMove = true;
                clickTargetX = e.getX() - player.getWidth() / 2;
                clickTargetY = e.getY() - player.getHeight() / 2;
            }
            /*if (moveTimer != null && moveTimer.isRunning()) {
                moveTimer.stop();
            }*/
            /*if (player.getX() != targetX || player.getY() != targetY) {
                moveTimer.start();
            }*/
            if(e.getButton()==MouseEvent.BUTTON3){
                //右键
                if(!isWatchingBag()){
                    bag.showBag();
                }else{
                    bag.hideBag();
                }
            }
        }
        if (gameState == START_STATE) {
            int x = e.getX(), y = e.getY();
            if (x > 0 && x <= gameStart.getWidth() && y > 0 && y <= gameStart.getHeight()) {
                gameStart.nextImage();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
