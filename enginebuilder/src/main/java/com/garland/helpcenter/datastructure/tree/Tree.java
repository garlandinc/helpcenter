package com.garland.helpcenter.datastructure.tree;

import java.io.Serializable;

import static com.garland.helpcenter.datastructure.tree.Color.BLACK;
import static com.garland.helpcenter.datastructure.tree.Color.RED;

/**
 * Created by lemon on 11/3/2017.
 */

public class Tree<K extends Comparable<K>> implements Serializable {
    private static final long serialVersionUID="Tree".hashCode();
    private final Node<K> nil=new Node<>();
    private int size;

    Node<K> root;

    public Tree() {
        root=nil;
        root.left=nil;
        root.right=nil;
        root.parent=nil;
        this.size=0;
    }

    private boolean isNil(Node<K> node) {
        return node==nil;
    }

    public void insert(K data) {
        this.insert(new Node<K>(data));
    }

    public void insert(Node<K> node) {
        Node<K> yNode=nil;
        Node<K> xNode=root;

        while(!isNil(xNode)) {
            yNode=xNode;
            if(node.data.compareTo(xNode.data)<0) 
                xNode=xNode.left;
            else xNode=xNode.right;
        }
        node.parent=yNode;
        if(isNil(yNode)) 
            root=node;
        else if(node.data.compareTo(yNode.data)<0) 
            yNode.left=node;
        else yNode.right=node;

        node.left=nil;
        node.right=nil;
        node.color=RED;
        insertFixUp(node);
        this.size++;
    }

    public void insertFixUp(Node<K> node) {
        Node<K> yNode=nil;
        while(node.parent.color==RED) {
            if(node.parent.equals(node.parent.parent.left)){
                yNode=node.parent.parent.right;
                if(yNode.color==RED){
                    node.parent.color=BLACK;
                    yNode.color=BLACK;
                    node.parent.parent.color=RED;
                    node=node.parent.parent;
                }
                else if(node.equals(node.parent.right)){
                    node=node.parent;
                    leftRotate(node);
                }
                else {
                    node.parent.color=BLACK;
                    node.parent.parent.color=RED;
                    rightRotate(node.parent.parent);
                }
            }

            else {
                yNode=node.parent.parent.right;
                if(yNode.color=RED) {
                    node.parent.color=BLACK;
                    yNode.color=BLACK;
                    node.parent.parent.color=RED;
                    node=node.parent.parent;
                }
                else if(node==node.parent.left) {
                    node=node.parent;
                    rightRotate(node);
                }
                else {
                    node.parent.color=BLACK;
                    node.parent.parent.color=RED;
                    leftRotate(node.parent.parent);
                }
            }
        }

        root.color=BLACK;
    }

    public void leftRotate(Node<K> xNode) {
        Node<K> yNode=xNode.right;
        xNode.right=yNode.left;
        if(!isNil(yNode.left))
            yNode.left.parent=xNode;
        yNode.parent=xNode.parent;
        if(isNil(xNode.parent))
            root=yNode;
        else if(xNode.equals(xNode.parent.left))
            xNode.parent.left=yNode;
        else xNode.parent.right=yNode;
        yNode.left=xNode;
        xNode.parent=yNode;
    }

    public void rightRotate(Node<K> yNode) {
        Node<K> xNode=yNode.left;
        yNode.left=xNode.right;
        if(!isNil(xNode.right))
            xNode.right.parent=yNode;
        xNode.parent=yNode.parent;
        if(isNil(yNode.parent))
            root=xNode;
        else if(yNode==yNode.parent.left)
            yNode.parent.left=xNode;
        else yNode.parent.right=xNode;
        xNode.right=yNode;
        yNode.parent=xNode;
    }

    public Node<K> search(K data) {
        return find(root,data);
    }

    private Node<K> find(Node<K> node, K data) {
        int cmp=data.compareTo(node.data);
        if(cmp==0) return node;
        if(cmp<0) {
            if(isNil(node.left)) return null;
            return find(node.left, data);
        }
        if(isNil(node.right)) return null;
		return find(node.right, data);
	}

    public Node<K> search(NodeCallback<K> callback) {
        return find(root,callback);
    }

	private Node<K> find(Node<K> node, NodeCallback<K> callback) {
        int cmp=callback.compare(node.data);
        if(cmp==0) return node;
        if(cmp<0){
            if(isNil(node.left)) return null;
            return find(node.left, callback);
        }
        if(isNil(node.right)) return null;
        return find(node.right, callback);
    }
    
    public int size() {
        return size;
    }
}