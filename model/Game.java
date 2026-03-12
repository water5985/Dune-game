package model;

import javax.swing.*;
import java.util.*;

public class Game {
    public static Map<String, Location<?>> createWorld(Player player) {
        Map<String, Location<?>> locations = new HashMap<>();

        // Locations
        Location<String> darkForest = new Location<>("Dark Forest", null);
        Location<String> spiceCave = new Location<>("Spice Cave", "keyToSpiceCave");
        Location<String> sitchTabr = new Location<>("Sitch Tabr", "keyToSitchTabr");



        darkForest.addExit("north", spiceCave);
        spiceCave.addExit("south", darkForest);
        spiceCave.addExit("north", sitchTabr);
        sitchTabr.addExit("south", spiceCave);


        // Characters
        Enemy worm1 = new Enemy("Worm 1", 35, EnemyType.WORM);
        worm1.setPosition(50, 150);
        Enemy worm2 = new Enemy("Worm 2", 35, EnemyType.WORM);
        worm2.setPosition(150, 150);
        Enemy worm3 = new Enemy("Worm 3", 35, EnemyType.WORM);
        worm3.setPosition(250, 150);
        Enemy worm4 = new Enemy("Worm 4", 35, EnemyType.WORM);
        worm4.setPosition(100, 220);
        Enemy worm5 = new Enemy("Worm 5", 35, EnemyType.WORM);
        worm5.setPosition(200, 220);
        Enemy worm6 = new Enemy("Worm 6", 35, EnemyType.WORM);
        worm6.setPosition(200, 220);
        Enemy sardukar1 = new Enemy("Sardukar Elite 1", 20, EnemyType.SARDUKAR);
        sardukar1.setPosition(700, 100);
        Enemy sardukar2 = new Enemy("Sardukar Elite 2", 20, EnemyType.SARDUKAR);
        sardukar2.setPosition(700, 200);
        Enemy sardukar3 = new Enemy("Sardukar Elite 3", 20, EnemyType.SARDUKAR);
        sardukar3.setPosition(700, 300);
        Enemy sardukar4 = new Enemy("Sardukar Elite 4", 20, EnemyType.SARDUKAR);
        sardukar4.setPosition(700, 400);
        Enemy fremen = new Enemy("Fremen Assassin", 30, EnemyType.FREMEN);
        fremen.setPosition(300, 150);
        sitchTabr.addCharacter(fremen);




        darkForest.addCharacter(worm1);
        darkForest.addCharacter(worm2);
        darkForest.addCharacter(worm3);
        darkForest.addCharacter(worm4);
        darkForest.addCharacter(worm5);
        spiceCave.addCharacter(sardukar1);
        spiceCave.addCharacter(sardukar2);
        spiceCave.addCharacter(sardukar3);
        spiceCave.addCharacter(sardukar4);



        // Merchant with infinite consumables
        List<Item<?>> merchantGoods = new ArrayList<>();
        merchantGoods.add(new Consumable("+50 HP", 50));

        Merchant stilgar = new Merchant("Stilgar", new ArrayList<>()) {
            @Override
            public List<Item<?>> getGoods() {
                List<Item<?>> infinite = new ArrayList<>();
                infinite.add(new Consumable("+50 HP", 50));
                infinite.add(new Consumable("+40 HP", 40));
                infinite.add(new Consumable("+25 HP", 25));
                return infinite;
            }
        };
        stilgar.setPosition(30, 480);
        darkForest.addCharacter(stilgar);
        spiceCave.addCharacter(stilgar);
        sitchTabr.addCharacter(stilgar);

        // Items
        Consumable potion = new Consumable("+40 HP", 40);
        potion.setPosition(300, 200);
        Weapon crysknife = new Weapon("crysknife", 30);
        crysknife.setPosition(400, 300);
        Key key = new Key("Key", "goldenKey");
        key.setPosition(250, 300);
        Consumable water = new Consumable("+25 HP", 25);
        player.addItem(water);

        darkForest.addItem(crysknife);
        spiceCave.addItem(potion);
        spiceCave.addItem(potion);
        sitchTabr.addItem(potion);
        sitchTabr.addItem(potion);

        // Register in map
        locations.put("Start", darkForest);
        locations.put("Cave", spiceCave);

        return locations;
    }

    public static void startWormAI(Player player, view.View view) {
        Thread wormAIThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }

                for (Character c : player.getCurrentLocation().getCharacters()) {
                    if (c instanceof Enemy enemy &&
                            enemy.isAlive() &&
                            enemy.getType() == EnemyType.WORM) {

                        // Fixed worm position — customize per enemy later
                        int wormX = enemy.getX();
                        int wormY = enemy.getY();


                        int playerX = view.getPlayerX();
                        int playerY = view.getPlayerY();

                        double distance = Math.sqrt(Math.pow(wormX - playerX, 2) + Math.pow(wormY - playerY, 2));
                        if (distance <= 60) {
                            player.takeDamage(15);

                            SwingUtilities.invokeLater(() -> {
                                view.setMessage("Shai-Hulud strikes from beneath — 15 damage!");
                                view.repaint();
                            });

                            if (player.getHealth() <= 0) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(null, "You were consumed by the worm!");
                                    System.exit(0);
                                });
                            }
                        }
                    }
                }
            }
        });

        wormAIThread.setDaemon(true);
        wormAIThread.start();
    }
    public static void startSardukarAI(Player player, view.View view) {
        Thread sardukarAIThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }

                for (Character c : player.getCurrentLocation().getCharacters()) {
                    if (c instanceof Enemy enemy &&
                            enemy.isAlive() &&
                            enemy.getType() == EnemyType.SARDUKAR) {

                        int ex = enemy.getX();
                        int ey = enemy.getY();
                        int px = view.getPlayerX();
                        int py = view.getPlayerY();


                        int dx = Integer.compare(px, ex);
                        int dy = Integer.compare(py, ey);

                        enemy.setPosition(ex + dx * 10, ey + dy * 10); // move step


                        double dist = Math.sqrt(Math.pow(px - ex, 2) + Math.pow(py - ey, 2));
                        if (dist <= 35) {
                            player.takeDamage(20);
                            SwingUtilities.invokeLater(() -> {
                                view.setMessage(enemy.getName() + " slashes you for 20!");
                                view.repaint();
                            });

                            if (player.getHealth() <= 0) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(null, "You were killed by " + enemy.getName() + "!");
                                    System.exit(0);
                                });
                            }
                        }
                    }
                }
            }
        });
        sardukarAIThread.setDaemon(true);
        sardukarAIThread.start();
    }

    public static void startFremenAI(Player player, view.View view) {
        Thread fremenAIThread = new Thread(() -> {
            int attackCooldown = 0;

            while (true) {
                try {
                    Thread.sleep(1000); // runs every second
                } catch (InterruptedException e) {
                    break;
                }

                for (Character c : player.getCurrentLocation().getCharacters()) {
                    if (c instanceof Enemy enemy &&
                            enemy.isAlive() &&
                            enemy.getType() == EnemyType.FREMEN) {

                        // Move away from player
                        int ex = enemy.getX();
                        int ey = enemy.getY();
                        int px = view.getPlayerX();
                        int py = view.getPlayerY();

                        int dx = Integer.compare(ex, px);
                        int dy = Integer.compare(ey, py);

                        enemy.setPosition(ex + dx * 10, ey + dy * 10); // run away step

                        // Only attack every 10 seconds
                        if (attackCooldown % 10 == 0) {
                            player.takeDamage(30);
                            SwingUtilities.invokeLater(() -> {
                                view.setMessage(enemy.getName() + " strikes from the shadows! -30 HP!");
                                view.repaint();
                            });

                            if (player.getHealth() <= 0) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(null, "You were assassinated by " + enemy.getName() + "!");
                                    System.exit(0);
                                });
                            }
                        }
                    }
                }

                attackCooldown++;
            }
        });

        fremenAIThread.setDaemon(true);
        fremenAIThread.start();
    }




}
