package agh.edu.pl.biology;

import agh.edu.pl.executable.World;
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
        Animal animal1 = new Animal(world, point, 0, genom);
        Animal animal2 = new Animal(world, point, -1, genom);
        Animal animal3 = new Animal(world, point, 1, genom);

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
        Animal animal = new Animal(world, point, 5, genom);

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
        Animal animal = new Animal(world, point, 5, genom);
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
        Animal animal = new Animal(world, point, 5, genom);
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
            animal.move(5);
            changed1 = (Point) field.get(animal);

            animal.rotate();
            animal.move(5);
            changed2 = (Point) field.get(animal);
        } catch(NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }

        // then
        Assertions.assertEquals(new Point(1,1), changed1);
        Assertions.assertEquals(new Point(1,1), changed2);
    }

    @Test
    public void copulate(){
        // given
        World world = Mockito.mock(World.class);
        Point point = Mockito.mock(Point.class);
        Genom genom = new Genom();
        Animal animal1 = new Animal(world, point, 8, genom);
        Animal animal2 = new Animal(world, point, 12, genom);

        // when
        when(world.getMinCopulateEnergy()).thenReturn(7);
        Animal newAnimal1 = Animal.copulate(animal1, animal2);
        Animal newAnimal2 = Animal.copulate(animal1, animal2);

        // then
        Assertions.assertNotNull(newAnimal1);
        Assertions.assertEquals(5, newAnimal1.getEnergy());
        Assertions.assertNull(newAnimal2);
        Assertions.assertEquals(6, animal1.getEnergy());
        Assertions.assertEquals(9, animal2.getEnergy());
    }
}
