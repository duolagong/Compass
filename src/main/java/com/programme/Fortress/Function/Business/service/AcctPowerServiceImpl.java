package com.programme.Fortress.Function.Business.service;

import com.programme.Fortress.Function.Business.dao.AcctPowerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AcctPowerServiceImpl implements AcctPowerService {

    @Autowired
    private AcctPowerMapper acctPowerMapper;

    @Override
    public List<HashMap> getSubbranchId() {
        return acctPowerMapper.getSubbranchId();
    }
}
