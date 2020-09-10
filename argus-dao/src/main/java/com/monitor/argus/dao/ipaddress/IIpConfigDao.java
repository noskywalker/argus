package com.monitor.argus.dao.ipaddress;

import com.monitor.argus.bean.ipaddress.IpConfigEntity;

import java.util.List;

/**
 * Created by huxiaolei on 2016/11/7.
 */
public interface IIpConfigDao {

    List<IpConfigEntity> getAllIpConfigList();

    List<IpConfigEntity> getShortIpConfigList();

    boolean insertIpConfig(IpConfigEntity ipConfigEntity);

    List<String> getShortAddress();

}
