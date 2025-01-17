package org.scrum.psd.battleship.ascii;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;
import org.scrum.psd.battleship.validator.FleetValidator;
import org.scrum.psd.battleship.validator.LineValidator;
import org.scrum.psd.battleship.validator.OverlappValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class Main {
    private static final Attribute SHIP_COLOR = MAGENTA_TEXT();
    private static final Attribute MESSAGE_COLOR = YELLOW_TEXT();
    
    private static final Attribute MISS_COLOR = BLUE_TEXT();
    private static final Attribute HIT_COLOR = RED_TEXT();
    
    private static List<Ship> myFleet;
    private static List<Ship> enemyFleet;
    
    private static List<FleetValidator> validators;
    static {
        validators = new ArrayList<>();
        validators.add(new OverlappValidator());
        validators.add(new LineValidator());
    }

    public static void main(String[] args) {
        System.out.println(colorize("                                     |__", SHIP_COLOR));
        System.out.println(colorize("                                     |\\/", SHIP_COLOR));
        System.out.println(colorize("                                     ---", SHIP_COLOR));
        System.out.println(colorize("                                     / | [", SHIP_COLOR));
        System.out.println(colorize("                              !      | |||", SHIP_COLOR));
        System.out.println(colorize("                            _/|     _/|-++'", SHIP_COLOR));
        System.out.println(colorize("                        +  +--|    |--|--|_ |-", SHIP_COLOR));
        System.out.println(colorize("                     { /|__|  |/\\__|  |--- |||__/", SHIP_COLOR));
        System.out.println(colorize("                    +---------------___[}-_===_.'____                 /\\", SHIP_COLOR));
        System.out.println(colorize("                ____`-' ||___-{]_| _[}-  |     |_[___\\==--            \\/   _", SHIP_COLOR));
        System.out.println(colorize(" __..._____--==/___]_|__|_____________________________[___\\==--____,------' .7", SHIP_COLOR));
        System.out.println(colorize("|                        Welcome to Battleship                         BB-61/", SHIP_COLOR));
        System.out.println(colorize(" \\_________________________________________________________________________|", SHIP_COLOR));
        System.out.println("");

        InitializeGame();

        StartGame();
    }

    private static void StartGame() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\033[2J\033[;H");
        System.out.println("                  __");
        System.out.println("                 /  \\");
        System.out.println("           .-.  |    |");
        System.out.println("   *    _.-'  \\  \\__/");
        System.out.println("    \\.-'       \\");
        System.out.println("   /          _/");
        System.out.println("  |      _  /\" \"");
        System.out.println("  |     /_\'");
        System.out.println("   \\    \\_/");
        System.out.println("    \" \"\" \"\" \"\" \"");

        do {
            System.out.println("Player, it's your turn");
            System.out.println(colorize("Enter coordinates for your shot:", MESSAGE_COLOR));
            String input = scanner.next();
            while (!Position.validatePosition(input)) {
                System.out.println(colorize("Enter coordinates for your shot:", MESSAGE_COLOR));
                input = scanner.next();
            }
            Position position = parsePosition(input);
            System.out.println(position);
            boolean isHit = GameController.checkIsHit(enemyFleet, position);
            if (isHit) {
                beep();

                System.out.println("                \\         .  ./");
                System.out.println("              \\      .:\" \";'.:..\" \"   /");
                System.out.println("                  (M^^.^~~:.'\" \").");
                System.out.println("            -   (/  .    . . \\ \\)  -");
                System.out.println("               ((| :. ~ ^  :. .|))");
                System.out.println("            -   (\\- |  \\ /  |  /)  -");
                System.out.println("                 -\\  \\     /  /-");
                System.out.println("                   \\  \\   /  /");

            }

            System.out.println(isHit ? colorize("Yeah ! Nice hit !", HIT_COLOR) : colorize("Miss!", MISS_COLOR));

            position = getRandomPosition();
            isHit = GameController.checkIsHit(myFleet, position);
            System.out.println("-------------------------------------------------------------------------");
            System.out.println(String.format("Computer shoot in %s%s and %s", position.getColumn(), position.getRow(), isHit ? colorize("hit your ship !", HIT_COLOR) : colorize("Miss!", MISS_COLOR)));
            if (isHit) {
                beep();

                System.out.println("                \\         .  ./");
                System.out.println("              \\      .:\" \";'.:..\" \"   /");
                System.out.println("                  (M^^.^~~:.'\" \").");
                System.out.println("            -   (/  .    . . \\ \\)  -");
                System.out.println("               ((| :. ~ ^  :. .|))");
                System.out.println("            -   (\\- |  \\ /  |  /)  -");
                System.out.println("                 -\\  \\     /  /-");
                System.out.println("                   \\  \\   /  /");

            }
            System.out.println("-------------------------------------------------------------------------");
        } while (true);
    }

    private static void beep() {
        System.out.print("\007");
    }

    protected static Position parsePosition(String input) {
        Letter letter = Letter.valueOf(input.toUpperCase().substring(0, 1));
        int number = Integer.parseInt(input.substring(1));
        return new Position(letter, number);
    }

    private static Position getRandomPosition() {
        int rows = 8;
        int lines = 8;
        Random random = new Random();
        Letter letter = Letter.values()[random.nextInt(lines)];
        int number = random.nextInt(rows);
        Position position = new Position(letter, number);
        return position;
    }

    private static void InitializeGame() {
        InitializeMyFleet();

        InitializeEnemyFleet();
    }

    private static void InitializeMyFleet() {
        Scanner scanner = new Scanner(System.in);
        myFleet = GameController.initializeShips();

        System.out.println("Please position your fleet (Game board has size from A to H and 1 to 8)");

        for (Ship ship : myFleet) {
            System.out.println("");
            System.out.println(String.format("Please enter the positions for the %s (size: %s)", ship.getName(), ship.getSize()));
            for (int i = 1; i <= ship.getSize(); i++) {
                System.out.println(colorize(String.format("Enter position %s of %s (i.e A3):", i, ship.getSize()), MESSAGE_COLOR));

                String positionInput = scanner.next();
                while (!Position.validatePosition(positionInput)) {
                    System.out.println(colorize(String.format("Enter position %s of %s (i.e A3):", i, ship.getSize()), MESSAGE_COLOR));

                    positionInput = scanner.next();
                }
                
                while (!validate(ship, positionInput)) {
                    System.out.println(colorize(String.format("Invalid position"), MESSAGE_COLOR));

                    System.out.println(colorize(String.format("Enter position %s of %s (i.e A3):", i, ship.getSize()), MESSAGE_COLOR));
                    positionInput = scanner.next();
                }
                
                ship.addPosition(positionInput);
            }
        }
    }
    
    private static boolean validate(Ship ship, String positionInput) {
        if (!Position.validatePosition(positionInput)) {
            return false;
        }
        Letter letter = Letter.valueOf(positionInput.toUpperCase().substring(0, 1));
        int number = Integer.parseInt(positionInput.substring(1));
        Position pos = new Position(letter, number);
        if (!Position.validatePosition(positionInput)) {

        }
        for (FleetValidator validator : validators) {
            if (!validator.validate(myFleet, ship, pos)) {
                return false;
            }
        }
        
        return true;
    }

    private static void InitializeEnemyFleet() {
        enemyFleet = GameController.initializeShips();

        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 4));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 5));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 6));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 7));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 8));

        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 6));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 7));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 8));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 9));

        enemyFleet.get(2).getPositions().add(new Position(Letter.A, 3));
        enemyFleet.get(2).getPositions().add(new Position(Letter.B, 3));
        enemyFleet.get(2).getPositions().add(new Position(Letter.C, 3));

        enemyFleet.get(3).getPositions().add(new Position(Letter.F, 8));
        enemyFleet.get(3).getPositions().add(new Position(Letter.G, 8));
        enemyFleet.get(3).getPositions().add(new Position(Letter.H, 8));

        enemyFleet.get(4).getPositions().add(new Position(Letter.C, 5));
        enemyFleet.get(4).getPositions().add(new Position(Letter.C, 6));
    }
}
