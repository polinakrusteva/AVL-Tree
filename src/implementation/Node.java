package implementation;

public class Node<T> {

	public Node<T> left;
	public Node<T> right;
	public Node<T> parent;
	public T value;
	public int balanceFactor;
	public int height;
	
	public Node(T value) {
		this.value = value;
		this.height = 0;
	}
	
}
