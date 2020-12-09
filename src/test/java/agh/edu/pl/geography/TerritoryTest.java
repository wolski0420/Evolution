package agh.edu.pl.geography;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

public class TerritoryTest {
    @Test
    public void sizeAndBounds(){
        // given
        Territory territory = new Territory(new Point(0,0), new Point(4,2));
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
        Assertions.assertEquals(territory.getLengthX(),5);
        Assertions.assertEquals(territory.getLengthY(),3);
        Assertions.assertTrue(territory.isInBounds(point1));
        Assertions.assertTrue(territory.isInBounds(point2));
        Assertions.assertFalse(territory.isInBounds(point3));
        Assertions.assertFalse(territory.isInBounds(point4));
        Assertions.assertFalse(territory.isInBounds(point5));
        Assertions.assertFalse(territory.isInBounds(point6));
        Assertions.assertTrue(territory.isInBounds(point7));
        Assertions.assertFalse(territory.isInBounds(point8));
        Assertions.assertFalse(territory.isInBounds(point9));
    }

    @Test
    public void randPlant(){
        // given
        Point point = new Point(0,0);
        Territory territory = new Territory(point, point);

        // when
        territory.randPlant(Collections.singleton(point));
        boolean result1 = territory.isOverGrown(point);
        territory.randPlant(Collections.emptySet());
        boolean result2 = territory.isOverGrown(point);

        // then
        Assertions.assertFalse(result1);
        Assertions.assertTrue(result2);
    }

    @Test
    public void removePlant(){
        // given
        Point point = new Point(0,0);
        Territory territory = new Territory(point, point);

        // when
        territory.randPlant(Collections.singleton(point));
        boolean before = territory.isOverGrown(point);
        territory.removePlant(new Point(0,0));
        boolean between = territory.isOverGrown(point);
        territory.randPlant(Collections.emptySet());
        boolean after = territory.isOverGrown(point);

        // then
        Assertions.assertFalse(before);
        Assertions.assertFalse(between);
        Assertions.assertTrue(after);
    }

    // TESTS WITH ZONE INSIDE

    @Test
    public void sizeAndBoundsZones(){
        // given
        Jungle jungle = new Jungle(new Point(1,1), new Point(3,3));
        Territory territory = new Territory(new Point(0,0), new Point(4,4), jungle);
        Point point1 = new Point(0,0);
        Point point2 = new Point(4,4);
        Point point3 = new Point(5,1);
        Point point4 = new Point(1,5);
        Point point5 = new Point(-1,0);
        Point point6 = new Point(0,-1);
        Point point7 = new Point(2,2);
        Point point8 = new Point(7,7);
        Point point9 = new Point(-3,-3);
        Point point10 = new Point(0,1);

        // when

        // then
        Assertions.assertEquals(territory.getLengthX(),5);
        Assertions.assertEquals(territory.getLengthY(),5);
        Assertions.assertTrue(territory.isInBounds(point1));
        Assertions.assertTrue(territory.isInBounds(point2));
        Assertions.assertFalse(territory.isInBounds(point3));
        Assertions.assertFalse(territory.isInBounds(point4));
        Assertions.assertFalse(territory.isInBounds(point5));
        Assertions.assertFalse(territory.isInBounds(point6));
        Assertions.assertTrue(territory.isInBounds(point7));
        Assertions.assertFalse(territory.isInBounds(point8));
        Assertions.assertFalse(territory.isInBounds(point9));
        Assertions.assertTrue(territory.isInBounds(point10));
    }

    @Test
    public void randPlantZones(){
        // given
        Point point1 = new Point(0,0);
        Point point2 = new Point(0,1);
        Jungle jungle = new Jungle(new Point(0,1), new Point(0,1));
        Territory territory = new Territory(new Point(0,0), new Point(0,1), jungle);

        // when
        territory.randPlant(Set.of(point1, point2));
        boolean result1 = territory.isOverGrown(point1);
        boolean result2 = territory.isOverGrown(point2);
        territory.randPlant(Collections.emptySet());
        boolean result3 = territory.isOverGrown(point1);
        boolean result4 = territory.isOverGrown(point2);

        // then
        Assertions.assertFalse(result1);
        Assertions.assertFalse(result2);
        Assertions.assertTrue(result3);
        Assertions.assertTrue(result4);
    }

    @Test
    public void removePlantZones(){
        // given
        Point point1 = new Point(0,0);
        Point point2 = new Point(0,1);
        Jungle jungle = new Jungle(new Point(0,1), new Point(0,1));
        Territory territory = new Territory(new Point(0,0), new Point(0,1), jungle);

        // when
        territory.randPlant(Set.of(point1, point2));
        boolean before1 = territory.isOverGrown(point1);
        boolean before2 = territory.isOverGrown(point1);
        territory.removePlant(point1);
        territory.removePlant(point2);
        boolean between1 = territory.isOverGrown(point1);
        boolean between2 = territory.isOverGrown(point2);
        territory.randPlant(Collections.emptySet());
        boolean after1 = territory.isOverGrown(point1);
        boolean after2 = territory.isOverGrown(point1);

        // then
        Assertions.assertFalse(before1);
        Assertions.assertFalse(before2);
        Assertions.assertFalse(between1);
        Assertions.assertFalse(between2);
        Assertions.assertTrue(after1);
        Assertions.assertTrue(after2);
    }
}

