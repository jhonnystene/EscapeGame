/*
 * Line.java
 * 
 * Makes storing lines somewhat nicer.
 */

package com.asuscomm.johnnystene.infinity;

public class Line {
	int x1;
	int y1;
	int x2;
	int y2;
	
	int rise;
	int run;
	int slope;
	int yIntercept;
	
	public Line(int setX1, int setY1, int setX2, int setY2) {
		x1 = setX1;
		y1 = setY1;
		x2 = setX2;
		y2 = setY2;
		
		rise = y2 - y1;
		run = x2 - x1;
		if(run == 0) slope = 25565;
		else slope = rise / run;
		// y = mx + b; so b = y - mx
		yIntercept = y1 - (slope * x1);
	}
}
