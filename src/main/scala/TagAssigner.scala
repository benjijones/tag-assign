import scala.io.Source
import scala.collection.mutable.Map

object TagAssigner {
	
	def main(args : Array[String]) {
		
		// parse the training data and create the TrainingDocuments
		val rawTrainingData = Source.fromFile(args(0)).getLines
		val trainingData = rawTrainingData map TrainingDocument.createTrainingDocumentFromDataString toList;
		train(trainingData)
		
		// parse the new document and create a NewDocument
		val rawNewDocument = Source.fromFile(args(1)).getLines.next
		val newDocument = NewDocument.createNewDocumentFromDataString(rawNewDocument)
		
		// get the scores assigned by the algorithm based on the contents of the document
		val results = getScores(newDocument)
		
		
		
		println(results.sortBy(_._2).reverse mkString "\n")
		
	}
	
	def train(data : List[TrainingDocument]) {
		data foreach TagClass.addTrainingDocument
	}
	
	def train(document : TrainingDocument) {
		TagClass addTrainingDocument document
	}
	
	def getScores(document : NewDocument) = {
		TagClass.tagClasses map { tc => (tc.name, tc.getScore(document)) } toList
	}
}