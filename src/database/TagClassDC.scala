package database

object TagClassDC {
	/**
	 * creates new TagClassDC with
	 * given properties
	 */
	def apply(name : String, documentCount : Int, wordCounts : java.util.Map[String,Int]) = {
		new TagClassDC(name, documentCount, wordCounts)
	}
}

class TagClassDC(	val name : String,
					val documentCount : Int,
					val wordCounts : java.util.Map[String,Int]){
}