package ru.giv13.chess;

import java.util.LinkedHashSet;
import java.util.Set;

public record CellShift(int fileShift, int rankShift) {
    public static Set<CellShift> getDirection(int fileShift, int rankShift, int count) {
        Set<CellShift> direction = new LinkedHashSet<>();
        for (int i = 1; i <= count; i++) {
            direction.add(new CellShift(fileShift * i, rankShift * i));
        }
        return direction;
    }
}
