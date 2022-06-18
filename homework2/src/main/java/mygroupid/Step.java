package mygroupid;

import java.util.ArrayList;

public class Step {
    private final ArrayList<Position> positions;
    private final int positionsCount;
    private boolean eating;
    private boolean syntaxError;

    /**
     * Getter for ith element of positions
     * @param i the queried index
     * @return positions.get(i)
     */
    public Position positionAt(int i) {
        return positions.get(i);
    }

    /**
     * Getter for positionsCount
     * @return positionsCount
     */
    public int getPositionsCount() {
        return positionsCount;
    }

    /**
     * Informs weather the player eats opponents draughts
     * @return true in case of eating, false otherwise
     */
    public boolean eating() {
        return eating;
    }

    /**
     * Informs weather there is '-' instead of ':' in the step chain
     * @return true in case there is such error and false otherwise
     */
    public boolean syntaxError() {
        return syntaxError;
    }

    public Step(String stepString) {
        positions = new ArrayList<>();
        positionsCount = (stepString.length() + 1) / 3;
        if (positionsCount == 2) {
            positions.add(new Position(stepString.substring(0, 2)));
            positions.add(new Position(stepString.substring(3, 5)));
            if (stepString.charAt(2) == ':') {
                eating = true;
            }
        } else {
            eating = true;
            positions.add(new Position(stepString.substring(0, 2)));
            for (int i = 1; i < positionsCount; ++i) {
                if (stepString.charAt(3 * i - 1) == '-') {
                    syntaxError = true;
                }
                positions.add(new Position(stepString.substring(3 * i, 3 * (i + 1) - 1)));
            }
        }
    }
}

