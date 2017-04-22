package com.tuzhi.app.util;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
/**
 * @Description: 
 * @company: 
 * @author: weifengnian
 * @Data: 2017年4月22日	
 * @Copyright:
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {
	// 日志对象
	private static final Logger logger = Logger.getLogger("Daolog");
	/*
	 * (non-Javadoc)
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)
	 */
	@SuppressWarnings("rawtypes")
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler);

		RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
		if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
			//if (logger.isDebugEnabled()) {
				BoundSql boundSql = statementHandler.getBoundSql();
				logger.info("生成SQL(拦截器) : \n" + boundSql.getSql());
			//}
			return invocation.proceed();
		}

		DefaultParameterHandler defaultParameterHandler = (DefaultParameterHandler) metaStatementHandler.getValue("delegate.parameterHandler");
		Map parameterMap = (Map) defaultParameterHandler.getParameterObject();
		Object sidx = parameterMap.get("_sidx");
		Object sord = parameterMap.get("_sord");

		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");

		if (sidx != null && sord != null) {
			originalSql = originalSql + " order by " + sidx + " " + sord;
		}

		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		String confDialect = null;
		try
		{
			confDialect = configuration.getVariables().getProperty("dialect").toLowerCase();
		} catch (Exception e) {
			// ignore
		}
		if (confDialect == null) {
			throw new RuntimeException("the value of the dialect property in configuration.xml is not defined : " + configuration.getVariables().getProperty("dialect"));
		}
		Dialect dialect = null;
		if ("sqlserver".equals(confDialect)){dialect = new SQLServerDialect();}
		
		metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
		metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		//if (logger.isDebugEnabled()) {
			BoundSql boundSql = statementHandler.getBoundSql();
			logger.info("生成分页SQL(拦截器) : \n" + boundSql.getSql());
		//}
		return invocation.proceed();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub

	}

}
