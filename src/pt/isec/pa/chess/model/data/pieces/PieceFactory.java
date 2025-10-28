package pt.isec.pa.chess.model.data.pieces;

import pt.isec.pa.chess.model.data.board.Position;

public class PieceFactory {
    public static PieceType fromIcon(String icon){
        for (PieceType type : PieceType.values()) {
            if (type.getIcon().equalsIgnoreCase(icon)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de peça inválido: " + icon);
    }

    public static Piece createPiece(PieceType type, Position pos, boolean isWhite) {
        return switch (type) {
            case KING -> new King(pos, isWhite);
            case QUEEN -> new Queen(pos, isWhite);
            case ROOK -> new Rook(pos, isWhite);
            case BISHOP -> new Bishop(pos, isWhite);
            case KNIGHT -> new Knight(pos, isWhite);
            case PAWN -> new Pawn(pos, isWhite);
        };
    }

    public static String getNamePT(String symbol) {
        return switch (symbol.toLowerCase()) {
            case "k" -> "rei";
            case "q" -> "rainha";
            case "b" -> "bispo";
            case "n" -> "cavalo";
            case "r" -> "torre";
            case "p" -> "peao";
            default -> "UnknownSymbol";
        };
    }

    public static Piece createPiece(String piece) {
        char typeChar = piece.charAt(0);
        PieceType type = fromIcon("" + typeChar);

        boolean isWhite = Character.isUpperCase(typeChar);
        Position pos = Position.convert("" + piece.charAt(1) + piece.charAt(2));
        return createPiece(type, pos, isWhite);
    }
}
