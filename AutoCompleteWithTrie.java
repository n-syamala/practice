package datastruct.trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * When the user types in a prefix of his search query, we need to give him all
 * recommendations to auto-complete. Assumption - We have already stored the
 * past searches stored. Example : Let's say we have already stored {"app",
 * "apple","apply","application","ask","best","bet"} When user types 'ap' , we
 * should return {"app", "apple","apply","application"}
 * 
 * Solution - we use a trie to store all the words and use standard trie search
 * for prefix.
 * 
 *
 */
public class AutoCompleteWithTrie {
	TrieNode root;

	public AutoCompleteWithTrie() {
		// create root with empty string
		root = new TrieNode(' ');
	}

	/**
	 * searches whole word is present in trie or not
	 * 
	 * @param input
	 * @return
	 */
	public boolean search(String input) {
		if (input != null && !input.isBlank()) {
			TrieNode current = root;
			for (int i = 0; i < input.length(); i++) {
				// check if current node has a child node with this character
				TrieNode child = current.children.get(input.charAt(i));
				if (child == null)
					return false;
				current = child;
			}
			return current.isEndOfWord; // check if the node with last char of input is marked as end of node

		}
		return false;
	}

	/**
	 * search for a given prefix, return all the words that starts with prefix
	 * 
	 * @param prefix
	 * @return
	 */
	public List<String> searchPrefix(String prefix) {
		List<String> result = new ArrayList<String>();
		if (prefix != null && !prefix.isBlank()) {
			TrieNode current = root;
			// search the given prefix is present in the trie
			for (int i = 0; i < prefix.length(); i++) {
				// check if current node has a child node with this character
				TrieNode child = current.children.get(prefix.charAt(i));
				if (child == null)
					return result; // return empty list, if prefix is not present
				current = child;
			}
			// if prefix is present, we want to find all the nodes branching from the last
			// char of prefix
			helper(prefix.substring(0, prefix.length() - 1), current, result);
		}
		return result;
	}

	private void helper(String prefix, TrieNode node, List<String> result) {
		// check if the node is marked as end of word, if so add to result set.
		if (node.isEndOfWord)
			result.add(prefix + node.data);
		// we want to find all the words branched from prefix, so look in all the child
		// nodes starting prefix recursively
		for (Entry<Character, TrieNode> e : node.children.entrySet()) {
			TrieNode childNode = e.getValue();
			helper(prefix + node.data, childNode, result);
		}
	}

	/**
	 * insert given word in trie
	 * 
	 * @param input
	 */
	public void insert(String input) {
		if (input != null && !input.isBlank()) {
			TrieNode current = root;
			for (int i = 0; i < input.length(); i++) {
				if (!current.children.containsKey(input.charAt(i))) {
					// add a new trieNode with this char as data and also add a pointer to the newly
					// created child node in the parent node's children map
					TrieNode newNode = new TrieNode(input.charAt(i));
					current.children.put(input.charAt(i), newNode);
				}
				// traverse to the next child node
				current = current.children.get(input.charAt(i));

			}
			// finally mark the end of word at the node which stores last character
			current.isEndOfWord = true;

		}
	}

	class TrieNode {
		char data;
		Map<Character, TrieNode> children;
		boolean isEndOfWord = false;

		public TrieNode(char c) {
			data = c;
			children = new HashMap<Character, TrieNode>();
		}

	}

	public static void main(String[] args) {
		AutoCompleteWithTrie test = new AutoCompleteWithTrie();
		test.insert("app");
		test.insert("apple");
		test.insert("application");
		test.insert("best");
		List<String> result = test.searchPrefix("ap");
		System.out.println(Arrays.toString(result.toArray()));
	}

}
