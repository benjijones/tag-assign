package main.scala.frontend

import main.scala.database.TagClassDC

object TrainingDocument {
	
	def apply(fileName : String, fileSize : Int, tags : List[String], words : List[String]) = {
		new TrainingDocument(fileName, fileSize, tags, words)
	}
	
	def createTrainingDocumentFromDataString(data : String) = {
		val args = data split " "
		val fileName = args(0)
		val fileSize = args(1) toInt
		val tags = args slice(args.indexOf("#TAGS#") + 1, args.indexOf("#WORDS#")) toList
		val words = args drop(args.indexOf("#WORDS#") + 1) toList;
		TrainingDocument(fileName, fileSize, tags, words)
	}
}

class TrainingDocument(	fileName : String,
						fileSize : Int,
						tags : List[String],
						words : List[String]) extends Document(fileName, fileSize, words) {
	
	val tagClasses = tags map {TagClassDC(_, 1, wordCounts)}
	
	override def toString = List("TRAINING DOCUMENT",
								 "File name: " + fileName,
								 "File size: " + fileSize,
								 "Tags: " + (tags mkString " "),
								 "WordCounts: " + (wordCounts mkString " ")) mkString ("\n")
}