package mygroupid;

public class Position {
    private int x;
    private int y;
    private boolean isQueen;

    public Position(String position) {
        isQueen = java.lang.Character.isUpperCase(position.charAt(0));
        x = position.charAt(0);
        if (isQueen) {
            x -= 'A';
        } else {
            x -= 'a';
        }
        y = position.charAt(1) - '1';
    }

    public Position(int x, int y, boolean isQueen) {
        this.x = x;
        this.y = y;
        this.isQueen = isQueen;
    }

    /**
     * Getter for x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for isQueen
     * @return isQueen
     */
    public boolean isQueen() {
        return isQueen;
    }

    /**
     * Checks if this equals to pos
     * @param pos instance of position
     * @return this == pos
     */
    public boolean equals(Position pos) {
        return this.x == pos.getX() && this.y == pos.getY();
    }

    /**
     * Converts the coordinate representation to draughts notation
     * @return the draughts notation
     */
    public String toString() {
        String s = "";
        s += isQueen ? (char) ('A' + x) : (char) ('a' + x);
        s += (char) ('0' + y + 1);
        return s;
    }
}

