package com.github.zklock;

import java.util.Collections;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by apa7 on 2019/2/26.
 */
public class ZkApplication {

    public static void main(String[] args) {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.12.100:2181", retryPolicy);
        client.start();

        //创建分布式锁, 锁控件的根节点路径为"/curator/lock"
        InterProcessMultiLock lock = new InterProcessMultiLock(client, Collections.singletonList("/curator/lock"));
        try {
            lock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获得锁, 进行业务流程
        System.out.println("Enter MutexLock");
        //完成, 释放锁
        try {
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关闭客户端
        client.close();
    }

}
