package com.areaofit.app.ftp.model;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 
 * @Description 对象池--掌管对象的生命周期，获取，激活，验证，钝化，销毁等
 * @Author Huangjinwen
 * @Date 2017年12月8日-上午9:44:28
 */
public abstract class Pool<T> {
	
	// 通用对象池
	private final GenericObjectPool<T> internalPool;
	
	public Pool(GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {
		this.internalPool = new GenericObjectPool<>(factory, poolConfig);
	}
	
	/**
	 * 获取池中资源
	 * @return
	 */
	public T getResource() {
		try {
			return this.internalPool.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 归还池中的资源
	 * @param resource
	 */
	public void returnResource(T resource) {
		try {
			this.internalPool.returnObject(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭池连接，释放池中资源
	 */
	public void destory() {
		try {
			this.internalPool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回池中空闲的资源个数
	 * @return
	 */
	public int inPoolSize() {
		try {
			return this.internalPool.getNumIdle();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 返回正在使用的池资源个数
	 * @return
	 */
	public int borrowSize() {
		try {
			return this.internalPool.getNumActive();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
