package view;

import model.Enemy;
import model.Player;

import javax.swing.*;
import java.awt.*;



public class View extends JPanel {
    private final Player player;
    private String message = "";

    private int playerX = 500;
    private int playerY = 500;

    private boolean victoryAchieved = false;
    public void showVictoryMessage() {
        this.victoryAchieved = true;
        repaint();
    }


    public void movePlayerInRoom(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        int playerSize = 30;
        int hudHeight = 100;

        // Clamp X
        if (newX >= 0 && newX + playerSize <= getWidth()) {
            playerX = newX;
        }

        // Clamp Y
        if (newY >= hudHeight && newY + playerSize <= getHeight()) {
            playerY = newY;
        }
    }


    public void resetPlayerPosition() {
        playerX = 100;
        playerY = 300;
    }


    public View(Player player) {
        this.player = player;
        setFocusable(true);
    }

    public void setMessage(String message) {
        this.message = message;
        repaint();
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // HUD
        g2d.setColor(Color.BLACK);
        g2d.drawString("Health: " + player.getHealth(), 10, 20);
        g2d.drawString("Coins: " + player.getCoins(), 10, 35);
        g2d.drawString("Inventory: " + player.describeInventory(), 10, 50);
        g2d.drawString("Message: " + message, 10, 65);

        // Location name
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Location: " + player.getCurrentLocation().getName(), 10, 80);



        // Characters
        int x = 50;
        int y = 120;

        for (model.Character c : player.getCurrentLocation().getCharacters()) {
            int cx = (c.getX() != 0 || c.getY() != 0) ? c.getX() : x;
            int cy = (c.getX() != 0 || c.getY() != 0) ? c.getY() : y;

            // Choose color by type
            if (c instanceof model.Enemy enemy) {
                switch (enemy.getType()) {
                    case WORM -> g2d.setColor(Color.RED);
                    case SARDUKAR -> g2d.setColor(new Color(128, 0, 128)); // Purple
                    case FREMEN -> g2d.setColor(Color.PINK);
                }
            } else if (c instanceof model.Merchant) {
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(Color.GRAY);
            }


            // Draw character circle
            g2d.fillOval(cx, cy, 30, 30);

            // Draw name only for merchants
            if (c instanceof model.Merchant) {
                g2d.setColor(Color.BLACK);
                g2d.drawString(c.getName(), cx, cy - 5);
            }

            // Draw health bar for enemies
            if (c instanceof model.Enemy enemy) {
                int hp = Math.max(enemy.getHealth(), 0);
                int barWidth = 30;
                int barFilled = (int) ((hp / 100.0) * barWidth);

                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(cx, cy - 10, barWidth, 5);
                g2d.setColor(Color.GREEN);
                g2d.fillRect(cx, cy - 10, barFilled, 5);
            }

            // Fallback auto layout step
            if (c.getX() == 0 && c.getY() == 0) {
                x += 80;
                if (x > getWidth() - 80) {
                    x = 50;
                    y += 80;
                }
            }
        }



        // Items
        x = 50;
        y = 180;
        for (model.Item<?> item : player.getCurrentLocation().getItems()) {
            g2d.setColor(Color.BLUE);
            g2d.fillRect(x, y, 10, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawString(item.getName(), x, y - 5);
            x += 80;
        }


        // Player circle
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(playerX, playerY, 30, 30);

        // Player health bar
        int hp = Math.max(player.getHealth(), 0);
        int maxHP = 100;
        int hpBar = (int) ((hp / (double) maxHP) * 30);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(playerX, playerY - 8, 30, 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(playerX, playerY - 8, hpBar, 5);

        if (victoryAchieved) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("You won!", getWidth() / 2 - 100, getHeight() / 2);
        }



    }




}
