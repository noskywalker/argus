package com.monitor.argus.monitor.tomcat.topology;

import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.monitor.tomcat.bolts.LogNormalizeBolts;
import com.monitor.argus.monitor.tomcat.spout.MessageScheme;
import org.apache.commons.lang.StringUtils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2016/7/17.
 */
public class TopoLogyPortal {
    static Logger logger = LoggerFactory.getLogger(TopoLogyPortal.class);

    private static TopologyBuilder builder = new TopologyBuilder();

    static final String KAFKA_ZK_HOSTS = "kafka.zk.hosts";
    static final String KAFKA_TOPIC = "kafka.mq.topic";
    static final String KAFKA_ZK_ROOT = "kafka.mq.zk.node";
    static final String KAFKA_STORM_SPOUT_NAME = "kafka.mq.spoutname";

    public static void main(String[] args) throws IOException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {

        Properties properties = PropertiesLoaderUtils.loadAllProperties("config.properties");
        String zkHosts = properties.getProperty(KAFKA_ZK_HOSTS);
        String zkTopic = properties.getProperty(KAFKA_TOPIC);
        String zkRoot = properties.getProperty(KAFKA_ZK_ROOT);
        String zkSpoutName = properties.getProperty(KAFKA_STORM_SPOUT_NAME);

        String environMent=properties.getProperty("environment.mode");
        ArgusUtils.IS_DEBUG_MODE=ArgusUtils.ENVIRONMENT_MODE.equals(environMent);
        Assert.notNull(zkHosts);
        Assert.notNull(zkTopic);
        Assert.notNull(zkRoot);
        Assert.notNull(zkSpoutName);

        logger.info("kafka配置信息:zkHosts:{},topic:{},zkRoot:{},spoutName:{}", zkHosts, zkTopic, zkRoot, zkSpoutName);

        BrokerHosts brokerHosts = new ZkHosts(zkHosts);
        SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, zkTopic, zkRoot, zkSpoutName);

        List<String> zks = new ArrayList<>();
        for (String zk : zkHosts.split(",")) {
            Assert.notNull(zk);
            zks.add(zk.substring(0, zk.indexOf(":")));
            spoutConfig.zkPort = Integer.parseInt(zk.substring(zk.indexOf(":") + 1));
        }
        spoutConfig.zkServers = zks;
        spoutConfig.ignoreZkOffsets=true;
        //set the begging offset of consumer
        spoutConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
        Config config = new Config();

        spoutConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());
        builder.setSpout("spout", new KafkaSpout(spoutConfig), 6);
        //　　1、随机分组（Shuffer Grouping）：随机分发元组到Bolt的任务，保证每个任务获得相等数量的元组
        builder.setBolt("LogNormalizeBolts", new LogNormalizeBolts(), 18).shuffleGrouping(
                "spout");

        config.setDebug(false);

        //通过是否有参数来控制是否启动集群，或者本地模式执行

        if (args != null && args.length > 0) {
            try {
                if (StringUtils.isBlank(args[1]) || StringUtils.isBlank(args[0])) {
                    logger.error("storm集群运行报错,参数为 [jobName] [concurrentNumber] ");
                    logger.warn("please run: [storm jar argus.jar TopoLogyPortal argusTopology 10]");
                    return;
                }
                config.setNumWorkers(Integer.parseInt(args[1]));
                StormSubmitter.submitTopology(args[0], config,
                        builder.createTopology());
            } catch (Exception e) {
                logger.warn("storm集群运行报错，详细信息如下：{}", e.getMessage());
                e.printStackTrace();
                throw e;
            }
        } else {
            config.setMaxTaskParallelism(1);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("logmonitor", config, builder.createTopology());
            try {
                Thread.sleep(1000 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
