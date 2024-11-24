package ru.giv13.chess.piece;


import ru.giv13.chess.Board;
import ru.giv13.chess.Cell;
import ru.giv13.chess.CellShift;
import ru.giv13.chess.Color;

import java.util.HashSet;
import java.util.Set;

abstract public class Piece {
    public final String code;
    public final Color color;
    public Cell cell;

    public Piece(String code, Color color, Cell cell) {
        this.code = code;
        this.color = color;
        this.cell = cell;
    }

    public Set<Cell> getAvailableCells(Board board) {
        Set<Cell> cells = new HashSet<>();
        for (Set<CellShift> direction : getDirections()) {
            for (CellShift shift : direction) {
                if (cell.canShift(shift)) {
                    Cell shiftedCell = cell.shift(shift);
                    if (isCellAvailableForMove(shiftedCell, board)) {
                        cells.add(shiftedCell);
                    }
                    if (isCellLastForMove(shiftedCell, board)) {
                        break;
                    }
                }
            }
        }
        return cells;
    }

    protected boolean isCellAvailableForMove(Cell shiftedCell, Board board) {
        Piece piece = board.getPiece(shiftedCell);
        return piece == null || piece.color != color;
    }

    protected boolean isCellLastForMove(Cell shiftedCell, Board board) {
        Piece piece = board.getPiece(shiftedCell);
        return piece != null;
    }

    protected abstract Set<Set<CellShift>> getDirections();

    public static Piece fromFEN(char fenChar, Cell cell) {
        char fenCharUpper = Character.toUpperCase(fenChar);
        Color color = fenCharUpper == fenChar ? Color.WHITE : Color.BLACK;
        return switch (fenCharUpper) {
            case 'B' -> new Bishop(color, cell);
            case 'K' -> new King(color, cell);
            case 'N' -> new Knight(color, cell);
            case 'P' -> new Pawn(color, cell);
            case 'R' -> new Rook(color, cell);
            case 'Q' -> new Queen(color, cell);
            default -> throw new RuntimeException("The char \"" + fenChar + "\" is unknown");
        };
    }
}
