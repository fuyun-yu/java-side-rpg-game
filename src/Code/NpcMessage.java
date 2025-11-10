package Code;

public class NpcMessage {
    public static final int FIGHTER=0;
    public static final int VENDER=1;
    private final String image;
    private final String name;
    private final int kind;

    public NpcMessage(String image, String name, int kind) {
        this.image = image;
        this.name = name;
        this.kind = kind;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getKind() {
        return kind;
    }
}
