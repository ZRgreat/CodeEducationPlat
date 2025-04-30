package sdu.codeeducationplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sdu.codeeducationplat.model.user.User;

import java.util.List;

public interface UserMapper extends  BaseMapper<User> {

    // 通过昵称模糊搜索用户
    @Select("SELECT * FROM user WHERE nickname LIKE CONCAT('%', #{nickname}, '%')")
    List<User> findByNicknameLike(@Param("nickname") String nickname);

}