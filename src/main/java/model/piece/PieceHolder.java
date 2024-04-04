package model.piece;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import model.piece.role.Role;
import model.piece.role.Square;
import model.position.Position;
import model.position.Route;

public class PieceHolder {
    private Role role;

    public PieceHolder(Role role) {
        this.role = role;
    }

    public Route findRoute(Position source, Position destination) {
        return this.role.findDirectRoute(source, destination);
    }

    public void moveToDestination(final List<PieceHolder> pieceHoldersInRoute) {
        PieceHolder destination = new LinkedList<>(pieceHoldersInRoute).getLast();
        if (canMoveThroughRoute(pieceHoldersInRoute)) {
            destination.role = this.role;
            leave();
            return;
        }
        throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
    }

    public boolean canMoveThroughRoute(final List<PieceHolder> pieceHoldersInRoute) {
        Deque<PieceHolder> pieceHolders = new ArrayDeque<>(pieceHoldersInRoute);
        PieceHolder destination = Objects.requireNonNull(pieceHolders.pollLast());
        return pieceHolders.stream()
                .noneMatch(PieceHolder::isOccupied) && role.canCapture(destination.role);
    }

    public boolean isKing() {
        return role.isKing();
    }

    private void leave() {
        this.role = new Square();
    }

    public boolean hasSameColor(Color color) {
        return this.role.isSameColor(color);
    }

    private boolean isOccupied() {
        return this.role.isOccupied();
    }

    public PieceHolder copy() {
        return new PieceHolder(this.role);
    }

    public double score(boolean hasPawn) {
        return this.role.score(hasPawn);
    }

    public Role getRole() {
        return role;
    }

    public Color getColor() {
        return role.getColor();
    }
}
