package ru.giv13.chess;

import ru.giv13.chess.piece.King;
import ru.giv13.chess.piece.Pawn;
import ru.giv13.chess.piece.Piece;
import ru.giv13.chess.piece.Rook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
    final private Map<Cell, Piece> pieces = new HashMap<>();
    public Map<Cell, Boolean> castlings = new HashMap<>();
    public List<Cell> lastMoveCells = new ArrayList<>();

    public Board() {
        castlings.put(new Cell(File.C, 1), true);
        castlings.put(new Cell(File.G, 1), true);
        castlings.put(new Cell(File.C, 8), true);
        castlings.put(new Cell(File.G, 8), true);
    }

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
        if (piece instanceof King) {
            if (castlings.containsKey(to)) {
                boolean isLong = from.file.ordinal() > to.file.ordinal();
                movePiece(new Cell(isLong ? File.A : File.H, from.rank), new Cell(File.values()[from.file.ordinal() + (isLong ? -1 : 1)], from.rank));
            }
            if (piece.color == Color.WHITE) {
                castlings.replace(new Cell(File.C, 1), false);
                castlings.replace(new Cell(File.G, 1), false);
            } else {
                castlings.replace(new Cell(File.C, 8), false);
                castlings.replace(new Cell(File.G, 8), false);
            }
        }
        if (piece instanceof Rook) {
            if (from.equals(new Cell(File.A, 1))) {
                castlings.replace(new Cell(File.C, 1), false);
            } else if (from.equals(new Cell(File.H, 1))) {
                castlings.replace(new Cell(File.G, 1), false);
            } else if (from.equals(new Cell(File.A, 8))) {
                castlings.replace(new Cell(File.C, 8), false);
            } else if (from.equals(new Cell(File.H, 8))) {
                castlings.replace(new Cell(File.G, 8), false);
            }
        }
        Cell enPassant = enPassant(from, to);
        if (enPassant != null) {
            removePiece(enPassant);
        }
        removePiece(from);
        setPiece(to, piece);
        lastMoveCells.clear();
        lastMoveCells.add(from);
        lastMoveCells.add(to);
    }

    public Cell enPassant(Cell from, Cell to) {
        if (lastMoveCells.size() == 3) {
            Piece piece = getPiece(from);
            if (piece instanceof Pawn) {
                Cell lastMoveCell = lastMoveCells.get(1);
                Piece lastMoveCellPiece = getPiece(lastMoveCell);
                if (lastMoveCellPiece instanceof Pawn) {
                    if (Math.abs(lastMoveCell.file.ordinal() - from.file.ordinal()) == 1 && lastMoveCell.rank.equals(from.rank) &&
                            lastMoveCell.file.ordinal() == to.file.ordinal() && Math.abs(lastMoveCell.rank - lastMoveCells.getFirst().rank) == 2) {
                        return lastMoveCell;
                    }
                }
            }
        }
        return null;
    }

    public Set<Piece> getPiecesByColor(Color color) {
        Set<Piece> piecesByColor = new HashSet<>();
        for (Piece piece : pieces.values()) {
            if (piece.color == color) {
                piecesByColor.add(piece);
            }
        }
        return piecesByColor;
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
