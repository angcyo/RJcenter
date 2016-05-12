package com.rsen.moudle;

/**
 * Created by robi on 2016-05-11 14:49.
 */
public interface ISdcardListener {

    /**
     * SD卡插入
     */
    void onMounted();

    /**
     * SD卡拔出
     */
    void onRemoved();
}
