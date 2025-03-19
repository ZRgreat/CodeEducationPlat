package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_school")
public class UserSchoolBinding {
    @TableId(type = IdType.AUTO)
    private Long id;
}
