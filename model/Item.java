package model;

public abstract class Item<T> {
    protected String name;
    protected T value;
    private final ItemType type;

    protected int x = 100;
    protected int y = 100;

    public Item(String name, T value, ItemType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public ItemType getType() {
        return type;
    }

    public abstract void use(Player player);
}
