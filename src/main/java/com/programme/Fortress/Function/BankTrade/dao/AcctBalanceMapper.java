package com.programme.Fortress.Function.BankTrade.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.BankTrade.entity.AcctBalance;

public interface AcctBalanceMapper extends BaseMapper<AcctBalance> {
    Long getDual();
}
