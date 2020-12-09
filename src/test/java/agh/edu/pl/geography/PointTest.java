package agh.edu.pl.geography;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointTest {
    @Test
    public void add(){
        // given
        Point point = new Point(3,3);

        // when
        Point point1 = point.add(new Point(1,3));
        Point point2 = point.add(new Point(-5,-1));

        // then
        Assertions.assertEquals(new Point(4,6), point1);
        Assertions.assertEquals(new Point(-2,2), point2);
    }

    @Test
    public void precedes(){
        // given
        Point subject = new Point(1,1);
        Point example1 = new Point(2,2);
        Point example2 = new Point(1,2);
        Point example3 = new Point(2,1);
        Point example4 = new Point(1,1);
        Point example5 = new Point(0,1);
        Point example6 = new Point(0,0);
        Point example7 = new Point(1,0);

        // when

        // then
        Assertions.assertTrue(subject.precedes(example1));
        Assertions.assertTrue(subject.precedes(example2));
        Assertions.assertTrue(subject.precedes(example3));
        Assertions.assertTrue(subject.precedes(example4));
        Assertions.assertFalse(subject.precedes(example5));
        Assertions.assertFalse(subject.precedes(example6));
        Assertions.assertFalse(subject.precedes(example7));
    }

    @Test
    public void follows(){
        // given
        Point subject = new Point(1,1);
        Point example1 = new Point(2,2);
        Point example2 = new Point(1,2);
        Point example3 = new Point(2,1);
        Point example4 = new Point(1,1);
        Point example5 = new Point(0,1);
        Point example6 = new Point(0,0);
        Point example7 = new Point(1,0);

        // when

        // then
        Assertions.assertFalse(subject.follows(example1));
        Assertions.assertFalse(subject.follows(example2));
        Assertions.assertFalse(subject.follows(example3));
        Assertions.assertTrue(subject.follows(example4));
        Assertions.assertTrue(subject.follows(example5));
        Assertions.assertTrue(subject.follows(example6));
        Assertions.assertTrue(subject.follows(example7));
    }
}
