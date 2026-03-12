import model.*;
import view.View;
import controller.Controller;

import javax.swing.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Player player = new Player();
        Map<String, Location<?>> world = Game.createWorld(player);
        player.setCurrentLocation(world.get("Start"));

        View view = new View(player);
        Controller controller = new Controller(player, view);

        JFrame frame = new JFrame("Dune");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(view);
        frame.addKeyListener(controller);
        frame.setVisible(true);
        Game.startWormAI(player, view);
        Game.startSardukarAI(player, view);
        Game.startFremenAI(player, view);



        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.addKeyListener(controller);

    }
}
