package ru.giv13.chess;

import ru.giv13.chess.piece.Piece;

import java.util.HashSet;
import java.util.Set;

public class Game {
    private final Board board;
    private final BoardRenderer renderer = new BoardRenderer();

    public Game(Board board) {
        this.board = board;
    }

    public void gameLoop() {
        boolean isWhiteTurn = true;
        boolean check = true;
        Set<Cell> lastMoveCells = new HashSet<>();
        while (true) {
            renderer.render(board, lastMoveCells);
            if (check) System.out.println(BoardRenderer.SUCCESS_TEXT_COLOR + "The game is started" + BoardRenderer.RESET_COLOR);
            check = false;

            Cell from = Input.inputPieceMoveFrom(isWhiteTurn, board);
            lastMoveCells.add(from);
            Piece piece = board.getPiece(from);
            Set<Cell> availableCells = piece.getAvailableCells(board);
            renderer.render(board, lastMoveCells, availableCells);

            Cell to = Input.inputPieceMoveTo(isWhiteTurn, availableCells);
            lastMoveCells.clear();
            lastMoveCells.add(from);
            lastMoveCells.add(to);
            board.movePiece(from, to);

            isWhiteTurn = !isWhiteTurn;
        }
    }
}
