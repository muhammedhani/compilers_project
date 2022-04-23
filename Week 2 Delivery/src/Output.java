import java.util.List;
import java.util.*;
public class Output{
    public static void main (String[] args) {
        Set<Integer> set = new HashSet<>();
        int x = 5, y = 6;
        int block1 = 0;
        for(int i = 0; i < 5;i++){
            block1 = 1;
            set.add(block1);
            System.out.println("For Loop");
            int block2 = 0;
            while(x < y){
                block2 = 2;
                set.add(block2);
                x++;
                System.out.println("ya wa3dy ya wa3dy");
                int block3 = 0;
                do{
                    block3 = 3;
                    set.add(block3);
                    System.out.println("Bravo 3aleeeeeeeeek");
                }while(x == 0);
            }
        }
        x = 5;
        y = 6;
        int block4 = 0;
        if(x < y){
            block4 = 4;
            set.add(block4);
            System.out.println("De7k");
        }
        else{
            int block5 = 0;
            block5 = 5;
            set.add(block5);
            System.out.println("De7ken");
        }
        int block6 = 0;
        if(x == 5){
            block6 = 6;
            set.add(block6);
            int block7 = 0;
            if(x > 5){
                block7 = 7;
                set.add(block7);
                System.out.println("x>5");
            }
            else{
                int block8 = 0;
                block8 = 8;
                set.add(block8);
                System.out.println("x<5");
            }
        }
        x = 6;
        y = 5;
        int block9 = 0;
        if(x > y){
            block9 = 9;
            set.add(block9);
            int block10 = 0;
            if(y > 4){
                block10 = 10;
                set.add(block10);
                int block11 = 0;
                if(x == 6){
                    block11 = 11;
                    set.add(block11);
                    int block12 = 0;
                    if(x == 4){
                        block12 = 12;
                        set.add(block12);
                        System.out.println("Aykalam");
                    }
                }
            }
            else{
                int block13 = 0;
                block13 = 13;
                set.add(block13);
                System.out.println("Aykalam Tany");
            }
        }
        for(int i: set){
            if(i != 0)System.out.println("Block#"+i+" is visited");
        }
    }
}
