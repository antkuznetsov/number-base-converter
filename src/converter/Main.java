package converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private enum Command {
        FROM("/from"),
        TO("/to"),
        EXIT("/exit"),
        UNKNOWN("");

        private final String value;

        Command(String value) {
            this.value = value;
        }

        public static Command getCommand(String name) {
            for (Command command : values()) {
                if (name.equals(command.value)) {
                    return command;
                }
            }
            return UNKNOWN;
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("Do you want to convert /from decimal or /to decimal? (To quit type /exit)");
            Command command = Command.getCommand(scanner.nextLine());
            executeCommand(command);
        }
    }

    private static void executeCommand(Command command) {
        switch (command) {
            case FROM:
                convertFrom();
                return;
            case TO:
                convertTo();
                return;
            case EXIT:
                System.exit(0);
                return;
            default:
                return;
        }
    }

    private static void convertFrom() {
        System.out.println("Enter number in decimal system:");
        int number = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter target base:");
        int targetBase = Integer.parseInt(scanner.nextLine());

        System.out.printf("Conversion result: %s%n", convertDec(number, targetBase));
    }


    private static String convertDec(int number, int targetBase) {
        switch (targetBase) {
            case 16:
                return convertDecToHex(number);
            case 8:
                return convertDecToOct(number);
            case 2:
                return convertDecToBin(number);
            default:
                return "";
        }
    }

    private static String convertDecToHex(int number) {
        Map<Integer, Character> hexReminders = new HashMap<>();
        hexReminders.put(10, 'A');
        hexReminders.put(11, 'B');
        hexReminders.put(12, 'C');
        hexReminders.put(13, 'D');
        hexReminders.put(14, 'E');
        hexReminders.put(15, 'F');
        int quotient = number;
        StringBuilder result = new StringBuilder();
        do {
            int remainder = quotient % 16;
            if (remainder > 9) {
                result.append(hexReminders.get(remainder));
            } else {
                result.append(remainder);
            }
            quotient /= 16;
        } while (quotient >= 1);

        return result.reverse().toString();
    }

    private static String convertDecToOct(int number) {
        int quotient = number;
        StringBuilder result = new StringBuilder();
        do {
            result.append(quotient % 8);
            quotient /= 8;
        } while (quotient >= 1);

        return result.reverse().toString();
    }

    private static String convertDecToBin(int number) {
        int quotient = number;
        StringBuilder result = new StringBuilder();
        do {
            result.append(quotient % 2);
            quotient /= 2;
        } while (quotient >= 1);

        return result.reverse().toString();
    }

    private static void convertTo() {
        System.out.println("Enter source number:");
        String number = scanner.nextLine();

        System.out.println("Enter source base:");
        int sourceBase = Integer.parseInt(scanner.nextLine());

        System.out.printf("Conversion to decimal result: %s%n", convertToDec(number, sourceBase));
    }

    private static int convertToDec(String number, int sourceBase) {
        switch (sourceBase) {
            case 16:
                return convertHexToDec(number);
            case 8:
                return convertOctToDec(number);
            case 2:
                return convertBinToDec(number);
            default:
                return 0;
        }
    }

    private static int convertHexToDec(String number) {
        Map<Character, Integer> hexReminders = new HashMap<>();
        hexReminders.put('A', 10);
        hexReminders.put('B', 11);
        hexReminders.put('C', 12);
        hexReminders.put('D', 13);
        hexReminders.put('E', 14);
        hexReminders.put('F', 15);

        char[] input = number.toCharArray();
        int result = 0;
        int d = 1;
        for (int i = input.length - 1; i >= 0; i--) {
            if (input[i] > 'A' && input[i] < 'F') {
                result += hexReminders.get(input[i]) * d;
            } else {
                result += Character.getNumericValue(input[i]) * d;
            }
            d *= 16;
        }
        return result;
    }

    private static int convertOctToDec(String number) {
        char[] input = number.toCharArray();
        int result = 0;
        int d = 1;
        for (int i = input.length - 1; i >= 0; i--) {
            result += Character.getNumericValue(input[i]) * d;
            d *= 8;
        }
        return result;
    }

    private static int convertBinToDec(String number) {
        char[] input = number.toCharArray();
        int result = 0;
        int d = 1;
        for (int i = input.length - 1; i >= 0; i--) {
            result += Character.getNumericValue(input[i]) * d;
            d *= 2;
        }
        return result;
    }
}
