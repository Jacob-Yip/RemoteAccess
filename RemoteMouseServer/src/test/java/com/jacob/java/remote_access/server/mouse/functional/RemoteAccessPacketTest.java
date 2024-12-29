package com.jacob.java.remote_access.server.mouse.functional;

import org.junit.Test;

import com.jacob.java.remote_access.server.mouse.entity.RemoteAccessPacket;

import junit.framework.TestCase;

public class RemoteAccessPacketTest extends TestCase {
	@Override
	public void setUp() {
		
	}
	
	@Override
	public void tearDown() {
		
	}
	
	@Test
	public void testParseString() {
		final String[] packetStrings = {
				"{button1:true,button2:false,button3:true,scroll:2,mouseDX:1.3,mouseDY:1.4,exit:true}"
		};
		final RemoteAccessPacket[] parsedPackets = {
				new RemoteAccessPacket(true, false, true, 2, 1.3, 1.4, true)
		};
		
		assertEquals(packetStrings.length, parsedPackets.length);
		
		for(int i = 0; i < packetStrings.length; i++) {
			String packetString = packetStrings[i];
			RemoteAccessPacket parsedPacket = parsedPackets[i];
			
			assertEquals(parsedPacket, RemoteAccessPacket.parseString(packetString));
		}
		
	}
	
	@Test
	public void testToString() {
		final String[] packetStrings = {
				"{button1:true,button2:false,button3:true,scroll:2,mouseDX:1.3,mouseDY:1.4,exit:true}"
		};
		final RemoteAccessPacket[] packets = {
				new RemoteAccessPacket(true, false, true, 2, 1.3, 1.4, true)
		};
		
		assertEquals(packetStrings.length, packets.length);
		
		for(int i = 0; i < packetStrings.length; i++) {
			String packetString = packetStrings[i];
			RemoteAccessPacket packet = packets[i];
			
			assertEquals(packetString, packet.toString());
		}
	}
}
