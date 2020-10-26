package tk.munditv.mundidlna.dmc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.SetCommand;

public class SetCommandCallback extends SetCommand {
    private String desiredCommand;

    private Handler handler;

    public SetCommandCallback(Service paramService, String paramString,
                            Handler paramHandler) {
        super(paramService, paramString);
        this.handler = paramHandler;
        this.desiredCommand = paramString;
    }

    public void failure(ActionInvocation paramActionInvocation,
                        UpnpResponse paramUpnpResponse, String paramString) {
        Log.e("set command failed", "set command failed");
    }

    public void success(ActionInvocation paramActionInvocation) {
        Log.e("set command success", "set command success");
        Message localMessage = new Message();
        localMessage.what = DMCControlMessage.SETCOMMANDSUC;
        Bundle localBundle = new Bundle();
        localBundle.putString("mute", desiredCommand);
        localMessage.setData(localBundle);
        this.handler.sendMessage(localMessage);
    }
}
