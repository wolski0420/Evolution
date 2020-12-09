package agh.edu.pl;

import agh.edu.pl.executable.World;

public class Main {
    public static void main(String[] args) {
        World world = new World(8, 8, 10,1, 3, 0.5);
        world.init(5,5);
        while(true){
            world.nextDay();
            try{
                Thread.sleep(100);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
