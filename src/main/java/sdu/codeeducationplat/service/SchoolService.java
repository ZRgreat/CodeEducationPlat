package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.SchoolMapper;
import sdu.codeeducationplat.model.School;

import java.util.List;

@Service
public class SchoolService extends ServiceImpl<SchoolMapper, School> {
    public List<School> getAllSchools() {
        return list();
    }
}