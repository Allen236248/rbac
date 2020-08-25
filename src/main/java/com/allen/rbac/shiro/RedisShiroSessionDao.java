package com.allen.rbac.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

import java.io.Serializable;

public class RedisShiroSessionDao extends CachingSessionDAO {

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        RedisShiroCache cache = (RedisShiroCache) getActiveSessionsCache();
        if(null != cache) {
            cache.setExpire(session.getTimeout());
            cache(session, sessionId);
        }
        return sessionId;
    }

    @Override
    protected void doUpdate(Session session) {
        //do nothing - 父类已更新缓存
    }

    @Override
    protected void doDelete(Session session) {
        //do nothing - 父类已删除缓存
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        //do nothing - 父类已从缓存中获取Session
        return null;
    }
}
