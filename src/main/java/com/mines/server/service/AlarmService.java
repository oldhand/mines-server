package com.mines.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mines.server.model.data.OvercrowdAlarmData;
import com.mines.server.model.data.OvertimeAlarmData;
import com.mines.server.model.data.RestrictedAreaData;
import com.mines.server.repository.OvercrowdAlarmDataRepository;
import com.mines.server.repository.OvertimeAlarmDataRepository;
import com.mines.server.repository.RestrictedAreaDataRepository;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlarmService {

    @Autowired
    private OvercrowdAlarmDataRepository overcrowdAlarmDataRepository;

    @Autowired
    private OvertimeAlarmDataRepository overtimeAlarmDataRepository;

    @Autowired
    private RestrictedAreaDataRepository restrictedAreaDataRepository;

    /**
     * 查询超员报警数据
     */
    public List<OvercrowdAlarmData> getOvercrowdAlarms(String enterpriseCode, Date startTime, Date endTime) {
        return overcrowdAlarmDataRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("ksbh"), enterpriseCode));
            predicates.add(cb.between(root.get("alarmTime"), startTime, endTime));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    /**
     * 查询超时报警数据
     */
    public List<OvertimeAlarmData> getOvertimeAlarms(String enterpriseCode, Date startTime, Date endTime) {
        return overtimeAlarmDataRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("ksbh"), enterpriseCode));
            predicates.add(cb.between(root.get("alarmTime"), startTime, endTime));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    /**
     * 查询限制区域报警数据
     */
    public List<RestrictedAreaData> getRestrictedAreaAlarms(String enterpriseCode, Date startTime, Date endTime) {
        return restrictedAreaDataRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("ksbh"), enterpriseCode));
            predicates.add(cb.between(root.get("alarmTime"), startTime, endTime));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
