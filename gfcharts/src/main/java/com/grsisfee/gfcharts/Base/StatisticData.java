package com.grsisfee.gfcharts.Base;

/**
 * Author: GrsisFee
 * Date:   2016/2/2
 * Desc:
 * All rights reserved.
 */
public class StatisticData<K, V> {

    public StatisticData(K k, V v) {
        //初始化key和value
        key = k;
        value = v;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K k) {
        key = k;
    }

    public void setValue(V v) {
        value = v;
    }

    public Class getKeyClass() {
        return key.getClass();
    }

    public Class getValueClass() {
        return value.getClass();
    }

    private K key;
    private V value;
}
