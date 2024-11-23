package ru.giv13.chess;

import java.util.Objects;

public class Cell {
    public final File file;
    public final Integer rank;
    public final Color color;

    public Cell(File file, Integer rank) {
        this.file = file;
        this.rank = rank;
        this.color = (file.ordinal() + 1 + rank) % 2 == 0 ? Color.BLACK : Color.WHITE;
    }

    public Cell shift(CellShift shift) {
        return new Cell(File.values()[file.ordinal() + shift.fileShift()], rank + shift.rankShift());
    }

    public boolean canShift(CellShift shift) {
        int f = file.ordinal() + shift.fileShift();
        int r = rank + shift.rankShift();
        return f >= 0 && f <= 7 && r >= 1 && r <= 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return file == cell.file && Objects.equals(rank, cell.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }
}
