package org.fourthline.cling.support.renderingcontrol.lastchange;

import org.fourthline.cling.support.model.Channel;

public class ChannelCommand {
    protected Channel channel;
    protected String command;

    public ChannelCommand(Channel channel, String command) {
        this.channel = channel;
        this.command = command;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "Command: " + getCommand() + " (" + getChannel() + ")";
    }

}
