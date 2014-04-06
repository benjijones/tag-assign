package database

import oracle.kv._

object DatabaseServer {

	def main(args: Array[String]) {
		val config = new KVStoreConfig("tagstore", "localhost:5000")
		val kvstore = KVStoreFactory.getStore(config)
		val stats = kvstore.getStats(false)
		println(stats)
	}

}