package com.monitor.argus.mis.system;

import com.sun.management.OperatingSystemMXBean;
import com.monitor.argus.common.util.IpUtil;
import org.springframework.stereotype.Component;

import java.lang.management.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huxiaolei on 2016/11/4.
 */
@Component("systemInfosLoad")
public class SystemIPJvmInfoLoad {

    long use = 0;
    long start = 0;
    ConcurrentHashMap<String, Double> gcinfo = new  ConcurrentHashMap<String, Double>();

    public SystemIPJvmInfo appendJvmInfo() {
        SystemIPJvmInfo systemIPJvmInfo = new SystemIPJvmInfo();
        try {
            // 获取系统jvm信息
            final CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
            final OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            //内存总览
            final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

            // 采集信息
            systemIPJvmInfo.setSystemId("-1");
            systemIPJvmInfo.setIp(IpUtil.localIp());

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-COMPILE")
                    .addJvmMetricsTag(new JvmMetricsTag("MX-COMPILE", compilationMXBean.getTotalCompilationTime())));

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-SYS-LOAD")
                    .addJvmMetricsTag(new JvmMetricsTag("MX-SYS-LOAD", operatingSystemMXBean.getSystemLoadAverage())));

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-CPU-LOAD")
                    .addJvmMetricsTag(new JvmMetricsTag("MX-CPU-LOAD", operatingSystemMXBean.getProcessCpuLoad())));

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("CPU-USE")
                    .addJvmMetricsTag(new JvmMetricsTag("CPU-USE", CPUUSE(operatingSystemMXBean))));

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("SWAP")
                    .addJvmMetricsTag(new JvmMetricsTag("free", operatingSystemMXBean.getFreeSwapSpaceSize() / 1000000))
                    .addJvmMetricsTag(new JvmMetricsTag("total", operatingSystemMXBean.getTotalSwapSpaceSize() / 1000000)));

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("SYS-MEMORY")
                    .addJvmMetricsTag(new JvmMetricsTag("virtual_mem", operatingSystemMXBean.getCommittedVirtualMemorySize() / 1000000))
                    .addJvmMetricsTag(new JvmMetricsTag("free_phy_mem", operatingSystemMXBean.getFreePhysicalMemorySize() / 1000000))
                    .addJvmMetricsTag(new JvmMetricsTag("total_phy_mem", operatingSystemMXBean.getTotalPhysicalMemorySize() / 1000000)));

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-THREAD")
                    .addJvmMetricsTag(new JvmMetricsTag("wait", MXTHREADwait(threadMXBean)))
                    .addJvmMetricsTag(new JvmMetricsTag("all", threadMXBean.getThreadCount()))
                    .addJvmMetricsTag(new JvmMetricsTag("demon", threadMXBean.getDaemonThreadCount())));

            // 内存详细分块信息
            List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
            for (final MemoryPoolMXBean bean : list) {
                systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-MEMORY-POOL-" + bean.getName())
                        .addJvmMetricsTag(new JvmMetricsTag("used", bean.getUsage().getUsed() / 1000000))
                        .addJvmMetricsTag(new JvmMetricsTag("max", bean.getUsage().getMax() / 1000000)));
            }

            //内存总览
            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-MEMORY-heap")
                    .addJvmMetricsTag(new JvmMetricsTag("used", memoryMXBean.getHeapMemoryUsage().getUsed() / 1000000))
                    .addJvmMetricsTag(new JvmMetricsTag("max", memoryMXBean.getHeapMemoryUsage().getMax() / 1000000)));

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-MEMORY-non-heap")
                    .addJvmMetricsTag(new JvmMetricsTag("used", memoryMXBean.getNonHeapMemoryUsage().getUsed() / 1000000))
                    .addJvmMetricsTag(new JvmMetricsTag("max", memoryMXBean.getNonHeapMemoryUsage().getMax() / 1000000)));

            //gc信息
            List<GarbageCollectorMXBean> gcList = ManagementFactory.getGarbageCollectorMXBeans();
            for (final GarbageCollectorMXBean bean : gcList) {
                String gcCountKey = "MX-GC-COUNT-" + bean.getName();
                String gcTimeKey = "MX-GC-TIME-" + bean.getName();
                if (!gcinfo.containsKey(gcCountKey)) {
                    gcinfo.put(gcCountKey, new Double(0));
                }
                if (!gcinfo.containsKey(gcTimeKey)) {
                    gcinfo.put(gcTimeKey, new Double(0));
                }
                systemIPJvmInfo.addJvmMetrics(new JvmMetrics(gcCountKey)
                        .addJvmMetricsTag(new JvmMetricsTag(gcCountKey, MXGCCOUNT(gcCountKey, bean))));
                systemIPJvmInfo.addJvmMetrics(new JvmMetrics(gcTimeKey)
                        .addJvmMetricsTag(new JvmMetricsTag(gcTimeKey, MXGCTIME(gcTimeKey, bean))));
            }

            systemIPJvmInfo.addJvmMetrics(new JvmMetrics("MX-CLASS-LOADER")
                    .addJvmMetricsTag(new JvmMetricsTag("Load", classLoadingMXBean.getLoadedClassCount()))
                    .addJvmMetricsTag(new JvmMetricsTag("TotalLoad", classLoadingMXBean.getTotalLoadedClassCount()))
                    .addJvmMetricsTag(new JvmMetricsTag("Unload", classLoadingMXBean.getUnloadedClassCount())));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemIPJvmInfo;
    }

    private double MXGCCOUNT(String gcCountKey, GarbageCollectorMXBean bean) {
        Double count = gcinfo.get(gcCountKey);
        double curr = bean.getCollectionCount();
        double ret = curr - count.doubleValue();
        gcinfo.put(gcCountKey, Double.valueOf(curr));
        return curr;
    }

    private double MXGCTIME(String gcTimeKey, GarbageCollectorMXBean bean) {
        Double time = gcinfo.get(gcTimeKey);
        double curr = bean.getCollectionTime();
        double ret = curr - time.doubleValue();
        gcinfo.put(gcTimeKey, Double.valueOf(curr));
        return curr;
    }


    private double CPUUSE(OperatingSystemMXBean operatingSystemMXBean) {
        long curr = System.nanoTime();
        long useC = operatingSystemMXBean.getProcessCpuTime();
        double r = (useC - use) * 1.0 / (curr - start);
        use = useC;
        start = curr;
        return r * 100;
    }

    private double MXTHREADwait(ThreadMXBean threadMXBean) {
        try {
            long[] ret = threadMXBean.findDeadlockedThreads();
            if (ret == null) {
                return 0;
            }
            return ret.length;
        } catch (Exception e) {
            return 0;
        }
    }

}
