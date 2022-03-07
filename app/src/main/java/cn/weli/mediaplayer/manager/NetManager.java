package cn.weli.mediaplayer.manager;

public class NetManager {

    private static NetManager mNetManager = null;

    private NetManager() {
    }

    public static NetManager getInstance() {
        if (mNetManager == null) {
            synchronized (NetManager.class) {
                if (mNetManager == null) {
                    mNetManager = new NetManager();
                }
            }
        }
        return mNetManager;
    }
}
