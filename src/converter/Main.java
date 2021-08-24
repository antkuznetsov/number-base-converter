package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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

                        if (number.contains(".")) {
                            String[] fractionalNumber = number.split("\\.");
                            if (sourceBase == 10) {
                                System.out.printf(
                                        "%s.%s%n",
                                        convertIntegerFromDec(fractionalNumber[0], targetBase),
                                        convertFractionalFromDec("0." + fractionalNumber[1], targetBase)
                                );
                            } else if (targetBase == 10) {
                                BigDecimal integer = new BigDecimal(convertIntegerToDec(fractionalNumber[0], sourceBase));
                                BigDecimal fractional = convertFractionalToDec(fractionalNumber[1], sourceBase);
                                System.out.println(integer.add(fractional));
                            } else {
                                BigDecimal decInteger = new BigDecimal(convertIntegerToDec(fractionalNumber[0], sourceBase));
                                BigDecimal decFractional = convertFractionalToDec(fractionalNumber[1], sourceBase);
                                System.out.printf(
                                        "%s.%s%n",
                                        convertIntegerFromDec(decInteger.toString(), targetBase),
                                        convertFractionalFromDec(decFractional.toString(), targetBase)
                                );
                            }
                        } else {
                            if (sourceBase == 10) {
                                System.out.println(convertIntegerFromDec(number, targetBase));
                            } else if (targetBase == 10) {
                                System.out.println(convertIntegerToDec(number, sourceBase));
                            } else {
                                String decNumber = convertIntegerToDec(number, sourceBase);
                                System.out.println(convertIntegerFromDec(decNumber, targetBase));
                            }
                        }
                    }
                }
            }
        }
    }

    private static String convertIntegerFromDec(String number, int targetBase) {
        BigInteger quotient = new BigInteger(number);
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

    private static String convertFractionalFromDec(String number, int targetBase) {
        BigDecimal fractional = new BigDecimal(number);
        StringBuilder result = new StringBuilder();
        int counter = 0;
        do {
            BigDecimal d = fractional.multiply(new BigDecimal(targetBase)).setScale(0, RoundingMode.FLOOR);
            if (d.intValue() > 9) {
                result.append(Character.toChars(LETTERS_START_POINT + d.byteValue()));
            } else {
                result.append(d);
            }
            fractional = fractional.multiply(new BigDecimal(targetBase)).remainder(BigDecimal.ONE);
            counter++;
        } while (fractional.compareTo(BigDecimal.ZERO) > 0 && counter < 5);
        while (result.length() < 5) {
            result.append(0);
        }
        return result.toString();
    }

    private static String convertIntegerToDec(String number, int sourceBase) {
        char[] input = number.toCharArray();
        BigInteger result = BigInteger.ZERO;
        BigInteger currentPow = BigInteger.ONE;
        for (int i = input.length - 1; i >= 0; i--) {
            BigInteger temp;
            if (Character.isAlphabetic(input[i])) {
                temp = BigInteger.valueOf(input[i]).subtract(BigInteger.valueOf(LETTERS_START_POINT)).multiply(currentPow);
            } else {
                temp = BigInteger.valueOf(Character.getNumericValue(input[i])).multiply(currentPow);
            }
            result = result.add(temp);
            currentPow = currentPow.multiply(BigInteger.valueOf(sourceBase));
        }
        return result.toString();
    }

    private static BigDecimal convertFractionalToDec(String number, int sourceBase) {
        char[] input = number.toCharArray();
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal currentPow = BigDecimal.valueOf(sourceBase);
        for (int i = 0; i < input.length; i++) {
            BigDecimal temp;
            if (Character.isAlphabetic(input[i])) {
                temp = BigDecimal.valueOf(input[i]).subtract(BigDecimal.valueOf(LETTERS_START_POINT)).divide(currentPow, 6, RoundingMode.HALF_UP);
            } else {
                temp = BigDecimal.valueOf(Character.getNumericValue(input[i])).divide(currentPow, 6, RoundingMode.HALF_UP);
            }
            result = result.add(temp);
            currentPow = currentPow.multiply(BigDecimal.valueOf(sourceBase));
        }
        return result;
    }
}
