package tk.munditv.mundidlna.dmc;

import android.util.Log;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;

public class SetVolumeCallback extends SetVolume{
	public SetVolumeCallback(Service paramService, long paramLong)
    {
      super(paramService, paramLong);
    }

    public void failure(ActionInvocation paramActionInvocation, UpnpResponse paramUpnpResponse, String paramString)
    {
      Log.d("SetVolumeCallback", "set volume failed");
    }

    public void success(ActionInvocation paramActionInvocation)
    {
      super.success(paramActionInvocation);
      Log.d("SetVolumeCallback", "set volume success");
    }

}
