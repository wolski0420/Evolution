package agh.edu.pl.biology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrientationTest {
    @Test
    public void rotate(){
        // given
        Orientation orientation1 = Orientation.EAST;
        Orientation orientation2 = Orientation.NORTH_WEST;

        // when

        // then
        Assertions.assertEquals(Orientation.SOUTH_WEST, orientation1.rotate(3));
        Assertions.assertEquals(Orientation.NORTH_EAST, orientation2.rotate(2));
    }
}
