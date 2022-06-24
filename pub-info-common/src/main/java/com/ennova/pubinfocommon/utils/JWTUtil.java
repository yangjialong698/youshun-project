package com.ennova.pubinfocommon.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.ennova.pubinfocommon.vo.UserVO;

import java.util.Date;

public class JWTUtil {
    public static final String SECRET_KEY = "123456"; //秘钥
    public static final long TOKEN_EXPIRE_TIME = 4 * 60 * 60 * 1000; //token过期时间
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 60 * 60 * 1000; //refreshToken过期时间
    private static final String ISSUER = "issuer"; //签发人

    /**
     * 生成签名
     */
    public static String generateToken(String username){
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法

        String token = JWT.create()
                .withIssuer(ISSUER) //签发人
                .withIssuedAt(now) //签发时间
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME)) //过期时间
                .withClaim("username", username) //保存身份标识
                .sign(algorithm);
        return token;
    }

    /**
     * 生成签名(1)
     */
    public static String generateRefreshToken(String username){
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法

        String token = JWT.create()
                .withIssuer(ISSUER) //签发人
                .withIssuedAt(now) //签发时间
                .withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME)) //过期时间
                .withClaim("username", username) //保存身份标识
                .sign(algorithm);
        return token;
    }

    /**
     * 生成签名(2)
     */
    public static String generateRefToken(String username, Integer userid, String companid){
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法

        String token = JWT.create()
                .withIssuer(ISSUER) //签发人
                .withIssuedAt(now) //签发时间
                .withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME)) //过期时间
                .withClaim("username", username) //保存身份标识
                .withClaim("userid", userid) //用户主键
                .withClaim("companid", companid) //用户主键
                .withClaim("companname", "ennova") //公司名称
                .sign(algorithm);
        return token;
    }

    /**
     * Web端生成token，用于后台管理的操作日志记录
     * @param username
     * @param userid
     * @return
     */
    public static String generateTokenForLog(String username, Integer userid, String companid){
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法

        String token = JWT.create()
                .withIssuer(ISSUER) //签发人
                .withIssuedAt(now) //签发时间
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXPIRE_TIME)) //过期时间
                .withClaim("username", username) //保存身份标识
                .withClaim("userid", userid) //用户主键
                .withClaim("companid", companid) //用户主键
                .sign(algorithm);
        return token;
    }


    /**
     * 验证token
     */
    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); //算法
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 从token获取username
     */
    public static String getUsername(String token){
        try{
            return JWT.decode(token).getClaim("username").asString();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 从token获取username
     */
    public static UserVO getUserVOByToken(String token){
        try{
            UserVO userVO = new UserVO();
            String username = JWT.decode(token).getClaim("username").asString();
            String companyid = JWT.decode(token).getClaim("companid").asString();
            Integer userid = JWT.decode(token).getClaim("userid").asInt();
            userVO.setId(userid);
            userVO.setUsername(username);
            userVO.setCompany(companyid);
            return userVO;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
