package com.huch.common.tree;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huchanghua
 * @create 2019-01-31-16:49
 */

public class TreeNodeTest {

    private Integer id;
    private Integer pid;
    private String name;
    private List<TreeNodeTest> children;

    TreeNodeTest(Integer id, Integer pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNodeTest> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeTest> children) {
        this.children = children;
    }

    public static void main(String[] args) {
        List<TreeNodeTest> list = new ArrayList<TreeNodeTest>();
        list.add(new TreeNodeTest(1, 0, "1"));
        list.add(new TreeNodeTest(2, 0, "2"));
        list.add(new TreeNodeTest(3, 2, "3"));
        list.add(new TreeNodeTest(4, 3, "4"));
        list.add(new TreeNodeTest(5, 4, "5"));
        list.add(new TreeNodeTest(6, 5, "6"));

        List<TreeNodeTest> treeList = new ArrayList<TreeNodeTest>();
        List<TreeNodeTest> treeList1 = new ArrayList<TreeNodeTest>();
        List<TreeNodeTest> treeList2 = new ArrayList<TreeNodeTest>();
        List<TreeNodeTest> treeList3 = new ArrayList<TreeNodeTest>();
        //方法一、
        treeList = listGetStree(list);
        treeList1 = listToTree(list);
        treeList2 = toTree(list);

        System.out.println(JSON.toJSONString(treeList));
        System.out.println(JSON.toJSONString(treeList));
        System.out.println(JSON.toJSONString(treeList));
    }

    private static List<TreeNodeTest> listGetStree(List<TreeNodeTest> list) {
        List<TreeNodeTest> treeList = new ArrayList<TreeNodeTest>();
        for (TreeNodeTest tree : list) {
            //找到根
            if (tree.getPid() == 0) {
                treeList.add(tree);
            }
            //找到子
            for (TreeNodeTest treeNodeTest : list) {
                if (treeNodeTest.getPid() == tree.getId()) {
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<TreeNodeTest>());
                    }
                    tree.getChildren().add(treeNodeTest);
                }
            }
        }
        return treeList;
    }

    /**
     * 方法二、
     * @param list
     * @return
     */
    public static List<TreeNodeTest> listToTree(List<TreeNodeTest> list) {
        //用递归找子。
        List<TreeNodeTest> treeList = new ArrayList<TreeNodeTest>();
        for (TreeNodeTest tree : list) {
            if (tree.getPid() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private static TreeNodeTest findChildren(TreeNodeTest tree, List<TreeNodeTest> list) {
        for (TreeNodeTest node : list) {
            if (node.getPid() == tree.getId()) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<TreeNodeTest>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }

    /**
     *方法三
     * @param list
     * @return
     */
    private static List<TreeNodeTest> toTree(List<TreeNodeTest> list) {
        List<TreeNodeTest> treeList = new ArrayList<TreeNodeTest>();
        for (TreeNodeTest tree : list) {
            if(tree.getPid() == 0){
                treeList.add(tree);
            }
        }
        for (TreeNodeTest tree : list) {
            toTreeChildren(treeList,tree);
        }
        return treeList;
    }

    private static void toTreeChildren(List<TreeNodeTest> treeList, TreeNodeTest tree) {
        for (TreeNodeTest node : treeList) {
            if(tree.getPid() == node.getId()){
                if(node.getChildren() == null){
                    node.setChildren(new ArrayList<TreeNodeTest>());
                }
                node.getChildren().add(tree);
            }
            if(node.getChildren() != null){
                toTreeChildren(node.getChildren(),tree);
            }
        }
    }


}
