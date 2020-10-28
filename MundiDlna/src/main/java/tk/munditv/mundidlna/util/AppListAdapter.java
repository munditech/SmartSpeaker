package tk.munditv.mundidlna.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tk.munditv.mundidlna.R;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder>
        implements  View.OnClickListener {

    private Context mContext;
    private List<PInfo> mAppList;
    private OnItemClickListener mOnItemClickListener = null;

    public AppListAdapter(Context context, List<PInfo> applist) {
        this.mContext = context;
        this.mAppList = applist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_cardview, parent ,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PInfo pInfo = mAppList.get(position);
        String appname = pInfo.getAppname();
        holder.appName.setText(appname);
        byte[] byteArray = pInfo.getIcon();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        holder.appImage.setImageBitmap(bmp);
        holder.itemView.setTag(pInfo);
        return;
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (PInfo) v.getTag());
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView appImage;
        private TextView appName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appImage = (ImageView) itemView.findViewById(R.id.remote_app_icon);
            appName = (TextView) itemView.findViewById(R.id.remote_app_name);
            return;
        }

    };

    public static interface OnItemClickListener {
        void onItemClick(View view , PInfo tag);
    }
}
