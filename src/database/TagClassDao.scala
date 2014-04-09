package database

import java.io.File

import scala.collection.JavaConversions.asScalaIterator
import scala.collection.JavaConversions.asJavaMap

import org.apache.avro.Schema.Parser
import org.apache.avro.generic.GenericData

import oracle.kv.Direction
import oracle.kv.Key
import oracle.kv.KeyValueVersion

/**
 * this dao should at some point be handled by a dependency injector
 * make it a class, have it be handled by an IOC manager, and
 * get new schema whenever needed
 */
object TagClassDao {
	private val tagClassSchema = new Parser parse new File("database/schema/TagClass.avsc")
	
	def getAll = {
		val store = DatabaseServer connect
		val binding = store.getAvroCatalog getGenericBinding tagClassSchema
		
		val rawTagClasses = store.multiGetIterator(Direction.FORWARD, 0, Key.createKey("TagClass"), null, null)
		val result = rawTagClasses.map { vv =>
			val member = binding toObject vv.getValue
			val name = member.get("name").toString
			val documentCount = member.get("document_count").asInstanceOf[Int]
			val wordCounts = member.get("word_counts").asInstanceOf[java.util.Map[String,Int]]
			TagClassDC(name, documentCount, wordCounts)
		} toList
		
		store.close
		
		result
	}
	
	def add(tagClass : TagClassDC) {
		val store = DatabaseServer connect
		val binding = store.getAvroCatalog getGenericBinding tagClassSchema
		
		val entity = new GenericData.Record(tagClassSchema)
		entity.put("name", tagClass.name)
		entity.put("document_count", tagClass.documentCount)
		entity.put("word_counts", tagClass.wordCounts)
		
		store.putIfAbsent(Key.createKey("TagClass"), binding toValue entity)
	}
}