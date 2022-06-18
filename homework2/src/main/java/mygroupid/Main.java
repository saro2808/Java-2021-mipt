package mygroupid;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public final class Main {
    private Main() { }

    private static ArrayList<Step> steps;
    private static int stepsCount;
    private static final Board BOARD = new Board();

//    public static final class BusyCellException extends RuntimeException { }
//    public static final class WhiteCellException extends RuntimeException { }
//    public static final class InvalidMoveException extends RuntimeException { }
//    public static final class ErrorException extends RuntimeException { }

    public static void boardInit() {
        BOARD.boardInit();
    }

    private static void readInput() {
        Scanner in = new Scanner(System.in);
        String nextLine = in.nextLine();
        BOARD.processLine(nextLine, true);
        nextLine = in.nextLine();
        BOARD.processLine(nextLine, false);
        steps = new ArrayList<>();
        while (in.hasNextLine()) {
            nextLine = in.nextLine();
            String[] lineArr = nextLine.split(" ");
            steps.add(new Step(lineArr[0]));
            steps.add(new Step(lineArr[1]));
        }
        stepsCount = steps.size();
    }

    public static void readInput(String[] input) {
        BOARD.processLine(input[0], true);
        BOARD.processLine(input[1], false);
        steps = new ArrayList<>();
        for (int i = 2; i < input.length; ++i) {
            String[] lineArr = input[i].split(" ");
            steps.add(new Step(lineArr[0]));
            steps.add(new Step(lineArr[1]));
        }
        stepsCount = steps.size();
    }

    public static void mainLoop()
            throws BusyCellException, WhiteCellException, InvalidMoveException, ErrorException {
        for (int currentStepIndex = 0; currentStepIndex < stepsCount; ++currentStepIndex) {
            Step currentStep = steps.get(currentStepIndex);
            int stepLength = currentStep.getPositionsCount();
            Position newCell = currentStep.positionAt(1);
            int newX = newCell.getX();
            int newY = newCell.getY();

            if (BOARD.getCellColor(newCell) != 0) {
                throw new BusyCellException();
            }

            if ((newX + newY) % 2 != 0) {
                throw new WhiteCellException();
            }

            boolean isWhite = currentStepIndex % 2 == 0;
            if (isWhite && BOARD.canStrike(true) || !isWhite && BOARD.canStrike(false)) {
                if (currentStep.syntaxError() || !currentStep.eating()) {
                    throw new InvalidMoveException();
                }
            }

            // for draughts that are eaten
            ArrayList<Position> toBeDeleted = new ArrayList<>();

            Position oldCell = currentStep.positionAt(0);
            int index = BOARD.currentIndex(oldCell);
            int oldX = oldCell.getX();
            int oldY = oldCell.getY();
            Position newQueenCell;

            for (int i = 1; i < stepLength; ++i) {
                newCell = currentStep.positionAt(i);
                newX = newCell.getX();
                newY = newCell.getY();

                if (BOARD.getCellColor(newCell) != 0) {
                    throw new BusyCellException();
                }

                if ((newX + newY) % 2 != 0) {
                    throw new WhiteCellException();
                }

                if (!newCell.isQueen()) {
                    if (!currentStep.eating()) {
                        if (Math.abs(newX - oldX) != 1 || Math.abs(newY - oldY) != 1) {
                            throw new ErrorException();
                        }
                    } else {
                        if (Math.abs(newX - oldX) != 2 || Math.abs(newY - oldY) != 2) {
                            throw new ErrorException();
                        }
                    }
                } else {
                    if (Math.abs(newX - oldX) != Math.abs(newY - oldY)) {
                        throw new ErrorException();
                    }
                }

                if (currentStep.eating()) {
                    // find opponent on diagonal
                    toBeDeleted.add(BOARD.findOpponentBetween(oldCell, newCell, isWhite));
                }

                // now there is no draught here
                BOARD.setCellColor(oldCell, 0);
                oldCell = newCell;
                oldX = newX;
                oldY = newY;
            }

            // the move is over
            BOARD.setCellColor(newCell, isWhite ? 1 : -1);

            for (Position cell : toBeDeleted) {
                BOARD.setCellColor(cell, 0);
                if (!isWhite) {
                    BOARD.setWhitePositionAt(BOARD.currentIndex(cell), Board.NULL_POSITION);
                } else {
                    BOARD.setBlackPositionAt(BOARD.currentIndex(cell), Board.NULL_POSITION);
                }
            }

            oldCell = currentStep.positionAt(0);
            if (stepLength > 2 && (isWhite && BOARD.canStrikeFrom(oldCell, true)
                    || !isWhite && BOARD.canStrikeFrom(oldCell, false))) {
                throw new InvalidMoveException();
            }

            if (isWhite) {
                BOARD.setWhitePositionAt(index, newCell);
            } else {
                BOARD.setBlackPositionAt(index, newCell);
            }

            if (!newCell.isQueen()) {
                if (isWhite && newY == Board.BOARD_DIMENSION - 1 || !isWhite && newY == 0) {
                    // become queen
                    if (isWhite) {
                        Position old = BOARD.getWhitePositionAt(index);
                        newQueenCell = new Position(old.getX(), old.getY(), true);
                        BOARD.setWhitePositionAt(index, newQueenCell);
                    } else {
                        Position old = BOARD.getBlackPositionAt(index);
                        newQueenCell = new Position(old.getX(), old.getY(), true);
                        BOARD.setBlackPositionAt(index, newQueenCell);
                    }
                }
            }
        }
    }

    public static String returnOutput() {
        Position[] whites = BOARD.getWhitePositions();
        Position[] blacks = BOARD.getBlackPositions();
        Comparator<Position> comp = (p1, p2) -> {
            int x1 = p1.getX();
            int y1 = p1.getY();
            int x2 = p2.getX();
            int y2 = p2.getY();
            boolean isQueen1 = p1.isQueen();
            boolean isQueen2 = p2.isQueen();
            if (isQueen1 && !isQueen2) {
                return -1;
            }
            if (isQueen2 && !isQueen1) {
                return 1;
            }
            if (x1 == x2) {
                if (y1 == y2) {
                    return 0;
                }
                return y1 - y2;
            }
            return x1 - x2;
        };
        Arrays.sort(whites, comp);
        Arrays.sort(blacks, comp);
        StringBuilder retVal = new StringBuilder();
        for (Position pos : whites) {
            if (pos.equals(Board.NULL_POSITION)) {
                break;
            }
            retVal.append(pos).append(" ");
        }
        retVal.append("\n");
        for (Position pos : blacks) {
            if (pos.equals(Board.NULL_POSITION)) {
                break;
            }
            retVal.append(pos).append(" ");
        }
        return retVal.toString();
    }

    public static void main(String[] args) {
        BOARD.boardInit();
        readInput();
        try {
            mainLoop();
        } catch (BusyCellException bce) {
            System.out.println("busy cell");
            return;
        } catch (WhiteCellException wce) {
            System.out.println("white cell");
            return;
        } catch (InvalidMoveException ime) {
            System.out.println("invalid move");
            return;
        } catch (ErrorException ee) {
            System.out.println("error");
            return;
        }
        String finalConfiguration = returnOutput() + "\n";
        System.out.print(finalConfiguration);
    }
}

