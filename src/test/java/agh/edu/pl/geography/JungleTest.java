package agh.edu.pl.geography;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class JungleTest {
    @Test
    public void sizeAndBounds(){
        // given
        Jungle jungle = new Jungle(new Point(0,0), new Point(4,2));
        Point point1 = new Point(0,0);
        Point point2 = new Point(4,2);
        Point point3 = new Point(5,1);
        Point point4 = new Point(1,3);
        Point point5 = new Point(-1,0);
        Point point6 = new Point(0,-1);
        Point point7 = new Point(2,1);
        Point point8 = new Point(7,7);
        Point point9 = new Point(-3,-3);

        // when

        // then
        Assertions.assertEquals(jungle.getLengthX(),5);
        Assertions.assertEquals(jungle.getLengthY(),3);
        Assertions.assertTrue(jungle.isInBounds(point1));
        Assertions.assertTrue(jungle.isInBounds(point2));
        Assertions.assertFalse(jungle.isInBounds(point3));
        Assertions.assertFalse(jungle.isInBounds(point4));
        Assertions.assertFalse(jungle.isInBounds(point5));
        Assertions.assertFalse(jungle.isInBounds(point6));
        Assertions.assertTrue(jungle.isInBounds(point7));
        Assertions.assertFalse(jungle.isInBounds(point8));
        Assertions.assertFalse(jungle.isInBounds(point9));
    }

    @Test
    public void randPlant(){
        // given
        Point point = new Point(0,0);
        Jungle jungle = new Jungle(point, point);

        // when
        jungle.randPlant(Collections.singleton(point));
        boolean result1 = jungle.isOverGrown(point);
        jungle.randPlant(Collections.emptySet());
        boolean result2 = jungle.isOverGrown(point);

        // then
        Assertions.assertFalse(result1);
        Assertions.assertTrue(result2);
    }

    @Test
    public void removePlant(){
        // given
        Point point = new Point(0,0);
        Jungle jungle = new Jungle(point, point);

        // when
        jungle.randPlant(Collections.singleton(point));
        boolean before = jungle.isOverGrown(point);
        jungle.removePlant(new Point(0,0));
        boolean between = jungle.isOverGrown(point);
        jungle.randPlant(Collections.emptySet());
        boolean after = jungle.isOverGrown(point);

        // then
        Assertions.assertFalse(before);
        Assertions.assertFalse(between);
        Assertions.assertTrue(after);
    }
}
