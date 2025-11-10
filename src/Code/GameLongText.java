package Code;

public class GameLongText {
    private static final String authorIntroduction = """
            \
            作者：xxx
            学号：xxxxxxxxxxxx
            班级：rrrrrrrrrrrrrrr
            学院：uuuuuuuuuuuuuuuuu
            """;
    private static final String gameGuidance = """
            \
            欢迎游玩本游戏：
            
            主菜单中，可通过属性设置调整人物初始血量、攻击、防御及姓名。
            自定义人物初始血量、攻击、防御时请输入整数，非法输入将不会被采纳（依旧按照默认值设置）。
            角色切换界面可以切换角色的形象。
            进入地图后，可用 AWSD 键、方向键或鼠标点击操控人物向八个方向行走（如同时按左键和上键，人物向左前方移动）；
            鼠标左键点击屏幕，人物会自动寻路前往该点，且支持中途更换目的地，但自动寻路时不会回避 NPC。
            按 V 键前往下一张地图，按 N 键返回上一张地图，四张地图循环（最后一张的下一张为第一张）。
            与 NPC 接触会触发对话，若冒犯 NPC 可能引发决斗；击败 NPC 可能掉落道具，靠近后可以按照提示拾取。
            按 B 键打开背包，按 C 键关闭背包（直接单击右键也可开关背包）。
            按 O 键存档，仅保留最近一次存档；游戏失败后，可读取存档重新开始。
            点击读取存档可以按照存档时的进度继续游戏。
            点击新游戏并不会导致原有存档消失，游戏并不会主动存档。
            """;

    public static String getGameGuidance() {
        return gameGuidance;
    }

    public static String getAuthorIntroduction() {
        return authorIntroduction;
    }
}
