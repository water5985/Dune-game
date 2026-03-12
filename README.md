# Java Dungeon Game

A small real-time dungeon game written in **Java** with a graphical interface.
The player explores dungeon locations, fights enemies with different behaviors, collects items, and interacts with a merchant.

## Gameplay

The player moves through several dungeon locations and fights enemies in real time.
Each enemy type behaves differently, forcing the player to adapt their strategy.

The game includes:

* Real-time combat
* Multiple enemy AI behaviors
* Item system (weapons and healing)
* Merchant trading
* Multiple dungeon locations
* Final enemy encounter

## Controls

| Key             | Action                  |
| --------------- | ----------------------- |
| W / A / S / D   | Move player             |
| F               | Use equipped item       |
| E               | Interact with merchant  |
| P               | Pick up item            |
| Arrow Up / Down | Change dungeon location |

## Enemy Types

Red enemies

* Stationary
* Attack when the player is close

Purple enemies

* Chase the player
* Melee attackers

Pink enemies

* Keep distance from the player
* Ranged attacks

## Win Condition

Defeat all enemies in the **final dungeon location**.

## Lose Condition

The game ends when the player's **health reaches 0**.

## Running the Game

Clone the repository:

```
git clone https://github.com/yourusername/your-repository-name.git
cd your-repository-name
```

Compile and run using Java:

```
javac src/*.java
java Main
```

(Adjust depending on the actual project structure.)

## Project Structure

```
src/        Java source files
assets/     game graphics / resources
docs/       documentation
```
