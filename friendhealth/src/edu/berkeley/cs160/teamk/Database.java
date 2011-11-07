package edu.berkeley.cs160.teamk;

import java.util.Random;

public class Database {
	
	final int MAXCOUNT = 7;
	Task[] tasks = new Task[7];
	int count = 0;
	Random randomGenerator = new Random();
	
	public Database()
	{	
		tasks[0] = new Task("Eat an Apple", 2, "Go eat an apple dude", false);
		tasks[1] = new Task("Go Swimming", 72, "The pool is open now, you should go get wet", false);
		tasks[2] = new Task("Do some Gardening", 33, "Your plants need some TLC", false);
		tasks[3] = new Task("Walk your Dog", 895, "Your dog wants to walk", false);
		tasks[4] = new Task("Run a Million Miles", 1, "You have two minutes to complete this task", false);
		tasks[5] = new Task("Eat hella Big Macs", 456, "Micky D's needs your money", false);
		tasks[6] = new Task("Gym Time", 20, "Go to gym and do some exercise", false);
	}
	
	public Task getTask()
	{	
		Task result = tasks[count];
		if (count == MAXCOUNT)
		{
			count = 0;
		}
		else
		{
			count++;
		}
		return result;
	}
	
	public Task generateTask()
	{
		
		return tasks[randomGenerator.nextInt(MAXCOUNT)-1];
	}

}
