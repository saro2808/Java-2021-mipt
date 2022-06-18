package mygroupid;

public final class Generator {
    private static int counter;

    private Generator() { }

    public static Ship generate() {
        return new Ship(counter++, Goods.getRandomGoods(), Volume.getRandomVolume());
    }
}

