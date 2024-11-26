package ru.giv13.chess;

import ru.giv13.chess.piece.Piece;

import java.util.Set;

public class Game {
    private final Board board;
    private final BoardRenderer renderer = new BoardRenderer();

    public Game(Board board) {
        this.board = board;
    }

    public void gameLoop() {
        boolean check = true;
        while (true) {
            renderer.render(board);
            if (check) System.out.println(BoardRenderer.SUCCESS_TEXT_COLOR + "The game is started" + BoardRenderer.RESET_COLOR);
            check = false;

            Cell from = Input.inputPieceMoveFrom(board);
            Piece piece = board.getPiece(from);
            Set<Cell> availableCells = piece.getAvailableCells(board, Type.MOVE);
            renderer.render(board, availableCells);

            Cell to = Input.inputPieceMoveTo(board.isWhiteTurn, availableCells);
            board.movePiece(from, to);

            board.isWhiteTurn = !board.isWhiteTurn;
        }
    }
}
