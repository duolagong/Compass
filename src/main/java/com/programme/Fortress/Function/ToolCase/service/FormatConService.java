package com.programme.Fortress.Function.ToolCase.service;

import java.util.Map;


public interface FormatConService {

    String formatConvert(Map map);

    String makeMessage1031(String version,String subBranchId,String agentId,
                    String respCode,String respMsg,String orderNum) throws Exception;

    String makeMessage1032(String subBranchId,String agentId,
                           String branchId,String acctId,String curCode,String txDate) throws Exception;
}
