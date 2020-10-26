package org.fourthline.cling.support.renderingcontrol.lastchange;

import org.fourthline.cling.model.types.Datatype;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.StringDatatype;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.model.Channel;
import org.fourthline.cling.support.shared.AbstractMap;

import java.util.Map;

public class EventedValueChannelCommand extends EventedValue<ChannelCommand> {
    public EventedValueChannelCommand(ChannelCommand value) {
        super(value);
    }

    public EventedValueChannelCommand(Map.Entry<String, String>[] attributes) {
        super(attributes);
    }

    @Override
    protected ChannelCommand valueOf(Map.Entry<String, String>[] attributes) throws InvalidValueException {
        Channel channel = null;
        String command = null;
        for (Map.Entry<String, String> attribute : attributes) {
            if (attribute.getKey().equals("channel"))
                channel = Channel.valueOf(attribute.getValue());
            if (attribute.getKey().equals("val"))
                command = new StringDatatype().valueOf(attribute.getValue());
        }
        return channel != null && command != null ? new ChannelCommand(channel, command) : null;
    }

    @Override
    public Map.Entry<String, String>[] getAttributes() {
        return new Map.Entry[]{
                new AbstractMap.SimpleEntry<>(
                        "val",
                        new StringDatatype().getString(getValue().getCommand())
                ),
                new AbstractMap.SimpleEntry<>(
                        "channel",
                        getValue().getChannel().name()
                )
        };
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    protected Datatype getDatatype() {
        return null; // Not needed
    }
}
