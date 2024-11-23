package ru.giv13.chess.piece;

import ru.giv13.chess.Color;
import ru.giv13.chess.Cell;
import ru.giv13.chess.CellShift;

import java.util.Set;

public class Pawn extends Piece {
    public Pawn(Color color, Cell cell) {
        super("♟", color, cell);
    }

    @Override
    protected Set<CellShift> getMoves() {
        return null;
    }
}
