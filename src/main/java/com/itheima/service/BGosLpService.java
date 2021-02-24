package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.data.pojo.BGosZhiyaEntity;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzj
 * @since 2021-02-22
 */
public interface BGosLpService extends IService<BGosZhiyaEntity> {

    public void getData(int pageNo, int pageSize, String outOrIn, HashSet<Integer> isSendMail);
}
