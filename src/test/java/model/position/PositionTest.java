package model.position;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.direction.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    @DisplayName("미리 생성된 Position를 제외한 Position을 요구하였을 때, 예외를 던진다")
    void of_ShouldThrowException_WhenInputInvalidFileAndRank() {
        assertThrows(IllegalArgumentException.class, () -> Position.of(9, 9));
    }

    @Test
    @DisplayName("전달 받은 위치가 체스판 위에 존재하는 위치인지 판별한다.")
    void isAvailablePosition_ShouldReturnTrue_WhenAvailableFileAndRank() {
        Position position = Position.of(1, 1);

        assertAll( () -> {
            assertTrue(position.hasAvailableNextPositionToDirection(Direction.N));
            assertTrue(position.hasAvailableNextPositionToDirection(Direction.NE));
            assertTrue(position.hasAvailableNextPositionToDirection(Direction.E));
            assertFalse(position.hasAvailableNextPositionToDirection(Direction.NW));
            assertFalse(position.hasAvailableNextPositionToDirection(Direction.W));
            assertFalse(position.hasAvailableNextPositionToDirection(Direction.SW));
            assertFalse(position.hasAvailableNextPositionToDirection(Direction.S));
            assertFalse(position.hasAvailableNextPositionToDirection(Direction.SE));
        });
    }
}
