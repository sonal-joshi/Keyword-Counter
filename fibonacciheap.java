
/**
 * @author Sonal Joshi
 *
 */
public class fibonacciheap {
	private static final double phi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);

	private fibonode maxnode;
	private int nodecount;
	
	fibonacciheap(){
		this.maxnode = null;
		this.nodecount = 0;
	}
	/**
	 * @return the nodecount
	 */
	public int getNodecount() {
		return nodecount;
	}

	/**
	 * @param nodecount the nodecount to set
	 */
	public void setNodecount(int nodecount) {
		this.nodecount = nodecount;
	}
	
	/**
	 * @param node Insert the node in the heap
	 */
	public void insert(fibonode node) {
		if (maxnode != null) {
			node.right = maxnode;
			node.left = maxnode.left;
			maxnode.left = node;
			
			if(node.left != null) {
				node.left.right = node;
			}  
			if(node.left == null){
				node.left = maxnode;
				maxnode.right = node;
			} 
			if(node.getCount() > maxnode.getCount()) {
				maxnode = node;
			}
		} else
			maxnode = node;
		nodecount++;
	}
	
	public void merge(fibonode child, fibonode parent)
    {
        // remove child from root-sibling list of heap
        child.right.left = child.left;
        child.left.right = child.right;

        // make parent node parent of child node
        child.parent = parent;
        
        //if parent has no child, make  child node its first child. Else add child node to parent's child linked list.
        if (parent.child == null) {
            parent.child = child;
            child.right = child;
            child.left = child;
        } else {
            child.right = parent.child;
            child.left = parent.child.left;
            parent.child.left = child;
            child.left.right = child;
        }

        // increase degree of parent by 1
        parent.setDegree(parent.getDegree()+1);

        // make childcut of child as false
        child.setChildcut(false);
    }
	
	public void cut(fibonode child, fibonode parent) {
		//remove child node from its linkedlist 
		child.right.left = child.left;
		child.left.right = child.right;
		
		//decrement parent degree by 1
		parent.setDegree(parent.getDegree()-1);
		
		//update parent's child pointer
		if(parent.child == child) {
			parent.child = child.left;
		}
		 
		if(parent.getDegree() == 0) {
			parent.child = null;
		}
		//child node to parent node's linked list
		child.right = maxnode;
		child.left = maxnode.left;
		maxnode.left = child;
		child.left.right = child;
	    child.parent = null;
	    
	    //update child cut to false
	    child.setChildcut(false);
	}
	
	public void cascadingcut(fibonode currentNode) {
		fibonode parent = currentNode.parent;
		if(parent != null) {
			//if childcut is true, cut the node and cascade cut parent. Keep going up the tree until either a node with false child cut is found or reached root.
			if(currentNode.isChildcut()) {
				cut(currentNode,parent);
				cascadingcut(parent);
			} else  
				currentNode.setChildcut(true);
		}
	}
	/*
	 * Increament count of keyword by count value
	 * 			New count cannot be less than old count
	 * */
	public void incrementcount(fibonode node, int count) {
		fibonode parent = node.parent;
		int newcount = node.getCount() + count;
		if(newcount > node.getCount()) {
			node.setCount(newcount);
		}
		//if node count is greater post increment, cut the node and cascade cut the parent in order to maintain max heap property.
		if((parent !=null) && node.getCount() > parent.getCount()) {
			cut(node,parent);
			cascadingcut(parent);
		} 
		if(node.getCount() > maxnode.getCount()) {
			maxnode = node;
		}
	}
	
	/*
	 * Remove max node from the heap
	 * */
	
	public fibonode removemax() {
		//fetch maxnode
		fibonode resultNode = maxnode;
		if(resultNode != null) {
			int children = resultNode.getDegree();
			fibonode child = resultNode.child;
			fibonode sibling;
			
			//traverse all maxnode children
			while(children > 0) {
				
				//remove node from the child linked list and add to parent linked list
				sibling = child.left;
				child.left.right = child.right;
	            child.right.left = child.left;
	            child.right = maxnode;
                child.left = maxnode.left;
                maxnode.left = child;
                child.left.right = child;

                // set parent to null
                child.parent = null;
                child = sibling;
                //decrement no of children by 1
                children--;
			}
			//remove maxnode from the heap
			resultNode.right.left = resultNode.left;
			resultNode.left.right = resultNode.right;
			
			//set new maxnode to null is current maxnode was the only node left in the heap
			if(resultNode == resultNode.left) {
				maxnode = null;
			} else { 
				maxnode = resultNode.left;
				//else combine all nodes degree wise
				combinefibotrees();
			}
			//decrement node count by 1
			nodecount--;
			return resultNode;
		}
		return null;
	}
	public void combinefibotrees() {
		//max degree of the node is bounded by O(log n) where log is computed to the base phi.
		int tablesize = ((int) Math.floor(Math.log(nodecount) * phi)) + 1;
		
		//initialize mergetable using tablesize
		fibonode[] mergetable = new fibonode[tablesize];
		fibonode currentNode = maxnode;
		int no_of_root_nodes = 0;
		//count the number of nodes at root level
		if(currentNode != null) {
			no_of_root_nodes++;
			currentNode = currentNode.left;
			while(currentNode !=maxnode) {
				no_of_root_nodes++;
				currentNode = currentNode.left;
			}
		}
		
			while(no_of_root_nodes > 0){
				int degree = currentNode.getDegree();
				fibonode nextnode = currentNode.left;
				while(mergetable[degree] != null) {
					fibonode sameDegNode = mergetable[degree];
					//check which node has greater count
					if(sameDegNode.getCount() > currentNode.getCount()) {
						fibonode swapnode = currentNode;
						currentNode = sameDegNode;
						sameDegNode = swapnode;
					}
					//since currentnode has greater count make sameDegNode its child
					merge(sameDegNode, currentNode);
					mergetable[degree] = null;
					degree = degree+1;
				}
				//update mergetable
				mergetable[degree] = currentNode;
				currentNode = nextnode;
				no_of_root_nodes--;
			} 

			maxnode = null;
			//update maxnode
			for(int k =0;k<tablesize;k++) {
				if(mergetable[k] != null) {
					if(maxnode == null) {
						maxnode = mergetable[k];
					} else {
						fibonode tableNode = mergetable[k]; 
						tableNode.right.left = tableNode.left;
						tableNode.left.right = tableNode.right;

		                // Now add to root list, again.
						tableNode.right = maxnode;
						tableNode.left = maxnode.left;
		                maxnode.left = tableNode;
		                tableNode.left.right = tableNode;

		                // Check if this is a new maximum
		                if (tableNode.getCount() > maxnode.getCount()) {
		                    maxnode = tableNode;
		                }
					}
				}
		}
	}	
}


















;