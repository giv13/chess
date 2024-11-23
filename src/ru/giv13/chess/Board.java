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

    public void setDefaultPosition() {
        for (File file : File.values()) {
            setPiece(new Cell(file, 2), new Pawn(Color.WHITE, new Cell(file, 2)));
            setPiece(new Cell(file, 7), new Pawn(Color.BLACK, new Cell(file, 7)));
        }

        setPiece(new Cell(File.A, 1), new Rook(Color.WHITE, new Cell(File.A, 1)));
        setPiece(new Cell(File.H, 1), new Rook(Color.WHITE, new Cell(File.H, 1)));
        setPiece(new Cell(File.A, 8), new Rook(Color.BLACK, new Cell(File.A, 8)));
        setPiece(new Cell(File.H, 8), new Rook(Color.BLACK, new Cell(File.H, 8)));

        setPiece(new Cell(File.B, 1), new Knight(Color.WHITE, new Cell(File.B, 1)));
        setPiece(new Cell(File.G, 1), new Knight(Color.WHITE, new Cell(File.G, 1)));
        setPiece(new Cell(File.B, 8), new Knight(Color.BLACK, new Cell(File.B, 8)));
        setPiece(new Cell(File.G, 8), new Knight(Color.BLACK, new Cell(File.G, 8)));

        setPiece(new Cell(File.C, 1), new Bishop(Color.WHITE, new Cell(File.C, 1)));
        setPiece(new Cell(File.F, 1), new Bishop(Color.WHITE, new Cell(File.F, 1)));
        setPiece(new Cell(File.C, 8), new Bishop(Color.BLACK, new Cell(File.C, 8)));
        setPiece(new Cell(File.F, 8), new Bishop(Color.BLACK, new Cell(File.F, 8)));

        setPiece(new Cell(File.D, 1), new Queen(Color.WHITE, new Cell(File.D, 1)));
        setPiece(new Cell(File.E, 1), new King(Color.WHITE, new Cell(File.E, 1)));
        setPiece(new Cell(File.D, 8), new Queen(Color.BLACK, new Cell(File.D, 8)));
        setPiece(new Cell(File.E, 8), new King(Color.BLACK, new Cell(File.E, 8)));
    }
}
