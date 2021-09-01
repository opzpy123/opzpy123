package com.opzpy123.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


/**
 * @description: Jwt控制类
 * @author: opzpy
 */
@Slf4j
public class JwtUtils {

    /**
     * @param EXPIRE 过期时间
     * @param APP_SECRET 密钥
     * @author: opzpy
     */
    public static final long EXPIRE = 1000 * 60 * 10;
    public static final String APP_SECRET = "opzpy123";

    //修改密码类型
    public static final String TYPE_PWD = "1";

    /**
     * @description: 生成Token字符串
     * @author: opzpy
     */
    public static String getJwtToken(String Subject, String id, Map<String, Object> map) {
        JwtBuilder builder = Jwts.builder()
                //设置Token头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS384")
                //设置分类
                .setSubject(Subject)
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                //获取当前时间
                .setIssuedAt(new Date())
                //设置id
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS384, APP_SECRET);
        //参数表
        map.forEach(builder::setHeaderParam);
        return builder.compact();
    }

    /**
     * @description: 判断token是否有效
     * @author: opzpy
     */

    public static boolean checkToken(String jwtToken) {

        //判空
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }

        try {
            //通过秘钥验证
            if (jwtToken.length() > 10) {
                Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public static String getValue(String jwt, String id) {

        if (!StringUtils.isEmpty(id)) {
            //验证
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwt);
            //获取id
            Claims claims = claimsJws.getBody();
            return (String) claims.get(id);
        }
        return null;
    }

    /**
     * @description: 判断token是否有效
     * @author: opzpy
     */

    public static boolean checkToken(HttpServletRequest request) {

        try {
            //获取token
            String jwtToken = request.getHeader("token");
            //判空
            if (StringUtils.isEmpty(jwtToken)) {
                return false;
            }
            //验证
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * @description: 根据token获取会员id
     * @author: opzpy
     */

    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        //获取前端cookie
        Cookie[] cookies = request.getCookies();
        //判断cookies是否为空
        if (cookies != null) {
            //遍历这个cookie
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    //获取这个cookie中的id
                    String value = cookie.getValue();
                    //验证
                    Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(value);
                    //获取id
                    Claims claims = claimsJws.getBody();
                    return (String) claims.get("id");
                }

            }
        }
        return "";
    }

    /**
     * @description: 根据token获取会员名
     * @author: opzpy
     */

    public static String getMemberNameByJwtToken(HttpServletRequest request) {
        //获取前端cookie
        Cookie[] cookies = request.getCookies();
        //判断cookies是否为空
        if (cookies != null) {
            //遍历这个cookie
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    //获取这个cookie中的id
                    String value = cookie.getValue();
                    //验证
                    Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(value);
                    //获取id
                    Claims claims = claimsJws.getBody();
                    return (String) claims.get("nickname");
                }

            }
        }
        return "";
    }
}
