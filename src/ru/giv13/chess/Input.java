package ru.giv13.chess;

import ru.giv13.chess.piece.Piece;

import java.util.Scanner;
import java.util.Set;

public class Input {
    private static final Scanner scanner = new Scanner(System.in);

    private static Cell inputCell() {
        while (true) {
            String line = scanner.nextLine();

            if (line.length() != 2) {
                error("Invalid coordinates format");
                continue;
            }

            char fileChar = line.charAt(0);
            char rankChar = line.charAt(1);
            if (!Character.isLetter(fileChar) || !Character.isDigit(rankChar)) {
                error("Invalid coordinates format");
                continue;
            }

            File file = File.fromChar(fileChar);
            if (file == null) {
                error("Invalid coordinates format");
                continue;
            }

            int rank = Character.getNumericValue(rankChar);
            if (rank < 1 || rank > 8) {
                error("Invalid coordinates format");
                continue;
            }

            return new Cell(file, rank);
        }
    }

    public static Cell inputPieceMoveFrom(Board board) {
        boolean check = true;
        while (true) {
            if (check) System.out.println("[" + (board.turn == Color.WHITE ? "WHITE" : "BLACK") +
                    "] Enter the coordinates of the piece you want to move to");
            check = false;

            Cell cell = inputCell();
            Piece piece = board.getPiece(cell);
            if (piece == null) {
                error("The cell is empty");
                continue;
            }

            if (piece.color != board.turn) {
                error("The piece is not yours");
                continue;
            }

            Set<Cell> availableCells = piece.getAvailableCells(board);
            if (availableCells.isEmpty()) {
                error("The piece doesn't have available moves");
                continue;
            }

            board.lastMoveCells.add(cell);
            return cell;
        }
    }

    public static Cell inputPieceMoveTo(Color turn, Set<Cell> cells) {
        boolean check = true;
        while (true) {
            if (check) System.out.println("[" + (turn == Color.WHITE ? "WHITE" : "BLACK") +
                    "] Enter the coordinates of the move");
            check = false;
            Cell cell = inputCell();
            if (!cells.contains(cell)) {
                error("The cell isn't available for move");
                continue;
            }

            return cell;
        }
    }

    public static char inputPiecePromoting(Color turn) {
        boolean check = true;
        while (true) {
            if (check) System.out.println("[" + (turn == Color.WHITE ? "WHITE" : "BLACK") +
                    "] What piece does a pawn promote to: Q, R, N or B?");
            check = false;

            String line = scanner.nextLine();
            if (!line.matches("(?i)[QRNB]")) {
                error("Invalid symbol");
                continue;
            }

            return line.charAt(0);
        }
    }

    private static void error (String error) {
        System.out.println(BoardRenderer.ERROR_TEXT_COLOR + error + BoardRenderer.RESET_COLOR);
    }
}
