package Trie;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

public class TrieNode {
	public int charASCII;
	public boolean isEndofWord;
	public TreeMap<Integer, TrieNode> table;
	public TrieNode parent;
	public TreeMap<Integer, TrieNode> prevTable;
	public ArrayList<TrieValue> myValues;
	public int freq;
	public ReentrantLock lock;
	TrieNode(int charASCII, boolean isEndofWord, int freq,  TreeMap<Integer, TrieNode> table, TrieNode parent, TreeMap<Integer, TrieNode> prevTable)
	{
		this.charASCII = charASCII;
		this.isEndofWord = isEndofWord;
		this.freq = freq;
		this.table = table;
		this.parent = parent;
		this.prevTable = prevTable;
		this.lock = new ReentrantLock();
		myValues = new ArrayList<TrieValue>();
	}
	public TreeMap<Integer, TrieNode> gettable()
	{
		return this.table;
	}
	
	public int getFreq()
	{
		return this.freq;
	}
	public int getChar()
	{
		return this.charASCII;
	}
	
	public boolean isEndofWord()
	{
		return this.isEndofWord;
	}
	public TrieNode parent()
	{
		return this.parent;
	}
	public String toString()
	{
		return "char is : "+(char)this.charASCII+" int : "+(int)this.charASCII;
	}	
}
