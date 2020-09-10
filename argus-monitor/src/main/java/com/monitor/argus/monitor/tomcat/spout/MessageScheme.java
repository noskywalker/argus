package com.monitor.argus.monitor.tomcat.spout;


import com.monitor.argus.common.util.ArgusUtils;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Administrator on 2016/7/17.
 */
public class MessageScheme implements org.apache.storm.spout.Scheme{
    @Override
    public List<Object> deserialize(ByteBuffer ser) {
        byte [] buffer=new byte[ser.remaining()];
        ser.get(buffer,0,buffer.length);
        String msg = null;
        try {
            msg = new String(buffer, "UTF-8");
            return new Values(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Fields getOutputFields() {
        return new Fields(ArgusUtils.ENTRY_LOG_KEY);
    }
}
