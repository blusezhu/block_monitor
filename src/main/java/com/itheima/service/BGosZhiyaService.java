package com.itheima.service;

import com.itheima.data.pojo.BGosZhiyaEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzj
 * @since 2021-02-22
 */
public interface BGosZhiyaService extends IService<BGosZhiyaEntity> {

    public void getDataAndSave(int pageNo, int pageSize, String outOrIn, Integer desId, AtomicReference<Boolean> isLatestId);
}
