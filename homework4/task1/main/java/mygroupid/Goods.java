package mygroupid;

import java.util.Random;

public enum Goods {
    Bread("bread"), Banana("banana"), Clothes("clothes");
    private static final Random RANDOM = new Random();
    private final String name;

    Goods(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Goods getRandomGoods() {
        return values()[RANDOM.nextInt(values().length)];
    }
}

