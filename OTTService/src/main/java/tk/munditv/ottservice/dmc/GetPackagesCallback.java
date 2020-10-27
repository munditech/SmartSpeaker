package tk.munditv.ottservice.dmc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.GetPackages;

public class GetPackagesCallback extends GetPackages {

    private Handler handler;

    public GetPackagesCallback(Service paramService, Handler paramHandler) {
        super(paramService);
        this.handler = paramHandler;
    }

    public void failure(ActionInvocation paramActionInvocation,
                        UpnpResponse paramUpnpResponse, String paramString) {
        Log.i("DMC", "get packages failed");
    }

    public void received(ActionInvocation paramActionInvocation,
                         String paramString) {
        Log.i("DMC", "get packages status:" + paramString);
        Message localMessage = new Message();
        localMessage.what = DMCControlMessage.GETPACKAGES;
        Bundle localBundle = new Bundle();
        localBundle.putString("packages", paramString);
        localMessage.setData(localBundle);
        handler.sendMessage(localMessage);
    }
}
