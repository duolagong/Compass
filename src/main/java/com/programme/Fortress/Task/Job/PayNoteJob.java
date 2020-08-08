package com.programme.Fortress.Task.Job;

import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Function.Business.service.PayNoteService;
import com.programme.Fortress.Other.ThreadPool.MyThreadPool;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.StringUtil;
import com.programme.Fortress.Util.TypeChange.MyListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.sql.Clob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component("PayNoteJob")
public class PayNoteJob {

    @Autowired
    private PayInforMapper payInforMapper;
    @Autowired
    private PayNoteService payNoteService;

    public void execute(){
        log.info("开始进行付款支付单生成任务！");
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String nowDate = timeFormatter.format(LocalDateTime.now());
        String nowDate =MyDateUtil.getMaxDate("yyyy-MM-dd");
        int inforNoteCount = payInforMapper.getInforNoteCount(nowDate);
        if(inforNoteCount !=0){
            List<HashMap<String, Object>> inforNote = payInforMapper.getInforNote(nowDate);
            List<PayNote> payNoteList= new ArrayList<PayNote>();
            for (HashMap hashMap:inforNote){
                try {
                    log.info("Map信息[{}]",hashMap);
                    String ordernum = hashMap.get("ORDERNUM").toString();
                    String tranxml = StringUtil.clobToString((Clob) hashMap.get("TRANXML"));
                    payNoteList.add(payNoteService.getOrdernumPayNote(tranxml, ordernum));
                }catch (Exception e){
                    log.error("付款支付单整理付款报文发生错误："+hashMap.get("ORDERNUM").toString(),e);
                }
            }
            log.info("共需要处理:"+payNoteList.size()+"笔交易");
            for (PayNote payNote : payNoteList){
                payNoteService.insertPayNote(payNote);
            }
            log.info("已处理完成"+payNoteList.size()+"笔交易");
        }

    }
}
