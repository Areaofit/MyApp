package com.areaofit.app.zookeeper.origin;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperWatch implements Watcher {

	AtomicInteger atomicInteger = new AtomicInteger();

	public static final String PARENT_NODE = "/Parent";

	public static final String CHILD_NODE = "/Parent/Child";

	private static final String LOG_PREFIX_OF_MAIN = "【Main】";

	private ZooKeeper zk = null;

	private CountDownLatch countDownLatch = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent event) {
		System.out.println("\n+++++++++++++++++++++++++++++++++++++++++++++\n");
		System.out.println("进入 process 方法：event = " + event);
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (event == null) {
			return;
		}
		KeeperState keeperState = event.getState();
		EventType eventType = event.getType();
		// 受影响的path
		String path = event.getPath();
		String logPrefix = "【Watcher-" + this.atomicInteger.incrementAndGet() + "】";
		System.out.println(logPrefix + "收到Watcher通知");
		System.out.println(logPrefix + "连接状态:\t" + keeperState.toString());
		System.out.println(logPrefix + "事件类型:\t" + eventType.toString());
		if (KeeperState.SyncConnected == keeperState) {
			if (EventType.None == eventType) {
				System.out.println(logPrefix + "成功连接上ZK服务器");
				countDownLatch.countDown();
			} else if (EventType.NodeCreated == eventType) {
				System.out.println(logPrefix + "节点创建");
				// TODO
			} else if (EventType.NodeDataChanged == eventType) {
				System.out.println(logPrefix + "节点数据更新");
				System.out.println("\n=========我看看走不走这里==========\n");
				// TODO
			} else if (EventType.NodeChildrenChanged == eventType) {
				System.out.println(logPrefix + "子节点变更");
				// TODO
			} else if (EventType.NodeDeleted == eventType) {
				System.out.println(logPrefix + "节点 " + path + " 被删除");
			}
		} else if (KeeperState.Expired == keeperState) {
			System.out.println(logPrefix + "会话失效");
		} else if (KeeperState.AuthFailed == keeperState) {
			System.out.println(logPrefix + "权限检查失败");
		} else if (KeeperState.Disconnected == keeperState) {
			System.out.println(logPrefix + "与ZK服务器断开连接");
		}
		System.out.println("\n-------------------------------------------\n");
	}

	/**
	 * 连接zookeeper服务器
	 * 
	 * @param addr
	 * @param sessionTimeOut
	 */
	public void connection(String addr, int sessionTimeOut) {
		try {
			zk = new ZooKeeper(addr, sessionTimeOut, this);
			System.out.println(LOG_PREFIX_OF_MAIN + "开始连接ZK服务器");
			countDownLatch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开zookeeper服务器
	 */
	public void disConnection() {
		try {
			if (zk != null) {
				zk.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建节点
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	public boolean createNode(String path, String data) {
		try {
			// 设置监控(由于zookeeper的监控都是一次性的,所以每次必须设置监控)
			this.zk.exists(path, true);
			System.out.println(LOG_PREFIX_OF_MAIN + "节点创建成功, Path: "
					+ this.zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT) + ", content: "
					+ data);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 读取节点的数据
	 * 
	 * @param path
	 * @param needWatch
	 * @return
	 */
	public String readData(String path, boolean needWatch) {
		try {
			return new String(new String(this.zk.getData(path, needWatch, null)));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 更新节点数据
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	public boolean writeData(String path, String data) {
		try {
			System.out.println(LOG_PREFIX_OF_MAIN + "更新数据成功，path：" + path + ", stat: "
					+ this.zk.setData(path, data.getBytes(), -1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除节点
	 * 
	 * @param path
	 */
	public void deleteNode(String path) {
		try {
			this.zk.delete(path, -1);
			System.out.println(LOG_PREFIX_OF_MAIN + "删除节点成功，path：" + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断节点是否存在
	 * 
	 * @param path
	 * @param needWatch
	 * @return
	 */
	public Stat exists(String path, boolean needWatch) {
		try {
			return this.zk.exists(path, needWatch);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取子节点
	 * 
	 * @param path
	 * @param needWatch
	 * @return
	 */
	private List<String> getChildren(String path, boolean needWatch) {
		try {
			return this.zk.getChildren(path, needWatch);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除所有节点
	 */
	public void deleteAllTestPath() {
		if (this.exists(CHILD_NODE, true) != null) {
			this.deleteNode(CHILD_NODE);
		}
		if (this.exists(PARENT_NODE, true) != null) {
			this.deleteNode(PARENT_NODE);
		}
	}

	public static void main(String[] args) throws Exception {
		ZookeeperWatch zookeeperWatch = new ZookeeperWatch();
		zookeeperWatch.connection(ZookeeperBase.CONNECTION_ADDR, ZookeeperBase.SESSION_TIMEOUT);
		Thread.sleep(1000);
		zookeeperWatch.deleteAllTestPath();
		if (zookeeperWatch.createNode(PARENT_NODE, System.currentTimeMillis() + "")) {
			Thread.sleep(1000);

			// 更新数据
			zookeeperWatch.writeData(PARENT_NODE, System.currentTimeMillis() + "");
			// 读取数据
			System.out.println("\n【【【【read parent】】】】");
			System.out.println(zookeeperWatch.readData(PARENT_NODE, true));

			Thread.sleep(1000);

			// 创建子节点
			zookeeperWatch.createNode(CHILD_NODE, System.currentTimeMillis() + "");

			Thread.sleep(1000);

			zookeeperWatch.writeData(CHILD_NODE, System.currentTimeMillis() + "");
			// 读取子节点
			System.out.println("\n【【【【read children path】】】】");
			System.out.println(zookeeperWatch.readData(PARENT_NODE, true));
		}
		zookeeperWatch.deleteAllTestPath();
		Thread.sleep(1000);
		zookeeperWatch.disConnection();
	}
}
