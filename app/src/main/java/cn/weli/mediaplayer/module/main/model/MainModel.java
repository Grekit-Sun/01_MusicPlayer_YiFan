package cn.weli.mediaplayer.module.main.model;

import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.db.DbCommand;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.helper.MediaHelper;

public class MainModel {

    public void stopMusicById(){
        new DbCommand<Void>() {
            @Override
            protected Void doInBackground() {
                int songId = MediaHelper.readIsPlaySongId();
                if(songId > 0) {
                    SongDataDbHelper.getInstance().updateStatusById(songId, SongsConstant.MUSIC_STATUS_STOP);
                }
                return null;
            }
        }.execute();
    }
}
