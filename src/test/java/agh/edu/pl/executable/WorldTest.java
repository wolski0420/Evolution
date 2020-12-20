package agh.edu.pl.executable;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.biology.Genom;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.world.World;
import com.google.common.collect.ArrayListMultimap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.mock;

public class WorldTest {
    @Test
    public void init(){
        // given
        Zone map1 = Creator.createMap(1,2,0.5);
        Zone map2 = Creator.createMap(1,1,0);
        Creator.setEnergy(10,1,3);
        World world1 = Creator.createWorld(map1);
        World world2 = Creator.createWorld(map2);

        // when
        world1.init(0,2);
        world2.init(1,0);

        // then
        Assertions.assertTrue(map1.isOverGrown(new Point(0,0)));
        Assertions.assertTrue(map1.isOverGrown(new Point(0,1)));
        Assertions.assertTrue(world2.isOccupied(new Point(0,0)));
    }

    @Test
    public void removeCorpses(){
        // given
        Creator.setEnergy(0,1,0);
        Zone map = Creator.createMap(1,1,0.5);
        World world = Creator.createWorld(map);

        // when
        world.init(1, 0);
        boolean occupied1 = world.isOccupied(new Point(0,0));
        world.nextDay();
        boolean occupied2 = world.isOccupied(new Point(0,0));
        world.nextDay();
        boolean occupied3 = world.isOccupied(new Point(0,0));

        // then
        Assertions.assertTrue(occupied1);
        Assertions.assertTrue(occupied2);
        Assertions.assertFalse(occupied3);
    }

    @Test
    public void animalsEat(){
        // given
        Creator.setEnergy(3,1,4);
        Zone zone = Creator.createMap(1,1,0.5);
        World world = Creator.createWorld(zone);
        Point point = new Point(0,0);
        Animal animal1 = new Animal(world, point, 3, new Genom());
        Animal animal2 = new Animal(world, point, 8, new Genom());
        Animal animal3 = new Animal(world, point, 12, new Genom());

        // when
        try{
            Method method = World.class.getDeclaredMethod("animalsEat");
            method.setAccessible(true);

            Field field = World.class.getDeclaredField("mapOfAnimals");
            field.setAccessible(true);
            ArrayListMultimap<Point, Animal> map = (ArrayListMultimap<Point, Animal>) field.get(world);

            map.put(point, animal1);
            method.invoke(world);

            zone.randPlant(Collections.emptySet());
            method.invoke(world);

            map.put(point, animal2);
            zone.randPlant(Collections.emptySet());
            method.invoke(world);

            zone.randPlant(Collections.emptySet());
            map.put(point, animal3);
            method.invoke(world);
        } catch(NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            e.printStackTrace();
        }

        // then
        Assertions.assertEquals(7, animal1.getEnergy());
        Assertions.assertEquals(14, animal2.getEnergy());
        Assertions.assertEquals(14, animal3.getEnergy());
    }

    @Test
    public void copulation(){
        //given
        Creator.setEnergy(4,1,3);
        Zone zone = Creator.createMap(1,1,0.5);
        World world = Creator.createWorld(zone);
        Point point = new Point(0,0);
        Animal animal1 = new Animal(world, point, 4, new Genom());
        Animal animal2 = new Animal(world, point, 16, new Genom());
        Animal animal3 = new Animal(world, point, 12, new Genom());
        int size = 0, size1 = 0, size2 = 0, size3 = 0;

        // when
        try{
            Method method = World.class.getDeclaredMethod("copulation");
            method.setAccessible(true);

            Field field = World.class.getDeclaredField("mapOfAnimals");
            field.setAccessible(true);
            ArrayListMultimap<Point, Animal> map = (ArrayListMultimap<Point, Animal>) field.get(world);

            size = map.size();
            map.put(point, animal1);

            method.invoke(world);
            size1 = map.size();

            map.put(point, animal2);
            method.invoke(world);
            size2 = map.size();

            map.put(point, animal3);
            method.invoke(world);
            size3 = map.size();
        } catch(NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            e.printStackTrace();
        }

        ArrayList<Animal> list = world.getListOfAnimals();

        // then
        Assertions.assertEquals(0, size);
        Assertions.assertEquals(1, size1);
        Assertions.assertEquals(3, size2);
        Assertions.assertNotNull(list.get(0));
        Assertions.assertEquals(5, list.get(0).getEnergy());
        Assertions.assertEquals(5, size3);
        Assertions.assertNotNull(list.get(1));
        Assertions.assertEquals(6, list.get(1).getEnergy());
    }

    @Test
    public void getCorrectPosition(){
        // given
        Creator.setEnergy(10,1,3);
        Zone map = Creator.createMap(5,5,0.25);
        World world1 = Creator.createWorld(map);
        Point point1 = new Point(-1,0);
        Point point2 = new Point(-1,-1);
        Point point3 = new Point(0,-1);
        Point point4 = new Point(0,0);
        Point point5 = new Point(4,4);
        Point point6 = new Point(4,5);
        Point point7 = new Point(5,4);
        Point point8 = new Point(5,5);
        Point point9 = new Point(2,2);

        // when

        // then
        Assertions.assertEquals(new Point(4,0), world1.getCorrectPosition(point1));
        Assertions.assertEquals(new Point(4,4), world1.getCorrectPosition(point2));
        Assertions.assertEquals(new Point(0,4), world1.getCorrectPosition(point3));
        Assertions.assertEquals(new Point(0,0), world1.getCorrectPosition(point4));
        Assertions.assertEquals(new Point(4,4), world1.getCorrectPosition(point5));
        Assertions.assertEquals(new Point(4,0), world1.getCorrectPosition(point6));
        Assertions.assertEquals(new Point(0,4), world1.getCorrectPosition(point7));
        Assertions.assertEquals(new Point(0,0), world1.getCorrectPosition(point8));
        Assertions.assertEquals(new Point(2,2), world1.getCorrectPosition(point9));
    }

    @Test
    public void getClosePosition(){
        // given
        Creator.setEnergy(10,1,3);
        Zone zone = Creator.createMap(5,5,0.25);
        World world = Creator.createWorld(zone);
        Point point = new Point(2,2);

        // when
        try{
            Field field = World.class.getDeclaredField("mapOfAnimals");
            field.setAccessible(true);
            ArrayListMultimap<Point, Animal> map = (ArrayListMultimap<Point, Animal>) field.get(world);
            map.put(new Point(1,1), mock(Animal.class));
            map.put(new Point(2,1), mock(Animal.class));
            map.put(new Point(3,2), mock(Animal.class));
            map.put(new Point(2,3), mock(Animal.class));
            map.put(new Point(3,3), mock(Animal.class));
            map.put(new Point(1,3), mock(Animal.class));
            map.put(new Point(3,1), mock(Animal.class));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        // then
        Assertions.assertEquals(new Point(1,2), world.getClosePosition(point));
    }

    @Test
    public void changePosition(){
        // given
        Creator.setEnergy(10,1,3);
        Zone zone = Creator.createMap(5,5,0.25);
        World world = Creator.createWorld(zone);
        Point location = new Point(1,1);
        Animal animal = new Animal(world, location, 5, mock(Genom.class));

        // when
        try{
            Field field = World.class.getDeclaredField("mapOfAnimals");
            field.setAccessible(true);
            ArrayListMultimap<Point, Animal> map = (ArrayListMultimap<Point, Animal>) field.get(world);
            map.put(location, animal);
        } catch(NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }

        animal.move();
        world.changePosition(location, animal);

        // then
        Assertions.assertTrue(world.isOccupied(new Point(1,2)));
    }
}
