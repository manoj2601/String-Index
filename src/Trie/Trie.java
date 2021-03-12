package Trie;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Trie implements TrieInterface{
	public TrieNode Root;
	public Trie()
	{
		TreeMap<Integer, TrieNode> l = new TreeMap<Integer, TrieNode>();
		this.Root = new TrieNode(-1, false, 0, l, null, null);
	}
	
	@Override
	public int insert(String word, TrieValue v) {
		Root.lock.lock();
		TrieNode locked = Root;
		TrieNode parentNode = Root;
		TreeMap<Integer, TrieNode> table = Root.table;
		TreeMap<Integer, TrieNode> prevtable = null;
			for(int i=0; i<word.length()-1; i++) {	
				if(!table.containsKey((int)word.charAt(i))) {
						for(int j=i; j<word.length()-1; j++) {
							TreeMap<Integer, TrieNode> child4 = new TreeMap<Integer, TrieNode>();
							TrieNode m1 = new TrieNode((int) word.charAt(j), false, 0, child4, parentNode, prevtable);
							table.put((int)word.charAt(j), m1);
							parentNode = table.get((int)word.charAt(j));
							prevtable = table;
							table = table.get((int)word.charAt(j)).table;
						}
						TrieNode m2 = new TrieNode((int)word.charAt(word.length()-1), true, 1, new TreeMap<Integer, TrieNode>(), parentNode, prevtable);
						m2.myValues.add(v);
						table.put((int)word.charAt(word.length()-1), m2);
						locked.lock.unlock();
						return 0;
				}
				else {
					table.get((int)word.charAt(i)).lock.lock();
					locked.lock.unlock();
					locked = table.get((int)word.charAt(i));
					parentNode = table.get((int)word.charAt(i));
					prevtable = table;
					table = table.get((int)word.charAt(i)).table;
				}
			}
			if(!table.containsKey((int)word.charAt(word.length()-1))) {
					TrieNode m = new TrieNode((int)word.charAt(word.length()-1), true, 1, new TreeMap<Integer, TrieNode>(), parentNode, prevtable);
					m.myValues.add(v);
					table.put((int)word.charAt(word.length()-1), m);
					locked.lock.unlock();
					return 0;
			}
			else {
					table.get((int)word.charAt(word.length()-1)).isEndofWord = true;
					table.get((int)word.charAt(word.length()-1)).myValues.add(v);
					table.get((int)word.charAt(word.length()-1)).freq++;
					locked.lock.unlock();
					return table.get((int)word.charAt(word.length()-1)).freq-1;
			}
	}

	@Override
	public TrieNode startsWith(String s)
	{
		TrieNode curr = this.Root;
			for(int i=0; i<s.length(); i++) {
				while(curr.lock.isLocked())
				{
					//waiting to unlock this trieNode by some thread
				}
				if(curr.table.containsKey((int)s.charAt(i))) {
					curr = curr.table.get((int)s.charAt(i));
				}
				else return null;	
			}
			return curr;
	}

	@Override
	public boolean remove(String word, TrieValue v)
	{
		TrieNode parentNode = Root;
		TreeMap<Integer, TrieNode> table = Root.table;
		for(int i=0; i<word.length()-1; i++) {
			if(!table.containsKey((int)word.charAt(i)))
				return false;
			else {
				parentNode = table.get((int)word.charAt(i));
				table = table.get((int)word.charAt(i)).table;
			}
		}
		if(word.compareTo("")==0) { //empty string
			parentNode.lock.lock();
			if(parentNode.myValues.contains(v)) {
				parentNode.myValues.remove(v);
				parentNode.freq--;
				if(parentNode.freq == 0)
					parentNode.isEndofWord = false;
				parentNode.lock.unlock();
				return true;
			}
			else
			{
				parentNode.lock.unlock();
				return false;
			}
		}
		if(!table.containsKey((int)word.charAt(word.length()-1)))
			return false;
		else {
			if(!table.get((int)word.charAt(word.length()-1)).isEndofWord)
				return false;
			else {
				parentNode.lock.lock();
				if(table.get((int)word.charAt(word.length()-1)).myValues.contains(v))
				{
					table.get((int)word.charAt(word.length()-1)).myValues.remove(v);
					table.get((int)word.charAt(word.length()-1)).freq -= 1;
					if(table.get((int)word.charAt(word.length()-1)).freq==0)			
						table.get((int)word.charAt(word.length()-1)).isEndofWord = false;
					parentNode.lock.unlock();
					return true;
				}
				else
					{
						parentNode.lock.unlock();
						return false;
					}
				
			}
		}
	}

	@Override
	public void printTrie(TrieNode t)
	{
		TrieNode curr = t;
		if(t.charASCII == -1)
			System.out.println("This is Root freq : "+curr.freq);
		else
			System.out.println("current char : "+(char)t.charASCII+" freq : "+curr.freq+" parent : "+(char)curr.parent.charASCII);
		Iterator<Entry<Integer, TrieNode>> hmIterator = curr.table.entrySet().iterator(); 
		while(hmIterator.hasNext())
		{
			Map.Entry<Integer, TrieNode> mapElement = (Map.Entry<Integer, TrieNode>)hmIterator.next();
			TrieNode m = (TrieNode) mapElement.getValue();
			printTrie(m);
		}
		System.out.println(" done");
	}

}
