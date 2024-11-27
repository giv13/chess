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
                    if (!isAttack && isCellAvailableForMove(shiftedCell, board) && !isKingUnderAttack(board, shiftedCell)
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

    private boolean isKingUnderAttack(Board board, Cell shiftedCell) {
        Piece king = getKing(board);
        if (king == null) {
            return false;
        }
        Cell sourceCell = cell;
        Piece removedPiece = board.preMovePiece(sourceCell, shiftedCell);
        boolean isCellUnderAttack = isCellUnderAttack(sourceCell, king.cell, board);
        board.rollbackPreMovePiece(sourceCell, shiftedCell, removedPiece);
        return isCellUnderAttack;
    }

    private Piece getKing(Board board) {
        Set<Piece> pieces = board.getPiecesByColor(color);
        for (Piece piece : pieces) {
            if (piece instanceof King) {
                return piece;
            }
        }
        return null;
    }

    private boolean isCellUnderAttack(Cell sourceCell, Cell kingCell, Board board) {
        Set<Piece> pieces = board.getPiecesByColor(color.opposite());
        for (Piece piece : pieces) {
            Set<Cell> cells = piece.getAvailableCells(board, true);
            if (cells.contains(kingCell)) {
                return true;
            }
            boolean isCastling = this instanceof King && Math.abs(kingCell.file.ordinal() - sourceCell.file.ordinal()) == 2;
            if (isCastling) {
                boolean isRight = kingCell.file.ordinal() > sourceCell.file.ordinal();
                Cell prevCell = new Cell(File.values()[kingCell.file.ordinal() + (isRight ? -1 : 1)], kingCell.rank);
                if (cells.contains(sourceCell) || cells.contains(prevCell)) {
                    return true;
                }
            }
        }
        return false;
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
            default -> null;
        };
    }
}
