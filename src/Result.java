import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import Trie.TrieNode;
import Trie.TrieValue;

public class Result implements ResultInterface{
	public ArrayList<String> list  = new ArrayList<String>();
	public TreeSet<TrieValue> objects  = new TreeSet<TrieValue>();
	public StringIndex parent;
	public Result(ArrayList<String> list, StringIndex parent)
	{
		this.list = list;
		this.parent = parent;
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	@Override
	public int remove() {
		// TODO Auto-generated method stub
		int ret = 0;
		Iterator<TrieValue> value = (Iterator<TrieValue>) objects.iterator();
		while(value.hasNext())
		{
			if(parent.remove(value.next()))
				ret++;
		}
		return ret;
	}
	
	public String toString()
	{
		String ret = "";
		ret += "Size : "+this.size()+" [";
		for(int i=0; i<this.list.size(); i++)
			ret+= list.get(i)+", ";
		ret +="]";
		return ret;
		
	}
}
