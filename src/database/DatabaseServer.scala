package database

import oracle.kv._

object DatabaseServer {

	def main(args : Array[String]) {
		TagClassDao.getAll
	}
	
	def connect = {
		val config = new KVStoreConfig("tagstore", "localhost:5000")
		KVStoreFactory.getStore(config)
	}

}