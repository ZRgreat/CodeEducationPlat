package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.ClassMapper;
import sdu.codeeducationplat.model.school.Class;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.util.BindCodeGenerator;

@Service
@RequiredArgsConstructor
public class ClassService extends ServiceImpl<ClassMapper, Class> {

    private final TeacherService teacherService;

    /**
     * 教师创建班级
     *
     * @param teacherId 教师ID
     * @param className 班级名称
     * @return 创建的班级对象
     */
    public Class createClass(Long teacherId, String className) {
        // 验证教师是否存在
        Teacher teacher = teacherService.getById(teacherId);
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }

        // 创建班级对象
        Class newClass = new Class();
        newClass.setTeacherId(teacherId);
        newClass.setSchoolId(teacher.getSchoolId()); // 班级学校ID与教师学校ID一致
        newClass.setName(className);

        // 生成唯一的班级绑定码
        String bindingCode;
        do {
            bindingCode = BindCodeGenerator.generateClassBindCode(); // 8 位绑定码
        } while (this.exists(new LambdaQueryWrapper<Class>().eq(Class::getBindingCode, bindingCode)));
        newClass.setBindingCode(bindingCode);

        // 保存班级
        this.save(newClass);
        return newClass;
    }

    /**
     * 根据绑定码查找班级
     *
     * @param bindingCode 班级绑定码
     * @return 班级对象
     */
    public Class findByBindingCode(String bindingCode) {
        return this.getOne(new LambdaQueryWrapper<Class>().eq(Class::getBindingCode, bindingCode));
    }
}