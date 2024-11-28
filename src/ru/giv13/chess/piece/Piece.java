package ru.giv13.chess.piece;

import ru.giv13.chess.*;

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
        return getAvailableCells(board, false);
    }

    public Set<Cell> getAvailableCells(Board board, boolean isAttack) {
        Set<Cell> cells = new HashSet<>();
        for (Set<CellShift> direction : getDirections()) {
            for (CellShift shift : direction) {
                if (cell.canShift(shift)) {
                    Cell shiftedCell = cell.shift(shift);
                    if (!isAttack && isCellAvailableForMove(shiftedCell, board) && !willKingUnderAttack(shiftedCell, board)
                            || isAttack && isCellAvailableForKingAttack(shiftedCell, board)) {
                        cells.add(shiftedCell);
                    }
                    if (board.getPiece(shiftedCell) != null) {
                        break;
                    }
                }
            }
        }
        return cells;
    }

    protected boolean isCellAvailableForMove(Cell shiftedCell, Board board) {
        Piece piece = board.getPiece(shiftedCell);
        return piece == null || (piece.color != color && !(piece instanceof King));
    }

    protected boolean isCellAvailableForKingAttack(Cell shiftedCell, Board board) {
        Piece piece = board.getPiece(shiftedCell);
        return piece == null || (piece.color != color && piece instanceof King);
    }

    protected boolean willKingUnderAttack(Cell shiftedCell, Board board) {
        Cell sourceCell = cell;
        Piece removedPiece = board.preMovePiece(sourceCell, shiftedCell);
        boolean isCheck = board.isCheck();
        board.rollbackPreMovePiece(sourceCell, shiftedCell, removedPiece);
        return isCheck;
    }

    protected abstract Set<Set<CellShift>> getDirections();

    public static Piece fromFEN(char fenChar, Cell cell) {
        Color color = Character.toUpperCase(fenChar) == fenChar ? Color.WHITE : Color.BLACK;
        return fromSymbol(fenChar, color, cell);
    }

    public static Piece fromSymbol(char symbol, Color color, Cell cell) {
        return switch (Character.toUpperCase(symbol)) {
            case 'B' -> new Bishop(color, cell);
            case 'K' -> new King(color, cell);
            case 'N' -> new Knight(color, cell);
            case 'P' -> new Pawn(color, cell);
            case 'R' -> new Rook(color, cell);
            case 'Q' -> new Queen(color, cell);
            default -> null;
        };
    }
}
