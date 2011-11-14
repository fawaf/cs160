package edu.berkeley.cs160.teamk;

import java.util.Random;

public class Database {
	
	final int MAXCOUNT = 21;
	Task[] tasks = new Task[MAXCOUNT];
	int count = 0;
	Random randomGenerator = new Random();
	
	public Database() {	
		tasks[0] = new Task("Eat an Apple", 2, "Go eat an apple dude", false);
		tasks[1] = new Task("Go Swimming", 32, "The pool is open now, you should go get wet", false);
		tasks[2] = new Task("Do some Gardening", 23, "Your plants need some TLC", false);
		tasks[3] = new Task("Walk your Dog", 895, "Your dog wants to walk", false);
		tasks[4] = new Task("Run a Million Miles", 1, "You have two minutes to complete this task", false);
		tasks[5] = new Task("Eat hella Big Macs", -15, "Micky D's needs your money", false);
		tasks[6] = new Task("Gym Time", 20, "Go to gym and do some exercise", false);
		tasks[7] = new Task("Play Basketball", 15, "", false);
		tasks[8] = new Task("Play Soccer", 12, "", false);
		tasks[9] = new Task("Community Service", 25, "", false);
		tasks[10] = new Task("Drink a glass of Water", 2, "", false);
		tasks[11] = new Task("Go For a Walk", 5, "", false);
		tasks[12] = new Task("Watch TV for 2 hours", -15, "", false);
		tasks[13] = new Task("Go Hiking", 35, "", false);
		tasks[14] = new Task("Tell your friend a joke", 6, "", false);
		tasks[15] = new Task("Eat Breakfast", 7, "", false);
		tasks[16] = new Task("Go Fishing", 14, "", false);
		tasks[17] = new Task("Do Cardio for 30 mins", 17, "", false);
		tasks[18] = new Task("null", -100, "", false);
		tasks[19] = new Task("none", -500, "", false);
		tasks[20] = new Task("no more! do not click again", -1000, "", false);
		tasks[21] = new Task("the system gonna crash", -9999, "", false);
	}
	
	public Task getTask() {	
		Task result = tasks[count];
		if (count == MAXCOUNT) {
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
