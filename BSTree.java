import java.io.*;

class BSTNode {  //class Node
    private String key;
    private BSTNode parent;
    private BSTNode leftChild;
    private BSTNode rightChild;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public BSTNode getParent() {
        return parent;
    }
    public void setParent(BSTNode parent) {
        this.parent = parent;
    }

    public BSTNode getLeftChild() {
        return leftChild;
    }
    public void setLeftChild(BSTNode leftChild) {
        this.leftChild = leftChild;
    }

    public BSTNode getRightChild() {
        return rightChild;
    }
    public void setRightChild(BSTNode rightChild) {
        this.rightChild = rightChild;
    }
}

class BSTree { //Binary Search Tree class with main operations like Insert, Delete, Search etc.
    private BSTNode root;

    public BSTree() {
        this.root = null;
    }
    public void setRoot(BSTNode root) { //Setters and Getters
        this.root = root;
    }
    public BSTNode getRoot() {
        return this.root;
    }
    private BSTNode helpFindSuccessor(BSTNode node) {
        if (node == null) {
            return null;
        }
        while (node.getLeftChild() != null) {
            node = node.getLeftChild();
        }
        return node;
    }
    public String searchNode1(String key) { //returns only the definition of the word searched   
        BSTNode temp = this.root;
        String wholeLine[] = new String[2];
        wholeLine = temp.getKey().split(" - ", 2);
        while (temp != null && !wholeLine[0].equalsIgnoreCase(key)) {
            if (key.compareToIgnoreCase(temp.getKey()) <= 0) {
                temp = temp.getLeftChild();
                wholeLine = temp.getKey().split(" - ", 2);
            } else {
                temp = temp.getRightChild();
                wholeLine = temp.getKey().split(" - ", 2);
            }
        }
        return wholeLine[1]; //returns definition only
    }
    public BSTNode searchNode2(String key) { //returns a BSTNode object (Not a string)
        BSTNode temp = this.root;            //This methos is used in getPredecessor() & getPredecessor() methods only
        while (temp != null && !temp.getKey().equalsIgnoreCase(key)) {
            if (key.compareToIgnoreCase(temp.getKey()) <= 0) {
                temp = temp.getLeftChild();
            } else {
                temp = temp.getRightChild();
            }
        }
        return temp;
    }

    // this method is used to find the successor node of the node with the given input key
    public BSTNode getSuccessor(String key) {
        BSTNode node = searchNode2(key);
        if (node == null) {
            return null;
        }
        if (node.getRightChild() != null) {
            return helpFindSuccessor(node.getRightChild());
        }
        BSTNode successorNode = node.getParent();
        while (successorNode != null && successorNode.getLeftChild() != node) {
            node = successorNode;
            successorNode = successorNode.getParent();
        }
        return successorNode;
    }

    // this private method helps us in find the predecessor node in the left subtree of a binary search tree
    private BSTNode helpFindPredecessor(BSTNode node) {
        if (node == null) {
            return null;
        }
        while (node.getRightChild() != null) {
            node = node.getRightChild();
        }
        return node;
    }

    // this method is used to find the predecessor node of the node with the given input key
    public BSTNode getPredecessor(String key) {
        BSTNode node = searchNode2(key);
        if (node == null) {
            return null;
        }
        if (node.getLeftChild() != null) {
            return helpFindPredecessor(node.getLeftChild());
        }
        BSTNode predecessorNode = node.getParent();
        while (predecessorNode != null && node != predecessorNode.getRightChild()) {
            node = predecessorNode;
            predecessorNode = predecessorNode.getParent();
        }
        return predecessorNode;
    }

    public void insertNode(String value) { //Insert a node in to BST
        BSTNode node = new BSTNode();
        node.setKey(value);
        node.setParent(null);
        node.setLeftChild(null);
        node.setRightChild(null);

        if (this.root == null) {
            this.root = node;
        } else {
            BSTNode parentNode = null;
            BSTNode temp = this.root;
            while (temp != null) {
                parentNode = temp;
                int compareValue = node.getKey().compareToIgnoreCase(temp.getKey());
                if (compareValue <= 0) {
                    temp = temp.getLeftChild();
                } else {
                    temp = temp.getRightChild();
                }
            }

            node.setParent(parentNode);
            if (node.getKey().compareToIgnoreCase(parentNode.getKey()) <= 0) {
                parentNode.setLeftChild(node);
            } else {
                parentNode.setRightChild(node);
            }
        }
    }
    public void updateTextFile(BSTNode node) { //To update the text file once a node is deleted form BST.
        if (node == null) {
            return;
        }
        updateTextFile(node.getLeftChild());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("input.txt", true));
            bw.write(node.getKey());
            bw.newLine();
            bw.close();

        } catch (Exception f) {
            System.out.println(f);
        }
        updateTextFile(node.getRightChild());
    }
    public void deleteNode(BSTNode node) {
        // check if the node to be deleted is a valid reference
        if (node == null) {
            return;
        }

        // Case-1 : If the node to be deleted has no children
        if (node.getLeftChild() == null && node.getRightChild() == null) {
            BSTNode parentNode = node.getParent();
            // if the node to be deleted is the root node
            if (parentNode == null) {
                this.root = null;
            } else if (parentNode.getLeftChild() == node) {
                parentNode.setLeftChild(null);
            } else {
                parentNode.setRightChild(null);
            }
            node.setParent(null);
        }

        // Case-2 : If the node to be deleted has only a one child
        if (node.getLeftChild() != null && node.getRightChild() == null) {
            BSTNode parentNode = node.getParent();
            // if the node to be deleted is the root node and it has a left child then make the left child of the root node as root
            if (parentNode == null) {
                this.root = node.getLeftChild();
            } else {
                // if the node to be deleted is the left child of its parent node
                if (parentNode.getLeftChild() == node) {
                    parentNode.setLeftChild(node.getLeftChild());
                } else {
                    parentNode.setRightChild(node.getLeftChild());
                }
            }
            node.getLeftChild().setParent(parentNode);
            node.setParent(null);
            node.setLeftChild(null);
        }

        if (node.getLeftChild() == null && node.getRightChild() != null) {
            BSTNode parentNode = node.getParent();
            // if the node to be deleted is the root node and it has a right child
            if (parentNode == null) {
                this.root = node.getRightChild();
            } else {
                // if the node to be deleted is the left child of its parent node
                if (parentNode.getLeftChild() == node) {
                    parentNode.setLeftChild(node.getRightChild());
                } else {
                    parentNode.setRightChild(node.getRightChild());
                }
            }
            node.getRightChild().setParent(parentNode);
            node.setParent(null);
            node.setRightChild(null);
        }

        // Case-3 : if the node to be deleted has both a left and a right child
        if (node.getLeftChild() != null && node.getRightChild() != null) {
            BSTNode parentNode = node.getParent();

            // first we get the successor of the node in the Binary Search Tree
            BSTNode successorNode = getSuccessor(node.getKey());
            BSTNode successorParent = successorNode.getParent();
            BSTNode successorRightChild = successorNode.getRightChild();

            // if the successor node doesn't have any right child, it obviously doesn't have any left child as its the successor node
            if (successorRightChild == null) {
                node.setKey(successorNode.getKey());
                if (successorParent.getRightChild() == successorNode) {
                    successorParent.setRightChild(null);
                } else {
                    successorParent.setLeftChild(null);
                }
                return;
            } else {
                node.setKey(successorNode.getKey());
                if (successorParent.getRightChild() == successorNode) {
                    successorParent.setRightChild(successorRightChild);
                } else {
                    successorParent.setLeftChild(successorRightChild);
                }
            }
            successorRightChild.setParent(successorParent);
            successorNode.setParent(null);
            successorNode.setLeftChild(null);
            successorNode.setRightChild(null);
        }
    }
}