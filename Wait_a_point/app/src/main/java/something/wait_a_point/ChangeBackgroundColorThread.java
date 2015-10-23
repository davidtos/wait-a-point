package something.wait_a_point;

import java.util.Random;

/**
 * Created by David on 23-10-2015.
 */
public class ChangeBackgroundColorThread implements Runnable {

    FirstChallange firstChallange;
    int RandomMin;
    int RandomMax;
    Random random = new Random();

    public ChangeBackgroundColorThread(FirstChallange firstChallange, int randomMin, int randomMax) {
        this.firstChallange = firstChallange;
        RandomMin = randomMin;
        RandomMax = randomMax;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(RandomMax - RandomMin) + RandomMin);
        } catch (InterruptedException e) {
            e.printStackTrace();
        };

        firstChallange.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                firstChallange.setBackgroundWhite();
            }
        });

    }
}
