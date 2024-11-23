package ru.giv13.chess.piece;

import ru.giv13.chess.Color;
import ru.giv13.chess.Cell;
import ru.giv13.chess.CellShift;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Knight extends Piece {
    public Knight(Color color, Cell cell) {
        super ("♞", color, cell);
    }

    @Override
    protected Set<CellShift> getMoves() {
        return new HashSet<>(List.of(
                new CellShift(2, 1),
                new CellShift(1, 2),
                new CellShift(-1, 2),
                new CellShift(-2, 1),
                new CellShift(-2, -1),
                new CellShift(-1, -2),
                new CellShift(1, -2),
                new CellShift(2, -1)
        ));
    }
}
