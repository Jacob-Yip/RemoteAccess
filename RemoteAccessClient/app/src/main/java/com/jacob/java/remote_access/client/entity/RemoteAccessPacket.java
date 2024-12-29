package com.jacob.java.remote_access.client.entity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RemoteAccessPacket {
    private boolean isButton1Down = false;    // Left button
    private boolean isButton2Down = false;    // Scroll
    private boolean isButton3Down = false;    // Right button
    private int scroll = 0;    // Positive: go down; Negative: go up (in my computer)
    private DeltaMouseCoordinates mouseCoordinates;
    private boolean exit = false;


    /**
     * Can change the visibility to private to create a factory method
     * If so, need to update RemoteAccessPacketTest
     *
     * @param isButton1Down
     * @param isButton2Down
     * @param isButton3Down
     * @param scroll
     * @param mouseDX
     * @param mouseDY
     * @param exit
     */
    public RemoteAccessPacket(boolean isButton1Down, boolean isButton2Down, boolean isButton3Down, int scroll, double mouseDX, double mouseDY, boolean exit) {
        this.isButton1Down = isButton1Down;
        this.isButton2Down = isButton2Down;
        this.isButton3Down = isButton3Down;
        this.scroll = scroll;
        this.mouseCoordinates = new DeltaMouseCoordinates(mouseDX, mouseDY);
        this.exit = exit;
    }

    public static RemoteAccessPacket parseString(String packet) {
        JsonObject packetObject = JsonParser.parseString(packet).getAsJsonObject();

        return new RemoteAccessPacket(packetObject.get("button1").getAsBoolean(), packetObject.get("button2").getAsBoolean(), packetObject.get("button3").getAsBoolean(), packetObject.get("scroll").getAsInt(), packetObject.get("mouseDX").getAsDouble(), packetObject.get("mouseDY").getAsDouble(), packetObject.get("exit").getAsBoolean());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof RemoteAccessPacket)) {
            return false;
        }

        RemoteAccessPacket instance = (RemoteAccessPacket) o;

        return instance.isButton1Down() == isButton1Down() && instance.isButton2Down() == isButton2Down() && instance.isButton3Down() == isButton3Down() && instance.getScroll() == getScroll() && getMouseCoordinates().equals(instance.getMouseCoordinates()) && instance.isExit() == isExit();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("{");

        builder.append("button1:");
        builder.append(isButton1Down());
        builder.append(",");

        builder.append("button2:");
        builder.append(isButton2Down());
        builder.append(",");

        builder.append("button3:");
        builder.append(isButton3Down());
        builder.append(",");

        builder.append("scroll:");
        builder.append(getScroll());
        builder.append(",");

        builder.append("mouseDX:");
        builder.append(getMouseCoordinates().getDx());
        builder.append(",");

        builder.append("mouseDY:");
        builder.append(getMouseCoordinates().getDy());
        builder.append(",");

        builder.append("exit:");
        builder.append(isExit());

        builder.append("}");

        return builder.toString();
    }


    // -------------------------------------------- Getters -------------------------------

    /**
     * @return the isButton1Down
     */
    public boolean isButton1Down() {
        return isButton1Down;
    }

    public void setIsButton1Down(boolean isButton1Down) {
        this.isButton1Down = isButton1Down;
    }


    /**
     * @return the isButton2Down
     */
    public boolean isButton2Down() {
        return isButton2Down;
    }

    public void setIsButton2Down(boolean isButton2Down) {
        this.isButton2Down = isButton2Down;
    }


    /**
     * @return the isButton3Down
     */
    public boolean isButton3Down() {
        return isButton3Down;
    }

    public void setIsButton3Down(boolean isButton3Down) {
        this.isButton3Down = isButton3Down;
    }


    /**
     * @return the scroll
     */
    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }


    /**
     * @return the mouseCoordinates
     */
    public DeltaMouseCoordinates getMouseCoordinates() {
        return mouseCoordinates;
    }

    public void setDeltaMouseCoordinates(double dx, double dy) {
        this.mouseCoordinates = new DeltaMouseCoordinates(dx, dy);
    }

    /**
     * @return the exit
     */
    public boolean isExit() {
        return exit;
    }

    public void setIsExit(boolean exit) {
        this.exit = exit;
    }
}
