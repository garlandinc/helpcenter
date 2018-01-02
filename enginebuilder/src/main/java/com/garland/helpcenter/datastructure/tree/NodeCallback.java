package com.garland.helpcenter.datastructure.tree;

/**
 * Created by lemon on 11/3/2017.
 */

public interface NodeCallback<K extends Comparable<K>> {
    int compare(K data);
}