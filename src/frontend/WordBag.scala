package frontend

import scala.collection.mutable.Map

class WordBag(words : List[String]) {
	
	val wordCounts = Map[String, Int]().withDefaultValue(0)
	
	words foreach {
		wordCounts(_) += 1
	}
	
	def getWords = wordCounts.keys
	
	override def toString = wordCounts mkString " "
}