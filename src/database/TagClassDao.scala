package database

object TagClassDao {
	def getAll = {
		val store = DatabaseServer.connect
		val keys = store.getAvroCatalog
		println(keys.getCurrentSchemas toString)
	}
}