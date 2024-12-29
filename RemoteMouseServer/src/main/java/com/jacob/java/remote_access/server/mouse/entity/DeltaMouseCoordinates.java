package com.jacob.java.remote_access.server.mouse.entity;

/**
 * Represents the change in the coordinates of the mouse (the Android device)
 */
public class DeltaMouseCoordinates {
	private double dx = 0;    // Positive: to the right; Negative: to the left
	private double dy = 0;    // Positive: downwards; Negative: upwards
	
	public DeltaMouseCoordinates(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * @return the dx
	 */
	public double getDx() {
		return dx;
	}

	/**
	 * @return the dy
	 */
	public double getDy() {
		return dy;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o  instanceof DeltaMouseCoordinates)) {
			return false;
		}
		
		DeltaMouseCoordinates instance = (DeltaMouseCoordinates) o;
		
		return instance.getDx() == getDx() && instance.getDy() == getDy();
	}
}
