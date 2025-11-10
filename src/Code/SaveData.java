package Code;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveData implements Serializable {
    //player信息
    transient Player p;
    private ArrayList<Boolean> hasUsed=new ArrayList<>();
    private String name;
    private int playerX, playerY, playerAttack, playerDefense, playerHp;
    private int imageNum;
    private ArrayList<String> bagItems = new ArrayList<>();
    //map(1到3）
    private int [][] npcKind=new int[3][2];
    private String[][] mapNPC = new String[3][2];
    private String[][] mapItem = new String[3][2];
    //map_0
    private ArrayList<String> venderItems = new ArrayList<>();
    private String vender;
    //GameWord 信息
    private int currentMapIndex; // 当前所在的地图索引
    private boolean inFighting; // 是否在战斗中
    private boolean watchingBag; // 是否打开了背包

    public SaveData(GameWorld gw) {
        //GameWorld信息
        currentMapIndex = gw.getIndex();
        inFighting = gw.getInFighting();
        watchingBag = gw.isWatchingBag();
        //palyer
        p = gw.getPlayer();
        name = p.getName();
        playerX = p.getX();
        playerY = p.getY();
        playerAttack = p.getAttackPower();
        playerDefense = p.getDefense();
        playerHp = p.getHp();
        for (int i = 0; i < p.getBag().size(); ++i) {
            bagItems.add(p.getBag().get(i).getTitle());
        }
        hasUsed.addAll(gw.getBag().getHasUsed());
        imageNum = gw.getCharacterNumber();
        //map(房子1到3）的NPC信息（坐标，血量）
        for (int i = 1; i < gw.getList().length; ++i) {
            GameMap map = gw.getList()[i];
            for (int j = 0; j < map.getArr().length; ++j) {
                var n = map.getArr()[j];
                if (n != null) {
                    mapNPC[i - 1][j] = n.getX() + "," + n.getY() + "," + n.getHp();
                    if(n instanceof NpcFighter){
                        npcKind[i-1][j]=NpcMessage.FIGHTER;
                    }else{
                        npcKind[i-1][j]=NpcMessage.VENDER;
                    }
                } else {
                    mapNPC[i - 1][j] = "null";
                    npcKind[i-1][j]=-1;
                }
            }
            for (int j = 0; j < map.getItems().length; ++j) {
                var it = map.getItems()[j];
                if (it != null) {
                    mapItem[i - 1][j] = it.getX() + "," + it.getY() + "," + it.getTitle();
                } else {
                    mapItem[i - 1][j] = "null";
                }
            }
        }
        //休息区的货商的道具情况
        vender = gw.getList()[0].getArr()[0].getX() + "," + gw.getList()[0].getArr()[0].getY();
        ArrayList<Item> items = ((NpcVender) gw.getList()[0].getArr()[0]).getVenderWindow().getItems();
        for (Item item : items) {
            venderItems.add(item.getTitle());
        }
    }


   public int[][] getNpcKind(){
        return npcKind;
   }

    public String getName() {
        return name;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getPlayerAttack() {
        return playerAttack;
    }

    public int getPlayerDefense() {
        return playerDefense;
    }

    public int getPlayerHp() {
        return playerHp;
    }

    public int getImageNum() {
        return imageNum;
    }

    public ArrayList<String> getBagItems() {
        return bagItems;
    }

    public String[][] getMapNPC() {
        return mapNPC;
    }

    public String[][] getMapItem() {
        return mapItem;
    }

    public ArrayList<String> getVenderItems() {
        return venderItems;
    }

    public String getVender() {
        return vender;
    }

    public int getCurrentMapIndex() {
        return currentMapIndex;
    }

    public boolean isInFighting() {
        return inFighting;
    }

    public boolean isWatchingBag() {
        return watchingBag;
    }

    public ArrayList<Boolean> getHasUsed(){
        return hasUsed;
    }
}
