package ru.giv13.chess;

import ru.giv13.chess.piece.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
    final private Map<Cell, Piece> pieces = new HashMap<>();
    public Color turn = Color.WHITE;
    public Set<Cell> castlings = new HashSet<>();
    public List<Cell> lastMoveCells = new ArrayList<>();
    public int halfmoveClock = 0;
    public int fullmoveNumber = 0;

    public Piece getPiece(Cell cell) { return pieces.get(cell); }

    public void setPiece(Cell cell, Piece piece) {
        piece.cell = cell;
        pieces.put(cell, piece);
    }

    public Piece removePiece(Cell cell) {
        return pieces.remove(cell);
    }

    public Piece preMovePiece(Cell from, Cell to) {
        Cell enPassant = enPassant(from, to);
        Piece removedPiece = enPassant == null ? getPiece(to) : removePiece(enPassant);
        Piece piece = removePiece(from);
        setPiece(to, piece);
        return removedPiece;
    }

    public void rollbackPreMovePiece(Cell from, Cell to, Piece removedPiece) {
        Piece piece = removePiece(to);
        setPiece(from, piece);
        if (removedPiece != null) {
            setPiece(removedPiece.cell, removedPiece);
        }
    }

    public void movePiece(Cell from, Cell to) {
        Piece piece = getPiece(from);

        if (piece instanceof Pawn || getPiece(to) != null) {
            halfmoveClock = 0;
        } else {
            halfmoveClock++;
        }
        if (piece.color == Color.BLACK) {
            fullmoveNumber++;
        }

        if (piece instanceof King) {
            if (castlings.contains(to)) {
                boolean isLong = from.file.ordinal() > to.file.ordinal();
                Piece rook = removePiece(new Cell(isLong ? File.A : File.H, from.rank));
                setPiece(new Cell(File.values()[from.file.ordinal() + (isLong ? -1 : 1)], from.rank), rook);
            }
            if (piece.color == Color.WHITE) {
                castlings.remove(new Cell(File.C, 1));
                castlings.remove(new Cell(File.G, 1));
            } else {
                castlings.remove(new Cell(File.C, 8));
                castlings.remove(new Cell(File.G, 8));
            }
        }
        if (piece instanceof Rook) {
            if (from.equals(new Cell(File.A, 1))) {
                castlings.remove(new Cell(File.C, 1));
            } else if (from.equals(new Cell(File.H, 1))) {
                castlings.remove(new Cell(File.G, 1));
            } else if (from.equals(new Cell(File.A, 8))) {
                castlings.remove(new Cell(File.C, 8));
            } else if (from.equals(new Cell(File.H, 8))) {
                castlings.remove(new Cell(File.G, 8));
            }
        }

        Cell enPassant = enPassant(from, to);
        if (enPassant != null) {
            removePiece(enPassant);
        }

        if (piece instanceof Pawn) {
            if (turn == Color.WHITE && to.rank == 8 || turn == Color.BLACK && to.rank == 1) {
                char symbol = Input.inputPiecePromoting(turn);
                Piece promoted = Piece.fromSymbol(symbol, turn, to);
                if (promoted != null) {
                    piece = promoted;
                }
            }
        }

        removePiece(from);
        setPiece(to, piece);

        lastMoveCells.clear();
        lastMoveCells.add(from);
        lastMoveCells.add(to);
        turn = turn.opposite();
    }

    public Cell enPassant(Cell from, Cell to) {
        if (lastMoveCells.size() >= 2) {
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

    private Piece getKingByColor(Color color) {
        Set<Piece> pieces = getPiecesByColor(color);
        for (Piece piece : pieces) {
            if (piece instanceof King) {
                return piece;
            }
        }
        return null;
    }

    public boolean isCheck() {
        Set<Piece> pieces = getPiecesByColor(turn.opposite());
        Piece king = getKingByColor(turn);
        if (king != null) {
            for (Piece piece : pieces) {
                Set<Cell> cells = piece.getAvailableCells(this, true);
                if (cells.contains(king.cell)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDeadPosition() {
        boolean isDeadPosition = true;

        int whiteKnights = 0;
        Set<Color> whiteBishops = new HashSet<>();
        int blackKnights = 0;
        Set<Color> blackBishops = new HashSet<>();

        for (Piece piece : pieces.values()) {
            if (piece instanceof Pawn || piece instanceof Rook || piece instanceof Queen) {
                isDeadPosition = false;
                break;
            } else if (piece instanceof Knight) {
                if (piece.color == Color.WHITE) {
                    if (whiteKnights > 0 || !whiteBishops.isEmpty()) {
                        isDeadPosition = false;
                        break;
                    }
                    whiteKnights++;
                } else {
                    if (blackKnights > 0 || !blackBishops.isEmpty()) {
                        isDeadPosition = false;
                        break;
                    }
                    blackKnights++;
                }
            } else if (piece instanceof Bishop) {
                if (piece.color == Color.WHITE) {
                    if (whiteKnights > 0 || (!whiteBishops.isEmpty() && !whiteBishops.contains(piece.cell.color))) {
                        isDeadPosition = false;
                        break;
                    }
                    whiteBishops.add(piece.cell.color);
                } else {
                    if (blackKnights > 0 || (!blackBishops.isEmpty() && !blackBishops.contains(piece.cell.color))) {
                        isDeadPosition = false;
                        break;
                    }
                    blackBishops.add(piece.cell.color);
                }
            }
        }

        return isDeadPosition;
    }

    public boolean isThereAvailableMoves() {
        Set<Piece> pieces = getPiecesByColor(turn);
        for (Piece piece : pieces) {
            Set<Cell> cells = piece.getAvailableCells(this);
            if (!cells.isEmpty()) {
                return true;
            }
        }
        return false;
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
                    Piece piece = Piece.fromFEN(fenChar, cell);
                    if (piece != null) {
                        board.setPiece(cell, piece);
                    }
                    file++;
                }
            }
        }

        if (parts[1].equals("b")) {
            board.turn = Color.BLACK;
        }

        if (parts[2].contains("K")) {
            Piece king = board.getPiece(new Cell(File.E, 1));
            Piece rook = board.getPiece(new Cell(File.H, 1));
            if (king instanceof King && king.color == Color.WHITE && rook instanceof Rook && rook.color == Color.WHITE) {
                board.castlings.add(new Cell(File.G, 1));
            }
        }
        if (parts[2].contains("Q")) {
            Piece king = board.getPiece(new Cell(File.E, 1));
            Piece rook = board.getPiece(new Cell(File.A, 1));
            if (king instanceof King && king.color == Color.WHITE && rook instanceof Rook && rook.color == Color.WHITE) {
                board.castlings.add(new Cell(File.C, 1));
            }
        }
        if (parts[2].contains("k")) {
            Piece king = board.getPiece(new Cell(File.E, 8));
            Piece rook = board.getPiece(new Cell(File.H, 8));
            if (king instanceof King && king.color == Color.BLACK && rook instanceof Rook && rook.color == Color.BLACK) {
                board.castlings.add(new Cell(File.G, 8));
            }
        }
        if (parts[2].contains("q")) {
            Piece king = board.getPiece(new Cell(File.E, 8));
            Piece rook = board.getPiece(new Cell(File.A, 8));
            if (king instanceof King && king.color == Color.BLACK && rook instanceof Rook && rook.color == Color.BLACK) {
                board.castlings.add(new Cell(File.C, 8));
            }
        }

        if (parts[3].length() == 2) {
            char fileChar = parts[3].charAt(0);
            char rankChar = parts[3].charAt(1);
            if (Character.isLetter(fileChar) && Character.isDigit(rankChar)) {
                File file = File.fromChar(fileChar);
                int rank = Character.getNumericValue(rankChar);
                if (file != null) {
                    if (board.turn == Color.WHITE && rank == 6) {
                        Piece piece = board.getPiece(new Cell(file, rank - 1));
                        if (piece instanceof Pawn) {
                            board.lastMoveCells.add(new Cell(file, rank + 1));
                            board.lastMoveCells.add(piece.cell);
                        }
                    }
                    if (board.turn == Color.BLACK && rank == 3) {
                        Piece piece = board.getPiece(new Cell(file, rank + 1));
                        if (piece instanceof Pawn) {
                            board.lastMoveCells.add(new Cell(file, rank - 1));
                            board.lastMoveCells.add(piece.cell);
                        }
                    }
                }
            }
        }

        if (parts[4].matches("\\d+")) {
            board.halfmoveClock = Integer.parseInt(parts[4]);
        }

        if (parts[5].matches("\\d+")) {
            board.fullmoveNumber = Integer.parseInt(parts[5]);
        }

        return board;
    }

    public static String toFEN(Board board) {
        StringBuilder fen = new StringBuilder();
        for (int rank = 8; rank >= 1; rank--) {
            int emptyCellsCount = 0;
            for (File file : File.values()) {
                Cell cell = new Cell(file, rank);
                Piece piece = board.getPiece(cell);
                if (piece == null) {
                    emptyCellsCount++;
                } else {
                    if (emptyCellsCount > 0) fen.append(emptyCellsCount);
                    fen.append(piece.color == Color.BLACK ? piece.code.toLowerCase() : piece.code);
                    emptyCellsCount = 0;
                }
            }
            if (emptyCellsCount > 0) fen.append(emptyCellsCount);
            if (rank > 1) fen.append("/");
        }
        fen.append(" ").append(board.turn == Color.WHITE ? "w" : "b").append(" ");

        if (board.castlings.isEmpty()) {
            fen.append("-");
        } else {
            if (board.castlings.contains(new Cell(File.G, 1))) {
                fen.append("K");
            }
            if (board.castlings.contains(new Cell(File.C, 1))) {
                fen.append("Q");
            }
            if (board.castlings.contains(new Cell(File.G, 8))) {
                fen.append("k");
            }
            if (board.castlings.contains(new Cell(File.C, 8))) {
                fen.append("q");
            }
        }

        String enPassant = "-";
        if (board.lastMoveCells.size() >= 2) {
            Piece piece = board.getPiece(board.lastMoveCells.get(1));
            int fromRank = board.lastMoveCells.getFirst().rank;
            if (piece instanceof Pawn && (
                    piece.color == Color.WHITE && piece.cell.rank == 4 && fromRank == 2 ||
                    piece.color == Color.BLACK && piece.cell.rank == 5 && fromRank == 7)
            ) {
                Piece enemyPiece = board.getPiece(new Cell(File.values()[piece.cell.file.ordinal() - 1] , piece.cell.rank));
                if (enemyPiece instanceof Pawn && enemyPiece.color == piece.color.opposite()) {
                    enPassant = piece.cell.file.name().toLowerCase() + ((piece.cell.rank + fromRank) / 2);
                } else {
                    enemyPiece = board.getPiece(new Cell(File.values()[piece.cell.file.ordinal() + 1] , piece.cell.rank));
                    if (enemyPiece instanceof Pawn && enemyPiece.color == piece.color.opposite()) {
                        enPassant = piece.cell.file.name().toLowerCase() + ((piece.cell.rank + fromRank) / 2);
                    }
                }
            }
        }
        fen.append(" ").append(enPassant).append(" ").append(board.halfmoveClock).append(" ").append(board.fullmoveNumber);

        return fen.toString();
    }
}
