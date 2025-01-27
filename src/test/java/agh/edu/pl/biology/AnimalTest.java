package agh.edu.pl.biology;

import agh.edu.pl.world.World;
import agh.edu.pl.geography.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;

import static org.mockito.Mockito.when;

public class AnimalTest {
    @Test
    public void isAlive(){
        // given
        World world = Mockito.mock(World.class);
        Point point = Mockito.mock(Point.class);
        Genom genom = Mockito.mock(Genom.class);
        Animal animal1 = new Animal(world, point, 0, genom, 0);
        Animal animal2 = new Animal(world, point, -1, genom, 0);
        Animal animal3 = new Animal(world, point, 1, genom, 0);

        // when

        // then
        Assertions.assertFalse(animal1.isAlive());
        Assertions.assertFalse(animal2.isAlive());
        Assertions.assertTrue(animal3.isAlive());
    }

    @Test
    public void eat(){
        // given
        World world = Mockito.mock(World.class);
        Point point = Mockito.mock(Point.class);
        Genom genom = Mockito.mock(Genom.class);
        Animal animal = new Animal(world, point, 5, genom, 0);

        // when
        animal.eat(7);

        // then
        Assertions.assertEquals(12, animal.getEnergy());
    }

    @Test
    public void rotate(){
        // given
        World world = Mockito.mock(World.class);
        Point point = Mockito.mock(Point.class);
        Genom genom = Mockito.mock(Genom.class);
        Animal animal = new Animal(world, point, 5, genom, 0);
        Orientation start = null, changed = null;

        // when
        when(genom.getRandomGene()).thenReturn(1);
        try{
            Field field = Animal.class.getDeclaredField("orientation");
            field.setAccessible(true);
            start = (Orientation) field.get(animal);
            animal.rotate();
            changed = (Orientation) field.get(animal);
        } catch(NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }

        // then
        Assertions.assertEquals(Orientation.NORTH, start);
        Assertions.assertEquals(Orientation.NORTH_EAST, changed);
    }

    @Test
    public void move(){
        // given
        World world = Mockito.mock(World.class);
        Point point = Mockito.mock(Point.class);
        Genom genom = Mockito.mock(Genom.class);
        Animal animal = new Animal(world, point, 5, genom, 0);
        Point start = new Point(0,0), changed1 = null, changed2 = null;

        // when
        when(genom.getRandomGene()).thenReturn(1);
        when(world.getCorrectPosition(new Point(1,1))).thenReturn(new Point(1,1));
        when(world.getCorrectPosition(new Point(2,1))).thenReturn(new Point(0,1));
        try{
            Field field = Animal.class.getDeclaredField("location");
            field.setAccessible(true);
            field.set(animal, start);

            animal.rotate();
            animal.move();
            changed1 = (Point) field.get(animal);

            animal.rotate();
            animal.move();
            changed2 = (Point) field.get(animal);
        } catch(NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }

        // then
        Assertions.assertEquals(new Point(1,1), changed1);
        Assertions.assertEquals(new Point(0,1), changed2);
    }

    @Test
    public void copulate(){
        // given
        World world = Mockito.mock(World.class);
        Point point = Mockito.mock(Point.class);
        Genom genom = new Genom();
        Animal animal1 = new Animal(world, point, 8, genom, 0);
        Animal animal2 = new Animal(world, point, 12, genom, 0);

        // when
        Animal newAnimal1 = Animal.copulate(animal1, animal2);
        Animal newAnimal2 = Animal.copulate(animal1, animal2);

        // then
        Assertions.assertNotNull(newAnimal1);
        Assertions.assertNotNull(newAnimal2);
        Assertions.assertEquals(5, newAnimal1.getEnergy());
        Assertions.assertEquals(3, newAnimal2.getEnergy());
        Assertions.assertEquals(5, animal1.getEnergy());
        Assertions.assertEquals(7, animal2.getEnergy());
    }
}
