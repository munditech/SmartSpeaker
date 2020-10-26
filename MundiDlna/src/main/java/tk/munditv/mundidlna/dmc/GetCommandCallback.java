package tk.munditv.mundidlna.dmc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.GetCommand;

public class GetCommandCallback extends GetCommand {
    private Handler handler;

    public GetCommandCallback(Service paramService, Handler paramHandler) {
        super(paramService);
        this.handler = paramHandler;
    }

    public void failure(ActionInvocation paramActionInvocation,
                        UpnpResponse paramUpnpResponse, String paramString) {
        Log.i("DMC", "get command failed");
    }

    public void received(ActionInvocation paramActionInvocation,
                         String paramString) {
        Log.i("DMC", "get command status:" + paramString);
        Message localMessage = new Message();
        localMessage.what = DMCControlMessage.SETCOMMAND;
        Bundle localBundle = new Bundle();
        localBundle.putString("command", paramString);
        localMessage.setData(localBundle);
        handler.sendMessage(localMessage);
    }

}
