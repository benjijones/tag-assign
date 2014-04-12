package database

import frontend.NewDocument

object TagClassDC {
	/**
	 * creates new TagClassDC with
	 * given properties
	 */
	/*def apply(name : String, documentCount : Int, wordCounts : Map[String,Int]) = {
		/*val wordCountMap = {
			if (wordCounts.isInstanceOf[Map[Utf8,Int]])
				wordCounts.asInstanceOf[Map[Utf8,Int]].map{ case (key, value) => key.toString -> value }
			else 
				wordCounts
		}*/
		new TagClassDC(name, documentCount, wordCounts.withDefaultValue(0))
	}*/
	def apply(name : String, documentCount : Int, wordCounts : Map[String, Int]) = {
		new TagClassDC(name, documentCount, wordCounts withDefaultValue(0))
	}
}

class TagClassDC(	val name : String,
					val documentCount : Int,
					val wordCounts : Map[String,Int]){
	
	def + (other : TagClassDC) = {
		//val newWordCounts = (wordCounts.keySet ++ other.wordCounts.keySet).map{key => {println(key); key}}.map{ key => key -> (wordCounts(key) + other.wordCounts(key)) }.toMap
		val a = (wordCounts.keySet ++ other.wordCounts.keySet)
		println(a.map{_ => ""})
		val b = a.map{ key => key -> (wordCounts(key) + other.wordCounts(key)) }
		val newWordCounts = b.toMap
		TagClassDC(name, documentCount + other.documentCount, newWordCounts)
	} 
	
	def getScore(document : NewDocument) = {
		val sumScore = wordCounts.values.sum + wordCounts.size
		
		document.wordCounts.foldLeft(Math.log(documentCount.toDouble / TagClassDao.getTotalDocumentCount.toDouble)) {
			case (prev, (word, count)) => { Math.log((wordCounts(word) + 1).toDouble / sumScore)*count + prev }
		}
	}
	
}