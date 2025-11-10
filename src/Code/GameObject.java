package Code;


public class GameObject {
    protected int x, y;
    protected int height, width;

    public GameObject(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setHeight(height);
        setWidth(width);
    }

    public boolean collision(GameObject go) {
        int thisMinX = x;
        int thisMaxX = x + width;
        int thisMinY = y;
        int thisMaxY = y + height;

        int otherMinX = go.x;
        int otherMaxX = go.x + go.width;
        int otherMinY = go.y;
        int otherMaxY = go.y + go.height;

        return (thisMinX <= otherMaxX &&
                thisMaxX >= otherMinX &&
                thisMinY <= otherMaxY &&
                thisMaxY >= otherMinY);
    }

    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y + height / 2;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height < 0 ? 10 : height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width < 0 ? 10 : width;
    }
}
