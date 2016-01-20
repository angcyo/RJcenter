package com.rsen.util;


import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 倒计时器
 *
 * @author ldm
 * @description 详细描述：
 * @date 2014-5-13 上午10:06:06
 */
public class TimeCount extends CountDownTimer {
  public static final int TIME_COUNT = 121000;// 时间防止从119s开始显示
  private Button btn;
  private int endStrRid;
  private int normalColor, timingColor;

  /**
   * @param millisInFuture 倒计时时间
   * @param countDownInterval 渐变时间
   */
  public TimeCount(long millisInFuture, long countDownInterval, Button btn, int endStrRid) {
    super(millisInFuture, countDownInterval);
    this.btn = btn;
    this.endStrRid = endStrRid;
  }

  /**
   * 1分钟
   *
   * @param btn 倒计时按钮
   * @param endStrRid 计时结束时显示胡字符rid
   */
  public TimeCount(Button btn, int endStrRid) {
    super(TIME_COUNT, 1000);
    this.btn = btn;
    this.endStrRid = endStrRid;
  }

  /**
   * 1分钟
   *
   * @param btn 倒计时按钮
   */
  public TimeCount(Button btn) {
    super(TIME_COUNT, 1000);
    this.btn = btn;
  }

  public TimeCount(Button tv_varify, int normalColor, int timingColor) {
    this(tv_varify);
    this.normalColor = normalColor;
    this.timingColor = timingColor;
  }

  // 计时完毕时触发
  @Override
  public void onFinish() {
    if (normalColor > 0) {
      btn.setTextColor(normalColor);
    }
    btn.setText(endStrRid);
    btn.setEnabled(true);
  }

  // 计时过程显示
  @Override
  public void onTick(long millisUntilFinished) {
    if (timingColor > 0) {
      btn.setTextColor(timingColor);
    }
    btn.setEnabled(false);
    btn.setText(millisUntilFinished / 1000 + "S");
  }
}
