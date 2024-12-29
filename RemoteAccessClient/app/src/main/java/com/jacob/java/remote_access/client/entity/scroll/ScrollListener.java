package com.jacob.java.remote_access.client.entity.scroll;

import androidx.recyclerview.widget.RecyclerView;

import com.jacob.java.remote_access.client.entity.Client;
import com.jacob.java.remote_access.client.entity.RemoteAccessPacket;
import com.jacob.java.remote_access.client.exception.InvalidClientException;
import com.jacob.java.remote_access.client.exception.InvalidPacketException;

public class ScrollListener extends RecyclerView.OnScrollListener {
    private final RemoteAccessPacket packet;
    private final Client client;

    public ScrollListener(final RemoteAccessPacket packet, final Client client) {
        if(packet == null) {
            throw new InvalidPacketException("Null packet passed to ScrollListener");
        } else {
            this.packet = packet;
        }
        if(client == null) {
            throw new InvalidClientException("Null client passed to ScrollListener");
        } else {
            this.client = client;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView view, int newState) {
        switch(newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                System.out.println("The RecyclerView is not scrolling");
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                System.out.println("Scrolling now");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                System.out.println("Scroll Settling");
                break;
            default:
                break;
        }
    }

    /**
     * If dy > 0, then we are scrolling downwards
     * If dy < 0, then we are scrolling upwards
     *
     * @param view The RecyclerView which scrolled.
     * @param dx The amount of horizontal scroll.
     * @param dy The amount of vertical scroll.
     */
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(packet == null) {
                    System.out.println("Null packet in onScrolled() in ScrollListener");
                    return;
                }
                if(client == null) {
                    System.out.println("Null client in onScrolled() in ScrollListener");
                    return;
                }

//                System.out.println("view = " + view + "; dx = " + dx + "; dy = " + dy);

                packet.setScroll(-1 * dy);    // Depend on preference: -1 is to follow my current mouse
                client.sendPacket();
            }
        }).start();
    }
}
