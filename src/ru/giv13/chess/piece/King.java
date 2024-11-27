package ru.giv13.chess.piece;

import ru.giv13.chess.Board;
import ru.giv13.chess.Cell;
import ru.giv13.chess.CellShift;
import ru.giv13.chess.Color;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece {
    public King(Color color, Cell cell) {
        super("♚", color, cell);
    }

    @Override
    protected boolean isCellAvailableForMove(Cell shiftedCell, Board board) {
        boolean isCastling = Math.abs(shiftedCell.file.ordinal() - cell.file.ordinal()) == 2;
        return super.isCellAvailableForMove(shiftedCell, board) && (!isCastling || board.castlings.contains(shiftedCell));
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
