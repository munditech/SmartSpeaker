package org.fourthline.cling.support.renderingcontrol.callback;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.model.Channel;

import java.util.logging.Logger;

public abstract class SetCommand extends ActionCallback  {
    private static Logger log = Logger.getLogger(SetCommand.class.getName());

    public SetCommand(Service service, String desiredCommand) {
        this(new UnsignedIntegerFourBytes(0), service, desiredCommand);
    }

    public SetCommand(UnsignedIntegerFourBytes instanceId, Service service, String desiredCommand) {
        super(new ActionInvocation(service.getAction("SetCommand")));
        getActionInvocation().setInput("InstanceID", instanceId);
        getActionInvocation().setInput("Channel", Channel.Master.toString());
        getActionInvocation().setInput("DesiredCommand", desiredCommand);
    }

    @Override
    public void success(ActionInvocation invocation) {
        log.fine("Executed successfully");

    }
}
