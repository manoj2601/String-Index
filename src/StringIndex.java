import java.util.*;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Trie.Trie;
import Trie.TrieNode;
import Trie.TrieValue;

public class StringIndex implements StringIndexInterface, Runnable{
	public ArrayList<String> list = new ArrayList<String>();
	HashMap<String, Result> returnedObjects = new HashMap<String, Result>();
	Queue<String> q;
	Trie Trie1;
	Trie Trie2;
	
	//constructor
	public StringIndex(Queue<String> q, Trie Trie1, Trie Trie2)
	{
		this.Trie1 = Trie1;
		this.Trie2 = Trie2;
		this.q = q;
	}
	
	private static String reverseString(String s) //helper function, it reverses the string
	{
		String srev = "";
		for(int i=0; i<s.length(); i++)
			srev += s.charAt(s.length()-1-i);
		return srev;
	}
	
	//it removes a string from the trie and returns success
	public boolean remove(TrieValue v)
	{
		if(v == null)
			return false;
		String s = v.str;
		String srev = reverseString(s);
		boolean ret = Trie1.remove(s, v);
		Trie2.remove(srev, v);
		v = null;
		return ret;
	}
	
	@Override
	public int insert(String s) {
		// TODO Auto-generated method stub
		if(s.compareTo("") == 0) //if the string is null
		{
			Trie1.Root.freq++;
			Trie1.Root.isEndofWord = true;
			Trie2.Root.freq++;
			Trie2.Root.isEndofWord = true;
			return (Trie1.Root.freq-1);
		}
		TrieValue v = new TrieValue(s);
		int ret = Trie1.insert(s, v);
		Trie2.insert(reverseString(s), v);
		return ret;
	}
	
	private void SWPhelper(TrieNode n, ArrayList<String> ret, String pre, TreeSet<TrieValue> objects) {
		n.lock.lock();
		if(n.isEndofWord)
		{
			for(int i=0; i<n.freq; i++)
				ret.add(pre);
			Iterator<TrieValue> tv = n.myValues.iterator();
			while(tv.hasNext())
			{
				objects.add(tv.next());
			}
		}
		if(n.table != null) {
			Iterator<Entry<Integer, TrieNode>> hmIterator = n.table.entrySet().iterator(); 
			
			while(hmIterator.hasNext()) {
				Map.Entry<Integer, TrieNode> mapElement = (Map.Entry<Integer, TrieNode>)hmIterator.next();
				int ch = (int) mapElement.getKey();
				TrieNode m = (TrieNode) mapElement.getValue();
				SWPhelper(m, ret, pre+(char)ch, objects);
			}
		}
		n.lock.unlock();
	}

	@Override
	public Result stringsWithPrefix(String s) {
		// TODO Auto-generated method stub
		TrieNode t = Trie1.startsWith(s);
		ArrayList<String> list = new ArrayList<String>();
		TreeSet<TrieValue> objects = new TreeSet<TrieValue>();
		if(t==null)
		{
			Result ret1 = new Result(list, this);
			ret1.objects = objects;
			return ret1;
		}
		SWPhelper(t, list, s, objects);
		Result ret1 = new Result(list, this);
		ret1.objects = objects;
		return ret1;
	}
	
	@Override
	public Result stringsWithSuffix(String s) {
		// TODO Auto-generated method stub
		String srev = reverseString(s);
		TrieNode t = Trie2.startsWith(srev);
		ArrayList<String> list = new ArrayList<String>();
		TreeSet<TrieValue> objects = new TreeSet<TrieValue>();
		if(t==null)
		{
			Result ret1 = new Result(list, this);
			ret1.objects = objects;
			return ret1;
		}
		SWPhelper(t, list, srev, objects);
		for(int i=0; i<list.size(); i++) {
			String rev = reverseString(list.get(i));
			list.set(i, rev);
		}
		Result ret1 = new Result(list, this);
		ret1.objects = objects;
		return ret1;
	}
	
	public static void main(String[] args) throws IOException {
		
		//comment this line if you want to take inputs directly from console
		System.setIn(new FileInputStream(args[0]));
		
		Queue<String> q = new LinkedList<>();
		Trie Trie1 = new Trie();
		Trie Trie2 = new Trie();
		StringIndex myobj = new StringIndex(q, Trie1, Trie2);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
		String line;
		while((line = reader.readLine()) != null)
		{
			if(line.compareTo("exit")==0)
				break;
			synchronized(myobj.q)
			{
				myobj.q.add(line);
			}
			Thread t = new Thread(myobj);
			t.start();
		}
		
	}

	@Override
	public void run() 
	{	
		String task = null;
		synchronized(q) {
			if(!q.isEmpty())
				task = q.remove();
		}
		if(task == null)
			return;
		String first = "";
		int j=0;
		for(int i=0; i<task.length(); i++)
		{
			if(task.charAt(i) == ' ')// || i==task.length()-1)
			{
				j=i+1;
				break;
			}
			first += task.charAt(i);
			if(i==task.length()-1)
			{
				j=i+1;
				break;
			}
		}
		//it is a insert query
		if(first.compareTo("insert") ==0)
		{
			String in="";
			if(j!=task.length())
				in = task.substring(j);
			int ret = this.insert(in);
			System.out.println(in+" inserted "+ret);
		}
		//it is a stringsWithPrefix query
		else if(first.compareTo("stringsWithPrefix")==0)
		{
			if(j==task.length())
			{
				System.out.println("invalid input");
			}
			String in = "";
			String nameObj = "";
			boolean objDone = false;
			for(int i=j; i<task.length(); i++)
			{
				if(!objDone)
				{
					if(task.charAt(i) == ' ')
						objDone = true;
					else nameObj += task.charAt(i);
				}
				else in += task.charAt(i);
			}
			
			this.returnedObjects.put(nameObj, null);
			Result ret = this.stringsWithPrefix(in);
			System.out.println(in+" stringsWithPrefix returned : "+nameObj+" "+ret);
			returnedObjects.put(nameObj, ret);
		}
		else if(first.compareTo("stringsWithSuffix")==0)
		{
			if(j==task.length())
			{
				System.out.println("invalid input");
				return;
			}
			String in = "";
			String nameObj = "";
			boolean objDone = false;
			for(int i=j; i<task.length(); i++)
			{
				if(!objDone)
				{
					if(task.charAt(i) == ' ')
						objDone = true;
					else nameObj += task.charAt(i);
				}
				else in += task.charAt(i);
			}
			
			this.returnedObjects.put(nameObj, null);
			Result ret = this.stringsWithSuffix(in);
			System.out.println(in+" stringsWithSuffix returned : "+nameObj+" "+ret);
			returnedObjects.put(nameObj, ret);
		}
		else if(first.compareTo("remove")==0)
		{
			if(j==task.length())
			{
				System.out.println("Invalid input");
				return;
			}
			String objName = "";
			for(int i=j; i<task.length(); i++)
			{
				objName += task.charAt(i);
			}
			if(!this.returnedObjects.containsKey(objName))
				System.out.println("Invalid Result object");
			else {
				Result obj = this.returnedObjects.get(objName);
				while(obj == null)
				{
					obj = this.returnedObjects.get(objName);
				}
					int a = obj.remove();
					System.out.println(objName+ " Removed. total items removed: "+a);								
			}
		}
		else if(first.compareTo("print") == 0)
		{
			Trie1.printTrie(Trie1.Root);
		}
		else
		{
			System.out.println("Invalid Input");
		}
	}
}
