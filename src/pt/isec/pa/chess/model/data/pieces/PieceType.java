package pt.isec.pa.chess.model.data.pieces;

public enum PieceType {
    KING("k"), QUEEN("q"),  ROOK("r"),  BISHOP("b"),  KNIGHT("n"),  PAWN("p");

    private final String icon;

    PieceType(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public static String getName(String symbol) {
        return switch (symbol.toLowerCase()) {
            case "k" -> "king";
            case "q" -> "queen";
            case "b" -> "bishop";
            case "n" -> "knight";
            case "r" -> "rook";
            case "p" -> "pawn";
            default -> "UnknownSymbol";
        };
    }
}
