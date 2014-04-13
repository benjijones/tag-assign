package main.scala.database

import main.scala.frontend.Document

object DocumentDao {
	
	def addDocument( document : Document ) {
		val store = DatabaseServer connect
		
		store.incr(List("tagclass", "document_count") mkString ":")
		
		store.disconnect
	}
	
	def getDocumentCount = {
		val store = DatabaseServer connect
		
		val result = store.get(List("tagclass", "document_count") mkString ":").get
		
		store.disconnect
		
		result
	}

}