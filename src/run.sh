if [[ $1 == "clean" ]];
then
	rm -f *.class
	rm -f Trie/*.class
else
	path=$1
	javac *.java Trie/*.java
	java StringIndex $path
fi