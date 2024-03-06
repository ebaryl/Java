package Pi;

import java.util.Random;

public class Pi_kodZzajec {
    public static void main(String[] args) {
        int allPoints = 1000000000;
        int pointsInCircle = 0;

        Random random = new Random();
        for (int i = 0; i < allPoints; i++)
        {
            double x = random.nextDouble();
            double y = random.nextDouble();

            double distanceFromCenter = Math.sqrt(x*x + y*y);

            if(distanceFromCenter < 1) {
                pointsInCircle++;
            }
        }
        double pi = ((double)pointsInCircle * 4) / ((double)allPoints);
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