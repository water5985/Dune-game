package model;
public abstract class Character {
    protected String name;

    public Character(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void interact(Player player);
    protected int x = 0;
    protected int y = 0;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
