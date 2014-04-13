package main.scala.database

import main.scala.frontend.NewDocument

object TagClassDC {
	/**
	 * creates new TagClassDC with
	 * given properties
	 */
	def apply(name : String, documentCount : Int, wordCounts : Map[String, Int]) = {
		new TagClassDC(name, documentCount, wordCounts withDefaultValue(0))
	}
}

class TagClassDC(	val name : String,
					val documentCount : Int,
					val wordCounts : Map[String,Int]){
	
	def + (other : TagClassDC) = {
		val a = (wordCounts.keySet ++ other.wordCounts.keySet)
		val b = a.map{ key => key -> (wordCounts(key) + other.wordCounts(key)) }
		val newWordCounts = b.toMap
		TagClassDC(name, documentCount + other.documentCount, newWordCounts)
	} 
	
	def getScore(document : NewDocument) = {
		val sumScore = wordCounts.values.sum + wordCounts.size
		
		document.wordCounts.foldLeft(Math.log(documentCount.toDouble / DocumentDao.getDocumentCount.toDouble)) {
			case (prev, (word, count)) => { Math.log((wordCounts(word) + 1).toDouble / sumScore)*count + prev }
		}
	}
	
}