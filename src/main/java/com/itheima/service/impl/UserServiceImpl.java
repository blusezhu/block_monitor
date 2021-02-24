package com.itheima.service.impl;

import com.itheima.data.pojo.UserEntity;
import com.itheima.data.mapper.UserDao;
import com.itheima.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzj
 * @since 2021-01-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

}
