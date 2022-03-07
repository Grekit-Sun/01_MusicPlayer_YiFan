package cn.weli.mediaplayer.manager;

public interface CallbackListener {

    void play(int oldId, int nowId);

    void pause(int id);

    void stop(int id);

}
