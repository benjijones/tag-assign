package frontend

object NewDocument{
	
	def apply(fileName : String, fileSize : Int, words : List[String]) = {
		new NewDocument(fileName, fileSize, words)
	}
	
	def createNewDocumentFromDataString(data : String) = {
		val args = data split " "
		val fileName = args(0)
		val fileSize = args(1) toInt
		val words = args drop(args.indexOf("#WORDS#") + 1) toList;
		NewDocument(fileName, fileSize, words)
	}
}

class NewDocument(	fileName : String,
					fileSize : Int,
					words : List[String]) extends Document(fileName, fileSize, words) {

}