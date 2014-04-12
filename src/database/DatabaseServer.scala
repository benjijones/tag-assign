package database

import oracle.kv._
import org.apache.avro.Schema.Parser
import java.io.File
import scala.collection.JavaConversions.asScalaIterator
import org.apache.avro.util.Utf8
import org.apache.avro.generic.GenericData

object DatabaseServer {

	private val config = new KVStoreConfig("tagstore", "localhost:5000")

	private val tagClassSchema = new Parser parse new File("database/schema/TagClass.avsc")

	tagClassSchema.
	def main(args : Array[String]) {
		
		//println(TagClassDao.getAll.map{a => List(a.name, a.documentCount, a.wordCounts toString) })
		
		val store = DatabaseServer connect
		val binding = store.getAvroCatalog getGenericBinding tagClassSchema
		
		//val raw = store.delete(Key.createKey("TagClass")) 
		println(TagClassDao.getOrCreateByName("test"))//getOrCreateByName(name)
		
		val newTagClass = TagClassDC("test", 0, )
		
		val entity = new GenericData.Record(tagClassSchema)
		entity.put("name", newTagClass.name)
		entity.put("document_count", newTagClass.documentCount)
		entity.put("word_counts", asJavaMap(newTagClass.wordCounts))
		
		store.put(Key.createKey("TagClass"), binding toValue entity)
		
		val store.put(Key.createKey("TagClass"), )
		
		val raw = store.multiGetIterator(Direction.FORWARD, 0, Key.createKey("TagClass"), null, null)
		val result = raw.map{ vv =>
			val member = binding toObject vv.getValue
			val name = member.get("name").asInstanceOf[Utf8].toString
			val documentCount = member.get("document_count").asInstanceOf[Int]
			val wordCounts = member.get("word_counts").asInstanceOf[java.util.Map[Utf8,Int]]
			(name, documentCount, wordCounts)
			//val wordCounts = member.get("word_counts").asInstanceOf[java.util.Map[String,Int]].asScala.toMap
			//TagClassDC(name, documentCount, wordCounts)
		} toList
		
		//println(raw)
		println(result mkString " ")
	}
	
	def connect = KVStoreFactory.getStore(config)

}