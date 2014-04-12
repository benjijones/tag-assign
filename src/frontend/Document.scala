package frontend

abstract class Document(val fileName : String, val fileSize : Int, words : List[String]) {
	
	val wordCounts = {
		words.foldLeft(Map[String,Int]()){
			(prev, word) => prev + { word -> words.count(_ == word) }
		}
	}
}