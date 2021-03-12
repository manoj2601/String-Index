package Trie;

public class TrieValue  implements Comparable<TrieValue>{
	public String str;
	public TrieValue(String str)
	{
		this.str = str;
	}
	@Override
	public int compareTo(TrieValue o) {
		// TODO Auto-generated method stub
		int a = this.str.compareTo(o.str);
		if(a==0)
			a++;
		return a;
	}
}
