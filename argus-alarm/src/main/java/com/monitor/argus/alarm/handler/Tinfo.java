package com.monitor.argus.alarm.handler;

import com.monitor.argus.common.util.JsonUtil;

class Tinfo{
            private String queue_name;
            private String begin;

            public String getQueue_name() {
                return queue_name;
            }

            public void setQueue_name(String queue_name) {
                this.queue_name = queue_name;
            }

            public String getBegin() {
                return begin;
            }

            public void setBegin(String begin) {
                this.begin = begin;
            }
            public String toString(){
                return JsonUtil.beanToJson(this);
            }
        }