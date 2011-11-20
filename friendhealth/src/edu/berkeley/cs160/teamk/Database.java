package edu.berkeley.cs160.teamk;

import java.util.Random;

public class Database {
	
	final int MAXCOUNT = 4;
	Task[] tasks = new Task[MAXCOUNT];
	int count = 0;
	Random randomGenerator = new Random();
	
	public Database() {	
		tasks[0] = new Task(1, "Eat an Apple", 2, 0,0,0);
		tasks[1] = new Task(2, "Go Swimming", 32, 0,0,0);
		tasks[2] = new Task(3, "Do some Gardening", 23, 0,0,0);
		tasks[3] = new Task(4, "Walk your Dog", 895, 0,0,0);
	}
	
	public Task getTask() {	
		Task result = tasks[count];
		if (count == MAXCOUNT - 1) {
			count = 0;
		} else {
			count++;
		}
		return result;
	}
	
	public Task generateTask() {
		return tasks[randomGenerator.nextInt(MAXCOUNT)-1];
	}

}
