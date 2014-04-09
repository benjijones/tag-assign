package database

import oracle.kv._

object DatabaseServer {

	private val config = new KVStoreConfig("tagstore", "localhost:5000")
	
	/*def main(args : Array[String]) {
		val entity = TagClassDC("english", 2, Map( "book" -> 1, "read" -> 3 ))
		TagClassDao.add(entity)
		
		println(TagClassDao.getAll.map{a => List(a.name, a.documentCount, a.wordCounts toString) })
	}*/
	
	def connect = KVStoreFactory.getStore(config)

}