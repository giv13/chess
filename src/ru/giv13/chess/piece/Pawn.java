package ru.giv13.chess.piece;

import ru.giv13.chess.Board;
import ru.giv13.chess.Cell;
import ru.giv13.chess.CellShift;
import ru.giv13.chess.Color;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {
    public Pawn(Color color, Cell cell) {
        super("♟", color, cell);
    }

    @Override
    protected boolean isCellAvailableForMove(Cell shiftedCell, Board board) {
        Piece piece = board.getPiece(shiftedCell);
        if (shiftedCell.file == cell.file) {
            return piece == null;
        } else {
            return piece == null ? board.enPassant(cell, shiftedCell) != null : piece.color != color;
        }
    }

    @Override
    protected boolean isCellAvailableForKingAttack(Cell shiftedCell, Board board) {
        return super.isCellAvailableForKingAttack(shiftedCell, board) && shiftedCell.file != cell.file;
    }

    @Override
    protected Set<Set<CellShift>> getDirections() {
        Set<Set<CellShift>> directions = new HashSet<>();
        boolean isWhite = color == Color.WHITE;
        boolean isFirst = isWhite ? cell.rank == 2 : cell.rank == 7;
        directions.add(CellShift.getDirection(0, (isWhite ? 1 : -1), isFirst ? 2 : 1));
        directions.add(CellShift.getDirection(1, (isWhite ? 1 : -1), 1));
        directions.add(CellShift.getDirection(-1, (isWhite ? 1 : -1), 1));
        return directions;
    }
}
