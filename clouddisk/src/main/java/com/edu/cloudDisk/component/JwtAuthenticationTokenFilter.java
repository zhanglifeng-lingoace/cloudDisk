package com.edu.cloudDisk.component;

import com.edu.cloudDisk.common.LingoAceUserBriefInfo;
import com.edu.cloudDisk.service.UserService;
import com.edu.cloudDisk.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 * Created by wugaoping on 2019/8/18.
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken;
        String authHeader = request.getHeader(tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            // The part after "Bearer "
            authToken = authHeader.substring(tokenHead.length());
        } else {
            authToken = request.getParameter("jwtToken");
        }

        //当token为空或格式错误时 直接放行
        if (authToken == null) {
            chain.doFilter(request, response);
            return;
        }

        //获取用户信息
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(authToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);

    }

    /**
     * token中获取用户信息
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String authToken) {
        String username = jwtTokenUtil.getUserNameFromToken(authToken);
        LOGGER.info("username authToken:{}", username);

        if (username != null) {
            LingoAceUserBriefInfo userDetails = null;
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                userDetails = (LingoAceUserBriefInfo) userServiceImpl.getUserByName(username);
            } else {
                UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                userDetails = (LingoAceUserBriefInfo) authentication.getPrincipal();
                if (!username.equals(userDetails.getUsername())) {
                    userDetails = (LingoAceUserBriefInfo) userServiceImpl.getUserByName(username);
                }
            }

            //authToken是否过期，用户是否是同一个人
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                return new UsernamePasswordAuthenticationToken(userDetails, null, null);
            }
        }

        return null;
    }
}
