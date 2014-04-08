package frontend

abstract class Document(val fileName : String, val fileSize : Int, words : List[String]) {
	
	val wordBag = new WordBag(words)
}