package ru.giv13.chess;

import ru.giv13.chess.piece.*;

import java.util.HashMap;

public class Board {
    HashMap<Cell, Piece> pieces = new HashMap<>();

    public Piece getPiece(Cell cell) { return pieces.get(cell); }

    public void setPiece(Cell cell, Piece piece) {
        piece.cell = cell;
        pieces.put(cell, piece);
    }

    public void removePiece(Cell cell) {
        pieces.remove(cell);
    }

    public void movePiece(Cell from, Cell to) {
        Piece piece = getPiece(from);
        removePiece(from);
        setPiece(to, piece);
    }

    public static Board setDefaultPosition() {
        return fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static Board fromFEN(String fen) {
        Board board = new Board();
        String[] parts = fen.split(" ");
        String[] lines = parts[0].split("/");

        for (int i = 0; i < lines.length; i++) {
            int file = 0;
            int rank = 8 - i;
            for (char fenChar : lines[i].toCharArray())  {
                if (Character.isDigit(fenChar)) {
                    file += Character.getNumericValue(fenChar);
                } else {
                    Cell cell = new Cell(File.values()[file], rank);
                    board.setPiece(cell, Piece.fromFEN(fenChar, cell));
                    file++;
                }
            }
        }
        return board;
    }
}
