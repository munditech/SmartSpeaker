package tk.munditv.mundidlna.dmc;

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
        Log.i("GetPackagesCallback", "GetPackagesCallback()");
    }

    public void failure(ActionInvocation paramActionInvocation,
                        UpnpResponse paramUpnpResponse, String paramString) {
        Log.i("GetPackagesCallback", "GetPackagesCallback() failure");
    }

    public void received(ActionInvocation paramActionInvocation,
                         String paramString) {
        super.success(paramActionInvocation);
        Log.i("GetPackagesCallback", "GetPackagesCallback() received string=" + paramString);
        Message localMessage = new Message();
        localMessage.what = DMCControlMessage.GETPACKAGES;
        Bundle localBundle = new Bundle();
        localBundle.putString("packages", paramString);
        localMessage.setData(localBundle);
        handler.sendMessage(localMessage);
    }
}
