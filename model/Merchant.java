package model;
import java.util.List;


public class Merchant extends Character {
    private List<Item<?>> inventory;

    public Merchant(String name, List<Item<?>> inventory) {
        super(name);
        this.inventory = inventory;
    }

    @Override
    public void interact(Player player) {

    }



    public List<Item<?>> getGoods() {
        return inventory;
    }

}
