package ru.giv13.chess.piece;

import ru.giv13.chess.Board;
import ru.giv13.chess.Cell;
import ru.giv13.chess.CellShift;
import ru.giv13.chess.Color;
import ru.giv13.chess.File;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece {
    public King(Color color, Cell cell) {
        super("♚", color, cell);
    }

    @Override
    protected boolean isCellAvailableForMove(Cell shiftedCell, Board board) {
        boolean isCellAvailableForMove = super.isCellAvailableForMove(shiftedCell, board);
        if (!isCellAvailableForMove) {
            return false;
        }
        boolean isCastling = Math.abs(cell.file.ordinal() - shiftedCell.file.ordinal()) == 2;
        if (isCastling) {
            if (!board.castlings.contains(shiftedCell)) {
                return false;
            }
            boolean isLong = cell.file.ordinal() > shiftedCell.file.ordinal();
            Cell prevCell = new Cell(File.values()[shiftedCell.file.ordinal() + (isLong ? 1 : -1)], shiftedCell.rank);
            if (isLong) {
                Cell nextCell = new Cell(File.values()[shiftedCell.file.ordinal() - 1], shiftedCell.rank);
                if (board.getPiece(nextCell) != null) {
                    return false;
                }
            }
            Set<Piece> pieces = board.getPiecesByColor(color.opposite());
            for (Piece piece : pieces) {
                Set<Cell> cells = piece.getAvailableCells(board, true);
                if (cells.contains(cell) || cells.contains(prevCell)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected Set<Set<CellShift>> getDirections() {
        Set<Set<CellShift>> directions = new HashSet<>();
        directions.add(CellShift.getDirection(0, 1, 1));
        directions.add(CellShift.getDirection(1, 1, 1));
        directions.add(CellShift.getDirection(1, 0, 2));
        directions.add(CellShift.getDirection(1, -1, 1));
        directions.add(CellShift.getDirection(0, -1, 1));
        directions.add(CellShift.getDirection(-1, -1, 1));
        directions.add(CellShift.getDirection(-1, 0, 2));
        directions.add(CellShift.getDirection(-1, 1, 1));
        return directions;
    }
}
