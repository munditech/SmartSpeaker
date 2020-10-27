package org.fourthline.cling.support.renderingcontrol.lastchange;

import org.fourthline.cling.support.model.Channel;

public class ChannelPackages {
    protected Channel channel;
    protected String packages;

    public ChannelPackages(Channel channel, String packages) {
        this.channel = channel;
        this.packages = packages;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getPackages() {
        return packages;
    }

    @Override
    public String toString() {
        return "getPackages: " + getPackages() + " (" + getChannel() + ")";
    }

}
