package edu.berkeley.cs160.teamk;


public class Task {
	
	int id;
	String name;
	int points;
	int timesFlagged;
	int timesDeclined;
	int timesAccepted;
	
	
	public Task() {
		this(-1, "", 0, 0, 0, 0);
	}
	
	public Task(String name) {
		this(-1, name, 1, 0, 0, 0);
	}
	
	public Task (int id, String name, int points, int tF, int tD, int tA) {
		this.id = id;
		this.name = name;
		this.points = points;
		timesFlagged = tF;
		timesDeclined = tD;
		timesAccepted = tA;
	}
	
	public String toString() {
		return (name + " (" + points + " pts)");
	}
	
}