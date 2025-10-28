package pt.isec.pa.chess.model;

import java.io.*;

public class ChessGameSerialization {
    private ChessGameSerialization() {}

    public static void exportGame(ChessGame game, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(game);
        }
    }

    public static ChessGame importGame(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (ChessGame) in.readObject();
        }
    }
}

