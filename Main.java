import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    static class FastPrimeFinder extends Thread {
        private int exponent;
        private int start;
        private int cores;
        private LongPoweredPrimes longPoweredPrime;

        public FastPrimeFinder(int exponent, int start, int cores, LongPoweredPrimes longPoweredPrime) {
            this.exponent = exponent;
            this.start = start;
            this.cores = cores;
            this.longPoweredPrime = longPoweredPrime;
        }

        @Override
        public void run() {
            try {
                longPoweredPrime.LongPoweredPrimesNumbers(exponent, new BigInteger("" + start), cores);
            } catch (Exception e) {
                System.out.println("Exception in thread: " + e.getMessage());
            }
        }
    }

    public static void main(String... args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("How many CPU threads do you want to use?");
        int cores = sc.nextInt();
        System.out.println("What exponent of primes do you want to check?");
        int exponent = sc.nextInt();
        while (exponent >= 0 && exponent <= 1024) {
            LongPoweredPrimes longPoweredPrime = new LongPoweredPrimes();
            Thread[] threads = new Thread[cores];
            for (int i = 0; i < cores; i++) {
                int start = 3 + 2 * i;
                threads[i] = new FastPrimeFinder(exponent, start, cores, longPoweredPrime);
                threads[i].start();
            }
            for (int i = 0; i < cores; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                longPoweredPrime.writePrimesToFile(exponent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Finished all threads for exponent " + exponent);
            if (exponent == 0) {
                System.out.println("What exponent of primes do you want to check?");
                exponent = sc.nextInt();
            }
            else {
                exponent *= 2;
            }
        }
        sc.close();
    }
}
