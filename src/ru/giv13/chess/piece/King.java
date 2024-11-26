package ru.giv13.chess.piece;

import ru.giv13.chess.Board;
import ru.giv13.chess.Cell;
import ru.giv13.chess.CellShift;
import ru.giv13.chess.Color;
import ru.giv13.chess.File;
import ru.giv13.chess.Type;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece {
    public King(Color color, Cell cell) {
        super("♚", color, cell);
    }

    @Override
    protected boolean isCellAvailableForMove(Cell shiftedCell, Board board) {
        boolean isCastling = Math.abs(shiftedCell.file.ordinal() - cell.file.ordinal()) == 2;
        return super.isCellAvailableForMove(shiftedCell, board) && !isCellUnderAttack(shiftedCell, board)
                && (!isCastling || board.castlings.contains(shiftedCell));
    }

    @Override
    protected boolean isCellAvailableForKingAttack(Cell shiftedCell, Board board) {
        Piece piece = board.getPiece(shiftedCell);
        return piece == null;
    }

    private boolean isCellUnderAttack(Cell shiftedCell, Board board) {
        Set<Piece> pieces = board.getPiecesByColor(color.opposite());
        for (Piece piece : pieces) {
            Set<Cell> cells = piece.getAvailableCells(board, Type.ATTACK);
            if (cells.contains(shiftedCell)) {
                return true;
            }
            boolean isCastling = Math.abs(shiftedCell.file.ordinal() - cell.file.ordinal()) == 2;
            if (isCastling) {
                boolean isRight = shiftedCell.file.ordinal() > cell.file.ordinal();
                Cell prevCell = new Cell(File.values()[shiftedCell.file.ordinal() + (isRight ? -1 : 1)], shiftedCell.rank);
                if (cells.contains(cell) || cells.contains(prevCell)) {
                    return true;
                }
            }
        }
        return false;
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
