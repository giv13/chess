package ru.giv13.chess;

import ru.giv13.chess.piece.Piece;

import java.util.HashSet;
import java.util.Set;

public class BoardRenderer {
    public static final String RESET_COLOR = "\u001B[0m";
    public static final String FIELD_COLOR = "\u001B[90m";
    public static final String WHITE_PIECE_COLOR = "\u001B[97m";
    public static final String BLACK_PIECE_COLOR = "\u001B[30m";
    public static final String WHITE_CELL_COLOR = "\u001B[48;2;122;133;147m";
    public static final String BLACK_CELL_COLOR = "\u001B[48;2;63;73;92m";
    public static final String WHITE_LAST_MOVE_CELL_COLOR = "\u001B[48;2;170;162;58m";
    public static final String BLACK_LAST_MOVE_CELL_COLOR = "\u001B[48;2;130;122;38m";
    public static final String WHITE_AVAILABLE_CELL_COLOR = "\u001B[48;2;120;150;86m";
    public static final String BLACK_AVAILABLE_CELL_COLOR = "\u001B[48;2;80;110;66m";
    public static final String ERROR_TEXT_COLOR = "\u001B[31m";
    public static final String SUCCESS_TEXT_COLOR = "\u001B[32m";

    public void render(Board board, Set<Cell> lastMoveCells) {
        render(board, lastMoveCells, new HashSet<>());
    }

    public void render(Board board, Set<Cell> lastMoveCells, Set<Cell> availableCells) {
        for (int rank = 8; rank >= 1; rank--) {
            StringBuilder line = new StringBuilder();
            for (File file : File.values()) {
                Cell cell = new Cell(file, rank);
                Piece piece = board.getPiece(cell);
                line.append(renderCell(cell, piece, lastMoveCells, availableCells));
            }
            line.append(RESET_COLOR)
                    .append(" ")
                    .append(FIELD_COLOR)
                    .append(rank)
                    .append(RESET_COLOR);
            System.out.println(line);
        }
        StringBuilder line = new StringBuilder();
        line.append(FIELD_COLOR);
        for (File file : File.values()) {
            line.append(" ")
                    .append(file)
                    .append("\u2003");
        }
        line.append(RESET_COLOR);
        System.out.println(line);
    }

    private String renderCell(Cell cell, Piece piece, Set<Cell> lastMoveCells, Set<Cell> availableCells) {
        StringBuilder line = new StringBuilder();
        boolean isCellWhite = cell.color == Color.WHITE;
        String cellColor;
        if (availableCells.contains(cell)) {
            cellColor = isCellWhite ? WHITE_AVAILABLE_CELL_COLOR : BLACK_AVAILABLE_CELL_COLOR;
        } else if (lastMoveCells.contains(cell)) {
            cellColor = isCellWhite ? WHITE_LAST_MOVE_CELL_COLOR : BLACK_LAST_MOVE_CELL_COLOR;
        } else {
            cellColor = isCellWhite ? WHITE_CELL_COLOR : BLACK_CELL_COLOR;
        }
        line.append(cellColor);
        if (piece == null) {
            line.append(" \u2003 ");
        } else {
            line.append(piece.color == Color.WHITE ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR)
                    .append(" ")
                    .append(piece.code)
                    .append(" ");
        }
        return line.toString();
    }
}
