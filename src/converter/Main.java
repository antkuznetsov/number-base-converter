package converter;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private final static byte LETTERS_START_POINT = 55;

    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            String userFirstAnswer = scanner.nextLine();

            if ("/exit".equals(userFirstAnswer)) {
                System.exit(0);
            } else {
                String[] bases = userFirstAnswer.split("\\s+");
                int sourceBase = Integer.parseInt(bases[0]);
                int targetBase = Integer.parseInt(bases[1]);

                while (true) {
                    System.out.printf("Enter number in base %d to convert to base %d (To go back type /back)%n",
                            sourceBase, targetBase);
                    String userSecondAnswer = scanner.nextLine();
                    if ("/back".equals(userSecondAnswer)) {
                        break;
                    } else {
                        System.out.print("Conversion result: ");
                        String number = userSecondAnswer.toUpperCase(Locale.ROOT);
                        if (sourceBase == 10) {
                            String res = convertFromDec(new BigInteger(number), targetBase);
                            System.out.println(res);
                        } else if (targetBase == 10) {
                            BigInteger res = convertToDec(number, sourceBase);
                            System.out.println(res);
                        } else {
                            BigInteger decNumber = convertToDec(number, sourceBase);
                            String res = convertFromDec(decNumber, targetBase);
                            System.out.println(res);
                        }
                    }
                }
            }
        }
    }

    private static String convertFromDec(BigInteger number, int targetBase) {
        BigInteger quotient = number;
        StringBuilder result = new StringBuilder();
        do {
            BigInteger remainder = quotient.remainder(BigInteger.valueOf(targetBase));
            if (remainder.intValue() > 9) {
                result.append(Character.toChars(LETTERS_START_POINT + remainder.byteValue()));
            } else {
                result.append(remainder);
            }
            quotient = quotient.divide(BigInteger.valueOf(targetBase));
        } while (quotient.compareTo(BigInteger.ONE) >= 0);

        return result.reverse().toString();
    }

    private static BigInteger convertToDec(String number, int sourceBase) {
        char[] input = number.toCharArray();
        BigInteger result = BigInteger.ZERO;
        BigInteger currentPow = BigInteger.ONE;
        for (int i = input.length - 1; i >= 0; i--) {
            BigInteger currentNum = BigInteger.valueOf(input[i]);
            BigInteger temp;
            if (Character.isAlphabetic(input[i])) {
                temp = currentNum.subtract(BigInteger.valueOf(LETTERS_START_POINT)).multiply(currentPow);
            } else {
                temp = BigInteger.valueOf(Character.getNumericValue(input[i])).multiply(currentPow);
            }
            result = result.add(temp);
            currentPow = currentPow.multiply(BigInteger.valueOf(sourceBase));
        }
        return result;
    }
}
