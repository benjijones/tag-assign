import scala.collection.mutable.Map
import scala.collection.mutable.Set

object TagClass {
	
	private val tagClassSet = Set[TagClass]()
	
	private val vocabulary = Set[String]()
	
	private var documentCount = 0
	
	/**
	 * getter for the tagClasses
	 */
	def tagClasses = tagClassSet
	
	/**
	 * returns for an existing TagClass
	 * with the given name, or returns a
	 * new one if none exists
	 */
	def apply(name : String) = {
		tagClassSet.find(_.name == name) match {
			case Some(tc) 	=> tc
			case None 		=> { 	
									val tc = new TagClass(name)
									tagClassSet += tc
									tc
								}
		}
	}
	
	/**
	 * adds a document to specified tag class
	 */
	def addTrainingDocument(document : TrainingDocument) {
		documentCount += 1
		document.tagClasses foreach { _ addTrainingDocument document }
		vocabulary ++= document.wordBag.getWords
		TagClass.tagClassSet foreach { _.updateVocabulary }
	}
}

/**
 * TagClasses should be initialized using
 * the object constructor, not using the
 * 'new' keyword, so that no duplicates are
 * created.
 */
class TagClass(val name : String){	
	val wordCounts = Map[String, Int]().withDefaultValue(0)
	
	def addTrainingDocument(document : TrainingDocument) {
		incrementDocumentCount
		addWordBag(document.wordBag)
		
	}
	
	def updateVocabulary {
		TagClass.vocabulary filter { wordCounts(_) == 0 } foreach { wordCounts(_) = 0 }
	}
	
	def getScore(document : NewDocument) = {
		val sumScore = wordCounts.values.sum + wordCounts.size
		
		document.wordBag.wordCounts.foldLeft(Math.log(documentCount.toDouble / TagClass.documentCount.toDouble)) {
			case (prev, (word, count)) => { Math.log((wordCounts(word) + 1).toDouble / sumScore)*count + prev }
		}
	}
	
	private var documentCount = 0
	
	private def incrementDocumentCount { documentCount += 1 }
	
	private def addWordBag(wordBag : WordBag) {
		wordBag.wordCounts.iterator.foreach {
			case (word, count) =>
				wordCounts(word) += count
		}
	}
}