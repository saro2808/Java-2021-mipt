package mygroupid;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskOneTest {
    @Test
    void helloTest() {
        Assertions.assertThat(2 + 2).isEqualTo(4);
    }

    private void mainTest(int shipsCount) throws ExecutionException, InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(Main.TUNNEL_WIDTH);
        Main.init(executorService, shipsCount);
        Main.sail(shipsCount, true);
        TimeUnit.SECONDS.sleep(shipsCount * 11L);
        Main.shutdown(executorService);
        for (int i = 0; i < shipsCount; ++i) {
            Ship ship = Main.getShip(i);
            Assertions.assertThat(Duration.between(
                    ship.getReachedBerthAt(), ship.getLeftAt()).getSeconds()).isLessThan(2);
            Assertions.assertThat(Duration.between(ship.getWasLoadedAt(),
                    ship.getEnteredBerthAt()).getSeconds()).isLessThan(ship.getVolume().getSize() / Main.LOAD_COEFFICIENT + 1);
        }
    }

    @Test
    void simpleTest() throws ExecutionException, InterruptedException {
        mainTest(2);
    }

    @Test
    void simpleTest2() throws ExecutionException, InterruptedException {
        mainTest(10);
    }

    @Test
    void simpleTest3() throws ExecutionException, InterruptedException {
        mainTest(20);
    }
}

