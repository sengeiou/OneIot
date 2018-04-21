package cn.leancloud.chatkit.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by lzw on 14/12/19.
 * 语音播放相关的 helper 类
 */
public class LCIMAudioHelper {
  private static final String TAG = "LCIMPlayButton";

  private static LCIMAudioHelper audioHelper;
  private MediaPlayer mediaPlayer;
  private AudioFinishCallback finishCallback;
  private String audioPath;
  private boolean onceStart = false;
  private AudioTrack mAudioTrack;

  private LCIMAudioHelper() {
    mediaPlayer = new MediaPlayer();
  }

  public static synchronized LCIMAudioHelper getInstance() {
    if (audioHelper == null) {
      audioHelper = new LCIMAudioHelper();
    }
    return audioHelper;
  }

  /**
   * 获取当前语音的文件地址
   *
   * @return
   */
  public String getAudioPath() {
    return audioPath;
  }

  /**
   * 停止播放
   */
  public void stopPlayer() {
    if (mediaPlayer != null) {
      tryRunFinishCallback();
      mediaPlayer.stop();
      mediaPlayer.reset();
    }
  }

  /**
   * 暂停播放
   */
  public void pausePlayer() {
    if (mediaPlayer != null) {
      tryRunFinishCallback();
      mediaPlayer.pause();
    }
  }

  /**
   * 判断当前是否正在播放
   *
   * @return
   */
  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }

  /**
   * 重新播放
   */
  public void restartPlayer() {
    if (mediaPlayer != null && mediaPlayer.isPlaying() == false) {
      mediaPlayer.start();
    }
  }

  public void addFinishCallback(AudioFinishCallback callback) {
    finishCallback = callback;
  }

  /**
   * 播放语音文件
   *
   * @param path
   */
  public synchronized void playAudio(String path) throws IOException {
    if (onceStart) {
      mediaPlayer.reset();
    }
    tryRunFinishCallback();
    audioPath = path;
    try {
      Log.d(TAG, "PATH >>>>>>>" + path);
      File file = new File(path);
      Log.d(TAG, "File exists >>>>>>>" + file.exists());
      mediaPlayer.setDataSource(path);
      mediaPlayer.prepare();
      mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
          tryRunFinishCallback();
        }
      });
      mediaPlayer.start();
      onceStart = true;
    } catch (IOException e) {
      Log.d(TAG, e.getMessage());
      throw e;
    }
  }

  private void tryRunFinishCallback() {
    if (finishCallback != null) {
      finishCallback.onFinish();
    }
  }

  public interface AudioFinishCallback {
    void onFinish();
  }


  /**
   * Play the sound by AudioTrack.
   *
   * @param soundData
   *            The sound which is to be played.
   */
  private void playSound(byte[] soundData) {
    int index = 0;
    int offset = 0;
    // 8000 byte every second
    final int minBufsize = AudioTrack.getMinBufferSize(8000,
            AudioFormat.CHANNEL_OUT_STEREO,// The channel
            AudioFormat.ENCODING_PCM_16BIT);

    mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
            8000,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBufsize,
            AudioTrack.MODE_STREAM);

    mAudioTrack.play();
    while (true) {
      try {
        offset = index * minBufsize;
        if (offset >= soundData.length) {
          break;
        }

//        publishProgress(offset);
        mAudioTrack.write(soundData, offset, minBufsize);
      } catch (Exception e) {
        break;
      }

      index++;
    }

    mAudioTrack.release();
  }
}
