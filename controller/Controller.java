package controller;

import model.*;
import model.Character;
import view.View;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;



public class Controller extends KeyAdapter {
    private final Player player;
    private final View view;

    private boolean isNear(int tx, int ty) {
        int dx = tx - view.getPlayerX();
        int dy = ty - view.getPlayerY();
        return Math.sqrt(dx * dx + dy * dy) <= 40;
    }



    private Merchant activeMerchant = null;
    private boolean waitingForTrade = false;


    public Controller(Player player, View view) {
        this.player = player;
        this.view = view;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (waitingForTrade && activeMerchant != null) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> {
                    tradeWithMerchant(0);
                    return;
                }
                case KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> {
                    tradeWithMerchant(1);
                    return;
                }
                case KeyEvent.VK_Q -> {
                    view.setMessage("Trade canceled.");
                    waitingForTrade = false;
                    activeMerchant = null;
                    return;
                }
            }
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                view.movePlayerInRoom(0, -10);
                view.setMessage("Moved up inside room");


            }
            case KeyEvent.VK_S -> {
                view.movePlayerInRoom(0, 10);
                view.setMessage("Moved down inside room");


            }
            case KeyEvent.VK_A -> {
                view.movePlayerInRoom(-10, 0);
                view.setMessage("Moved left inside room");


            }
            case KeyEvent.VK_D -> {
                view.movePlayerInRoom(10, 0);
                view.setMessage("Moved right inside room");


            }


            case KeyEvent.VK_UP -> {
                if (player.move("north")) {
                    view.resetPlayerPosition();
                    view.setMessage("Moved to: " + player.getCurrentLocation().getName());
                } else {
                    view.setMessage("No exit north.");
                }


            }
            case KeyEvent.VK_DOWN -> {
                if (player.move("south")) {
                    view.resetPlayerPosition();
                    view.setMessage("Moved to: " + player.getCurrentLocation().getName());
                } else {
                    view.setMessage("No exit south.");
                }


            }
            case KeyEvent.VK_LEFT -> {
                if (player.move("west")) {
                    view.resetPlayerPosition();
                    view.setMessage("Moved to: " + player.getCurrentLocation().getName());
                } else {
                    view.setMessage("No exit west.");
                }


            }
            case KeyEvent.VK_RIGHT -> {
                if (player.move("east")) {
                    view.resetPlayerPosition();
                    view.setMessage("Moved to: " + player.getCurrentLocation().getName());
                } else {
                    view.setMessage("No exit east.");
                }


            }


            case KeyEvent.VK_P -> {
                List<Item<?>> items = player.getCurrentLocation().getItems();
                if (!items.isEmpty()) {
                    Item<?> item = items.get(0);
                    items.remove(item);

                    if (item instanceof Coin coin) {
                        player.addCoins(coin.getValue());
                        view.setMessage("Picked up a coin! Total coins: " + player.getCoins());
                    } else if (item instanceof Consumable c) {
                        c.use(player); // auto use
                        view.setMessage("+" + c.getValue() + " HP");
                    } else {
                        player.addItem(item);
                        view.setMessage("Picked up: " + item.getName());
                    }
                } else {
                    view.setMessage("Nothing to pick up here.");
                }
            }



            case KeyEvent.VK_F -> {
                List<Item<?>> inventory = player.getInventory();
                if (!inventory.isEmpty()) {
                    Item<?> item = inventory.get(0);

                    if (item instanceof Weapon weapon) {
                        boolean hit = false;
                        List<Character> chars = new ArrayList<>(player.getCurrentLocation().getCharacters());
                        for (Character c : chars) {
                            if (c instanceof Enemy enemy && enemy.isAlive() && isNear(enemy.getX(), enemy.getY())) {

                                enemy.takeDamage(weapon.getValue());
                                if (!enemy.isAlive()) {
                                    // Check if this is the last alive enemy
                                    long otherAliveEnemies = player.getCurrentLocation().getCharacters().stream()
                                            .filter(other -> other instanceof Enemy && other != enemy && ((Enemy) other).isAlive())
                                            .count();


                                    if (otherAliveEnemies == 0) {
                                        String locName = player.getCurrentLocation().getName();
                                        Key key = switch (locName) {
                                            case "Dark Forest" -> new Key("Key to Spice Cave", "keyToSpiceCave");
                                            case "Spice Cave" -> new Key("Key to Sitch Tabr", "keyToSitchTabr");
                                            default -> null;
                                        };

                                        if (key != null) {
                                            key.setPosition(enemy.getX(), enemy.getY());
                                            player.getCurrentLocation().getItems().add(key);
                                            view.setMessage(enemy.getName() + " dropped: " + key.getName());
                                        } else {
                                            view.setMessage(enemy.getName() + " defeated and dropped a coin!");
                                        }

                                    } else {
                                        view.setMessage(enemy.getName() + " defeated and dropped a coin!");
                                    }

                                    player.getCurrentLocation().getCharacters().remove(enemy);
                                    // Check for win condition in Sitch Tabr
                                    if ("Sitch Tabr".equals(player.getCurrentLocation().getName())) {
                                        boolean anyEnemiesLeft = player.getCurrentLocation().getCharacters().stream()
                                                .anyMatch(other -> other instanceof Enemy && ((Enemy) other).isAlive());


                                        if (!anyEnemiesLeft) {
                                            view.setMessage("You won!");
                                            view.showVictoryMessage();
                                        }
                                    }

                                    player.getCurrentLocation().getItems().add(new Coin(1));
                                }
                                else {
                                    view.setMessage("You hit " + enemy.getName() + " for " + weapon.getValue());
                                }
                                hit = true;
                                break;
                            }
                        }
                        if (!hit) view.setMessage("No enemy nearby.");
                    } else if (item instanceof Consumable) {
                        item.use(player);
                        inventory.remove(item);
                        view.setMessage("Used and consumed: " + item.getName());
                    } else {
                        item.use(player);
                        view.setMessage("Used: " + item.getName());
                    }

                } else {
                    view.setMessage("Inventory is empty.");
                }
            }


            case KeyEvent.VK_E -> {
                List<Character> characters = new ArrayList<>(player.getCurrentLocation().getCharacters());

                if (characters.isEmpty()) {
                    view.setMessage("No one to interact with.");
                    return;
                }

                Character nearest = null;
                double minDist = Double.MAX_VALUE;

                int px = view.getPlayerX();
                int py = view.getPlayerY();

                for (Character c : characters) {
                    int cx = c.getX();
                    int cy = c.getY();
                    double dist = Math.sqrt(Math.pow(cx - px, 2) + Math.pow(cy - py, 2));
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = c;
                    }
                }

                if (minDist > 40 || nearest == null) {
                    view.setMessage("No one close enough to interact with.");
                    return;
                }

                if (nearest instanceof Enemy enemy && enemy.getType() == EnemyType.WORM) {
                    view.setMessage("You stare at " + enemy.getName() + ". It rumbles... but does nothing.");
                    return;
                }

                nearest.interact(player);

                if (nearest instanceof Merchant merchant) {
                    activeMerchant = merchant;
                    waitingForTrade = true;

                    StringBuilder sb = new StringBuilder(merchant.getName() + " offers:\n");
                    List<Item<?>> goods = merchant.getGoods();

                    for (int i = 0; i < goods.size(); i++) {
                        sb.append((i + 1)).append(". ").append(goods.get(i).getName()).append(" (1 coin)\n");
                    }

                    view.setMessage(sb.toString());
                } else {
                    view.setMessage("You interacted with: " + nearest.getName());
                }
            }



        }

        view.repaint();
    }

    private void tradeWithMerchant(int index) {
        List<Item<?>> goods = activeMerchant.getGoods();
        if (index < goods.size()) {
            int price = index + 1; // 1st item = 1 coin, 2nd = 2 coins...
            if (player.getCoins() >= price) {
                player.addCoins(-price);

                Item<?> item = goods.get(index);
                player.addItem(item);
                goods.remove(index);
                view.setMessage("You paid " + price + " coins and received: " + item.getName());
            } else {
                view.setMessage("Not enough coins to trade.");
            }
        } else {
            view.setMessage("Invalid choice.");
        }

        waitingForTrade = false;
        activeMerchant = null;
    }











}
