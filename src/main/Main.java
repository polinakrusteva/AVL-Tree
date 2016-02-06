package main;

import java.util.Arrays;
import java.util.Comparator;

import implementation.Action;
import implementation.AvlTree;
import implementation.Node;

public class Main {

	public static void main(String[] args) {
		AvlTree<Integer> tree = new AvlTree<>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.compare(o1, o2);
			}
		});
		System.out.println("Inserting 10, 5, 13, 4, 6, 17, 3 from a list.");
		tree.insert(Arrays.asList(10, 5, 13, 4, 6, 17, 3));
		tree.traverse(new Action<Integer>() {

			@Override
			public void execute(Node<Integer> node) {
				System.out.println(String.format("Node: %s, Balance factor: %s", node.value, node.balanceFactor));
			}
		});
		System.out.println(String.format("Size: %s", tree.getSize()));

		System.out.println("Inserting only 2.");
		tree.insert(2);
		tree.traverse(new Action<Integer>() {

			@Override
			public void execute(Node<Integer> node) {
				System.out.println(String.format("Node: %s, Balance factor: %s", node.value, node.balanceFactor));
			}
		});
		System.out.println(String.format("Size: %s", tree.getSize()));

		System.out.println("Deleting 3 and 5.");
		tree.delete(3);
		tree.delete(5);
		tree.traverse(new Action<Integer>() {

			@Override
			public void execute(Node<Integer> node) {
				System.out.println(String.format("Node: %s, Balance factor: %s", node.value, node.balanceFactor));
			}
		});
		System.out.println(String.format("Size: %s", tree.getSize()));

		Node<Integer> temp = tree.find(13);
		if (null != temp) {
			System.out.println(
					String.format("Found element: %s with balance factor: %s", temp.value, temp.balanceFactor));
		}
	}

}
