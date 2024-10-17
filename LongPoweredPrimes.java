import java.io.IOException;
import java.math.BigInteger;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LongPoweredPrimes {
    List<BigInteger> secondPrimes = Collections.synchronizedList(new ArrayList<>());
    List<BigInteger> actualPrimes = Collections.synchronizedList(new ArrayList<>());
    final BigInteger TWO = new BigInteger("" + 2);

    public void LongPoweredPrimesNumbers(int exponent, BigInteger start, int cores) {
        System.out.println("Started with number: " + start);
        for (; start.compareTo(new BigInteger("15485863")) <= 0; start = start.add(new BigInteger("" + 2 * cores))) {
            if(start.mod(new BigInteger("" + 1000000)).equals(BigInteger.ONE)) {
                System.out.println("Checking number: " + start);
            }
            boolean isNPrime = start.isProbablePrime(100);
            if (isNPrime) {
                BigInteger check = TWO.pow(exponent).add(start.pow(exponent));
                boolean isPrime = check.isProbablePrime(100);
                if (isPrime) {
                    synchronized (actualPrimes) {
                        actualPrimes.add(check);
                    }
                    synchronized (secondPrimes) {
                        secondPrimes.add(start);
                    }
                }
            }
        }
    }

    public void writePrimesToFile(int exponent) throws IOException {
        synchronized (actualPrimes) {
            List<BigInteger> sortedActualPrimes = actualPrimes.stream().sorted().toList();
            writeToFile(sortedActualPrimes, "actualPrimes_exponent_" + exponent + ".txt");
        }
        synchronized (secondPrimes) {
            List<BigInteger> sortedSecondPrimes = secondPrimes.stream().sorted().toList();
            writeToFile(sortedSecondPrimes, "secondPrimes_exponent_" + exponent + ".txt");
        }
    }

    private void writeToFile(List<BigInteger> list, String filename) throws IOException {
        List<String> lines = Collections.singletonList(list.toString());
        Path file = Paths.get(filename);
        Files.write(file, lines, UTF_8);
    }
}
