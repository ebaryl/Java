package Pi;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class Pi {
    public static void main(String[] args) {
        final int allPoints = 1000000000;
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger pointsInCircle = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                Random random = new Random();
                int pointsPerThread = allPoints / threadCount;
                int localPointsInCircle = 0;
                for (int j = 0; j < pointsPerThread; j++) {
                    double x = random.nextDouble();
                    double y = random.nextDouble();
                    double distanceFromCenter = Math.sqrt(x * x + y * y);
                    if (distanceFromCenter < 1) {
                        localPointsInCircle++;
                    }
                }
                pointsInCircle.addAndGet(localPointsInCircle);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double pi = ((double) pointsInCircle.get() * 4) / ((double) allPoints);
        System.out.println(pi);
    }
}


        /*
        double pi = ((double)pointsInCircle * 4) / ((double)allPoints);
        rozpiska
        IPk / IPo = Pk / Po
        AP / IPo = 4 / pi
        pi = 4IPo / AP
        */