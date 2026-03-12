package model;
import java.util.function.Consumer;

public class GameAction {
    private String name;
    private Consumer<Player> action;

    public GameAction(String name, Consumer<Player> action) {
        this.name = name;
        this.action = action;
    }

    public void execute(Player player) {
        action.accept(player);
    }
}