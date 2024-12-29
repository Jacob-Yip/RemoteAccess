package com.jacob.java.remote_access.server.mouse.functional;

import org.junit.Test;

import com.jacob.java.remote_access.server.mouse.entity.DeltaMouseCoordinates;

import junit.framework.TestCase;

public class DeltaMouseCoordinatesTest extends TestCase {
	@Override
	public void setUp() {
		
	}
	
	@Override
	public void tearDown() {
		
	}
	
	
	@Test
	public void testConstructor() {
		final double[] deltaXs = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		final double[] deltaYs = {10, 20, 30, 40, 50, 60, 70, 80, 90};
		
		assertEquals(deltaXs.length, deltaYs.length);
		
		for(int i = 0; i < deltaXs.length; i++) {
			double deltaX = deltaXs[i];
			double deltaY = deltaYs[i];
			
			DeltaMouseCoordinates mouseCoordinates = new DeltaMouseCoordinates(deltaX, deltaY);
			
			assertEquals(deltaX, mouseCoordinates.getDx());
			assertEquals(deltaY, mouseCoordinates.getDy());
		}
	}
}
