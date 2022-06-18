package mygroupid;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class Main {
    private Main() { }

    public static final int TUNNEL_WIDTH = 5;
    public static final int LOAD_COEFFICIENT = 10;
    private static final Map<Goods, Berth> BERTH_BY_GOODS = new HashMap<>();
    private static List<Ship> ships = new ArrayList<>();
    private static List<Future<?>> futures = new ArrayList<>();

    public static void init(ExecutorService executorService, int shipsCount) {
        ships.clear();
        futures.clear();
        for (int i = 0; i < shipsCount; ++i) {
            Ship newShip = Generator.generate();
            ships.add(newShip);
            futures.add(executorService.submit(newShip));
        }
        for (Goods goods : Goods.values()) {
            BERTH_BY_GOODS.put(goods, new Berth(goods));
        }
    }

    public static void sail(int shipsCount, boolean print) throws ExecutionException, InterruptedException {
        for (int i = 0; i < shipsCount; ++i) {
            futures.get(i).get();
            Ship ship = ships.get(i);
            Goods goods = ship.getGoods();
            int volume = ship.getVolume().getSize();
            futures.set(i, BERTH_BY_GOODS.get(goods).executorSubmit(() -> {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
                try {
                    ship.setEnteredBerthAt(LocalDateTime.now());
                    if (print) {
                        System.out.println("Ship " + ship.getDescription()
                                + " entered berth at " + df.format(ship.getEnteredBerthAt()));
                    }
                    TimeUnit.SECONDS.sleep(volume / LOAD_COEFFICIENT);
                    ship.setWasLoadedAt(LocalDateTime.now());
                    if (print) {
                        System.out.println("Ship " + ship.getDescription()
                                + " was loaded at " + df.format(ship.getWasLoadedAt()));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public static void shutdown(ExecutorService executorService) {
        for (Berth berth : BERTH_BY_GOODS.values()) {
            berth.executorShutdown();
        }
        executorService.shutdown();
    }

    public static Ship getShip(int i) {
        return ships.get(i);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner in = new Scanner(System.in);
        final int shipsCount = in.nextInt();

        final ExecutorService executorService = Executors.newFixedThreadPool(TUNNEL_WIDTH);

        init(executorService, shipsCount);
        sail(shipsCount, true);
        shutdown(executorService);
    }
}

