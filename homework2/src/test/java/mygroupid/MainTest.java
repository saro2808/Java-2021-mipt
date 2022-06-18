package mygroupid;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Scanner;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class MainTest {
    @Test
    void helloTest() {
        Assertions.assertThat(2 + 2).isEqualTo(4);
    }

    @Test
    void simpleTest() {
        String[] data = {
                "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2",
                "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8",
                "g3-f4 f6-e5\n", "c3-d4 e5:c3\n", "b2:d4 d6-c5\n",
                "d2-c3 g7-f6\n", "h2-g3 h8-g7\n", "c1-b2 f6-g5\n",
                "g3-h4 g7-f6\n", "f4-e5 f8-g7\n"
        };

        String answer = "a1 a3 b2 c3 d4 e1 e3 e5 f2 g1 h4 \n"
                + "a7 b6 b8 c5 c7 d8 e7 f6 g5 g7 h6 ";

        Main.boardInit();
        Main.readInput(data);
        Main.mainLoop();
        String output = Main.returnOutput();

        Assertions.assertThat(output).isEqualTo(answer);
    }

    @Test
    void simpleTest2() {
        String[] data = {
                "d4", "c3 E5 g7", "d4:b2 E5:A1"
        };

        String answer = "\n"
                + "A1 g7 ";

        Main.boardInit();
        Main.readInput(data);
        Main.mainLoop();
        String output = Main.returnOutput();

        Assertions.assertThat(output).isEqualTo(answer);
    }

    @Test
    void simpleTest3() {
        String[] data = {
                "d4", "c3 E5 g7",
                "d4:f6:h8 c3-d2", "H8-B2 d2-e1"
        };

        String answer = "B2 \n"
                + "E1 ";

        Main.boardInit();
        Main.readInput(data);
        Main.mainLoop();
        String output = Main.returnOutput();

        Assertions.assertThat(output).isEqualTo(answer);
    }

    @Test
    void simpleTest4() {
        String[] data = {
                "D6 d4 b2 f2", "b4 f6 h2 h4",
                "D6:A3 f6-g7"
        };

        String answer = "A3 b2 d4 f2 \n"
                + "g7 h2 h4 ";

        Main.boardInit();
        Main.readInput(data);
        Main.mainLoop();
        String output = Main.returnOutput();

        Assertions.assertThat(output).isEqualTo(answer);
    }

    @Test
    void busyCellTest1() {
        String[] data = {
                "A3 b4 f2 g7 h8", "b6 c3 d6 d8 E1",
                "b4-a3 d6-e7\n"
        };

        String answer = "busy cell\n";
        String output;

        Main.boardInit();
        Main.readInput(data);
        try {
            Main.mainLoop();
        } catch (BusyCellException bce) {
            output = "busy cell\n";
            Assertions.assertThat(output).isEqualTo(answer);
        }
    }

    @Test
    void whiteCellTest1() {
        String[] data = {
                "b2 c3 E5 g5", "E3 f2 f4",
                "E5:G3:E1 E3-F3\n"
        };

        String answer = "white cell\n";
        String output;

        Main.boardInit();
        Main.readInput(data);
        try {
            Main.mainLoop();
        } catch (WhiteCellException wce) {
            output = "white cell\n";
            Assertions.assertThat(output).isEqualTo(answer);
        }
    }

    @Test
    void invalidMoveTest1() {
        String[] data = {
                "D8 c5 f6 g1", "a5 f4 h6 h8",
                "c5-d6 f4-e3\n", "d6-c7 h8-g7\n",
                "c7-b8 g7:e5\n", "B8:F4:D2 h6-g5\n"
        };

        String answer = "invalid move\n";
        String output;

        Main.boardInit();
        Main.readInput(data);
        try {
            Main.mainLoop();
        } catch (InvalidMoveException ime) {
            output = "invalid move\n";
            Assertions.assertThat(output).isEqualTo(answer);
        }
    }

    @Test
    void errorTest1() {
        String[] data = {
                "d2 d6 f2 H2", "A1 e3 E5 g5",
                "f2:d4:f6:h4 A1-A3\n"
        };

        String answer = "error\n";
        String output;

        Main.boardInit();
        Main.readInput(data);
        try {
            Main.mainLoop();
        } catch (ErrorException ee) {
            output = "error\n";
            Assertions.assertThat(output).isEqualTo(answer);
        }
    }

    @Test
    void errorTest2() {
        String[] data = {
                "B4 d2 e2", "c3 d4 f4",
                "B4:A3 f4:g5\n"
        };

        String answer = "error\n";
        String output;

        Main.boardInit();
        Main.readInput(data);
        try {
            Main.mainLoop();
        } catch (ErrorException ee) {
            output = "error\n";
            Assertions.assertThat(output).isEqualTo(answer);
        }
    }
}

