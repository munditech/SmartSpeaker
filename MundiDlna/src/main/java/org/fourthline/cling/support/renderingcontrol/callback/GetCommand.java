package org.fourthline.cling.support.renderingcontrol.callback;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.model.Channel;

import java.util.logging.Logger;

public abstract class GetCommand extends ActionCallback  {
    private static Logger log = Logger.getLogger(GetCommand.class.getName());

    public GetCommand(Service service) {
        this(new UnsignedIntegerFourBytes(0), service);
    }
    public GetCommand(UnsignedIntegerFourBytes instanceId, Service service) {
        super(new ActionInvocation(service.getAction("GetCommand")));
        getActionInvocation().setInput("InstanceID", instanceId);
        getActionInvocation().setInput("Channel", Channel.Master.toString());
    }

    public void success(ActionInvocation invocation) {
        String currentCommand = (String) invocation.getOutput("CurrentMute").getValue();
        received(invocation, currentCommand);
    }

    public abstract void received(ActionInvocation actionInvocation, String currentCommand);
}
