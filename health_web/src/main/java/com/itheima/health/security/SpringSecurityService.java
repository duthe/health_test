package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component
public class SpringSecurityService implements UserDetailsService {

    @Reference
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        根据用户名查询用户 包括  角色 权限
        User user = userService.findByUsername(username);
        if (user != null) {
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            Set<Role> roles = user.getRoles();
            if (roles!=null) {
                //如果角色不为空 遍历角色 并添加到权限集合
                roles.forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getKeyword()));

                    Set<Permission> permissions = role.getPermissions();

                    if (permissions != null){
                        //判断权限不为空 遍历权限 添加到权限集合
                        permissions.forEach(permission -> {
                            authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                        });
                    }
                });
            }

            return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
        }
        return null;
    }
}
