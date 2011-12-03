package edu.berkeley.cs160.teamk;


public class Task {
	
	int id;
	String name;
	int points;
	int timesFlagged;
	int timesDeclined;
	int timesAccepted;
	
	
	public Task() {
		id = -1;
		name = "";
		points = 0;
		timesFlagged = 0;
		timesDeclined = 0;
		timesAccepted = 0;
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