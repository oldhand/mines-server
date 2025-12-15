package com.mines.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mines.server.model.data.PersonLocationData;
import com.mines.server.model.data.PowerData;
import com.mines.server.model.vo.PersonStatVO;
import com.mines.server.model.vo.PowerStatVO;
import com.mines.server.repository.PersonLocationDataRepository;
import com.mines.server.repository.PowerDataRepository;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private PowerDataRepository powerDataRepository;

    @Autowired
    private PersonLocationDataRepository personLocationDataRepository;

    /**
     * 统计用电量
     */
    public PowerStatVO statPower(String enterpriseCode, Date startTime, Date endTime, String statType) {
        List<PowerData> powerDatas = powerDataRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("ksbh"), enterpriseCode));
            predicates.add(cb.between(root.get("dataTime"), startTime, endTime));
            return cb.and(predicates.toArray(new Predicate[0]));
        });

        PowerStatVO result = new PowerStatVO();
        result.setEnterpriseCode(enterpriseCode);
        result.setStartTime(startTime);
        result.setEndTime(endTime);
        result.setStatType(statType);

        // 按日/月聚合统计（示例逻辑）
        Map<String, Double> statMap = new HashMap<>();
        for (PowerData data : powerDatas) {
            String key = formatStatKey(data.getDataTime(), statType);
            statMap.put(key, statMap.getOrDefault(key, 0.0) + data.getPowerConsumption().doubleValue());
        }
        result.setPowerStats(statMap);
        return result;
    }

    /**
     * 统计人员位置分布
     */
    public PersonStatVO statPersonLocation(String enterpriseCode) {
        List<PersonLocationData> locationDatas = personLocationDataRepository.findAll((root, query, cb) ->
                cb.equal(root.get("ksbh"), enterpriseCode)
        );

        // 按区域分组统计人数
        Map<String, Long> locationCount = locationDatas.stream()
                .collect(Collectors.groupingBy(PersonLocationData::getLocation, Collectors.counting()));

        PersonStatVO result = new PersonStatVO();
        result.setEnterpriseCode(enterpriseCode);
        result.setLocationStats(locationCount);
        result.setTotalPerson(locationDatas.size());
        return result;
    }

    /**
     * 格式化统计时间key（日：yyyy-MM-dd，月：yyyy-MM）
     */
    private String formatStatKey(Date date, String statType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if ("DAY".equals(statType)) {
            return String.format("%d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        } else {
            return String.format("%d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
        }
    }
}
