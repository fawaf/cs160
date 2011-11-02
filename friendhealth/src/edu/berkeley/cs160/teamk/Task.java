package edu.berkeley.cs160.teamk;

public class Task {
	
	String name = "";
	int points = 0;
	String description = "";
	boolean isFlagged = false;
	
	public Task(String name, int points, String description, boolean isFlagged){
		this.name = name;
		this.points = points;
		this.description = description;
		this.isFlagged = isFlagged;	
	}

}
