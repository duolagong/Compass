package com.programme.Fortress.Function.Business.service.ErpInfor;

import java.util.HashMap;
import java.util.List;

public interface ErpInforService {

    Integer getTradVolume(String queryDate);

    HashMap<String, Object> getPayTimeDistribution(String payDate);
}
