package com.programme.Fortress.Function.Business.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReturnData<T> {

    private List<T> rows = new ArrayList<T>();//数据集合

    private int total;//数据总条数
}

