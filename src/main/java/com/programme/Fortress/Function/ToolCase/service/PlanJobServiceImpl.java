package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.programme.Fortress.Function.ToolCase.entity.PlanJob;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Task.JobOperateEnum;
import com.programme.Fortress.Function.ToolCase.dao.PlanJobMapper;
import com.programme.Fortress.Task.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class PlanJobServiceImpl extends ServiceImpl<PlanJobMapper, PlanJob> implements PlanJobService {

    @Autowired
    private QuartzService quartzService;
    @Autowired
    private PlanJobMapper planJobMapper;

    @Override
    public ResultBean add(PlanJob planJob) {
        try {
            if(!CronExpression.isValidExpression(planJob.getCronExpression())) return ResultUtil.fail("规则填写不合规");
            planJob.setId(planJobMapper.getDual());
            planJob.setStatus(0);
            planJob.setDeleteFlag(0);
            planJob.setCreatorId(1);
            planJob.setCreatorName("System");
            planJobMapper.insert(planJob);
            return ResultUtil.success(null,"新增周期任务成功");
        } catch (Exception e) {
            log.error("新增周期任务异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean start(int id) {
        try {
            PlanJob job = this.getById(id);
            job.setStatus(1);
            this.updateById(job);
            quartzService.operateJob(JobOperateEnum.START, job);
            return ResultUtil.success(null,job.getJobName()+"启动成功");
        } catch (Exception e) {
            log.error("启动周期任务异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean pause(int id) {
        try {
            PlanJob job = this.getById(id);
            job.setStatus(0);
            this.updateById(job);
            quartzService.operateJob(JobOperateEnum.PAUSE, job);
            return ResultUtil.success(null,job.getJobName()+"暂停成功");
        } catch (Exception e) {
            log.error("暂停周期任务异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean delete(int id) {
        try {
            PlanJob job = this.getById(id);
            this.removeById(id);
            quartzService.operateJob(JobOperateEnum.DELETE, job);
            return ResultUtil.success(null,job.getJobName()+"删除成功");
        } catch (Exception e) {
            log.error("删除周期任务异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean update(PlanJob planJob){
        try {
            if(!CronExpression.isValidExpression(planJob.getCronExpression())) return ResultUtil.fail("规则填写不合规");
            PlanJob job = planJobMapper.selectById(planJob.getId());
            job.setJobName(planJob.getJobName());
            job.setCronExpression(planJob.getCronExpression());
            job.setBeanName(planJob.getBeanName());
            job.setMethodName(planJob.getMethodName());
            planJobMapper.updateById(job);
            quartzService.operateJob(JobOperateEnum.UPDATE, job);
            return ResultUtil.success(null,planJob.getJobName()+"修改成功");
        } catch (Exception e) {
            log.error("修改周期任务异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean startAllJob() {
        try {
            List<PlanJob> planJobs = planJobMapper.selectList(new QueryWrapper<PlanJob>().eq("status", 0));//new QueryWrapper<PayInfor>().eq("to_char(trandate,'yyyy-MM-dd')",nowDate)
            for (PlanJob planJob:planJobs){
                planJob.setStatus(1);
                planJobMapper.update(planJob,new QueryWrapper<PlanJob>().eq("id",planJob.getId()));
                quartzService.operateJob(JobOperateEnum.START, planJob);
            }
            return ResultUtil.success(null,"周期任务已全部启用");
        } catch (SchedulerException e) {
            log.error("全部启用周期任务异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean pauseAllJob() {
        try {
            List<PlanJob> planJobs = planJobMapper.selectList(new QueryWrapper<PlanJob>().eq("status", 1));
            for (PlanJob planJob:planJobs){
                planJob.setStatus(0);
                planJobMapper.update(planJob,new QueryWrapper<PlanJob>().eq("id",planJob.getId()));
            }
            quartzService.pauseAllJob();
            return ResultUtil.success(null,"周期任务已全部停用");
        } catch (SchedulerException e) {
            log.error("全部停用周期任务异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }
}
