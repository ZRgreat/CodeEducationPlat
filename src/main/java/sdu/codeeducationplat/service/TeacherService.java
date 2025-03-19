package sdu.codeeducationplat.service;

import sdu.codeeducationplat.model.Teacher;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import java.util.List;

public interface TeacherService{
    void addTeacher(Teacher teacher); //添加教师
    void deleteTeacherById(Long id);  //删除教师
    Teacher getTeacherById(Long id);  //根据Id查询教师
    List<Teacher> getTeachersByName(String name); //根据姓名查找教师列表
    List<Teacher> getTeachersByNumName(String numName); //根据职工号或者姓名查找教师列表
    List<Teacher> getTeachersByCertified(CertificationStatus certified);//根据认证状查找教师
//    void updateTeacher(Teacher teacher);//管理员通用更新
//    void updateTeacherShelf(Long teacherId, Teacher teacher);
}
