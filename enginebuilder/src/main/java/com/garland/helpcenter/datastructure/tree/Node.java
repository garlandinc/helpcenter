package com.garland.helpcenter.datastructure.tree;

import java.io.Serializable;

import static com.garland.helpcenter.datastructure.tree.Color.BLACK;

/**
 * Created by lemon on 11/3/2017.
 */

public class Node<K extends Comparable<K>> implements Serializable {
    private static final long serialVersionUID="Node".hashCode();

    public Node<K> left,right,parent;
    public K data;
    public boolean color=BLACK;

    public Node() {
        this(null,null,null,null,BLACK);
    }

    public Node(K data) {
        this(null,null,null,data,BLACK);
    }

	public Node(Node<K> left, Node<K> right, Node<K> parent, K data,boolean color) {
        this.data=data;
        this.left=left;
        this.right=right;
        this.parent=parent;
        this.color=color;
    }
    
}