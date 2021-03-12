package Trie;

public interface TrieInterface {
	public int insert(String word, TrieValue v);
	public TrieNode startsWith(String s);
	public boolean remove(String word, TrieValue v);
	public void printTrie(TrieNode t);
}
