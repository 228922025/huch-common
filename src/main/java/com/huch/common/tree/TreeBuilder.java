package com.huch.common.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huchanghua
 * @create 2019-01-31-19:18
 */
public class TreeBuilder {


    /**
     * 两层循环实现建树
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static List<TreeNode> build(List<TreeNode> treeNodes, String pid) {
        List<TreeNode> trees = new ArrayList<TreeNode>();
        for (TreeNode treeNode : treeNodes) {
            if (pid.equals(treeNode.getPid())) {
                trees.add(treeNode);
            }

            for (TreeNode it : treeNodes) {
                if (it.getPid().equals(treeNode.getId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 使用递归方法建树
     * @param treeNodes
     * @return
     */
    public static List<TreeNode> buildByRecursive(List<TreeNode> treeNodes, String pid) {
        List<TreeNode> trees = new ArrayList<TreeNode>();
        for (TreeNode treeNode : treeNodes) {
            if (pid.equals(treeNode.getPid())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static TreeNode findChildren(TreeNode treeNode, List<TreeNode> treeNodes) {
        for (TreeNode it : treeNodes) {
            if (treeNode.getId().equals(it.getPid())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeNode>());
                }
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * trees转list
     * @param list
     * @param trees
     */
    public static void iteratorTree(List<TreeNode> list, List<TreeNode> trees) {
        for (TreeNode node : trees) {
            list.add(node);
            if(node.getChildren() != null){
                iteratorTree(list, node.getChildren());
                node.setChildren(null);
            }
        }
    }

    public static void main(String[] args) {
        TreeNode treeNode1 = new TreeNode("1", "0", "广州");
        TreeNode treeNode2 = new TreeNode("2", "0", "深圳");

        TreeNode treeNode3 = new TreeNode("3", "天河区", treeNode1);
        TreeNode treeNode4 = new TreeNode("4", "越秀区", treeNode1);
        TreeNode treeNode5 = new TreeNode("5", "黄埔区", treeNode1);
        TreeNode treeNode6 = new TreeNode("6", "石牌", treeNode3);
        TreeNode treeNode7 = new TreeNode("7", "百脑汇", treeNode6);

        TreeNode treeNode8 = new TreeNode("8", "南山区", treeNode2);
        TreeNode treeNode9 = new TreeNode("9", "宝安区", treeNode2);
        TreeNode treeNode10 = new TreeNode("10", "科技园", treeNode8);

        List<TreeNode> list = new ArrayList<TreeNode>();

        list.add(treeNode1);
        list.add(treeNode2);
        list.add(treeNode3);
        list.add(treeNode4);
        list.add(treeNode5);
        list.add(treeNode6);
        list.add(treeNode7);
        list.add(treeNode8);
        list.add(treeNode9);
        list.add(treeNode10);

        List<TreeNode> trees = TreeBuilder.build(list, "0");

        List<TreeNode> list2 = new ArrayList<TreeNode>();
        iteratorTree(list2, trees);
        System.out.println(list2);

//        List<TreeNode> trees2 = TreeBuilder.buildByRecursive(list, "0");
//        System.out.println(gson.toJson(trees2));

//        System.out.println(list);
//        System.out.println(trees);

    }

    


}