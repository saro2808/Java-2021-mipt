package mygroupid;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Ship implements Runnable {
    private final int id;
    private final Goods goods;
    private final Volume volume;

    private LocalDateTime leftAt;
    private LocalDateTime reachedBerthAt;
    private LocalDateTime enteredBerthAt;
    private LocalDateTime wasLoadedAt;

    public Ship(int id, Goods goods, Volume volume) {
        this.id = id;
        this.goods = goods;
        this.volume = volume;
    }

    /**
     * Getter for the goods
     * @return goods
     */
    public Goods getGoods() {
        return goods;
    }

    /**
     * Getter for the volume
     * @return volume
     */
    public Volume getVolume() {
        return volume;
    }

    /**
     * Describes the ship
     * @return (id, goods, volume)
     */
    public String getDescription() {
        return "(" + id + ", " + goods.getName() + ", " + volume.getSize() + ")";
    }

    /**
     * Setter for enteredBerthAt
     * @param enteredBerthAt the time when the ship entered berth
     */
    public void setEnteredBerthAt(LocalDateTime enteredBerthAt) {
        this.enteredBerthAt = enteredBerthAt;
    }

    /**
     * Setter for wasLoadedAt
     * @param wasLoadedAt the time when the ship was loaded
     */
    public void setWasLoadedAt(LocalDateTime wasLoadedAt) {
        this.wasLoadedAt = wasLoadedAt;
    }

    /**
     * Getter for leftAt
     * @return leftAt
     */
    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    /**
     * Getter for reachedBerthAt
     * @return reachedBerthAt
     */
    public LocalDateTime getReachedBerthAt() {
        return reachedBerthAt;
    }

    /**
     * Getter for enteredBerthAt
     * @return enteredBerthAt
     */
    public LocalDateTime getEnteredBerthAt() {
        return enteredBerthAt;
    }

    /**
     * Getter for wasLoadedAt
     * @return wasLoadedAt
     */
    public LocalDateTime getWasLoadedAt() {
        return wasLoadedAt;
    }

    /**
     * The main routine of the ship
     */
    @Override
    public void run() {
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
            leftAt = LocalDateTime.now();
            System.out.println("Ship " + getDescription() + " was generated at " + df.format(leftAt));
            TimeUnit.SECONDS.sleep(1);
            reachedBerthAt = LocalDateTime.now();
            System.out.println("Ship " + getDescription() + " reached berth at " + df.format(reachedBerthAt));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

