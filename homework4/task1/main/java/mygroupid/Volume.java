package mygroupid;

import java.util.Random;

public enum Volume {
    Small(10), Medium(50), Large(100);
    private static final Random RANDOM = new Random();
    private final int size;

    Volume(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static Volume getRandomVolume() {
        return values()[RANDOM.nextInt(values().length)];
    }
}

