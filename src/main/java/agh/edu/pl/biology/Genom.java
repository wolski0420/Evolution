package agh.edu.pl.biology;

import java.util.Arrays;
import java.util.Random;

public class Genom {
    private static final int typesOfGenes = 8;
    private static final int size = 32;
    private final int[] genes;
    private final Random random;

    public Genom() {
        this.genes = new int[size];
        this.random = new Random();
        createRandomGenes();
    }

    public Genom(Genom genom1, Genom genom2){
        this.genes = new int[size];
        this.random = new Random();
        mix(genom1, genom2);
    }

    private void createRandomGenes(){
        for(int i=0; i<size; i++)
            genes[i] = random.nextInt(8);

        repair();
    }

    private void mix(Genom genom1, Genom genom2){
        int firstSplit = random.nextInt(size-2) + 1;
        int secondSplit = random.nextInt(size-firstSplit-1) + firstSplit + 1;
        int[] bounds = new int[]{0,firstSplit, firstSplit, secondSplit, secondSplit, size};
        int firstGenomParts = random.nextInt(2) + 1;
        int secondGenomParts = 3 - firstGenomParts;

        Genom first, second;
        if(firstGenomParts < secondGenomParts) {
            first = genom1;
            second = genom2;
        }
        else {
            first = genom2;
            second = genom1;
        }

        int part = random.nextInt(3);
        for(int i=0; i<bounds.length; i+=2){
            if(2*part == i)
                for(int j=bounds[i]; j<bounds[i+1]; j++)
                    genes[j] = first.genes[j];
            else
                for(int j=bounds[i]; j<bounds[i+1]; j++)
                    genes[j] = second.genes[j];
        }

        repair();
    }

    private void repair(){
        int[] counts = new int[typesOfGenes];
        Arrays.fill(counts, 0);
        for(Integer gene : genes)
            counts[gene]++;

        for(int g=0; g<typesOfGenes; g++){
            if(counts[g] == 0){
                int gene = random.nextInt(typesOfGenes);
                while(counts[gene] <= 1){
                    gene = random.nextInt(typesOfGenes);
                }

                for(int i=0; i<size; i++){
                    if(genes[i] == gene){
                        counts[genes[i]]--;
                        genes[i] = g;
                        counts[g]++;
                        break;
                    }
                }
            }
        }


        Arrays.sort(genes);
    }

    public int getRandomGene(){
        return genes[random.nextInt(size)];
    }

    public void print(){
        System.out.println(Arrays.toString(genes));
    }
}
