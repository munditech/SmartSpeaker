package org.fourthline.cling.support.renderingcontrol.callback;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.model.Channel;

import java.util.logging.Logger;

public abstract class GetPackages extends ActionCallback  {

    private static Logger log = Logger.getLogger(GetPackages.class.getName());

    public GetPackages(Service service) {
        this(new UnsignedIntegerFourBytes(0), service);
    }
    public GetPackages(UnsignedIntegerFourBytes instanceId, Service service) {
        super(new ActionInvocation(service.getAction("GetPackages")));
        getActionInvocation().setInput("InstanceID", instanceId);
        getActionInvocation().setInput("Channel", Channel.Master.toString());
    }

    public void success(ActionInvocation invocation) {
        String currentPackages = (String) invocation.getOutput("currentPackages").getValue();
        received(invocation, currentPackages);
    }

    public abstract void received(ActionInvocation actionInvocation, String currentPackages);
}
