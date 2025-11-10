package Code;


public class Sword extends Item {

    public Sword(int x, int y, String title,String introduction,String url,GameWorld gw) {
        super(x, y,  title,introduction, url, gw);
    }

    @Override
    public Object function(int index) {
        gw.getBag().hasUsed.set(index,true);
        gw.getPlayer().setAttackPower(gw.getPlayer().getAttackPower()+10);
        return GameEnum.Function.WEAR;
    }

    @Override
    public Object antiFunction(int index) {
        gw.getBag().hasUsed.set(index,false);
        gw.getPlayer().setAttackPower(gw.getPlayer().getAttackPower()-10);
        return GameEnum.Function.WEAR;
    }


    @Override
    public Object collisionReact(GameObject go) {
        setInCollision(true);
        return GameEnum.Collision.OK;
    }
}
