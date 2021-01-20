package com.programme.Fortress.Function.ToolCase.action;

import com.programme.Fortress.Function.ToolCase.entity.PlanJob;
import com.programme.Fortress.Function.ToolCase.service.PlanJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planRule")
public class PlanJobAction {

    @Autowired
    private PlanJobService jobService;

    @GetMapping
    public Object getPlanJob() {
        try {
            List<PlanJob> planJobs = jobService.list();
            /*List<PlanJob> planJobs = planJobMapper.selectList(null);另一种写法*/
            return planJobs;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @PostMapping
    public Object add(@RequestBody PlanJob planJob, @CookieValue(value = "COMPASS_TOKEN", required = true) String cookie) {
        return jobService.add(planJob, cookie);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public Object delete(@PathVariable("id") Integer id) {
        return jobService.delete(id);
    }

    @PutMapping
    public Object update(@RequestBody PlanJob planJob) {
        return jobService.update(planJob);
    }

    @PutMapping(value = "/state/{id}", produces = {"application/json"})
    public Object start(@PathVariable("id") Integer id) {
        return jobService.start(id);
    }

    @PutMapping(value = "/pause/{id}", produces = {"application/json"})
    public Object pause(@PathVariable("id") Integer id) {
        return jobService.pause(id);
    }

    @GetMapping(value = "/start", produces = {"application/json"})
    public Object startAllJob() {
        return jobService.startAllJob();
    }

    @GetMapping(value = "/pause", produces = {"application/json"})
    public Object pauseAllJob() {
        return jobService.pauseAllJob();
    }
}
