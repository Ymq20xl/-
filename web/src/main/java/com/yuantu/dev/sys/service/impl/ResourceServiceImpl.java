package com.yuantu.dev.sys.service.impl;

import com.yuantu.dev.common.utils.RegexUtils;
import com.yuantu.dev.sys.entity.Resource;
import com.yuantu.dev.sys.mapper.ResourceMapper;
import com.yuantu.dev.sys.service.IResourceService;
import com.yuantu.dev.sys.service.IRoleResourceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统资源表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-13
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Autowired
    private IRoleResourceService roleResourceService;

    @Override
    public IPage<Resource> page(Page page, Resource resource) {
        QueryWrapper<Resource> qw = new QueryWrapper<>();
        if (RegexUtils.isEnglish(resource.getName())) {
            resource.setInitial(resource.getName());
            resource.setName(null);
        }
        qw.setEntity(resource);
        return super.page(page, qw);
    }

    @Override
    public List<Resource> listMenuByUserId(Long userId) {
        return baseMapper.selectMenuByUserId(userId);
    }

    @Override
    public List<Resource> listEffective() {
        return list(new QueryWrapper<Resource>().select("id", "pid", "name").eq("type", 0).eq("status", 0).orderByDesc("sort", "id"));
    }

    @Override
    public List<Resource> listFunctions() {
        return list(Wrappers.<Resource>query().eq("type", 1).eq("status", 0));
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        Resource resource = new Resource();
        resource.setId(id);
        resource.setStatus(status.intValue() > -1 ? 0 : -1);
        if (resource.getStatus() > -1) {
            return updateById(resource);
        } else {
            List<Resource> childrenIds = this.baseMapper.queryChildrenIds(id);
            for (Resource r : childrenIds) {
                r.setStatus(-1);
            }
            return updateBatchById(childrenIds);
        }
    }

    @Override
    public boolean save(Resource resource) {
        if (null == resource) {
            return false;
        }
        return super.save(resource.initialName());
    }

    @Override
    public boolean removeById(Serializable id) {
        Assert.fail(roleResourceService.relation(id), "存在关联角色操作无效");
        List<Resource> childrenIds = this.baseMapper.queryChildrenIds(id);
        ArrayList<Serializable> ids = new ArrayList<Serializable>();
        for (Resource childrenId : childrenIds) {
            ids.add(childrenId.getId());
        }
        return super.removeByIds(ids);
    }
}
