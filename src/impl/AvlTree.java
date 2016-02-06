package impl;

import java.util.Comparator;
import java.util.List;

public class AvlTree<T> {

	public Node<T> root;
	public Comparator<T> cmp;
	private int size;

	public AvlTree(Comparator<T> cmp) {
		this.cmp = cmp;
	}

	public AvlTree(Comparator<T> cmp, Node<T> root) {
		this(cmp);
		this.root = root;
		this.setSize(1);
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void insert(T value) {
		insert(value, root);
		setSize(getSize() + 1);
	}

	public void insert(List<T> values) {
		for (T value : values) {
			insert(value);
		}
	}

	private void insert(T value, Node<T> root) {
		if (null == root) {
			this.root = new Node<T>(value);
			return;
		}
		if (cmp.compare(root.value, value) >= 0) {
			if (null == root.left) {
				root.left = new Node<T>(value);
				root.left.parent = root;
				assertBalanceFactors(this.root);
				balance();
				return;
			}
			insert(value, root.left);
			return;
		}
		if (null == root.right) {
			root.right = new Node<T>(value);
			root.right.parent = root;
			assertBalanceFactors(this.root);
			balance();
			return;
		}
		insert(value, root.right);
	}

	
	public Node<T> find(T value) {
		return find(value, root);
	}

	private Node<T> find(T value, Node<T> root) {

		if (null == root || cmp.compare(root.value, value) == 0) {
			return root;
		}

		if (cmp.compare(root.value, value) < 0) {
			return find(value, root.right);
		} else {
			return find(value, root.left);
		}

	}

	public void traverse(Action<T> action) {
		traverse(action, root);
	}

	private void traverse(Action<T> action, Node<T> root) {
		if (null == root) {
			return;
		}
		if (null != root.left) {
			traverse(action, root.left);
		}
		action.execute(root);
		if (null != root.right) {
			traverse(action, root.right);
		}
	}
	
	private boolean isLeftSubtree(Node<T> node, Node<T> parent) {
		return parent.left.equals(node);
	}
	
	private void performDoubleLeftRotation(Node<T> node) {
		Node<T> child = node.left;
		Node<T> grandchildren = child.right;
		Node<T> parent = node.parent;

		if (null != parent) {
			if (isLeftSubtree(node, parent)) {
				parent.left = child;
			} else {
				parent.right = child;
			}
			child.parent = parent;
		} else {
			child.parent = null;
			this.root = child;
		}
		child.right = node;
		node.parent = child;
		node.left = grandchildren;
		if (null != grandchildren) {
			grandchildren.parent = node;
		}
		assertBalanceFactors(this.root);
	}

	private void performLeftRightRotation(Node<T> node) {
		Node<T> child = node.left;
		Node<T> grandchildren = child.right;
		Node<T> parent = node.parent;
		Node<T> leftGrand = grandchildren.left;
		Node<T> rightGrand = grandchildren.right;

		if (null != parent) {
			if (isLeftSubtree(node, parent)) {
				parent.left = grandchildren;
			} else {
				parent.right = grandchildren;
			}
			grandchildren.parent = parent;
		} else {
			grandchildren.parent = null;
			this.root = grandchildren;
		}
		grandchildren.left = child;
		child.parent = grandchildren;
		grandchildren.right = node;
		node.parent = grandchildren;
		node.left = leftGrand;
		child.right = rightGrand;
		if (null != leftGrand) {
			leftGrand.parent = child;
		}
		if (null != rightGrand) {
			rightGrand.parent = node;
		}
		assertBalanceFactors(this.root);
	}

	private void performDoubleRightRotation(Node<T> node) {
		Node<T> child = node.right;
		Node<T> grandchildren = child.left;
		Node<T> parent = node.parent;

		if (null != parent) {
			if (isLeftSubtree(node, parent)) {
				parent.left = child;
			} else {
				parent.right = child;
			}
			child.parent = parent;
		} else {
			child.parent = null;
			this.root = child;
		}
		child.left = node;
		node.parent = child;
		node.right = grandchildren;
		if (null != grandchildren) {
			grandchildren.parent = node;
		}
		assertBalanceFactors(this.root);
	}

	private void performRightLeftRotation(Node<T> node) {
		Node<T> child = node.right;
		Node<T> grandchildren = child.left;
		Node<T> parent = node.parent;
		Node<T> leftGrand = grandchildren.left;
		Node<T> rightGrand = grandchildren.right;

		if (null != parent) {
			if (isLeftSubtree(node, parent)) {
				parent.left = grandchildren;
			} else {
				parent.right = grandchildren;
			}
			grandchildren.parent = parent;
		} else {
			grandchildren.parent = null;
			this.root = grandchildren;
		}
		grandchildren.left = node;
		node.parent = grandchildren;
		grandchildren.right = child;
		child.parent = grandchildren;
		child.left = leftGrand;
		node.right = rightGrand;
		if (null != leftGrand) {
			leftGrand.parent = child;
		}
		if (null != rightGrand) {
			rightGrand.parent = node;
		}
		assertBalanceFactors(this.root);
	}

	private void assertBalanceFactor(Node<T> node) {
		node.balanceFactor = findTreeHeight(node.left) - findTreeHeight(node.right);
	}

	private int findTreeHeight(Node<T> node) {
		if (null == node) {
			return 0;
		}
		return 1 + Math.max(findTreeHeight(node.left), findTreeHeight(node.right));
	}

	private void assertBalanceFactors(Node<T> root) {
		traverse(new Action<T>() {

			@Override
			public void execute(Node<T> node) {
				assertBalanceFactor(node);
			}
		});
	}
	
	private void balanceIfNecessary(Node<T> node) {
		if (node.balanceFactor == -2) {
			if (null != node.right && node.right.balanceFactor == 1) {
				performRightLeftRotation(node);
			}
			if (null != node.right && node.right.balanceFactor == -1) {
				performDoubleRightRotation(node);
			}
		}
		if (node.balanceFactor == 2) {
			if (null != node.left && node.left.balanceFactor == 1) {
				performDoubleLeftRotation(node);
			}
			if (null != node.left && node.left.balanceFactor == -1) {
				performLeftRightRotation(node);
			}
		}
	}
	
	private void balance() {
		dfs(new Action<T>() {

			@Override
			public void execute(Node<T> node) {
				balanceIfNecessary(node);
			}
		}, this.root);
	}
	
	private void dfs(Action<T> action, Node<T> root) {
		if (null == root) {
			return;
		}
		if (null != root.left) {
			traverse(action, root.left);
		}
		if (null != root.right) {
			traverse(action, root.right);
		}
		action.execute(root);
	}

	public void delete(T value) {
		if (delete(value, this.root)) {
			setSize(getSize() - 1);
		}
	}

	private boolean delete(T value, Node<T> root) {
		if (null == root) {
			return false;
		}
		if (cmp.compare(root.value, value) == 0) {
			Node<T> parent = root.parent;
			Node<T> leftChild = root.left;
			Node<T> rightChild = root.right;
			if (null == rightChild && null == leftChild) {
				root.parent = null;
				if (isLeftSubtree(root, parent)) {
					parent.left = null;
				} else {
					parent.right = null;
				}
				return true;
			}
			T tmp = findMin(getChild(root)).value;
			delete(tmp, root);
			root.value = tmp;
			return true;
		}
		if (cmp.compare(root.value, value) > 0) {
			if (delete(value, root.left)) {
				assertBalanceFactor(root);
				balanceIfNecessary(root);
				return true;
			}
			return false;
		} else {
			if (delete(value, root.right)) {
				assertBalanceFactor(root);
				balanceIfNecessary(root);
				return true;
			}
			return false;
		}

	}

	private Node<T> getChild(Node<T> node) {
		if (null != node.left && null != node.right) {
			return node.right;
		}
		return node.left == null ? node.right : node.left;
	}

	private Node<T> findMin(Node<T> root) {
		if (null == root.left) {
			return root;
		}
		return findMin(root.left);
	}
}