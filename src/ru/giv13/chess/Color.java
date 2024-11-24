package ru.giv13.chess;

public enum Color {
    WHITE,
    BLACK;

    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}
