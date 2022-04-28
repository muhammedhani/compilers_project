import java.util.List;
import java.util.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
public class Output {
	static Set<Integer> set = new TreeSet<>();
	static void __write(String string) throws Exception {
    	String outputFileName = "visited_blocks.txt";
    	FileOutputStream outputFile = new FileOutputStream(outputFileName, false);
    	BufferedOutputStream buffer = new BufferedOutputStream(outputFile);
    	// buffer can only write with characters
    	byte[] bytes = string.getBytes();
    	buffer.write(bytes);
    	buffer.close();
	}
	public static void main (String[] args) throws Exception{
		int block1 = 0;
			block1 = 1;
			set.add(block1);
		int x = 5, y = 6;
		int block2 = 0;
		for(int i = 0; i < 5;i++){
			block2 = 2;
			set.add(block2);
			System.out.println("For Loop");
			int block3 = 0;
			while(x < y){
				block3 = 3;
				set.add(block3);
				x++;
				System.out.println("while loop");
				int block4 = 0;
				do {
					block4 = 4;
					set.add(block4);
					System.out.println("do while loop");
				} while(x == 0);
			}
		}
		x = 5;
		y = 6;
		int block5 = 0;
		if(x < y){
			block5 = 5;
			set.add(block5);
			System.out.println("if statement");
		}
		else {
			int block6 = 0;
			block6 = 6;
			set.add(block6);
			System.out.println("else statement");
		}
		int block7 = 0;
		if(x == 5){
			block7 = 7;
			set.add(block7);
			int block8 = 0;
			if(x > 5){
				block8 = 8;
				set.add(block8);
				System.out.println("x>5");
			}
			else {
				int block9 = 0;
				block9 = 9;
				set.add(block9);
				System.out.println("x<5");
			}
		}
		x = 6;
		y = 5;
		int block10 = 0;
		if(x > y){
			block10 = 10;
			set.add(block10);
			int block11 = 0;
			if(y > 4){
				block11 = 11;
				set.add(block11);
				int block12 = 0;
				if(x == 6){
					block12 = 12;
					set.add(block12);
					int block13 = 0;
					if(x == 4){
						block13 = 13;
						set.add(block13);
						System.out.println("x == 4");
					}
				}
			}
			else {
				int block14 = 0;
				block14 = 14;
				set.add(block14);
				System.out.println("y > x");
			}
		}
		foo();
		String visited = "";
		for(int i: set){
			System.out.println("Block#"+i+" is visited");
			visited += i+"\n";
		}
		__write(visited);
	}
	static void foo () throws Exception{
		int block16 = 0;
			block16 = 16;
			set.add(block16);
		int x = 1;
		switch(x){
			case 0: 
			int block17 = 0;
			block17 = 17;
			set.add(block17);
				System.out.println("case 0");
			break;
			case 1: 
			int block18 = 0;
			block18 = 18;
			set.add(block18);
				System.out.println("case 1");
				int block19 = 0;
				if(x == 1){
					block19 = 19;
					set.add(block19);
					System.out.println("if in case 1");
				}
			break;
			default: 
			int block20 = 0;
			block20 = 20;
			set.add(block20);
				System.out.println("default");
			break;
		}
		System.out.println("method");
	}
}
