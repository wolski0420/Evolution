package agh.edu.pl.biology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;


public class GenomTest {
    private final Genom genom = new Genom();
    private final int[] genes = new int[32];
    private final Random random = new Random();

    @BeforeEach
    public void setGenes(){
        Arrays.fill(genes, random.nextInt(5));
    }

    @Test
    public void create() throws Exception {
        // given
        Field field = Genom.class.getDeclaredField("genes");
        Method method = Genom.class.getDeclaredMethod("repair");

        // when
        field.setAccessible(true);
        field.set(genom, genes);

        method.setAccessible(true);
        method.invoke(genom);

        // then
        int[] genes = (int [])field.get(genom);
        for(int gene=0; gene<8; gene++){
            int finalGene = gene;
            Assertions.assertTrue(Arrays.stream(genes).anyMatch(g -> g== finalGene));
        }
    }
}
