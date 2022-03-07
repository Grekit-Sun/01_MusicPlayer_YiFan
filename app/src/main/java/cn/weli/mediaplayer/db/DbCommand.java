package cn.weli.mediaplayer.db;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DbCommand<T> {

    //只有一个线程池
    private static ExecutorService sExecutorService = Executors.newSingleThreadExecutor();

    //主线程消息队列的Handler
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    //执行数据库操作
    public final void execute() {
        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                possResult(doInBackground());
            }
        });
    }

    /**
     * 将结果投递到UI线程
     */
    protected void possResult(final T result) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(result);
            }
        });
    }

    /**
     * 在后台执行的数据库操作
     * @return
     */
    protected abstract T doInBackground();

    /**
     * 在UI线程中操作
     * @param result
     */
    protected void onPostExecute(T result) {
    }

}
