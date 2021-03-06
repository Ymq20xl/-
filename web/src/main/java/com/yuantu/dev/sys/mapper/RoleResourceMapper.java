package com.yuantu.dev.sys.mapper;

import com.yuantu.dev.sys.entity.RoleResource;
import com.yuantu.dev.sys.vo.ResourceZTreeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色资源关联表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-24
 */
@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

    List<ResourceZTreeVO> selectZTreeVO(@Param("roleId") Long roleId);
}
