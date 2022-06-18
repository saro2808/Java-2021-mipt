package mygroupid;

public class Board {
    private int[][] board;
    private Position[] whitePositions;
    private Position[] blackPositions;
    private int whiteCount;
    private int blackCount;

    public static final int BOARD_DIMENSION = 8;
    public static final Position NULL_POSITION = new Position("i9");

    /**
     * Set color for the position on the board
     * @param pos the position
     * @param color the color to be set
     */
    public void setCellColor(Position pos, int color) {
        board[pos.getX()][pos.getY()] = color;
    }

    /**
     * Get the color of the position on the board
     * @param pos the position
     * @return the color of pos
     */
    public int getCellColor(Position pos) {
        return board[pos.getX()][pos.getY()];
    }

    /**
     * Set the position in the whitesPositions
     * @param index the index in whitesPositions
     * @param pos the position to be set
     */
    public void setWhitePositionAt(int index, Position pos) {
        whitePositions[index] = pos;
    }

    /**
     * Set the position in the blacksPositions
     * @param index the index in blacksPositions
     * @param pos the position to be set
     */
    public void setBlackPositionAt(int index, Position pos) {
        blackPositions[index] = pos;
    }

    /**
     * Get the white's position at index
     * @param index the index in whitesPositions
     * @return the position
     */
    public Position getWhitePositionAt(int index) {
        return whitePositions[index];
    }

    /**
     * Get the black's position at index
     * @param index the index in blacksPositions
     * @return the position
     */
    public Position getBlackPositionAt(int index) {
        return blackPositions[index];
    }

    /**
     * Get whitePositions
     * @return whitePositions
     */
    public Position[] getWhitePositions() {
        return whitePositions;
    }

    /**
     * Get blackPositions
     * @return blackPositions
     */
    public Position[] getBlackPositions() {
        return blackPositions;
    }

    /**
     * Parses nextLine and fill in whitePositions or blackPositions
     * @param nextLine the line from input describing the initial configuration
     * @param isWhite the color of draughts described in nextLine
     */
    public void processLine(String nextLine, boolean isWhite) {
        String[] lineArr = nextLine.split(" ");
        int draughtCount = lineArr.length;
        if (isWhite) {
            whitePositions = new Position[draughtCount];
            whiteCount = draughtCount;
        } else {
            blackPositions = new Position[draughtCount];
            blackCount = draughtCount;
        }
        Position[] draughtPositions = isWhite ? whitePositions : blackPositions;
        int color = isWhite ? 1 : -1;
        for (int i = 0; i < draughtCount; ++i) {
            draughtPositions[i] = new Position(lineArr[i]);
            setCellColor(draughtPositions[i], color);
        }
    }

    /**
     * Initializes the board
     */
    public void boardInit() {
        board = new int[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0; i < BOARD_DIMENSION; ++i) {
            for (int j = 0; j < BOARD_DIMENSION; ++j) {
                board[i][j] = 0;
            }
        }
    }

    private boolean canStrikeFrom(int i, boolean isWhite) {
        int x;
        int y;
        boolean isQueen;
        if (isWhite) {
            x = whitePositions[i].getX();
            y = whitePositions[i].getY();
            isQueen = whitePositions[i].isQueen();
        } else {
            x = blackPositions[i].getX();
            y = blackPositions[i].getY();
            isQueen = blackPositions[i].isQueen();
        }
        if (x == BOARD_DIMENSION) {
            return false;
        }
        if (isQueen) {
            for (int j = 1; j < BOARD_DIMENSION; ++j) {
                if (x + j + 1 < BOARD_DIMENSION && y + j + 1 < BOARD_DIMENSION
                        && board[x + j][y + j] * board[x][y] == -1
                        && board[x + j + 1][y + j + 1] == 0) {
                    return true;
                }
                if (x + j + 1 < BOARD_DIMENSION && y - j - 1 >= 0
                        && board[x + j][y - j] * board[x][y] == -1
                        && board[x + j + 1][y - j - 1] == 0) {
                    return true;
                }
                if (x - j - 1 >= 0 && y + j + 1 < BOARD_DIMENSION
                        && board[x - j][y + j] * board[x][y] == -1
                        && board[x - j - 1][y + j + 1] == 0) {
                    return true;
                }
                if (x - j - 1 >= 0 && y - j - 1 >= 0
                        && board[x - j][y - j] * board[x][y] == -1
                        && board[x - j - 1][y - j - 1] == 0) {
                    return true;
                }
            }
        } else {
            if (x + 2 < BOARD_DIMENSION && y + 2 < BOARD_DIMENSION
                    && board[x + 1][y + 1] * board[x][y] == -1
                    && board[x + 2][y + 2] == 0) {
                return true;
            }
            if (x + 2 < BOARD_DIMENSION && y - 2 >= 0
                    && board[x + 1][y - 1] * board[x][y] == -1
                    && board[x + 2][y - 2] == 0) {
                return true;
            }
            if (x - 2 >= 0 && y + 2 < BOARD_DIMENSION
                    && board[x - 1][y + 1] * board[x][y] == -1
                    && board[x - 2][y + 2] == 0) {
                return true;
            }
            if (x - 2 >= 0 && y - 2 >= 0
                    && board[x - 1][y - 1] * board[x][y] == -1
                    && board[x - 2][y - 2] == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the index of pos in whitePositions or blackPositions
     * @param pos the position to return the index of
     * @return the index of pos
     */
    public int currentIndex(Position pos) {
        for (int i = 0; i < whiteCount; ++i) {
            if (whitePositions[i].equals(pos)) {
                return i;
            }
        }
        for (int i = 0; i < blackCount; ++i) {
            if (blackPositions[i].equals(pos)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns true or false if whites or blacks have moves from cell
     * @param cell the cell considered
     * @param isWhite the color considered
     * @return returns true if the draught in cell has moves
     */
    public boolean canStrikeFrom(Position cell, boolean isWhite) {
        return canStrikeFrom(currentIndex(cell), isWhite);
    }

    /**
     * Finds out weather the player of corresponding color has a move
     * @param isWhite the color
     * @return true if there is a move and false otherwise
     */
    public boolean canStrike(boolean isWhite) {
        int draughtCount = isWhite ? whiteCount : blackCount;
        for (int i = 0; i < draughtCount; ++i) {
            if (!canStrikeFrom(i, isWhite)) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * Finds out weather there is opponent draught between oldCell and newCell
     * @param oldCell first cell considered
     * @param newCell second cell considered
     * @param isWhite the color considered
     * @return the position if there is opponent
     * @throws ErrorException in case there is no opponent this exception is thrown
     */
    public Position findOpponentBetween(Position oldCell, Position newCell, boolean isWhite)
            throws ErrorException {
        int oldX = oldCell.getX();
        int oldY = oldCell.getY();
        int newX = newCell.getX();
        int newY = newCell.getY();
        int color = isWhite ? 1 : -1;
        if (oldCell.isQueen()) {
            int xDirection = (int) Math.signum(newX - oldX);
            int yDirection = (int) Math.signum(newY - oldY);
            int x = oldX + xDirection;
            int y = oldY + yDirection;
            while (x != newX) {
                if (board[x][y] * color == -1) {
                    for (int i = 0; i < whiteCount; ++i) {
                        if (x == whitePositions[i].getX() && y == whitePositions[i].getY()) {
                            return new Position(whitePositions[i].getX(), y, whitePositions[i].isQueen());
                        }
                    }
                    for (int i = 0; i < blackCount; ++i) {
                        if (x == blackPositions[i].getX() && y == blackPositions[i].getY()) {
                            return new Position(blackPositions[i].getX(), y, blackPositions[i].isQueen());
                        }
                    }
                }
                x += xDirection;
                y += yDirection;
            }
            throw new ErrorException();
        } else {
            int x = (oldX + newX) / 2;
            int y = (oldY + newY) / 2;
            if (board[x][y] * color != -1) {
                // other error: no opponent between " + oldCell + " and " + newCell
                throw new ErrorException();
            }
            for (int i = 0; i < whiteCount; ++i) {
                if (x == whitePositions[i].getX() && y == whitePositions[i].getY()) {
                    return new Position(whitePositions[i].getX(), y, whitePositions[i].isQueen());
                }
            }
            for (int i = 0; i < blackCount; ++i) {
                if (x == blackPositions[i].getX() && y == blackPositions[i].getY()) {
                    return new Position(blackPositions[i].getX(), y, blackPositions[i].isQueen());
                }
            }
            return NULL_POSITION;
        }
    }
}

