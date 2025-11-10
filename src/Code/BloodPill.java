package Code;

public class BloodPill extends Item{
    public BloodPill(int x, int y, String title,String introduction, String url, GameWorld gw) {
        super(x, y, title, introduction,url, gw);
    }

    @Override
    public Object function(int index) {
        gw.getBag().hasUsed.set(index,true);
        gw.getPlayer().setHp(gw.getPlayer().getHp()+50);
        this.dropped(index);
        return GameEnum.Function.WEAR;
    }

    @Override
    public Object antiFunction(int index) {
        return null;
    }

    @Override
    public Object collisionReact(GameObject go) {
        setInCollision(true);
        return GameEnum.Collision.OK;
    }
}
